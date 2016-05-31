package com.dealwala.main.dealwala.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {

    Button btnSignUp,btnFacebook;

    EditText etEmailId,etPassword,etConfPassword,etFullName,etMobile;
    TextInputLayout tilEmailId,tilPassword,tilConfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmailId = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        etConfPassword = (EditText) findViewById(R.id.input_conf_password);

        etFullName = (EditText) findViewById(R.id.input_fullname);
        etMobile = (EditText) findViewById(R.id.input_mobile_number);

        tilEmailId = (TextInputLayout) findViewById(R.id.input_layout_email);
        tilPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        tilConfPassword = (TextInputLayout) findViewById(R.id.input_layout_conf_password);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etEmailId.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Please enter your email address",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etPassword.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etConfPassword.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Please enter a confirm password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etFullName.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Please enter a Full Name",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etMobile.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Please enter a Mobile number",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!etPassword.getText().toString().equals(etConfPassword.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"Password not match",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isEmailValid(etEmailId.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"Email Address is incorrect",Toast.LENGTH_LONG).show();
                    return;
                }

                new RegisterTask(etEmailId.getText().toString(),etPassword.getText().toString(),etFullName.getText().toString(),etMobile.getText().toString()).execute();
            }
        });

        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);*/
                Toast.makeText(SignUpActivity.this,"Not Implemented yet",Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isEmailValid(String paramString) {
        return EmailValidator.isValidEmail(paramString);
    }

    class RegisterTask extends AsyncTask<Void,Void,Void> {

        String email,password,fullName,mobile;
        boolean success;
        String responseError;
        ProgressDialog dialog;

        public RegisterTask(String email,String password,String fullName,String mobile){
            this.email = email;
            this.password = password;
            this.fullName = fullName;
            this.mobile = mobile;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(SignUpActivity.this,responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "customer_register"));
            inputArray.add(new BasicNameValuePair("email", email));
            inputArray.add(new BasicNameValuePair("password", password));
            inputArray.add(new BasicNameValuePair("name", fullName));
            inputArray.add(new BasicNameValuePair("mobile", mobile));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"index.php", "GET", inputArray);
            Log.d("Register ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {

                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");

                    for(int i = 0;i < dataArray.length();i++){
                        JSONObject object = dataArray.getJSONObject(i);

                        if(object.getString("responsecode").equals("1")){
                            success = true;
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
            dialog = new ProgressDialog(SignUpActivity.this,R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }
}
