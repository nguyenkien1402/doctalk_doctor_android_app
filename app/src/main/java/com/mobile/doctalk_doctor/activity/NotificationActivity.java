package com.mobile.doctalk_doctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.api_controller.DoctorController;
import com.mobile.doctalk_doctor.api_controller.RequestConsultController;
import com.mobile.doctalk_doctor.utility.Config;
import com.mobile.doctalk_doctor.utility.Message;
import com.mobile.doctalk_doctor.utility.UtilityFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private TextView content;
    private Button btnReject, btnAccept;
    private String token,userId;
    private Ringtone defaultRingtone;
    private int doctorId;
    private int requestId;
    HubConnection hubConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        content = (TextView) findViewById(R.id.noti_text_view);
        btnReject = (Button) findViewById(R.id.btn_noti_reject);
        btnAccept = (Button) findViewById(R.id.btn_noti_accept);

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

        Log.d("Notic",token +"/" + doctorId +"/"+userId);
        String not = Config.title + "\n" + Config.content + "\n" + Config.imageUrl + "\n" + Config.gameUrl;
        content.setText(not);

        hubConnection = HubConnectionBuilder.create("http://192.168.132.1:5001/searchingdoctorhub").build();
        if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
            hubConnection.start();
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultRingtone.stop();

                // userId here play a role as a doctorId
                if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED)
                    hubConnection.start();
                if(hubConnection.getConnectionState() == HubConnectionState.CONNECTED)
                    hubConnection.send("GettingDoctorResponse",requestId,Config.patientId,userId,0);
                // Save reject to the database
                RequestCancellationAsync requestCancellationAsync = new RequestCancellationAsync();
                requestCancellationAsync.execute();
                hubConnection.stop();
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
                    hubConnection.send("GettingDoctorResponse",requestId,Config.patientId,userId,1);
                // Accept request, go to the Chat session.
                // Save the session first, after save to the database, direct to chat window
                RequestAcceptAsync requestAcceptAsync = new RequestAcceptAsync();
                requestAcceptAsync.execute();

                hubConnection.stop();
               finish();
            }
        });
    }

    private class RequestCancellationAsync extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            return RequestConsultController.RequestCancellation(token,doctorId,requestId);
        }

        @Override
        protected void onPostExecute(Boolean jsonObject) {
            super.onPostExecute(jsonObject);
            UtilityFunction.ShowToast(getApplicationContext(),"Cancel request successfully");
        }
    }


    private class RequestAcceptAsync extends AsyncTask<Void, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... voids) {
            return RequestConsultController.acceptRequest(token, doctorId, requestId);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                int sessionId = jsonObject.getInt("id");
                Intent intent = new Intent(getApplicationContext(), DemoAcceptChatActivity.class);
                intent.putExtra("SessionId",sessionId);
                startActivity(intent);
            }catch (JSONException e){
                Log.d("NotiActivity",e.getMessage());
            }

        }
    }
}
