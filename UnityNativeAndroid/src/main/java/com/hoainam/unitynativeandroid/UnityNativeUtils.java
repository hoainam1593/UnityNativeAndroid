package com.hoainam.unitynativeandroid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

public class UnityNativeUtils {

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

    public static void GetDeviceId(){

    }
}
