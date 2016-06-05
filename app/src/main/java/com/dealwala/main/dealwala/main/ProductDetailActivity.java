package com.dealwala.main.dealwala.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.util.JSONParser;
import com.dealwala.main.dealwala.util.ModuleClass;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ProductDetailActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ImageView imgMapView;
    MapView mapView;
    GoogleMap map;
    String dealId;
    TextView tvDealTitle, tvDealDesc, tvDealLongDesc, tvShopName, tvShopAddress, tvShopDistance, tvDealOrgValue, tvDealDiscValue;
    JSONObject resultObject;
    Button btnSaveForLater;

    ZXingScannerView scannerView;

    private MaterialDialog dialogQRRedeem;

    private String idVerify;

    FragmentManager fm;

    public static final int REQUEST_PERMISSION_CAMERA = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null)
            dealId = getIntent().getExtras().getString("dealId");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnRedeem = (Button) findViewById(R.id.btnRedeemNow);
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ProductDetailActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ProductDetailActivity.this.requestPermissions(new String[]{Manifest.permission.CAMERA},
                                REQUEST_PERMISSION_CAMERA);
                    } else {
                        //showQRDialog();
                        openQRScanFragment();
                    }

                }else{
                    //showQRDialog();
                    openQRScanFragment();
                }

            }
        });

        btnSaveForLater = (Button) findViewById(R.id.btnSaveForLater);
        btnSaveForLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                if(resultObject != null) {
                    if (resultObject.getString("IsSaved").equals("0")) {
                        if (ModuleClass.isInternetOn)
                            new SaveDealLaterTask(dealId).execute();
                    } else {
                        Toast.makeText(ProductDetailActivity.this,"This deal is already saved", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            }
        });

        tvDealDesc = (TextView) findViewById(R.id.tvDealDesc);
        tvDealDiscValue = (TextView) findViewById(R.id.tvDealDiscountValue);
        tvDealLongDesc = (TextView) findViewById(R.id.tvLongDesc);
        tvDealLongDesc.setVisibility(View.GONE);
        tvDealOrgValue = (TextView) findViewById(R.id.tvDealOriginalValue);
        tvDealDiscValue = (TextView) findViewById(R.id.tvDealDiscountValue);
        tvDealTitle = (TextView) findViewById(R.id.tvDealTitle);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        tvShopDistance = (TextView) findViewById(R.id.tvShopDistance);
        tvShopName = (TextView) findViewById(R.id.tvShopName);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        Log.v("Notification", "Internet ON : " + ((ModuleClass.isInternetOn) ? "YES" : "NO"));
        //if (ModuleClass.isInternetOn) {
        new GetDealDetailTask(dealId).execute();
        //}
    }

    public void openQRScanFragment(){
        QRScannerFragment fragment = new QRScannerFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment.setFragmentManager(fm);
        fragment.setHandler(handler);
        Bundle args = new Bundle();
        args.putString("verification_code",idVerify);
        fragment.setArguments(args);
        ft.replace(R.id.fragment_container,fragment);
        ft.addToBackStack("");
        ft.commit();
    }

    public void showQRDialog(){
        dialogQRRedeem = new MaterialDialog.Builder(ProductDetailActivity.this)
                .title("Scan QR code to get the Offer")
                .customView(R.layout.dialog_redeem, true)
                .titleGravity(GravityEnum.CENTER)
                .negativeText("CLOSE")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeColorRes(R.color.text_blue)
                .buttonsGravity(GravityEnum.CENTER)
                .build();

        dialogQRRedeem.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.v("Notification", "Dismiss called");
                scannerView.stopCamera();
            }
        });

        View view = dialogQRRedeem.getCustomView();

        scannerView = (ZXingScannerView) view.findViewById(R.id.scannerView);

        scannerView.setResultHandler(ProductDetailActivity.this);
        scannerView.setAutoFocus(true);
        scannerView.startCamera();

        dialogQRRedeem.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Notification", "Camera Access permission granted");
            //showQRDialog();
            openQRScanFragment();

        } else if (requestCode == REQUEST_PERMISSION_CAMERA
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.v("Notification", "Camera Access permission Denied");
            new MaterialDialog.Builder(ProductDetailActivity.this)
                    .title("Permission")
                    .content("You can not redeem deal without this permission.")
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                ProductDetailActivity.this.requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_PERMISSION_CAMERA);
                            }
                        }
                    })
                    .show();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("qrscan_done")){
                try {
                    new RedeemDealTask(ModuleClass.USER_ID, resultObject.getString("dealid"), resultObject.getString("dealamount")).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public String getMap(double lattitude, double longitude) {
        String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
                + lattitude
                + ","
                + longitude
                + "&sensor=false";
        return getMapURL;
    }

    public void updateDetail(final JSONObject object) {
        try {
            tvDealTitle.setText(object.getString("dealtitle"));
            tvShopName.setText(object.getString("shopname"));
            tvShopDistance.setText("1.5 km");
            tvShopAddress.setText(object.getString("shop_addres"));
            tvDealDesc.setText(object.getString("dealdescription"));

            idVerify = object.getString("merchantid")+object.getString("shopid");

            final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            String orgPrice = getResources().getString(R.string.Rs) + " " + object.getString("dealamount");
            //String orgPrice = getResources().getString(R.string.Rs) + " " + object.getString("orignal_value");
            tvDealOrgValue.setText(orgPrice, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) tvDealOrgValue.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, orgPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //String discPrice = getResources().getString(R.string.Rs) + " " + object.getString("discountvalue");
           /* String discPrice = getResources().getString(R.string.Rs) + " " + object.getString("orignal_value");
            tvDealDiscValue.setText(discPrice);*/
            if(object.getString("discounttype").equals("1")){
                long originalValue = Long.parseLong(object.getString("dealamount"));
                long discountValue = Long.parseLong(object.getString("discountvalue"));
                long discountPrice = originalValue - (originalValue/100 * discountValue);
                String discPrice = this.getResources().getString(R.string.Rs)+" "+discountPrice;
                tvDealDiscValue.setText(discPrice);
            }else{
                String discPrice = getResources().getString(R.string.Rs) + " " + object.getString("discountvalue");
                tvDealDiscValue.setText(discPrice);
            }

            final double latitude = Double.parseDouble(object.getString("shop_latitude"));
            final double longitude = Double.parseDouble(object.getString("shop_longitude"));

            // Gets to GoogleMap from the MapView and does initialization stuff
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.getUiSettings().setMyLocationButtonEnabled(false);

                    // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                    MapsInitializer.initialize(ProductDetailActivity.this);

                    // Updates the location and zoom of the MapView
                    LatLng latLng = null;
                    try {
                        // latLng = new LatLng(Double.parseDouble(object.getString("shop_latitude")), Double.parseDouble(object.getString("shop_longitude")));
                        latLng = new LatLng(latitude, longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    map.animateCamera(cameraUpdate);

                    Marker perth = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true));
                }
            });

            mapView.onResume();
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    @Override
    public void handleResult(Result rawResult) {
        /*Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();*/

        Log.v("Notification","QR code result"+rawResult.getText());

        if(idVerify.equals(rawResult.getText())) {
            dialogQRRedeem.dismiss();
            try {
                new RedeemDealTask(ModuleClass.USER_ID, resultObject.getString("dealid"), resultObject.getString("dealamount")).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"Invalid QR code", Toast.LENGTH_SHORT).show();
        }

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(ProductDetailActivity.this);
            }
        }, 2000);
    }

    class GetDealDetailTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError;
        String dealId;
        ProgressDialog dialog;

        GetDealDetailTask(String dealId) {
            this.dealId = dealId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                updateDetail(resultObject);
            } else {
                Toast.makeText(ProductDetailActivity.this, responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "deal_detail"));
            inputArray.add(new BasicNameValuePair("id", dealId));
            inputArray.add(new BasicNameValuePair("userid",ModuleClass.USER_ID));

            double latitude = 0.0000;
            double longitude = 0.0000;
            if(MainActivity.mCurrentLocation != null){
                inputArray.add(new BasicNameValuePair("long",""+MainActivity.mCurrentLocation.getLongitude()));
                inputArray.add(new BasicNameValuePair("lat",""+MainActivity.mCurrentLocation.getLatitude()));
            }else{
                inputArray.add(new BasicNameValuePair("lat",""+latitude));
                inputArray.add(new BasicNameValuePair("long",""+longitude));
            }

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Deal detail ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            resultObject = dataArray.getJSONObject(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailActivity.this, R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    class SaveDealLaterTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError, resultMessage;
        String dealId;
        JSONObject dataObject;
        ProgressDialog dialog;

        SaveDealLaterTask(String dealId) {
            this.dealId = dealId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                Toast.makeText(ProductDetailActivity.this, resultMessage, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ProductDetailActivity.this, responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "save_dealto_drafts"));
            inputArray.add(new BasicNameValuePair("customer_id", ModuleClass.USER_ID));
            inputArray.add(new BasicNameValuePair("deal_id", dealId));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Save Later ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    resultMessage = responseJSON.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailActivity.this, R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    class RedeemDealTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError, resultMessage;
        String dealId, custId, amount;
        JSONObject dataObject;
        ProgressDialog dialog;

        RedeemDealTask(String customerId, String dealId, String amount) {
            this.dealId = dealId;
            this.custId = customerId;
            this.amount = amount;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                //Toast.makeText(ProductDetailActivity.this,resultMessage,Toast.LENGTH_LONG).show();
                new MaterialDialog.Builder(ProductDetailActivity.this)
                        .customView(R.layout.dialog_redeem_success, true)
                        .positiveText("Close")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                Toast.makeText(ProductDetailActivity.this, responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "redeem_book"));
            inputArray.add(new BasicNameValuePair("customer_id", custId));
            inputArray.add(new BasicNameValuePair("deal_id", dealId));
            inputArray.add(new BasicNameValuePair("amount", amount));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Deal Redeem ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    resultMessage = dataArray.getString(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailActivity.this, R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fm != null) {
            if (fm.getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                fm.popBackStack();
            }
        }
    }
}
