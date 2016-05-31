package com.dealwala.main.dealwala.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dealwala.main.dealwala.util.ModuleClass;
import com.google.zxing.Result;

import org.json.JSONException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    Handler handler;
    String verificationCode;
    FragmentManager fm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            verificationCode = getArguments().getString("verification_code");
            Log.v("Notification","Verification code : "+verificationCode);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setClickable(true);
        return mScannerView;
    }

    public void setFragmentManager(FragmentManager manager){
        fm = manager;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        /*Toast.makeText(getActivity(), "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();*/

        Log.v("Notification","Scan Result : "+rawResult.getText());
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        if(verificationCode.equals(rawResult.getText())) {
            if(handler != null) {
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putBoolean("qrscan_done", true);
                msg.setData(b);
                handler.sendMessage(msg);
                if(fm != null)
                fm.popBackStack();
            }
        }else{
            Toast.makeText(getActivity(),"Invalid QR code", Toast.LENGTH_SHORT).show();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(QRScannerFragment.this);
            }
        }, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
