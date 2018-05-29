package com.computer.mazhihuapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.computer.mazhihuapp.MainActivity;
import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.model.Latest;
import com.computer.mazhihuapp.utils.net.PreUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 2015/9/18.
 */
public class MainNewsItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<Latest.stories> entities;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;
    private boolean isLight;

    public MainNewsItemAdapter(Context context) {
        mContext = context;
        entities = new ArrayList<>();
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        isLight = ((MainActivity) context).isLight();
    }

    public void addList(List<Latest.stories> items) {
        entities.addAll(items);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_news_item, parent, false);
            holder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String readSeq = PreUtils.getStringFromDefault(mContext, "read", "");
        if (readSeq.contains(entities.get(position).getId() + "")) {
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.clicked_tv_textcolor));
        } else {
            holder.tv_title.setTextColor(mContext.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));
        }
        ((LinearLayout) holder.iv_title.getParent().getParent().getParent()).setBackgroundColor(mContext.getResources().getColor(isLight ? R.color.light_news_item : R.color.dark_news_item));
        holder.tv_topic.setTextColor(mContext.getResources().getColor(isLight ? R.color.light_news_topic : R.color.dark_news_topic));
        Latest.stories entity = entities.get(position);
        if(entity.getType() == 131){
            ((FrameLayout) holder.tv_topic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            holder.tv_title.setVisibility(View.GONE);
            holder.iv_title.setVisibility(View.GONE);
            holder.tv_topic.setVisibility(View.VISIBLE);
            holder.tv_topic.setText(entity.getTitle());
        }else {
            ((FrameLayout) holder.tv_topic.getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
            holder.tv_topic.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.iv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(entity.getTitle());
            mImageloader.displayImage(entity.getImages().get(0), holder.iv_title, options);
        }
        return convertView;
    }

    public void updateTheme(){
        isLight = ((MainActivity) mContext).isLight();
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv_topic;
        TextView tv_title;
        ImageView iv_title;

    }
}
