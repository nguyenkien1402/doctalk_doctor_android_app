package com.mobile.doctalk_doctor.api_controller;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.doctalk_doctor.model.ConsultSession;
import com.mobile.doctalk_doctor.utility.EndpointAPI;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestConsultController {
    public static String TAG = "RequestController";

    public static boolean RequestCancellation(String token, int doctorId, int requestId){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndpointAPI.get_request_cancellation+"?doctorId="+doctorId+"&requestId="+requestId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Bearer "+token)
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONObject result = new JSONObject(response.body().string());
                return result.getBoolean("isOk");
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return false;
    }

    public static JSONObject acceptRequest(String token,int doctorId, int requestId){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = EndpointAPI.get_request_accept;
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        ConsultSession consultSession = new ConsultSession();
        consultSession.setDoctorId(doctorId);
        consultSession.setRequestConsultId(requestId);
        try{
            String object = new Gson().toJson(consultSession,ConsultSession.class);
            Log.d(TAG,"Object" + object);
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE, object);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization","Bearer "+token)
                    .addHeader("Content-Type","application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                JSONObject result = new JSONObject(response.body().string());
                Log.d(TAG,result.toString());
                return result;
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;
    }
}
