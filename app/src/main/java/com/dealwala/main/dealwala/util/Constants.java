package com.dealwala.main.dealwala.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RatingBar;

import com.dealwala.main.dealwala.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Constants {
    public static final int ADD_SHARE_BIKE_INTENT_ID = 999;
    public static final boolean CRASH_APPLICATION = false;
    public static final boolean DEBUG_BACKUP_DATABASE;
    public static final boolean DEBUG_SHOW_DBHELPER_STATUS;
    public static final boolean DEBUG_SHOW_ENGINE_DEBUG_BAR;
    public static final boolean DEBUG_SKIP_WELCOME_SYNC;
    public static final boolean DEBUG_SWITCH_SERVER;
    public static final boolean DISPLAY_WW_AUDIT_OPTIONS = false;
    public static final boolean DWL_ENABLED = true;
    public static final boolean IS_PRODUCTION_RELEASE = false;
    public static final String PREFERENCES_LOGGED_USER_ID = "USER_ID";
    public static final String PREFERENCES_LOGGED_USER_NAME = "USER_NAME";
    public static final String PREFERENCES_LOGGED_USER_EMAIL = "USER_EMAIL";
    public static final String PREFERENCES_LOGGED_USER_PASSWORD = "USER_PASSWORD";
    public static final String PREFERENCES_LOGGED_USER_MOBILE = "USER_MOBILE";
    public static final String PREFERENCES_NAME = "YBBPREFERENCES";
    public static final boolean SHOW_GRAPH_FILTERING;
    public static final boolean USE_SPEECH;
    public static final String UTF8 = "UTF-8";
    public static final String MIME_TYPE = "text/html; charset=UTF-8";

    static {
        DEBUG_BACKUP_DATABASE = setDevFlag(true);
        DEBUG_SWITCH_SERVER = setDevFlag(true);
        DEBUG_SKIP_WELCOME_SYNC = setDevFlag(false);
        DEBUG_SHOW_DBHELPER_STATUS = setDevFlag(true);
        DEBUG_SHOW_ENGINE_DEBUG_BAR = setDevFlag(false);
        SHOW_GRAPH_FILTERING = setDevFlag(false);
        USE_SPEECH = setDevFlag(false);
    }

    public static void clearUserId(Context paramContext) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.remove(PREFERENCES_LOGGED_USER_ID);
        editor.commit();
    }

    public static void clearUserEmail(Context paramContext) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.remove(PREFERENCES_LOGGED_USER_EMAIL);
        editor.commit();
    }

    public static void clearUserPassword(Context paramContext) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.remove(PREFERENCES_LOGGED_USER_PASSWORD);
        editor.commit();
    }



    public static String getRupeesSymbol(Context paramContext) {
        //return paramContext.getResources().getString(R.string.rs_lettor) + " ";
        return "INR ";
    }

    public static String getUserId(Context paramContext) {
        return paramContext.getSharedPreferences(PREFERENCES_NAME, 0).getString(PREFERENCES_LOGGED_USER_ID, "");
    }

    public static String getUserEmail(Context paramContext) {
        return paramContext.getSharedPreferences(PREFERENCES_NAME, 0).getString(PREFERENCES_LOGGED_USER_EMAIL, "");
    }

    public static String getUserPassword(Context paramContext) {
        return paramContext.getSharedPreferences(PREFERENCES_NAME, 0).getString(PREFERENCES_LOGGED_USER_PASSWORD, "");
    }

    public static String getUserMobile(Context paramContext) {
        return paramContext.getSharedPreferences(PREFERENCES_NAME, 0).getString(PREFERENCES_LOGGED_USER_MOBILE, "");
    }

    public static String getUserName(Context paramContext) {
        return paramContext.getSharedPreferences(PREFERENCES_NAME, 0).getString(PREFERENCES_LOGGED_USER_NAME, "");
    }

    public static String getValidPrice(String paramString) {
        String str;
        if ((paramString != null) && (paramString.length() != 0)) {
            str = paramString;
            if (!paramString.contains("null")) {
            }
        } else {
            str = "No price available!";
        }
        return str;
    }

    public static String getValidRating(String paramString) {
        String str = "0.000";
        if ((paramString != null) && (paramString.length() != 0)) {
            if (!paramString.contains("null")) {
                double rating = Double.parseDouble(paramString);
                int intRating = (int) Math.round(rating);
                str = "" + intRating;
            }
        } else {
            str = "0.000";
        }
        return str;
    }

    public static boolean isJSONValid(String paramString) {
        try {
            new JSONObject(paramString);
            return true;
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        return false;
    }

    public static final boolean isOnline(Context paramContext) {
        NetworkInfo info = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (info != null) && (info.isConnectedOrConnecting());
    }

    public static boolean isUserLoggedin(Context paramContext) {
        return true;
    }

    public static void saveUserId(Context paramContext, String userID) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.putString(PREFERENCES_LOGGED_USER_ID, userID);
        editor.commit();
    }

    public static void saveUserEmail(Context paramContext, String userEmail) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.putString(PREFERENCES_LOGGED_USER_EMAIL, userEmail);
        editor.commit();
    }

    public static void saveUserPassword(Context paramContext, String userPassword) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.putString(PREFERENCES_LOGGED_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public static void saveUserMobile(Context paramContext, String userMobile) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.putString(PREFERENCES_LOGGED_USER_MOBILE, userMobile);
        editor.commit();
    }

    public static void saveUserName(Context paramContext, String userName) {
        SharedPreferences.Editor editor = paramContext.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        editor.putString(PREFERENCES_LOGGED_USER_NAME, userName);
        editor.commit();
    }

    public static boolean setDevFlag(boolean paramBoolean) {
        return paramBoolean;
    }

    public static void setRatingBarColor(RatingBar ratingBar, Context paramContext) {
        LayerDrawable progressDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        //progressDrawable.getDrawable(2).setColorFilter(paramContext.getResources().getColor(R.color.action_bar_red), PorterDuff.Mode.SRC_ATOP);
        progressDrawable.getDrawable(2).setColorFilter(paramContext.getResources().getColor(R.color.action_bar_blue), PorterDuff.Mode.SRC_ATOP);
        progressDrawable.getDrawable(1).setColorFilter(paramContext.getResources().getColor(R.color.ratingStar_empty), PorterDuff.Mode.SRC_ATOP);
        progressDrawable.getDrawable(0).setColorFilter(paramContext.getResources().getColor(R.color.ratingStar_empty), PorterDuff.Mode.SRC_ATOP);
    }
}
