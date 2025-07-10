package com.hoainam.unitynativeandroid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.unity3d.player.UnityPlayer;

public class UnityNativeUtils {

    //region open store page

    public static void OpenStorePage(Activity activity, String packageName) {
        try {
            String url = "market://details?id=" + packageName;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //if play store not present, open web browser instead
            OpenAppOnBrowser(activity, packageName);
        }
    }

    private static void OpenAppOnBrowser(Activity activity, String packageName){
        String url = "https://play.google.com/store/apps/details?id=" + packageName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    //endregion

    //region get device id

    public static void GetDeviceId(Activity activity, String gameObjName, String funcName_success, String funcName_failed){
        try {
            new Thread(()->{
                try{
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity);
                    String adId = adInfo.getId();

                    GetDeviceId_success(adId, gameObjName, funcName_success);
                }
                catch (Exception e){
                    String msg = "cannot get ad id, msg=" + e.getMessage();

                    GetDeviceId_failed(msg, gameObjName, funcName_failed);
                }
            }).start();
        }
        catch (Exception e) {
            String msg = "cannot run another thread, msg=" + e.getMessage();

            GetDeviceId_failed(msg, gameObjName, funcName_failed);
        }
    }

    private static void GetDeviceId_success(String adId, String gameObjName, String funcName_success){
        if (gameObjName != null){
            UnityPlayer.UnitySendMessage(gameObjName, funcName_success, adId);
        }
        else {
            Log.d("UnityNative", "ad id=" + adId);
        }
    }

    private static void GetDeviceId_failed(String errMsg, String gameObjName, String funcName_failed){
        if (gameObjName != null){
            UnityPlayer.UnitySendMessage(gameObjName, funcName_failed, errMsg);
        }
        else {
            Log.e("UnityNative", errMsg);
        }
    }

    //endregion
}
