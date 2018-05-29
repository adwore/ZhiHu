package com.computer.mazhihuapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.computer.mazhihuapp.model.AppInfo;

import java.util.ArrayList;

/**
 * Created by computer on 2015/10/8.
 */
public class DownloadListAdapter extends BaseAdapter {


    ArrayList<AppInfo> list;
    private Context context;

    public DownloadListAdapter(ArrayList<AppInfo> list,Context context){
        this.list = list;
        this.context = context;

    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
