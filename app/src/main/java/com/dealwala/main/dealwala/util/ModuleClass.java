package com.dealwala.main.dealwala.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;


/**
 * Created by IBU on 6/27/2015.
 */
public class ModuleClass {

    public static String SERVER_PATH = "http://www.ibizsolutions.net/dealwala/api/index.php?webmethod=";
    //public static String IMAGE_SERVER_PATH = "http://www.yourbikebuddy.com/api/bikeyourride/upload/";
    public static String LIVE_API_PATH = "http://www.ibizsolutions.net/dealwala/api/";

    public static String USER_ID = "9";
    public static String USER_NAME = "";

    public static SharedPreferences appPreferences;

    public static String APP_PREFERENCE = "app_preference";

    public static String KEY_IS_REMEMBER = "key_is_remember";
    public static String KEY_USER_ID = "key_user_id";
    public static String KEY_USER_NAME = "key_user_name";
    public static String KEY_EMAIL_ID = "key_email_id";
    public static String KEY_PASSWORD = "key_password";

    public static double locationLatitude = 0.0;
    public static double locationLongitude = 0.0;

    public static Typeface typefaceRThin;
    public static Typeface typefaceRLight;
    public static Typeface typefaceRRegular;
    public static Typeface typefaceRCLight;
    public static Typeface typefaceRCRegular;

    public static Typeface typefaceBariol;
    public static Typeface typefaceLatoLight;
    public static Typeface typefaceLatoThin;
    public static Typeface typefaceOpenSansLight;
    public static Typeface typefaceOstrichSansMedium;
    public static Typeface typefaceOstrichSansMediumRounded;
    public static Typeface typefaceOswald;

    public static int notification_count = 0;

    public static GPSTracker gpsTracker;

    public static boolean isInternetOn;

    public static boolean setLocationParameters(Context context) {

        /*LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //criteria object will select best service based on
        //Accuracy, power consumption, response, bearing and monetary cost
        //set false to use best service otherwise it will select the default Sim network
        //and give the location based on sim network
        //now it will first check satellite than Internet than Sim network location
        String provider = locationManager.getBestProvider(criteria, false);
        //now you have best provider
        //get location
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //get latitude and longitude of the location
            Log.v("Notification", "Location is not null...");
            locationLongitude = location.getLongitude();
            locationLatitude = location.getLatitude();
        } else {
            Log.v("Notification", "Location is null...");
            locationLatitude = 0.0;
            locationLongitude = 0.0;
        }*/

        gpsTracker = new GPSTracker(context);
        //locationLatitude = gpsTracker.getLatitude();
        //locationLongitude = gpsTracker.getLongitude();
        //gpsTracker.stopUsingGPS();

        return gpsTracker.canGetLocation();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        /*Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);*/
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }
}
