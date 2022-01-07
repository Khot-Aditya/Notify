package com.ad.app.notify.receiver;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Constants;

import java.util.List;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            SharedPreferences sharedPreferences =
                    context.getSharedPreferences("reboot", Activity.MODE_PRIVATE);
            List<NotificationModel> oldModelList = new NotificationDatabaseHandler(context)
                    .getActiveNotificationList(true);

            if (sharedPreferences.getString("power", "power_on").equals("power_off")) {

                sharedPreferences.edit().putString("power", "power_on").apply();

                for (NotificationModel model : oldModelList) {
                    if (model.isNotificationPinned()) {
                        Intent i = new Intent(context, NotificationService.class);
                        i.putExtra(NOTIFICATION_MODEL, model);
                        i.putExtra(Constants.ACTION, Constants.ACTION_REBOOTED);

                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(i);
                            } else {
                                context.startService(i);
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }


        }
    }

}
