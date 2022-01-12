package com.ad.app.notify.utils;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    public Utils(Context context) {
        this.context = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public String getCurrentTime() {
        //TODO - LOGIC TO CHECK DEFAULT IS 24H FORMAT OR 12H FORMAT
        return new SimpleDateFormat("h:mm a" /* 12:08 PM */, Locale.getDefault()).format(new Date());
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("EEE, dd MMM"  /* Mon, 01 Jan */, Locale.getDefault()).format(new Date());
    }

    public int getNotificationId() {
        return new Random().nextInt((1000 + 10000) + 1) + 1000;
    }

    public void shareText(String text) {

        new Utils(context).log("shareIntent");

        context.startActivity(Intent.createChooser(
                new Intent(android.content.Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(android.content.Intent.EXTRA_TEXT, text),
                "Share Using"));
    }


    public void copyToClipboard(String text) {

        new Utils(context).log("copyToClipBoard");

        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("label", text));

        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    public static int getAttrColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }

    public void searchUrl(String url) {
        new Utils(context).log("searchUrl");

        Intent intent;
        intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (url.length() < 100) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Search word limit", Toast.LENGTH_SHORT).show();
        }
    }

    public void log(String message) {
        String className = context.getClass().getSimpleName();

        Bundle params = new Bundle();
        params.putString("Class", className);
        params.putString("Message", message);
        mFirebaseAnalytics.logEvent("log", params);
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

    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        private static final int PADDING = 50;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
            int left = parent.getPaddingLeft() + PADDING;
            int right = parent.getWidth() - parent.getPaddingRight() - PADDING;

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}
