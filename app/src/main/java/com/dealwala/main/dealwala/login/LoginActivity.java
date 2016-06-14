package com.dealwala.main.dealwala.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.main.MainActivity;
import com.dealwala.main.dealwala.util.JSONParser;
import com.dealwala.main.dealwala.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnFacebook;
    TextView tvSignUp,tvForget;
    TextInputLayout tilEmail,tilPassword;
    EditText etEmailId;
    EditText etPassword;

    SwitchCompat switchRemember;
    boolean isRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailId = (EditText) findViewById(R.id.input_email);
        etEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tilEmail.isErrorEnabled()) {
                    tilEmail.setError(null);
                    tilEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword = (EditText) findViewById(R.id.input_password);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tilPassword.isErrorEnabled()) {
                    tilPassword.setError(null);
                    tilPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tilEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        tilPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmailId.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter your email address",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isEmailValid(etEmailId.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Email address is incorrect",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etPassword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(switchRemember.isChecked()){
                    isRememberMe = true;
                }else{
                    isRememberMe = false;
                }

                new LoginTask(etEmailId.getText().toString(),etPassword.getText().toString()).execute();

            }
        });

        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"Not implemented yet....",Toast.LENGTH_LONG).show();
            }
        });

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        tvForget = (TextView) findViewById(R.id.tvForget);
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        switchRemember = (SwitchCompat) findViewById(R.id.switchRemember);
        switchRemember.setChecked(true);

        if(!ModuleClass.appPreferences.getString(ModuleClass.KEY_EMAIL_ID,"").equals("")){
            etEmailId.setText(ModuleClass.appPreferences.getString(ModuleClass.KEY_EMAIL_ID,""));
        }

        if(!ModuleClass.appPreferences.getString(ModuleClass.KEY_PASSWORD,"").equals("")){
            etPassword.setText(ModuleClass.appPreferences.getString(ModuleClass.KEY_PASSWORD,""));
        }
    }

    private boolean isEmailValid(String paramString) {
        return EmailValidator.isValidEmail(paramString);
    }

    class LoginTask extends AsyncTask<Void,Void,Void>{

        String email,password;
        boolean success;
        String responseError;

        ProgressDialog dialog;

        public LoginTask(String email,String password){
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }else{
                Toast.makeText(LoginActivity.this,responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "login"));
            inputArray.add(new BasicNameValuePair("email", email));
            inputArray.add(new BasicNameValuePair("password", password));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"index.php", "GET", inputArray);
            Log.d("LOGIN ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        if(object.getString("responsecode").equals("1")){
                            success = true;
                            ModuleClass.USER_ID = object.getString("userid");
                            ModuleClass.USER_NAME = object.getString("username");
                            SharedPreferences.Editor editor = ModuleClass.appPreferences.edit();
                            editor.putBoolean(ModuleClass.KEY_IS_REMEMBER,isRememberMe);
                            editor.putString(ModuleClass.KEY_USER_ID,ModuleClass.USER_ID);
                            editor.putString(ModuleClass.KEY_USER_NAME,ModuleClass.USER_NAME);
                            if(isRememberMe) {
                                editor.putString(ModuleClass.KEY_EMAIL_ID, email);
                                editor.putString(ModuleClass.KEY_PASSWORD, password);
                            }
                            editor.commit();
                        }else{
                            success = false;
                            responseError = object.getString("message");
                        }
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

            dialog = new ProgressDialog(LoginActivity.this,R.style.MyThemeDialog);
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

        this.finish();
    }
}
