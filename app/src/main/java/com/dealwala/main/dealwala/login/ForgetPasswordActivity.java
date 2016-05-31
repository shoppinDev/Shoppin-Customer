package com.dealwala.main.dealwala.login;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.main.RecyclerViewDataAdapter;
import com.dealwala.main.dealwala.util.JSONParser;
import com.dealwala.main.dealwala.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForgetPasswordActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText etEmailId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etEmailId = (EditText) findViewById(R.id.input_email);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmailId.getText().toString().equals("")){
                    //tilEmail.setErrorEnabled(true);
                    //tilEmail.setError("Please enter your email address");
                    Toast.makeText(ForgetPasswordActivity.this,"Please enter your email address",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isEmailValid(etEmailId.getText().toString())){
                    //tilEmail.setErrorEnabled(true);
                    //tilEmail.setError("Email address is incorrect");
                    Toast.makeText(ForgetPasswordActivity.this,"Email address is incorrect",Toast.LENGTH_LONG).show();
                    return;
                }

                if(ModuleClass.isInternetOn){
                    new GetPasswordTask(etEmailId.getText().toString()).execute();
                }
            }
        });
    }

    private boolean isEmailValid(String paramString) {
        return EmailValidator.isValidEmail(paramString);
    }

    class GetPasswordTask extends AsyncTask<Void,Void,Void> {
        boolean success;
        String responseError;
        String response;
        String emailId;

        ProgressDialog dialog;
        public GetPasswordTask(String emailId){
            this.emailId = emailId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(success){
                Toast.makeText(ForgetPasswordActivity.this,response,Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ForgetPasswordActivity.this,responseError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "forgotpassword"));
            inputArray.add(new BasicNameValuePair("email", emailId));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH+"index.php", "GET", inputArray);
            Log.d("Forget Password ", responseJSON.toString());

            if(responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    response = responseJSON.getString("data");

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
            dialog = new ProgressDialog(ForgetPasswordActivity.this,R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }
}
