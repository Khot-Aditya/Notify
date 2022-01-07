package com.ad.app.notify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class DeviceShutDownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN") ||
                intent.getAction().equals("android.intent.action.QUICKBOOT_POWEROFF") ||
                intent.getAction().equals("com.htc.intent.action.QUICKBOOT_POWEROFF")) {

            Log.d("DeviceShutDownReceiver", "Power Off Received");

            SharedPreferences sharedPreferences =
                    context.getSharedPreferences("reboot", Activity.MODE_PRIVATE);

            sharedPreferences.edit().putString("power", "power_off").apply();
        }
    }
}