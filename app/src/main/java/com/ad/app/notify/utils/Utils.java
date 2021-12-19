package com.ad.app.notify.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public String getCurrentTime() {
        //TODO - LOGIC TO CHECK DEFAULT IS 24H FORMAT OR 12H FORMAT
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
    }

    public int getNotificationId() {
        return new Random().nextInt((1000 + 10000) + 1) + 1000;
    }

}
