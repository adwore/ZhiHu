package com.computer.mazhihuapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.model.AppInfo;
import com.computer.mazhihuapp.utils.net.AppUtil;
import com.computer.mazhihuapp.utils.net.DownloadInfo;
import com.computer.mazhihuapp.utils.net.DownloadManager;
import com.computer.mazhihuapp.utils.net.FileUtil;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 2015/10/15.
 */
public class RecommendAdapter extends BaseAdapter {
    ArrayList<AppInfo> list;
    private List<ViewHolder> mDisplayedHolders;
    private FinalBitmap finalBitmap;
    private Context context;


    public RecommendAdapter(ArrayList<AppInfo> list, FinalBitmap finalBitmap, Context context) {
        this.list = list;
        this.finalBitmap = finalBitmap;
        this.context = context;
        mDisplayedHolders = new ArrayList<ViewHolder>();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppInfo appInfo = list.get(position);
        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder(context);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(appInfo);
        mDisplayedHolders.add(holder);
        return holder.getRootView();
    }

    public class ViewHolder {

        public TextView textView01;
        public TextView textView02;
        public TextView textView03;
        public TextView textView04;
        public ImageView imageView_icon;
        public Button button;
        public LinearLayout linearLayout;

        public AppInfo mData;
        private DownloadManager mDownloadManager;
        private int mState;
        private float mProgress;
        protected View mRootView;
        private Context context;
        private boolean hasAttached;


        public ViewHolder(Context context) {
            mRootView = initView();
            mRootView.setTag(this);
            this.context = context;
        }


        public View getRootView() {

            return mRootView;
        }

        public View initView() {
            View view = AppUtil.inflate(R.layout.item_recommend_award);
            imageView_icon = (ImageView) view.findViewById(R.id.imageview_task_app_cion);
            textView01 = (TextView) view.findViewById(R.id.textview_task_app_name);
            textView02 = (TextView) view.findViewById(R.id.textview_task_app_size);
            textView03 = (TextView) view.findViewById(R.id.textview_task_app_desc);
            textView04 = (TextView) view.findViewById(R.id.textview_task_app_love);
            button = (Button) view.findViewById(R.id.button_task_download);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_task);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mState == DownloadManager.STATE_NONE || mState == DownloadManager.STATE_PAUSED || mState == DownloadManager.STATE_ERROR) {
                        mDownloadManager.download(mData);
                    } else if (mState == DownloadManager.STATE_WAITING || mState == DownloadManager.STATE_DOWNLOADING) {
                        mDownloadManager.pause(mData);
                    } else if (mState == DownloadManager.STATE_DOWNLOADED) {
                        mDownloadManager.install(mData);
                    }
                }
            });
            return view;
        }

        public void setData(AppInfo data) {
            if (mDownloadManager == null) {

                mDownloadManager = DownloadManager.getInstance();
            }

            String filepath = FileUtil.getDownloadDir(AppUtil.getContext()) + File.separator + data.getName() + ".apk";
            boolean existsFile = FileUtil.isExistsFile(filepath);
            if (existsFile) {
                int fileSize = FileUtil.getFileSize(filepath);
                if (data.getSize() == fileSize) {
                    DownloadInfo downloadInfo = DownloadInfo.clone(data);
                    downloadInfo.setCurrentSize(data.getSize());
                    downloadInfo.setHasFinished(true);
                    mDownloadManager.setDownloadInfo(data.getId(), downloadInfo);
                }
            }
            DownloadInfo downloadInfo = mDownloadManager.getDownloadInfo(data.getId());
            if (downloadInfo!= null){
                mState = downloadInfo.getDownloadState();
                mProgress = downloadInfo.getProgress();
            }else {
                mState = DownloadManager.STATE_NONE;
                mProgress = 0;
            }
            this.mData = data;
            refreshView();
        }

        public AppInfo getData(){
            return mData;
        }

        public void refreshView() {
            linearLayout.removeAllViews();
            AppInfo info = getData();
            textView01.setText(info.getName());
            textView02.setText(FileUtil.FormetFileSize(info.getSize()));
            textView03.setText(info.getBaike_name());
            textView04.setText(info.getDownload_times() + "下载量");
            finalBitmap.display(imageView_icon, info.getLogo_url());


            if (info.getType().equals("0")) {
                textView02.setVisibility(View.GONE);
            }else{
                String  path=FileUtil.getDownloadDir(AppUtil.getContext()) + File.separator + info.getName() + ".apk";
                hasAttached = FileUtil.isValidAttach(path, false);

                DownloadInfo downloadInfo = mDownloadManager.getDownloadInfo(info
                        .getId());
                if (downloadInfo != null && hasAttached) {
                    if(downloadInfo.isHasFinished()){

                        mState = DownloadManager.STATE_DOWNLOADED;
                    }else{
                        mState = DownloadManager.STATE_PAUSED;

                    }

                } else {
                    mState = DownloadManager.STATE_NONE;
                    if(downloadInfo !=null){
                        downloadInfo.setDownloadState(mState);
                    }
                }
            }

            refreshState(mState, mProgress);
        }

        public void refreshState(int state, float progress) {
            mState = state;
            mProgress = progress;
            switch (mState) {
                case DownloadManager.STATE_NONE:
                    button.setText("下载");
                    break;
                case DownloadManager.STATE_PAUSED:
                    button.setText("暂停");
                    break;
                case DownloadManager.STATE_ERROR:
                    button.setText("失败");
                    break;
                case DownloadManager.STATE_WAITING:
                    button.setText("等待");
                    break;
                case DownloadManager.STATE_DOWNLOADING:
                    button.setText((int) (mProgress * 100) + "%");
                    break;
                case DownloadManager.STATE_DOWNLOADED:
                    button.setText("安装");
                    break;
//			case DownloadManager.STATE_READ:
//				button.setText(R.string.app_state_read);
//				break;
                default:
                    break;
            }
        }

    }
}
