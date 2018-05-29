package com.computer.mazhihuapp.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.computer.mazhihuapp.MainActivity;
import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.activity.LatestContentActivity;
import com.computer.mazhihuapp.adapter.MainNewsItemAdapter;
import com.computer.mazhihuapp.model.Before;
import com.computer.mazhihuapp.model.Latest;
import com.computer.mazhihuapp.utils.net.Constant;
import com.computer.mazhihuapp.utils.net.HttpUtils;
import com.computer.mazhihuapp.utils.net.PreUtils;
import com.computer.mazhihuapp.view.Kanner;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;


/**
 * Created by computer on 2015/9/17.
 */
public class MainFragment extends BaseFragment {

    private ListView lv_news;
    private Latest latest;
    private Before before;
    private Kanner kanner;
    private String date;
    private boolean isLoading = false;
    private MainNewsItemAdapter mAdapter;
    private Handler handler = new Handler();
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) mActivity).setToolbarTitle("今日热闻");
        View view = inflater.inflate(R.layout.main_news_layout, container, false);
        lv_news = (ListView) view.findViewById(R.id.lv_news);
        View header = inflater.inflate(R.layout.kanner, lv_news, false);
        kanner = (Kanner) header.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, Latest.top_stories entity) {
                int [] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth()/2;
                Latest.stories storiesEntity = new Latest.stories();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                Intent intent = new Intent(mActivity,LatestContentActivity.class);
                intent.putExtra(Constant.START_LOCATION,startingLocation);
                intent.putExtra("entity",storiesEntity);
                intent.putExtra("isLight",((MainActivity)mActivity).isLight());
                startActivity(intent);
                mActivity.overridePendingTransition(0,0);
            }
        });
        lv_news.addHeaderView(header);
        mAdapter = new MainNewsItemAdapter(mActivity);
        lv_news.setAdapter(mAdapter);
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                        loadMore(Constant.BEFORE + date);
                    }
                }
            }
        });
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  int [] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth()/2;
                Latest.stories entity = (Latest.stories) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, LatestContentActivity.class);
                intent.putExtra(Constant.START_LOCATION,startingLocation);
                intent.putExtra("entity",entity);
                intent.putExtra("isLight",((MainActivity)mActivity).isLight());
                String readSequence = PreUtils.getStringFromDefault(mActivity, "read", "");
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                if (splits.length>=200){
                    for (int i = 100;i <splits.length;i++){
                        sb.append(splits[i]+",");
                    }
                    readSequence = sb.toString();
                }
                if (!readSequence.contains(entity.getId()+"")){
                    readSequence = readSequence+entity.getId()+",";
                }
                PreUtils.putStringToDefault(mActivity,"read",readSequence);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setTextColor(getResources().getColor(R.color.clicked_tv_textcolor));
                startActivity(intent);
                mActivity.overridePendingTransition(0,0);
            }
        });

        return view;
    }

    protected void initData(){
        super.initData();
        loadFirst();
    }

    private void loadFirst(){
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = ((MainActivity)mActivity).getCachDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",' " + responseString + "')");
                    db.close();
                    parseLatestJson(responseString);
                }
            });
        }else {
            SQLiteDatabase db = ((MainActivity)mActivity).getCachDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constant.LATEST_COLUMN, null);
            if(cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            }else {
                isLoading = false;

            }
            cursor.close();
            db.close();
        }

    }

    private void parseLatestJson(String responseString){
        Gson gson = new Gson();
        latest = gson.fromJson(responseString,Latest.class);
        date = latest.getDate();
        kanner.setTopEntities(latest.getTop_stories());
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Latest.stories> storiesEntities = latest.getStories();
                Latest.stories topic = new Latest.stories();
                topic.setType(131);
                topic.setTitle("今日热闻");
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }


    private void loadMore(final String url){
        isLoading = true;
        if(HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = ((MainActivity)mActivity).getCachDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + responseString + "')");
                    db.close();
                    parseBeforeJoson(responseString);
                }
            });

        }else {
            SQLiteDatabase db = ((MainActivity)mActivity).getCachDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date =" +date,null);
            if(cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJoson(json);
            }else {
                db.delete("CacheList","date<"+date,null);
                isLoading = false;
                Snackbar sb = Snackbar.make(lv_news,"没有更新的离线内容了~",Snackbar.LENGTH_LONG);
                sb.getView().setBackgroundColor(getResources().getColor(((MainActivity)mActivity).isLight()?android.R.color.holo_blue_dark:android.R.color.black));
                sb.show();
            }
            cursor.close();
            db.close();

        }
    }
    private void parseBeforeJoson(String responseString){
        Gson gson = new Gson();
        before = gson.fromJson(responseString, Before.class);
        if (before == null){
            isLoading = false;
            return;
        }
        date = before.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Latest.stories> storiesEntities = before.getStories();
                Latest.stories topic = new Latest.stories();
                topic.setType(131);
                topic.setTitle(convertDate(date));
                storiesEntities.add(0,topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }

    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }
    public void updateTheme() {
        mAdapter.updateTheme();
    }
}
