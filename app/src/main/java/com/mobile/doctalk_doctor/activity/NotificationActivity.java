package com.mobile.doctalk_doctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.api_controller.DoctorController;
import com.mobile.doctalk_doctor.model.Doctor;
import com.mobile.doctalk_doctor.utility.Config;
import com.mobile.doctalk_doctor.utility.Message;
import com.mobile.doctalk_doctor.utility.UtilityFunction;

import org.json.JSONObject;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private TextView content;
    private Button btnReject, btnAccept;
    private String token,userId;
    private Ringtone defaultRingtone;
    private int doctorId;
    private int requestId;
    private HubConnection hubConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        content = (TextView) findViewById(R.id.noti_text_view);
        btnReject = (Button) findViewById(R.id.btn_noti_reject);
        btnAccept = (Button) findViewById(R.id.btn_noti_accept);

        hubConnection = HubConnectionBuilder.create("http://192.168.132.1:5001/searchingdoctorhub").build();

        // set ringtone
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_RINGTONE);
        defaultRingtone = RingtoneManager.getRingtone(this,ringtoneUri);
        defaultRingtone.play();

        // get token from sharepreferences
        SharedPreferences pref = getSharedPreferences(Message.SHAREPREFECES_DOCTOR_INFO,MODE_PRIVATE);
        token = pref.getString("Token",null);
        doctorId = pref.getInt("DoctorId",0);
        userId = pref.getString("UserId",null);

        requestId = Config.id;
        String not = Config.title + "\n" + Config.content + "\n" + Config.imageUrl + "\n" + Config.gameUrl;
        final List<String> userId = Config.userIds;
        for(int i = 0 ; i < userId.size() ; i++){
            not = not + userId.get(i) + " ";
        }
        content.setText(not);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultRingtone.stop();
                SendNotiToDoctorAsync sendNotiToDoctorAsync = new SendNotiToDoctorAsync();
                sendNotiToDoctorAsync.execute();
                RequestCancellationAsync requestCancellation = new RequestCancellationAsync();
                requestCancellation.execute();
                if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
                    hubConnection.start();
                if(hubConnection.getConnectionState() == HubConnectionState.CONNECTED)
                    hubConnection.send("GettingDoctorResponse",1005,"d31eac6d-6849-49b4-be3c-d05a25a3d8c4",userId,0);
                finish();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultRingtone.stop();
                if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
                    hubConnection.start();
                if(hubConnection.getConnectionState() == HubConnectionState.CONNECTED)
                    hubConnection.send("GettingDoctorResponse",1005,"d31eac6d-6849-49b4-be3c-d05a25a3d8c4",userId,1);
//                finish();
            }
        });
    }

    private class RequestCancellationAsync extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return DoctorController.RequestCancellation(token,doctorId,requestId);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            UtilityFunction.ShowToast(getApplicationContext(),jsonObject.toString());
        }
    }
    private class SendNotiToDoctorAsync extends AsyncTask<Void, Void ,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {

            return DoctorController.RejectAndSendNotification();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                UtilityFunction.ShowToast(getApplicationContext(),"Send to another doctor successfully");
            }else{
                UtilityFunction.ShowToast(getApplicationContext(),"Failed to send to another doctor");
            }
//            finish();
        }
    }
}
