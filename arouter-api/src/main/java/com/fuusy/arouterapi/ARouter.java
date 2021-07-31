package com.fuusy.arouterapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

public class ARouter {
    private static final String TAG = "ARouter";

    private static ARouter instance;
    private Context mContext;
    private static Map<String, Class<? extends Activity>> activityMap;

    public static ARouter getInstance() {
        if (instance == null) {
            synchronized (ARouter.class) {
                instance = new ARouter();
                activityMap = new ArrayMap<>();
            }

        }
        return instance;
    }

    /**
     * 将activity压入
     *
     * @param activityName
     * @param cls
     */
    public void putActivity(String activityName, Class cls) {
        Log.d(TAG, "putActivity: "+activityName);
        if (cls != null && !TextUtils.isEmpty(activityName)) {
            activityMap.put(activityName, cls);
        }
    }

    public void init(Context context) {
        Log.d(TAG, "init: ");
        mContext = context;
        List<String> className = getAllActivityUtils("com.fuusy.arouterapi");
        for (String cls : className) {
            try {
                Class<?> aClass = Class.forName(cls);
                if (IRouter.class.isAssignableFrom(aClass)) {
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {

            }

        }
    }

    /**
     * 通过之前定义的path就行启动
     *
     * @param activityName
     */
    public void jumpActivity(String activityName) {
        jumpActivity(activityName, null);
    }

    public void jumpActivity(String activityName, Bundle bundle) {
        Intent intent = new Intent();
        Class<? extends Activity> aCls = activityMap.get(activityName);
        Log.d(TAG, "jumpActivity: " + aCls);
        if (aCls == null) {
            return;
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(mContext, aCls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "jumpActivity: startActivity");
        mContext.startActivity(intent);
    }

    public List<String> getAllActivityUtils(String packageName) {
        List<String> list = new ArrayList<>();
        String path;
        try {
            path = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0).sourceDir;
            DexFile dexFile = null;
            dexFile = new DexFile(path);
            Enumeration enumeration = dexFile.entries();
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                if (name.contains(packageName)) {
                    list.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
