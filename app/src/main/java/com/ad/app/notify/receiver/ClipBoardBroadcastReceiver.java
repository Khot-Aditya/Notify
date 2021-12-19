package com.ad.app.notify.receiver;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ClipBoardBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationId = String.valueOf(intent.getStringExtra("notificationId"));
        String notificationSubText = String.valueOf(intent.getStringExtra("notificationSubText"));

        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", notificationSubText);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Exception Found", Toast.LENGTH_SHORT).show();
        }

    }
}
