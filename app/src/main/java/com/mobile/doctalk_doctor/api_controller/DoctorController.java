package com.mobile.doctalk_doctor.api_controller;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.doctalk_doctor.model.Doctor;
import com.mobile.doctalk_doctor.utility.Config;
import com.mobile.doctalk_doctor.utility.EndpointAPI;
import com.mobile.doctalk_doctor.utility.UtilityFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.zip.CheckedOutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorController {

    private static String TAG = "DoctorController";
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

    public static boolean RejectAndSendNotification(){
        Config.userIds.remove(0);
        JSONObject notic = new JSONObject();
        JSONObject data = new JSONObject();
        OkHttpClient okHttpClient = new OkHttpClient();
        try{
            data.put("title", Config.title);
            data.put("content",Config.content);
            data.put("imageUrl",Config.imageUrl);
            data.put("gameUrl",Config.gameUrl);
            JSONArray userIds = new JSONArray();
            for(int i = 0 ; i < Config.userIds.size() ; i++){
                userIds.put(Config.userIds.get(i));
            }
            data.put("userId",userIds);
            notic.put("data",data);
            if(Config.userIds.size()>0){
                notic.put("to","/topics/doctor_"+Config.userIds.get(0));
            }else{
                //Send back to patient that no doctor available.
            }

            // Do Post Request
            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE, notic.toString());
            Request request = new Request.Builder()
                    .url(EndpointAPI.firebase_push_notification)
                    .post(requestBody)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Authorization","key=AAAAST4PGVw:APA91bHGopJqbpePv79V5qiClVfF4PIm6N0s09MN889BqlfgfXvCQfkO4sSTyeyP0Yr5WCvftz7ftqqoJSJ2SwGS_d44-Rw01PtIqJnuL-p_6oXqY1uKUbffixfXsZiBtbRmn7Do7V2u")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                // Mean the notification has been send to next doctor.
                Log.d(TAG,"Send to another doctor successfully ");
                return true;
            }else{
                Log.d("Failure response","Something went wrong");
            }
        }catch(Exception e){
            Log.d(TAG, e.getMessage());
        }
        return false;
    }


    public static JSONObject RequestCancellation(String token, int doctorId, int requestId){
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
            JSONObject result = new JSONObject(response.body().string());
            return result;
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;
    }
}
