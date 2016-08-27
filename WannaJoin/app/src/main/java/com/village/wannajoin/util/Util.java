package com.village.wannajoin.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String getFormattedDateFromTimeStamp(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM d, yyyy");
        return formatter.format(date);

    }

    public static String getTimeFromTimeStamp(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(date);

    }

    public static String capitalizeWords(String s){
        String[] sa = s.split(" ");
        StringBuilder sb = new StringBuilder();
        int i=0;
        for(i=0;i<sa.length-1;i++){
            if (!sa[i].equals("")){
                sb.append(sa[i].substring(0,1).toUpperCase()+sa[i].substring(1)+" ");
            }
        }
        if (!sa[i].equals("")){
            sb.append(sa[i].substring(0,1).toUpperCase()+sa[i].substring(1));
        }
        return sb.toString();
    }

    public static String getMidNightTimeStamp(){
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String currentDateString = formatter.format(currentDate);
        Date utilDate = null;
        try {
            utilDate = formatter.parse(currentDateString);
        }catch (ParseException e){
            Log.d(LOG_TAG, e.toString());
        }
        long currentTimeStamp = utilDate.getTime();
        return String.valueOf(currentTimeStamp);
    }

    public static String getMidNightTimeStampNextDay(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,1);
        Date nextDate = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String nextDateString = formatter.format(nextDate);
        Date utilDate = null;
        try {
            utilDate = formatter.parse(nextDateString);
        }catch (ParseException e){
            Log.d(LOG_TAG, e.toString());
        }
        long nextTimeStamp = utilDate.getTime();
        return String.valueOf(nextTimeStamp);
    }

    public static void hideSoftKeyBoard(Activity activity){
        View view = activity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
