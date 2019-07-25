package com.mobile.doctalk_doctor.api_controller;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.doctalk_doctor.model.Doctor;
import com.mobile.doctalk_doctor.utility.EndpointAPI;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DoctorController {

    public static String login(String username, String password){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndpointAPI.get_token_mobile + username +"/"+password;
        Log.d("URL",url);
        String token = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d("Code",response.code()+"");
            String result = response.body().string();
            if(response.code() == 200){
                Log.d("Result",result);
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.getBoolean("isDoctor") == true){
                    token = jsonObject.getString("access_token");
                }else{
                    return "Please register as a doctor before login";
                }
            }else{
                return result;
            }
        } catch (Exception e) {
            Log.d("Error","Error in parsing");
            e.printStackTrace();
        }
        return token;
    }

    public static Doctor getDoctorInfo(String token, String userId){
        String url = EndpointAPI.get_doctor_info + userId;
        Log.d("URL",url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization","Bearer "+token)
                .addHeader("Content-Type","application/json")
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                String object = response.body().string();
                Log.d("Doctor", object);
                Doctor doctor = new Gson().fromJson(object,Doctor.class);
                Log.d("Doctor2",doctor.toString());
                return doctor;
            }
        }catch (Exception e){
            Log.d("DoctorCtr", e.getMessage());
        }
        return null;

    }

    public static JSONObject getUserTokenInfo(String token){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(EndpointAPI.get_user_token_info)
                .get()
                .addHeader("Authorization","Bearer "+token)
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONObject result = new JSONObject(response.body().string());
                return result;
            }
        }catch (Exception e){
            Log.d("Utitility Controller",e.getMessage());
        }
        return null;
    }

    public static JSONObject getActivateSeassion(String token, int doctorId, int activated){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndpointAPI.get_doctor_activate_session+"?doctorId="+doctorId+"&state="+activated;
        Log.d("URL",url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization","Bearer "+token)
                .addHeader("Content-Type","application/json")
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.d("result",result);
            if(response.code() == 200){
                return new JSONObject(result);
            }
        }catch (Exception e){
            Log.d("DoctorCtr",e.getMessage());
        }
        return null;
    }
}
