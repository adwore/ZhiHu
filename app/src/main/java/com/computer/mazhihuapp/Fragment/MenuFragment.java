package com.computer.mazhihuapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.computer.mazhihuapp.MainActivity;
import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.adapter.NewsTypeAdapter;
import com.computer.mazhihuapp.model.NewsListItem;
import com.computer.mazhihuapp.utils.net.Constant;
import com.computer.mazhihuapp.utils.net.HttpUtils;
import com.computer.mazhihuapp.utils.net.PreUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 2015/9/16.
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener {

    private ListView lv_item;
    private TextView tv_download,tv_main,tv_backup,tv_login;
    private LinearLayout ll_menu;
    private List<NewsListItem> items;
    private boolean isLight;
    private NewsTypeAdapter mAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu,container,false);
        ll_menu = (LinearLayout) view.findViewById(R.id.ll_menu);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_backup = (TextView) view.findViewById(R.id.tv_backup);
        tv_download = (TextView) view.findViewById(R.id.tv_download);
        tv_backup.setOnClickListener(this);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        tv_main.setOnClickListener(this);
        lv_item = (ListView) view.findViewById(R.id.lv_item);
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getFragmentManager()
                        .beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                        .replace(
                                R.id.fl_content,
                                new NewFragment(items.get(position)
                                        .getId(), items.get(position).getTitle()), "news").commit();
                ((MainActivity)mActivity).setCurId(items.get(position).getId());
                ((MainActivity) mActivity).closeMenu();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        isLight = ((MainActivity)mActivity).isLight();
        items = new ArrayList<NewsListItem>();
        if(HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(Constant.THEMES, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String json = response.toString();
                    PreUtils.putStringToDefault(mActivity, "themes", json);
                    parseJson(response);
                }
            });
        }else {
            String json = PreUtils.getStringFromDefault(mActivity,"themes","");
            try{
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);

            }catch (JSONException e){
                e.printStackTrace();
            }


        }
    }

    private void parseJson(JSONObject response) {
        try {
            JSONArray itemsArray = response.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++) {
                NewsListItem newsListItem = new NewsListItem();
                JSONObject itemObject = itemsArray.getJSONObject(i);
                newsListItem.setTitle(itemObject.getString("name"));
                newsListItem.setId(itemObject.getString("id"));
                items.add(newsListItem);
            }
            mAdapter = new NewsTypeAdapter(mActivity,items);
            lv_item.setAdapter(mAdapter);
            updateTheme();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                ((MainActivity) mActivity).loadLatest();
                ((MainActivity) mActivity).closeMenu();
                break;
        }
    }

    public  void updateTheme(){
        isLight = ((MainActivity)mActivity).isLight();
        ll_menu.setBackgroundColor(getResources().getColor(isLight?R.color.light_menu_header:R.color.dark_menu_header));
        tv_login.setTextColor(getResources().getColor(isLight?R.color.light_menu_header_tv:R.color.dark_menu_header_tv));
        tv_backup.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_download.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_main.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_index_background : R.color.dark_menu_index_background));
        lv_item.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        mAdapter.notifyDataSetChanged();
    }
}
