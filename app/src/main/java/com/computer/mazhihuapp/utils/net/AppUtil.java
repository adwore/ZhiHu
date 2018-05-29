package com.computer.mazhihuapp.utils.net;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;

import com.computer.mazhihuapp.R;
import com.computer.mazhihuapp.activity.DownloadActivity;
import com.computer.mazhihuapp.application.myApplication;

import java.util.List;

/**
 * @author zhaoyuliang
 * @date 2012 上午10:48:18
 */
public class AppUtil {

	/**
	 * 获取当前应用版本名
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		String version = "";
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;

	}

	/**
	 * 获取当前应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		int versionCode = 0;
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			versionCode = 12;
			e.printStackTrace();
		}
		return versionCode;

	}

	/**
	 * 获取Umeng渠道号
	 * 
	 * @param context
	 * @return 渠道号
	 */
	public static String getChannel(Context context) {
		String channel = "bbliveBYD";
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (info != null && info.metaData != null) {
				String metaData = info.metaData.getString("UMENG_CHANNEL");
				if (!StringUtil.isBlank(metaData)) {
					channel = metaData;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return channel;
	}

	/**
	 * 是否已存在桌面快捷方式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasShortcut(Context context) {
		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();

		int versionLevel = android.os.Build.VERSION.SDK_INT;
		String AUTHORITY = "com.android.launcher2.settings";

		// 2.2以上的系统的文件文件名字是不一样的
		if (versionLevel >= 8) {
			AUTHORITY = "com.android.launcher2.settings";
		} else {
			AUTHORITY = "com.android.launcher.settings";
		}

		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?", new String[] { context.getString(R.string.app_name) }, null);

		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
			c.close();
		}
		return isInstallShortcut;
	}

	/**
	 * 添加桌面快捷方式
	 * 
	 * @param context
	 */
	public static void addShortCut(Context context) {
		if (hasShortcut(context)) {
			return;
		}
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
		// ShortcutIconResource iconRes =
		// Intent.ShortcutIconResource.fromContext(context.getApplicationContext(),
		// R.drawable.logo_ooupai);
		// shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON,iconRes);

		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);

		// 设置桌面快捷方式的图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(context, DownloadActivity.class);

		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 广播通知桌面去创建
		context.sendBroadcast(shortcut);
	}

	/**
	 * 判断服务是否已经开启
	 * 
	 * @param context
	 * @param serviceName 服务名称
	 * @return true：开启，false：未开启
	 */
	public static boolean isStartedService(Context context, String serviceName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(50);
		for (RunningServiceInfo serviceInfo : serviceList) {
			if (serviceName.equals(serviceInfo.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	
	public static Context getContext() {
		return myApplication.getApplication();
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		// 获得主线程的looper
		Looper mainLooper = myApplication.getMainThreadLooper();
		// 获取主线程的handler
		Handler handler = new Handler(mainLooper);
		return handler;
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}
}
