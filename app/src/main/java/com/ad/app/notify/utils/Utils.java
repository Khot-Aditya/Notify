package com.ad.app.notify.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public String getCurrentTime() {
        //TODO - LOGIC TO CHECK DEFAULT IS 24H FORMAT OR 12H FORMAT
        return new SimpleDateFormat("h:mm a" /* 12:08 PM */, Locale.getDefault()).format(new Date());
    }

    public String getCurrentDate() {

        return new SimpleDateFormat("EEE, d MMM"  /* Mon, 01 Jan */, Locale.getDefault()).format(new Date());
    }

    public int getNotificationId() {
        return new Random().nextInt((1000 + 10000) + 1) + 1000;
    }

}
