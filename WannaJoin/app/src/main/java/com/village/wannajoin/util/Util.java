package com.village.wannajoin.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by richa on 3/29/16.
 */
public class Util {
    private static final String LOG_TAG = Util.class.getSimpleName();
    public static String formatDate(int year, int month, int day){
        String date = year + "/" + month + "/" + day;
        Date utilDate = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            utilDate = formatter.parse(date);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.toString());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM d, yyyy");
        return  dateFormat.format(utilDate);
    }

    public static String formatTime(int hourOfDay, int minute){
        String time = hourOfDay+":"+minute;
        Date utilDate=null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            utilDate = formatter.parse(time);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.toString());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return  dateFormat.format(utilDate);
        //return time;
    }

    public static long getTimeStamp(String date, String time){
        String newDate = date +" "+ time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM d, yyyy hh:mm a");
        Date utilDate = null;
        try {
            utilDate = dateFormat.parse(newDate);
        }catch (ParseException e){
            Log.d(LOG_TAG, e.toString());
        }

        return utilDate.getTime();

    }

    public static String getDateFromTimeStamp(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);

    }

    public static String getTimeFromTimeStamp(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(date);

    }


}
