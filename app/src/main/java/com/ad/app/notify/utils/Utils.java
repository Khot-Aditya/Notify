package com.ad.app.notify.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

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

    public void shareText(String text) {

        context.startActivity(Intent.createChooser(
                new Intent(android.content.Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(android.content.Intent.EXTRA_TEXT, text),
                "Share Using"));
    }

    public void copyToClipboard(String text) {

        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("label", text));

    }

    public static int getAttrColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }

    public int colorBrightness(int color, String method, float brightness) {

        if (method.equals("light")) {
            int red = (int) ((Color.red(color) * (1 - brightness) / 255 + brightness) * 255);
            int green = (int) ((Color.green(color) * (1 - brightness) / 255 + brightness) * 255);
            int blue = (int) ((Color.blue(color) * (1 - brightness) / 255 + brightness) * 255);
            return Color.argb(Color.alpha(color), red, green, blue);
        } else {

            int a = Color.alpha(color);
            int r = Math.round(Color.red(color) * brightness);
            int g = Math.round(Color.green(color) * brightness);
            int b = Math.round(Color.blue(color) * brightness);
            return Color.argb(a,
                    Math.min(r, 255),
                    Math.min(g, 255),
                    Math.min(b, 255));
        }
    }
}
