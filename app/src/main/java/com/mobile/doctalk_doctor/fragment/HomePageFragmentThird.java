package com.mobile.doctalk_doctor.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.mobile.doctalk_doctor.R;
import com.mobile.doctalk_doctor.api_controller.DoctorController;
import com.mobile.doctalk_doctor.utility.Message;
import com.mobile.doctalk_doctor.utility.SavingLocalData;
import com.mobile.doctalk_doctor.utility.UtilityFunction;

import org.json.JSONObject;

public class HomePageFragmentThird extends Fragment {
    Button btnDoctorRegister;
    View view;
    SwitchCompat doctorMode;
    String token;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_3, container, false);
        doctorMode = (SwitchCompat) view.findViewById(R.id.switch_doctor_mode);
        doctorMode.setSwitchPadding(40);

        // Get doctorId;
        SharedPreferences pref = getContext().getSharedPreferences(Message.SHAREPREFECES_DOCTOR_INFO, Context.MODE_PRIVATE);
        final int doctorId = pref.getInt("DoctorId",0);
        token = pref.getString("Token",null);
        //Get activated mode from share preferences
        if(pref.contains("Activated")){
            doctorMode.setChecked(pref.getBoolean("Activated",false));
        }
        doctorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UtilityFunction.ShowToast(getContext(),isChecked+"");
                SavingLocalData.saveInSharePreferences(getContext(), Message.SHAREPREFECES_DOCTOR_INFO,"Activated",isChecked);
                int check = 0;
                if(isChecked == true){
                    check = 1;
                }else {
                    check = 0;
                }
                DoctorSessionActivated doctorSessionActivated = new DoctorSessionActivated();
                doctorSessionActivated.execute(new Integer[]{doctorId,check});
            }
        });

        return view;
    }

    private class DoctorSessionActivated extends AsyncTask<Integer, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(Integer... integers) {
            JSONObject result = DoctorController.getActivateSeassion(token,integers[0],integers[1]);
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            UtilityFunction.ShowToast(getContext(),jsonObject.toString());
        }
    }

}
