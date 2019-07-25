package com.mobile.doctalk_doctor.utility;

import android.content.Context;
import android.widget.Toast;

public class UtilityFunction {

    public static void ShowToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
