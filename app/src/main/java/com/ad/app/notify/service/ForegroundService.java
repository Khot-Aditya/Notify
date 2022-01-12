package com.ad.app.notify.service;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ad.app.notify.R;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.Utils;

import java.util.List;

public class ForegroundService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String CHANNEL_NAME = "Foreground Service Channel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setSmallIcon(R.drawable.ic_app_logo)
                .build();
        startForeground(100000, notification);

        //do heavy work on a background thread -----------------------------------------------------
        List<NotificationModel> oldModelList = new NotificationDatabaseHandler(this)
                .getActiveNotificationList(true);

        for (NotificationModel model : oldModelList) {

            if (model.isNotificationPinned()) {
                Intent i = new Intent(this, NotificationService.class);
                i.putExtra(NOTIFICATION_MODEL, model);
                i.putExtra(Constants.ACTION, Constants.ACTION_REBOOTED);

                try {
                    startService(i);
                } catch (Exception e) {
                    new Utils(this).log("Error: " + e.getLocalizedMessage());
                }
            }
        }

        stopSelf(); //------------------------------------------------------------------------------
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
