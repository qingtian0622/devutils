package com.ht.devutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 抓取应用bug，错误信息已json字符串的形式保留，可以上传给服务器
 * 在应用的application里面要进行初始化
 * CrashUtils crashUtil=CrashUtils.getInstance();
 * crashUtils.init(this);
 *
 * Created by huangtao on 2017/3/18.
 */

public class CrashUtils implements Thread.UncaughtExceptionHandler
{

    private static final String TAG = "CrashUtils";

    private volatile static CrashUtils mInstance;

    private Thread.UncaughtExceptionHandler mHandler;

    private boolean mInitialized;
    private String versionName;
    private int versionCode;

    private CrashUtils()
    {
    }

    public static CrashUtils getInstance()
    {
        if (mInstance == null)
        {
            synchronized (CrashUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new CrashUtils();
                }
            }
        }
        return mInstance;
    }

    public boolean init(Context context)
    {
        if (mInitialized)
            return true;
        try
        {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable)
    {
        Throwable ex = throwable.getCause() == null ? throwable : throwable.getCause();
        StackTraceElement[] stacks = ex.getStackTrace();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"deviceinfo\":" + getCrashHead() + ",");
        sb.append("\"error_msg\":" + getMessage(stacks[0]) + ",");
        sb.append("\"error_summer\":\"" + ex.toString() + "\",");
        sb.append("\"error_time\":\"" + now + "\"}");


        Log.e(TAG, "uncaughtException: " + sb.toString());

        if (mHandler != null)
        {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    private String getMessage(StackTraceElement ste)
    {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"error_path\":\"" + ste.getClassName() + "\",");
        sb.append("\"error_classname\":\"" + ste.getFileName() + "\",");
        sb.append("\"error_method\":\"" + ste.getMethodName() + "\",");
        sb.append("\"error_location\":\"" + "第" + ste.getLineNumber() + "行\"}");
        Log.e(TAG, "getMessage: " + ste.toString());
        return sb.toString();
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead()
    {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"phone_brand\":\"" + Build.BRAND + "\",");
        sb.append("\"phone_model\":\"" + Build.MODEL + "\",");
        sb.append("\"system_version\":\"" + Build.VERSION.RELEASE + "\",");
        sb.append("\"sdk_version\":\"" + Build.VERSION.SDK_INT + "\",");
        sb.append("\"versionName\":\"" + versionName + "\",");
        sb.append("\"versionCode\":\"" + versionCode + "\"}");

        return sb.toString();
    }
}
