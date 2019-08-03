package com.mobile.doctalk_doctor.utility;

public class EndpointAPI {

    public static String account_url = "http://192.168.132.1:5000/";
    public static String application_url = "http://192.168.132.1:5001/";

    public static String get_token_mobile = application_url + "api/account/doctor/token/";
    public static String get_user_token_info = account_url + "connect/userinfo";
    public static String get_doctor_info = application_url + "api/doctors/userId/";
    public static String get_doctor_activate_session = application_url + "api/doctor/session/activate";

    public static String get_request_cancellation = application_url + "api/requestcancel/cancelby";
    public static String get_request_accept = application_url + "api/consultsession";

    // Firebase messaging service
    public static String firebase_push_notification = "https://fcm.googleapis.com/fcm/send";

}
