package com.ad.app.notify.receiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.Utils;

public class NotificationActionReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        int notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0);
        String notificationSubText = String.valueOf(intent.getStringExtra(Constants.NOTIFICATION_SUB_TEXT));
        String notificationAction = String.valueOf(intent.getStringExtra(Constants.NOTIFICATION_INTENT_ACTION));


        switch (notificationAction) {

            case  Constants.ACTION_COPY:
                actionCopyToClipboard(notificationSubText);

                break;

            case "pin":
                actionPin(notificationId);
                break;

            default:
                Toast.makeText(context, "NotificationActionReceiver: Exception Found", Toast.LENGTH_SHORT).show();
        }


    }

    private void actionCopyToClipboard(String subText) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", subText);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Exception Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void actionPin(int id) {

        Intent new_intent = new Intent(context, NotificationService.class);

        new_intent.putExtra("notification_id", id);
        new_intent.putExtra("notification_body", "subText" + new Utils().getNotificationId());
        context.startService(new_intent);
    }
}
