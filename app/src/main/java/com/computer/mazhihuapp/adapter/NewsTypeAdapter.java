package com.computer.mazhihuapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.computer.mazhihuapp.MainActivity;
import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.model.NewsListItem;

import java.util.List;

/**
 * Created by computer on 2015/9/17.
 */
public class NewsTypeAdapter extends BaseAdapter {
    private List<NewsListItem> mList;
    private boolean isLight;
    private Context mContext;

    public NewsTypeAdapter(Context context, List<NewsListItem> list) {
        mContext = context;
        mList = list;
        isLight = ((MainActivity) context).isLight();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_item, parent, false);
        }
        TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
        tv_item.setTextColor(mContext.getResources().getColor(isLight ? R.color.light_menu_listview_textcolor : R.color.dark_menu_listview_textcolor));
        tv_item.setText(mList.get(position).getTitle());

        return convertView;
    }
}
