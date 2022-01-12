package com.ad.app.notify.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.ad.app.notify.service.ForegroundService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            SharedPreferences sharedPreferences =
                    context.getSharedPreferences("reboot", Activity.MODE_PRIVATE);

            if (sharedPreferences.getString("power", "power_on").equals("power_off")) {
                sharedPreferences.edit().putString("power", "power_on").apply();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, ForegroundService.class));
                } else {
                    context.startService(new Intent(context, ForegroundService.class));
                }
            }
        }
    }
}
