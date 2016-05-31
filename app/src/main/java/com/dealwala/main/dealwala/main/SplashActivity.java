package com.dealwala.main.dealwala.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.login.LoginActivity;
import com.dealwala.main.dealwala.util.ConnectionDetector;
import com.dealwala.main.dealwala.util.ModuleClass;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ModuleClass.appPreferences = getSharedPreferences(ModuleClass.APP_PREFERENCE, MODE_PRIVATE);

        ModuleClass.USER_ID = ModuleClass.appPreferences.getString(ModuleClass.KEY_USER_ID,"0");
        ModuleClass.USER_NAME = ModuleClass.appPreferences.getString(ModuleClass.KEY_USER_NAME,"");

        new CheckInternetConnectionTask().execute();
    }

    class BackGroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void checkInternetInLollipop() {

        final ConnectivityManager connectivity = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        Log.v("Notification", "Checking internet lollipop");

        Log.v("Notification", (connectivity != null) ? "Connectivity NOT null" : "Connectivity NULL");

        if (connectivity != null) {

            Log.v("Notification", "SDK Int  : " + Build.VERSION.SDK_INT + " LOllipop : " + Build.VERSION_CODES.LOLLIPOP);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 ) {

                    Log.v("Notification", "Permissions granted...");

                    NetworkRequest.Builder builder = new NetworkRequest.Builder();

                    builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

                    connectivity.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            Log.d("Notification", "Internet connected");

                            ModuleClass.isInternetOn = true;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                connectivity.unregisterNetworkCallback(this);
                            }

                            SplashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ModuleClass.appPreferences.getBoolean(ModuleClass.KEY_IS_REMEMBER, false)) {
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onLost(Network network) {

                            Log.v("Notification", "LOST connection");
                            ModuleClass.isInternetOn = false;
                        }

                        @Override
                        public void onLosing(Network network, int maxMsToLive) {
                            super.onLosing(network, maxMsToLive);
                            Log.v("Notification", "LOOSING connection");
                        }
                    });
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP ) {
                    Log.v("Notification", "Asking for Permissions...");

                    if (!Settings.System.canWrite(SplashActivity.this)) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new MaterialDialog.Builder(SplashActivity.this)
                                        .title("Permission")
                                        .content("Shoppin can not go ahead without you allowing this permission please allow us. ")
                                        .iconRes(R.mipmap.ic_launcher)
                                        .positiveText("Go to Setting")
                                        .negativeText("Exit")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();

                                                Intent goToSettings = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                goToSettings.setData(Uri.parse("package:" + SplashActivity.this.getPackageName()));
                                                startActivityForResult(goToSettings, 1001);


                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                                SplashActivity.this.finish();
                                            }
                                        })
                                        .autoDismiss(false)
                                        .show();
                            }
                        });

                    } else {
                        if (ModuleClass.appPreferences.getBoolean(ModuleClass.KEY_IS_REMEMBER, false)) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        } else {
            ModuleClass.isInternetOn = false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ConnectivityManager connectivity = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();

                builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

                connectivity.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.d("Notification", "Internet connected");

                        ModuleClass.isInternetOn = true;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            connectivity.unregisterNetworkCallback(this);
                        }

                        SplashActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ModuleClass.appPreferences.getBoolean(ModuleClass.KEY_IS_REMEMBER, false)) {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }

                    @Override
                    public void onLost(Network network) {

                        Log.v("Notification", "LOST connection");
                        ModuleClass.isInternetOn = false;
                    }

                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                        Log.v("Notification", "LOOSING connection");
                    }
                });
            }
        }
    }

    class CheckInternetConnectionTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                checkInternetInLollipop();

                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {

                    Runnable timeoutThread = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);

                                if (!ModuleClass.isInternetOn) {
                                    SplashActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (SplashActivity.this != null) {
                                                new MaterialDialog.Builder(SplashActivity.this)
                                                        .title("Shoppin")
                                                        .content("Unable to proceed: No Internet Connection, Kindly re-load the APP once you have Internet Connectivity!")
                                                        .iconRes(R.mipmap.ic_launcher)
                                                        .positiveText("Ok")
                                                        .show();
                                            }

                                        }
                                    });
                                }

                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                    };

                    timeoutThread.run();
                }

            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ModuleClass.isInternetOn = new ConnectionDetector(SplashActivity.this).isConnectingToInternet();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (ModuleClass.isInternetOn) {
                    Log.v("Notification", "Internet is connected");
                    if (ModuleClass.appPreferences.getBoolean(ModuleClass.KEY_IS_REMEMBER, false)) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    new MaterialDialog.Builder(SplashActivity.this)
                            .title("Shoppin")
                            .content("Unable to proceed: No Internet Connection, Kindly re-load the APP once you have Internet Connectivity!")
                            .iconRes(R.mipmap.ic_launcher)
                            .positiveText("OK")
                            .show();

                }
            }
        }
    }
}
