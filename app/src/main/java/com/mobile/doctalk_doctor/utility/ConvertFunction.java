package com.mobile.doctalk_doctor.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertFunction {

    public static String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}

