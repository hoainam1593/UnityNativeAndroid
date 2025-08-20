package com.hoainam.unitynativeandroid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
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

    //region get install source

    public static void GetInstallSource(Activity activity, String gameObjName, String funcName_success, String funcName_failed){
        InstallReferrerClient client = InstallReferrerClient.newBuilder(activity).build();
        client.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int code) {
                switch (code){
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try{
                            ReferrerDetails res = client.getInstallReferrer();
                            GetInstallSource_success(res.getInstallReferrer(), gameObjName, funcName_success);
                        }
                        catch (Exception e){
                            String msg = "extract install source info have exception:\n" + e.toString();
                            GetInstallSource_failed(msg, gameObjName, funcName_failed);
                        }
                        finally {
                            client.endConnection();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                        GetInstallSource_failed("get failed response from server: DEVELOPER_ERROR", gameObjName, funcName_failed);
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        GetInstallSource_failed("get failed response from server: FEATURE_NOT_SUPPORTED", gameObjName, funcName_failed);
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.PERMISSION_ERROR:
                        GetInstallSource_failed("get failed response from server: PERMISSION_ERROR", gameObjName, funcName_failed);
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                        GetInstallSource_failed("get failed response from server: SERVICE_DISCONNECTED", gameObjName, funcName_failed);
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        GetInstallSource_failed("get failed response from server: SERVICE_UNAVAILABLE", gameObjName, funcName_failed);
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                String msg = "cannot connect to Google play server";
                GetInstallSource_failed(msg, gameObjName, funcName_failed);
            }
        });
    }

    private static void GetInstallSource_success(String source, String gameObjName, String funcName_success){
        if (gameObjName != null){
            UnityPlayer.UnitySendMessage(gameObjName, funcName_success, source);
        }
        else {
            Log.d("UnityNative", "install source=" + source);
        }
    }

    private static void GetInstallSource_failed(String errMsg, String gameObjName, String funcName_failed){
        if (gameObjName != null){
            UnityPlayer.UnitySendMessage(gameObjName, funcName_failed, errMsg);
        }
        else {
            Log.e("UnityNative", errMsg);
        }
    }

    //endregion
}
