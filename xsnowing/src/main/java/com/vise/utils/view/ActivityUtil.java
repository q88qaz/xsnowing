package com.vise.utils.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vise.log.ViseLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by xyy on 16/4/10.
 */
public class ActivityUtil {

    public static void startForwardActivity(Activity context, Class<?> forwardActivity) {
        startForwardActivity(context, forwardActivity, false);
    }

    public static void startForwardActivity(Activity context, Class<?> forwardActivity, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
    }

    public static void startForwardActivity(Activity context, Class<?> forwardActivity, Bundle bundle, Boolean
            isFinish, int animIn, int animOut) {
        Intent intent = new Intent(context, forwardActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
        try {
            context.overridePendingTransition(animIn, animOut);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.e(e);
        }
    }

    public static void startForwardActivity(Activity context, Class<?> forwardActivity, Bundle bundle, Boolean
            isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
    }

    public static void startForResultActivity(Activity context, Class<?> forwardActivity, int requestCode, Bundle
            bundle, Boolean isFinish) {
        Intent intent = new Intent(context, forwardActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
        if (isFinish) {
            context.finish();
        }
    }

    public static void startForResultActivity(Activity context, Class<?> forwardActivity, int requestCode, Bundle
            bundle, Boolean isFinish, int animIn, int animOut) {
        Intent intent = new Intent(context, forwardActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
        if (isFinish) {
            context.finish();
        }
        try {
            context.overridePendingTransition(animIn, animOut);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.e(e);
        }
    }

    /**
     * @param @param  context
     * @param @return 设定文件
     * @return String    返回类名
     * @Title: getTopActivity
     * @Description: 获取栈顶activity
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);

        if (runningTaskInfo != null)
            return (runningTaskInfo.get(0).topActivity.getClassName()).toString();
        else
            return "";
    }

    /**
     * 判断某一Activity是否在当前栈顶
     *
     * @return true 当前Activity在栈顶，即在最前端显示
     * false 当前Activity不在栈顶，即在后台运行
     */
    public static boolean isTopActivity(Context context, String className) {
        final String topActivity = getTopActivity(context);
        if (className.equals(topActivity))
            return true;
        return false;
    }

    /**
     * 设置Activity全屏显示。
     *
     * @param activity Activity引用
     * @param isFull   true为全屏，false为非全屏
     */
    public static void setFullScreen(Activity activity, boolean isFull) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (isFull) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 默认隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    public static void hideSoftInput(Activity activity, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏Activity的系统默认标题栏
     *
     * @param activity Activity对象
     */
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 强制设置Activity的显示方向为垂直方向。
     *
     * @param activity Activity对象
     */
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 强制设置Activity的显示方向为横向。
     *
     * @param activity Activity对象
     */
    public static void setScreenHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 使UI适配输入法
     *
     * @param activity
     */
    public static void adjustSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    // 以下解决Android 8.0透明Activity指定屏幕方向崩溃问题
    private static int isOreo = 0;
    private static final int VERSION_OREO = Build.VERSION_CODES.O;

    /**
     * java.lang.IllegalStateException: Only fullscreen opaque activities can request orientation
     * <p>
     * 修复android 8.0存在的问题
     * <p>
     * 在Activity中onCreate()中super之前调用
     *
     * @param activity
     */
    public static void lookOrientation(Activity activity) {
        //目标版本8.0及其以上
        if (isOreo == 0) {
            if (activity.getApplicationInfo().targetSdkVersion == VERSION_OREO
                    || Build.VERSION.SDK_INT == VERSION_OREO) {
                isOreo = 1;
            } else {
                isOreo = 2;
            }
        }
        if (isOreo == 1 && isTranslucentOrFloating(activity)) {
            fixOrientation(activity);
        }
    }

    /**
     * 设置屏幕不固定，绕过检查
     *
     * @param activity
     */
    private static void fixOrientation(Activity activity) {
        try {
            Class<Activity> activityClass = Activity.class;
            Field mActivityInfoField = activityClass.getDeclaredField("mActivityInfo");
            mActivityInfoField.setAccessible(true);
            ActivityInfo activityInfo = (ActivityInfo) mActivityInfoField.get(activity);
            //设置屏幕不固定
            activityInfo.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查屏幕 横竖屏或者锁定就是固定
     *
     * @param activity
     * @return
     */
    @SuppressLint("SoonBlockedPrivateApi")
    private static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            Class<?> styleableClass = Class.forName("com.android.internal.R$styleable");
            Field WindowField = styleableClass.getDeclaredField("Window");
            WindowField.setAccessible(true);
            int[] styleableRes = (int[]) WindowField.get(null);
            //先获取到TypedArray
            final TypedArray typedArray = activity.obtainStyledAttributes(styleableRes);
            Class<?> ActivityInfoClass = ActivityInfo.class;
            //调用检查是否屏幕旋转
            Method isTranslucentOrFloatingMethod = ActivityInfoClass.getDeclaredMethod("isTranslucentOrFloating", TypedArray.class);
            isTranslucentOrFloatingMethod.setAccessible(true);
            isTranslucentOrFloating = (boolean) isTranslucentOrFloatingMethod.invoke(null, typedArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }
}
