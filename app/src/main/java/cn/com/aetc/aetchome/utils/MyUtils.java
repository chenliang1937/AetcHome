package cn.com.aetc.aetchome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by chenliang on 16/7/25.
 */

public class MyUtils {

    //文件保存路径--隐藏文件夹,加个"."
    public static final String SAVE_PATH = "/.aetc/";

    /**
     * 设置状态栏透明
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 导航栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * dp2px
     */
    public static int dip2px(Context ctx,float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     *	px2dp
     */
    public static int px2dip(Context ctx,float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取文件的保持目录
     * @param fileName
     * @return
     */
    public static String getSavePath(String fileName) {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath() + SAVE_PATH + fileName;
        }
        return path;
    }

    /**
     * bitmap 转 file
     * @param filePath
     * @param bm
     * @return
     */
    public static File bitmap2File(String filePath,Bitmap bm){
        try {
            filePath = getSavePath(filePath);
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开activity
     * @param context
     * @param cls
     */
    public static void startActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

}
