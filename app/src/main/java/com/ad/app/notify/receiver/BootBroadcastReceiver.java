package com.ad.app.notify.receiver;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Constants;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BootBroadcastReceiver extends BroadcastReceiver {

    final int[] index = {0};

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            List<NotificationModel> model = new NotificationDatabaseHandler(context)
                    .getActiveNotificationList(true);

            new Timer().scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {

                    if (index[0] < model.size()) {
                        //if notification is pinned then only it will revive else notification will be removed
                        if(model.get(index[0]).isNotificationPinned()){
                            Intent i = new Intent(context, NotificationService.class);
                            i.putExtra(NOTIFICATION_MODEL, model.get(index[0]));
                            i.putExtra(Constants.ACTION, Constants.ACTION_REBOOTED);
                            context.startService(i);
                        }
                    }

                    index[0]++;
                }
            }, 0, 1000);


        }
    }

}
