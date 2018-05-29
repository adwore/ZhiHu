package com.computer.mazhihuapp.application;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import com.computer.mazhihuapp.utils.net.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import net.tsz.afinal.FinalBitmap;

import java.io.File;

/**
 * Created by computer on 2015/9/15.
 */
public class myApplication extends Application {
    /** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
    private static myApplication mInstance;
    /** 主线程Looper */
    private static Looper mMainLooper;

    private FinalBitmap mFinalBitmap;
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        mMainLooper = getMainLooper();
        super.onCreate();
        initImageLoader(getApplicationContext());

    }

    public static myApplication getApplication() {
        return mInstance;
    }


    /** 获取主线程的looper */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    /**
     * 获取FinalBitmap
     *
     * @return
     */
    public FinalBitmap getFinalBitmap() {
        if (mFinalBitmap == null) {
            mFinalBitmap = FinalBitmap.create(this);
            mFinalBitmap.configDiskCachePath(ImageUtil.getDiskCacheDir(this,
                    "imageCache/"));
            mFinalBitmap
                    .configMemoryCachePercent(0.3f);
            mFinalBitmap.configDiskCacheSize(30 * 1024 * 1024);
            mFinalBitmap.configBitmapLoadThreadSize(5);
        }
        return mFinalBitmap;
    }


    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }
}
