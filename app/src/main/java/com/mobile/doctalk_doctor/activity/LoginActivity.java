package com.mobile.doctalk_doctor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.api_controller.DoctorController;
import com.mobile.doctalk_doctor.utility.Message;
import com.mobile.doctalk_doctor.utility.SavingLocalData;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edUsername, edPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_app_login);
        edUsername = (EditText) findViewById(R.id.et_login_user_name);
        edPassword = (EditText) findViewById(R.id.et_login_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                GetTokenAsync getTokenAsync = new GetTokenAsync();
                getTokenAsync.execute(new String[]{username,password});
            }
        });
    }

    private class GetTokenAsync extends AsyncTask<String,Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            String token = DoctorController.login(strings[0],strings[1]);
            SavingLocalData.saveInSharePreferences(getApplicationContext(), Message.SHAREPREFECES_DOCTOR_INFO,"Token",token);
            JSONObject result = DoctorController.getUserTokenInfo(token);
            if(result != null){
                return result;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            // save userId in sharepreference
            if(result != null){
                try{
                    String userId = result.getString("sub");
                    String email = result.getString("email");
                    SavingLocalData.saveInSharePreferences(getApplicationContext(),Message.SHAREPREFECES_DOCTOR_INFO,"UserId",userId);
                    SavingLocalData.saveInSharePreferences(getApplicationContext(),Message.SHAREPREFECES_DOCTOR_INFO,"Email",email);
                    signinWithFirebase(email,edPassword.getText().toString());
                }catch (Exception e){
                    Log.d("Error",e.getMessage());
                }
            }else{
                Toast.makeText(getApplicationContext(),"Incorrect email or password",Toast.LENGTH_SHORT).show();
            }

            // start new activity

        }
    }

    private void signinWithFirebase(String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Cannot login to Firebase server",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Subscribe to listening the request
        FirebaseMessaging.getInstance().subscribeToTopic("doctor_"+edUsername.getText().toString());
        FirebaseMessaging.getInstance().subscribeToTopic("doctor");
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.d("LoginActivity",task.getException().toString());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("LoginActivity","Firebase Token: " + token);
                    }
                });
    }

    public void signup(View view){

    }

    public void forgotpassword(View view){

    }
}
