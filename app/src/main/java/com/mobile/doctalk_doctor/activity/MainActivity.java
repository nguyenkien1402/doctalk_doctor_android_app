package com.mobile.doctalk_doctor.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.api_controller.DoctorController;
import com.mobile.doctalk_doctor.fragment.HomePageFragmentFirst;
import com.mobile.doctalk_doctor.fragment.HomePageFragmentSecond;
import com.mobile.doctalk_doctor.fragment.HomePageFragmentThird;
import com.mobile.doctalk_doctor.model.Doctor;
import com.mobile.doctalk_doctor.utility.Message;
import com.mobile.doctalk_doctor.utility.SavingLocalData;
import com.mobile.doctalk_doctor.utility.UtilityFunction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private String userId;
    private String token;
    private int doctorId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomePageFragmentFirst();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new HomePageFragmentSecond();
                    break;
                case R.id.navigation_notifications:
                    fragment = new HomePageFragmentThird();
                    break;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,new HomePageFragmentFirst()).commit();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Message.SHAREPREFECES_DOCTOR_INFO,MODE_PRIVATE);
        userId = pref.getString("UserId",null);
        token = pref.getString("Token",null);

        //Get Patient Info
        GetDoctorInforAsync getDoctorInforAsync = new GetDoctorInforAsync();
        getDoctorInforAsync.execute();
    }

    private class GetDoctorInforAsync extends AsyncTask<Void, Void, Doctor>{
        @Override
        protected Doctor doInBackground(Void... voids) {
            Doctor result = DoctorController.getDoctorInfo(token,userId);
            return result;
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            super.onPostExecute(doctor);
            SavingLocalData.saveInSharePreferences(getApplicationContext(),Message.SHAREPREFECES_DOCTOR_INFO,"DoctorId",doctor.getId());
            UtilityFunction.ShowToast(getApplicationContext(),doctor.toString());
        }
    }

}
