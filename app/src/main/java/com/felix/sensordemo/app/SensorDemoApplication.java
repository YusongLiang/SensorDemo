package com.felix.sensordemo.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.felix.sensordemo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Felix
 */
public class SensorDemoApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    private static List<Activity> mActivities;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mActivities = new ArrayList<>();
    }

    /**
     * 获取当前App的{@link Context}
     *
     * @return 当前Application对象
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 添加{@link Activity}到集合中
     *
     * @param activity 要添加的{@link Activity}
     */
    public static void addActivity(Activity activity) {
        mActivities.add(activity);
        Log.d(Constants.APP_TAG, "create activity: " + activity.getClass().getSimpleName());
    }

    /**
     * 将指定{@link Activity}移除出集合
     *
     * @param target 要移除的{@link Activity}
     */
    public static void removeActivity(Activity target) {
        for (Activity activity : mActivities) {
            if (activity.hashCode() == target.hashCode()) {
                mActivities.remove(activity);
                Log.d(Constants.APP_TAG, "destroy activity: " + target.getClass().getSimpleName());
                break;
            }
        }
    }
}
