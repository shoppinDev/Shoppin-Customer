package com.dealwala.main.dealwala.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.util.JSONParser;
import com.dealwala.main.dealwala.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView tvEmail,tvMobile,tvName;
    EditText etName,etMobile;
    ImageButton imgBtnEdit;
    Button btnChangePassword,btnUpdate;
    EditText etPassword,etConfPassword,etCurrPassword;
    boolean isEditMode = false;

    MaterialDialog dialogChangePwd;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvMobile = (TextView) view.findViewById(R.id.tvMobile);
        tvName = (TextView) view.findViewById(R.id.tvName);

        etName = (EditText) view.findViewById(R.id.etName);
        etMobile = (EditText) view.findViewById(R.id.etMobile);

        imgBtnEdit = (ImageButton) view.findViewById(R.id.imgBtnEdit);
        imgBtnEdit.setOnClickListener(this);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangePwd = new MaterialDialog.Builder(getActivity())
                        .title("Change Password")
                        .customView(R.layout.dialog_change_password,true)
                        .positiveText("Submit")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if(etCurrPassword.getText().toString().equals("")){
                                    Toast.makeText(getActivity(),"Please enter your current password",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(etPassword.getText().toString().equals("")){
                                    //tilPassword.setError("Please enter your password");
                                    Toast.makeText(getActivity(),"Please enter your password",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(etConfPassword.getText().toString().equals("")){
                                    //tilConfPassword.setError("Please enter a confirm password");
                                    Toast.makeText(getActivity(),"Please enter a confirm password",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(!etPassword.getText().toString().equals(etConfPassword.getText().toString())){
                                    //tilConfPassword.setError("Password not match");
                                    Toast.makeText(getActivity(),"Password not match",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if(ModuleClass.isInternetOn){
                                    new ChangePasswordTask(etPassword.getText().toString(),etCurrPassword.getText().toString()).execute();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .cancelable(false)
                        .autoDismiss(false)
                        .build();

                View dialogView = dialogChangePwd.getCustomView();

                etPassword = (EditText) dialogView.findViewById(R.id.input_password);
                etConfPassword = (EditText) dialogView.findViewById(R.id.input_conf_password);
                etCurrPassword = (EditText) dialogView.findViewById(R.id.input_curr_password);

                dialogChangePwd.show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if(ModuleClass.isInternetOn){
            new GetMyAccountDetailTask().execute();
        //}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(menu != null)
            menu.findItem(R.id.action_search).setVisible(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imgBtnEdit){

            if(isEditMode){
                switchToNormalMode();
                isEditMode = false;
            }else{
                switchToEditMode();
                isEditMode = true;
            }

            updateEditButton();

        }else if(v.getId() == R.id.btnUpdate){
            if(etName.getText().toString().equals("")){
                Toast.makeText(getActivity(),"Please enter a Name",Toast.LENGTH_LONG).show();
                return;
            }

            if(etMobile.getText().toString().equals("")){
                Toast.makeText(getActivity(),"Please enter a Mobile number",Toast.LENGTH_LONG).show();
                return;
            }

            new UpdateMyAccountDetailTask(etName.getText().toString(),etMobile.getText().toString()).execute();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void switchToEditMode(){
        tvName.setVisibility(View.GONE);
        tvMobile.setVisibility(View.GONE);
        etName.setVisibility(View.VISIBLE);
        etName.setText(tvName.getText());
        etMobile.setVisibility(View.VISIBLE);
        etMobile.setText(tvMobile.getText());
        btnUpdate.setVisibility(View.VISIBLE);
    }

    public void switchToNormalMode(){
        tvName.setVisibility(View.VISIBLE);
        tvMobile.setVisibility(View.VISIBLE);
        etName.setVisibility(View.GONE);
        etMobile.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.GONE);
    }

    public void updateEditButton(){
        if(isEditMode){
            imgBtnEdit.setBackgroundResource(R.drawable.icon_close);
        }else{
            imgBtnEdit.setBackgroundResource(R.drawable.icon_edit);
        }
    }

    public void updateDetail(JSONObject object){

        try {
            Log.v("Notification","Customer name : "+object.getString("customer_name"));
            tvName.setText(object.getString("customer_name"));
            tvMobile.setText(object.getString("customer_mobile"));
            tvEmail.setText(object.getString("customer_email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class GetMyAccountDetailTask extends AsyncTask<Void,Void,Void> {
        boolean success;
        String responseError;
        JSONObject dataObject;
        ProgressDialog dialog;
        GetMyAccountDetailTask(){

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                updateDetail(dataObject);
            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "my_account"));
            inputArray.add(new BasicNameValuePair("id", ModuleClass.USER_ID));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"index.php", "GET", inputArray);
            Log.d("My Account ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    if(dataArray.length() > 0){
                        for(int i = 0 ; i < dataArray.length();i++){
                            dataObject = dataArray.getJSONObject(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            }else{
                success = false;
                responseError = "There is some problem in server connection";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity(),R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();

        }
    }

    class ChangePasswordTask extends AsyncTask<Void,Void,Void> {

        String password,currPassword;
        boolean success,isCurrRight;
        String responseError,resultMessage;
        ProgressDialog dialog;

        public ChangePasswordTask(String password,String currPassword){
            this.password = password;
            this.currPassword = currPassword;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                if(isCurrRight)
                    if(dialogChangePwd != null){
                        if(dialogChangePwd.isShowing())
                            dialogChangePwd.dismiss();
                    }
                Toast.makeText(getActivity(),resultMessage,Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "changepassword"));
            inputArray.add(new BasicNameValuePair("id", ModuleClass.USER_ID));
            inputArray.add(new BasicNameValuePair("curr_pass", currPassword));
            inputArray.add(new BasicNameValuePair("password", password));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"index.php", "GET", inputArray);
            Log.d("Register ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    resultMessage = dataArray.getString(0);

                    if(resultMessage.equalsIgnoreCase("PASSWORD UPDATE SUCCESSFULLY")){
                        isCurrRight = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                success = false;
                responseError = "There is some problem in server connection";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity(),R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    class UpdateMyAccountDetailTask extends AsyncTask<Void,Void,Void> {
        boolean success;
        String responseError,resposeResult;
        JSONObject dataObject;
        ProgressDialog dialog;
        String name,mobile;
        UpdateMyAccountDetailTask(String name,String mobile){
            this.name = name;
            this.mobile = mobile;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
               // updateDetail(dataObject);
                Toast.makeText(getActivity(),resposeResult,Toast.LENGTH_LONG).show();
                isEditMode = false;
                tvName.setText(name);
                tvMobile.setText(mobile);
                switchToNormalMode();
                updateEditButton();
            }else{
                Toast.makeText(getActivity(),responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "edit_profile"));
            inputArray.add(new BasicNameValuePair("customer_id", ModuleClass.USER_ID));
            inputArray.add(new BasicNameValuePair("name",name));
            inputArray.add(new BasicNameValuePair("mobile", mobile));


            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"index.php", "GET", inputArray);
            Log.d("Update My Account ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    resposeResult = dataArray.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            }else{
                success = false;
                responseError = "There is some problem in server connection";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity(),R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();

        }
    }
}
