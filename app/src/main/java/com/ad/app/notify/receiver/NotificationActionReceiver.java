package com.ad.app.notify.receiver;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.Utils;

public class NotificationActionReceiver extends BroadcastReceiver {

    private Context context;
    private NotificationModel notificationModel;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        notificationModel = (NotificationModel) intent.getSerializableExtra(NOTIFICATION_MODEL);
        String notificationAction = intent.getStringExtra(Constants.NOTIFICATION_INTENT_ACTION);


        switch (notificationAction) {

            case Constants.ACTION_COPY:
                if(notificationModel.getNotificationSubText() != null){
                    new Utils(context).copyToClipboard(notificationModel.getNotificationSubText());
                }
                break;

            case Constants.ACTION_ATTACH_PIN:

                notificationModel.setNotificationPinned(true);
                context.startService(new Intent(context, NotificationService.class)
                        .putExtra(NOTIFICATION_MODEL, notificationModel).putExtra(Constants.ACTION,Constants.ACTION_UPDATE));
                break;

            case Constants.ACTION_REMOVE_PIN:

                notificationModel.setNotificationPinned(false);
                context.startService(new Intent(context, NotificationService.class)
                        .putExtra(NOTIFICATION_MODEL, notificationModel).putExtra(Constants.ACTION,Constants.ACTION_UPDATE));
                break;

            default:
                Toast.makeText(context, "NotificationActionReceiver: Exception Found", Toast.LENGTH_SHORT).show();
        }
    }




}
