package com.wifidemo.utils;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;


/**
 * 权限工具类
 * 作者： JairusTse
 * 日期： 18/5/15 16:57
 */
public class PermissionUtil {

    //权限组，用户授权了某个权限，同一个权限组里面的其他权限也会自动授予权限。
    //这里每组选择一个权限，如果这个规则改变了，就要授权具体的权限。
    public static final String CALENDAR = "android.permission.READ_CALENDAR"; //日历
    public static final String CAMERA = "android.permission.CAMERA"; //相机
    public static final String CONTACTS = "android.permission.READ_CONTACTS"; //通讯录
    public static final String LOCATION = "android.permission.ACCESS_FINE_LOCATION"; //定位
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO"; //麦克风
    public static final String PHONE = "android.permission.CALL_PHONE"; //电话
    public static final String SENSORS = "android.permission.BODY_SENSORS"; //传感器
    public static final String SMS = "android.permission.READ_SMS"; //短信
    public static final String STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"; //存储

    //用户禁止授权的默认提示语
    private static String SHOW_AGAIN_MESSAGE = "您拒绝了授权，无法正常使用";
    //用户禁止授权并且勾选了不再询问的默认提示语
    private static String NOT_SHOW_AGAIN_MESSAGE = "您禁止了授权，请在手机设置里面授权";

    private static boolean createAPPDir = false; //是否创建APP的文件目录

    /**
     * 请求危险权限的授权
     *
     * @param activity
     * @param listener
     * @param args
     */
    public static void requestEach(Activity activity, final OnPermissionListener listener,
                                   String... args) {
        requestEach(activity, SHOW_AGAIN_MESSAGE, NOT_SHOW_AGAIN_MESSAGE, listener, args);
    }

    /**
     * 请求危险权限的授权
     *
     * @param activity
     * @param showAgainMsg    用户禁止授权的提示语
     * @param notShowAgainMsg 用户禁止授权并且勾选了不再询问的提示语
     * @param listener
     * @param args            权限组
     */
    public static void requestEach(Activity activity, final String showAgainMsg, final String
            notShowAgainMsg, final OnPermissionListener listener, String... args) {

        if (args.length == 1) {
            //只请求一个权限
            requestSingleEach(activity, showAgainMsg, notShowAgainMsg, listener, args[0]);
        } else if (args.length > 1) {
            //请求多个权限
            requestMultipleEach(activity, showAgainMsg, listener, args);
        }
    }

    /**
     * 请求单个授权
     *
     * @param activity
     * @param showAgainMsg    用户禁止授权的提示语
     * @param notShowAgainMsg 用户禁止授权并且勾选了不再询问的提示语
     * @param listener
     * @param permission      权限
     */
    private static void requestSingleEach(final Activity activity, final String showAgainMsg,
                                          final String
                                                  notShowAgainMsg, final OnPermissionListener
                                                  listener, String permission) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(permission)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {

                        if (permission.granted) {
                            //授权成功
                            if (listener != null) {
                                listener.onSucceed();
                            }
                        } else {
                            if (permission.shouldShowRequestPermissionRationale == true) {
                                //拒绝后允许再次提示
                                Toast.makeText(activity, !TextUtils.isEmpty(showAgainMsg) ?
                                        showAgainMsg :
                                        SHOW_AGAIN_MESSAGE, Toast.LENGTH_SHORT).show();
                            } else if (permission.shouldShowRequestPermissionRationale == false) {
                                //拒绝后不再提示
                                Toast.makeText(activity, !TextUtils.isEmpty(notShowAgainMsg) ?
                                        notShowAgainMsg :
                                        NOT_SHOW_AGAIN_MESSAGE, Toast.LENGTH_SHORT).show();
                            }

                            if (listener != null) {
                                listener.onFailed(permission.shouldShowRequestPermissionRationale);
                            }
                        }


                    }
                });
    }

    /**
     * 同时请求多个授权
     *
     * @param activity
     * @param showMsg  没有全部授权的提示语
     * @param listener
     * @param args     权限组
     */
    private static void requestMultipleEach(final Activity activity, final String showMsg, final
    OnPermissionListener listener, String... args) {

        //判断是否创建APP文件目录，获取存储权限的时候创建
        createAPPDir = false;
        for (String arg : args) {
            if (arg.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    arg.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                createAPPDir = true;
                break;
            }
        }

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(args)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                        if (aBoolean) {
                            //全部已经授权
                            if (listener != null) {
                                listener.onSucceed();
                            }

                        } else {
                            //起码有一个没有授权
                            if (listener != null) {
                                listener.onFailed(true);
                            }
                            //显示拒绝授权提示
                            Toast.makeText(activity, !TextUtils.isEmpty(showMsg) ? showMsg :
                                    SHOW_AGAIN_MESSAGE, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public interface OnPermissionListener {
        void onSucceed();

        void onFailed(boolean showAgain);
    }

}
