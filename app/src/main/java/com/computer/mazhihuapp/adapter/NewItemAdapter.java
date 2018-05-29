package com.computer.mazhihuapp.adapter;

import android.content.Context;
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

import java.util.List;

/**
 * Created by computer on 2015/9/21.
 */
public class NewItemAdapter extends BaseAdapter {

    private List<Latest.stories> entities;
    private Context mcontext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private boolean isLight;

    public NewItemAdapter(Context context, List<Latest.stories> item){
        mcontext = context;
        entities = item;
        isLight = ((MainActivity)context).isLight();
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
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
        ViewHolder viewHolder = null;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.main_news_item,parent,false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String readSeq = PreUtils.getStringFromDefault(mcontext,"read","");
        if(readSeq.contains(entities.get(position).getId()+"")){
            viewHolder.tv_title.setTextColor(mcontext.getResources().getColor(R.color.clicked_tv_textcolor));
        }else {
            viewHolder.tv_title.setTextColor(mcontext.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));
        }
        ((LinearLayout) viewHolder.iv_title.getParent().getParent().getParent()).setBackgroundColor(mcontext.getResources().getColor(isLight ? R.color.light_news_item : R.color.dark_news_item));
        ((FrameLayout) viewHolder.tv_title.getParent().getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
        Latest.stories entity = entities.get(position);
        viewHolder.tv_title.setText(entity.getTitle());
        if (entity.getImages() != null) {
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(entity.getImages().get(0), viewHolder.iv_title, options);
        } else {
            viewHolder.iv_title.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class  ViewHolder{
        TextView tv_title;
        ImageView iv_title;

    }

    public void updateTheme() {
        isLight = ((MainActivity) mcontext).isLight();
        notifyDataSetChanged();
    }
}
