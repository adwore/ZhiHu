package com.computer.mazhihuapp.utils.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.StatFs;

import com.computer.mazhihuapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class ImageUtil {
	private static final int MB = 1024 * 1024;

	public static Bitmap mBitmap = null;

	/**
	 * Drawable转Bitmap
	 * 
	 * @param drawable
	 * @return
	 * 
	 *         zhaoyuliang
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Bitmap转Drawable
	 * 
	 * @param bitmap
	 * @return
	 * 
	 *         zhaoyuliang
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable d = new BitmapDrawable(bitmap);
		bitmap = null;
		return d;
	}

	/**
	 * 把Bitmap保存成文件
	 * 
	 * @param bmp
	 * @param filename
	 * @return
	 * 
	 *         zhaoyuliang
	 */
	public static boolean saveBitmap2File(Context context, Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		boolean b = false;
		try {
			File file = new File(getExtStorageDirectory().toString() + "/" + context.getString(R.string.file_path) + "/" + context.getString(R.string.image_data) + "/" + filename);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			stream = new FileOutputStream(file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			b = bmp.compress(format, quality, stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 读取
	 *
	 * @param bmp
	 * @param filename
	 * @return
	 *
	 *         zhaoyuliang
	 */
	public static Bitmap readFile2Bitmap(Context context, String filename) {
		mBitmap = null;
		mBitmap = BitmapFactory.decodeFile(getExtStorageDirectory().toString() + "/" + context.getString(R.string.file_path) + "/" + context.getString(R.string.image_data) + "/" + filename);
		return mBitmap;
	}

	/**
	 * 删除 文件
	 *
	 * @param context
	 * @param filename
	 * @return
	 */
	public static void delFile(Context context, String filename) {
		File myFile = new File(getExtStorageDirectory().toString() + "/" + context.getString(R.string.file_path) + "/" + context.getString(R.string.image_data) + "/" + filename);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * bitmap转byte[]
	 *
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmap2Byte(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

		return os.toByteArray();
	}

	/**
	 * 获取扩展磁盘目录
	 *
	 * @return 默认为/mnt/sdcard/
	 */
	public static File getExtStorageDirectory() {
		return getExtStorageState() ? Environment.getExternalStorageDirectory() : Environment.getDataDirectory();
	}

	/**
	 * 获取缓存路径
	 *
     * @param context
     * @param dirName Only the folder name, not contain full path.
     * @return app_cache_path/dirName
     */
    /*public static String getDiskCacheDir(Context context, String dirName) {
        final String cachePath = getExtStorageState() ?
                context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();

        return cachePath + File.separator + dirName;
    }*/

    /**
	 * 获取缓存路径
	 *
     * @param context
     * @param dirName Only the folder name, not contain full path.
     * @return app_cache_path/dirName
     */
    public static String getDiskCacheDir(Context context, String dirName) {
//        final String cachePath = getExtStorageState() ?
//                context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        String cachePath = "";
        if (getExternalCacheDirSpace(context) > 5 * 1024 * 1024) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return cachePath + File.separator + dirName;
    }

    /**
     * »ñÈ¡¿ÉÓÃ¿ÕŒä
     *
     * @return
     */
    public static long getExternalCacheDirSpace(Context context) {
        if (getExtStorageState()) {
            File path = context.getExternalCacheDir();
            if (path == null) {
                return 0;
            }
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();

            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

	/**
	 * 检测sdcard的可用状态
	 *
	 * @return
	 */
	public static boolean getExtStorageState() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * @author LiLiang
	 *
	 * @description get local video thumbnail image, must android 2.2 and above
	 * @param videoPath
	 * @param width
	 * @param height
	 * @param kind
	 * @return
	 */
	public static Bitmap getLocVidBitmap(String videoPath, int width, int height, int kind) {
		// Bitmap bitmap = null;
		mBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		// Drawable drawable =new BitmapDrawable(bitmap);

		return mBitmap;
	}

	/**
	 * @author LiLiang
	 *
	 * @description get local video thumbnail drawable, must android 2.2 and
	 *              above
	 * @param videoPath
	 * @param width
	 * @param height
	 * @param kind
	 * @return
	 */
	public static Drawable getLocVidDrawable(String videoPath, int width, int height, int kind) {
		// Bitmap bitmap = null;
		mBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		Drawable drawable = new BitmapDrawable(mBitmap);

		return drawable;
	}

	/**
	 * @author LiLiang
	 *
	 * @description save bitmap to jpeg on sdcard
	 * @param bitName
	 * @param mBitmap
	 */
	public static void saveBitmap(String bitName, Bitmap bitmap) {
		// File f = new File("/mnt/sdcard/wpk" + bitName + ".jpg");
		File f = new File(bitName);
		try {
			f.createNewFile();
		} catch (IOException e) {

		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * url转文件名
	 *
	 * @param url
	 * @return
	 */
	public static String convertUrlToFileName(String url) {
		String fileName = null;
		try {
			if (!StringUtil.isBlank(url) && url.lastIndexOf(".jpg") >= 0) {
				fileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
			} else if (!StringUtil.isBlank(url) && url.lastIndexOf(".png") >= 0) {// 默认头像
				// fileName = "portrait_.png";
				fileName = url.substring(url.lastIndexOf("/") + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * url转文件名(无限制)
	 *
	 * @param url
	 * @return
	 */
	public static String convertUrl2FileName(String url) {
		String fileName = null;
		try {
			if (!StringUtil.isBlank(url)) {
				fileName = url.substring(url.lastIndexOf("/") + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 文件名转关键字
	 *
	 * @param fileName
	 */
	public static String convertFileToKeyword(String fileName) {
		String keyword = null;
		try {
			keyword = fileName.substring(0, fileName.indexOf("_"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyword;
	}

	/**
	 * 保存缓存文件
	 *
	 * @param path
	 *            目录路径
	 * @param fileName
	 *            文件名
	 * @param rate
	 *            压缩比率
	 * @param d
	 *            保存的图片
	 */
	public synchronized static void saveImageCache(String path, String fileName, int rate, Drawable d,CompressFormat format) {
		File dir = new File(path);// "/mnt/sdcard/wpk/imageCache/firstFrame/"
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (freeSpaceOnSd() < 20) {// 如果SDCard小于20M，不存储
			return;
		}

		File bitmapFile = new File(path + fileName);
		if (!bitmapFile.exists()) {
			// FileOutputStream fos;
			try {
				mBitmap = null;
				mBitmap = drawable2Bitmap(d);
				if (mBitmap != null) {
					FileOutputStream fos = new FileOutputStream(bitmapFile.getPath());
					mBitmap.compress(format, rate, fos);
					fos.flush();
					fos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

			}
		}
	}

	/**
	 * 读取缓存文件
	 *
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static Drawable readImageCache(String path, String fileName) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;// 2:表示图片宽高都为原来的二分之一，即图片为原来的四分之一
			mBitmap = null;
			File file = new File(path + fileName);
			if (file.length() == 0) {
				if (file.exists())
					file.delete();
				return null;
			}
//			Log.d("file", "file size = " + file.length());
			mBitmap = BitmapFactory.decodeFile(path + fileName, options);
			if (mBitmap != null) {
				Drawable draw = bitmap2Drawable(mBitmap);
				mBitmap = null;
				return draw;
			} else {
				return null;
			}

			// Drawable drawable = Drawable.createFromPath(path+fileName);
			// return drawable;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

	}

	/** * 计算sdcard上的剩余空间 * @return */
	public static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 根据视频地址和分辨率 转成视频首帧地址
	 *
	 * @param videoUrl
	 *            视频Url
	 * @param resolution
	 *            图片格式：小图：cif,大图：vga
	 * @return
	 */
	public static String videoUrl2FirstFrameUrl(String videoUrl, String resolution) {
		String firstFrameUrl = null;
		if (videoUrl != null && !"".equals(videoUrl)) {
			if (resolution == null || "".equals(resolution))
				resolution = "vga";
			firstFrameUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_" + resolution + "_0.jpg";
		}
		return firstFrameUrl;
	}

	/**
	 * 轮播视频地址转图片地址
	 *
	 * @param videoUrl
	 *            视频地址
	 * @param resolution
	 *            暂时只有HD
	 * @return
	 */
	public static String videoUrl2ImageUrl(String videoUrl, String resolution) {
		String firstFrameUrl = null;
		if (!StringUtil.isBlank(videoUrl)) {
			if (StringUtil.isBlank(resolution))
				resolution = "HD";
			firstFrameUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_" + resolution + ".jpg";
		}
		return firstFrameUrl;
	}

	/**
	 * 图片地址转html地址
	 *
	 * @param imageUrl
	 *            图片地址
	 * @param id
	 *            图片Id
	 * @return
	 */
	public static String ImageUrl2HtmlUrl(String imageUrl, String id) {
		String htmlUrl = null;
		if (!StringUtil.isBlank(imageUrl)) {
			htmlUrl = imageUrl.substring(0, imageUrl.lastIndexOf(".")) + "/" + id + ".html";
		}
		return htmlUrl;
	}

	/**
	 * 检查图片类型
	 *
	 * @param filename
	 * @return vga:返回true，其他返回false
	 */
	public static boolean checkPicType(String filename) {
		if (filename != null && filename.contains("_vga_")) {
			return true;
		}
		return false;
	}

	// public static void releaseBitmap(Bitmap bitmap) {
	// if (bitmap != null && !bitmap.isRecycled()) {
	// bitmap.recycle();
	// }
	//
	// }

	/**
	 * 画一个圆角图
	 *
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	 public Bitmap convertToBitmap(String path, int w, int h) {

		             BitmapFactory.Options opts = new BitmapFactory.Options();
		             // 设置为ture只获取图片大小
		            opts.inJustDecodeBounds = true;
		            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		            // 返回为空
		            BitmapFactory.decodeFile(path, opts);
		             int width = opts.outWidth;
		            int height = opts.outHeight;
		            float scaleWidth = 0.f, scaleHeight = 0.f;
		             if (width > w || height > h) {
		                // 缩放
		                 scaleWidth = ((float) width) / w;
		                 scaleHeight = ((float) height) / h;
		             }
		             opts.inJustDecodeBounds = false;
		             float scale = Math.max(scaleWidth, scaleHeight);
		            opts.inSampleSize = (int)scale;
		            WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		            return Bitmap.createScaledBitmap(weak.get(), w, h, true);
		         }

}
