package com.mobile.doctalk_doctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.utility.Config;

public class NotificationActivity extends AppCompatActivity {

    private TextView content;
    private Button btnReject, btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        content = (TextView) findViewById(R.id.noti_text_view);
        btnReject = (Button) findViewById(R.id.btn_noti_reject);
        btnAccept = (Button) findViewById(R.id.btn_noti_accept);

        // set ringtone
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_RINGTONE);
        final Ringtone defaultRingtone = RingtoneManager.getRingtone(this,ringtoneUri);
        defaultRingtone.play();
        // get data from intent;
        String not = Config.title + "\n" + Config.content + "\n" + Config.imageUrl + "\n" + Config.gameUrl;
        content.setText(not);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultRingtone.stop();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultRingtone.stop();
            }
        });
    }
}
