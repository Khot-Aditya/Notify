package com.ad.app.notify.service;

import static com.ad.app.notify.utils.Constants.CHANNEL_ID;
import static com.ad.app.notify.utils.Constants.CHANNEL_NAME;
import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;
import static com.ad.app.notify.utils.Constants.TAG_EMAIL;
import static com.ad.app.notify.utils.Constants.TAG_NOTE;
import static com.ad.app.notify.utils.Constants.TAG_PHONE_NUMBER;
import static com.ad.app.notify.utils.Constants.TAG_URL;
import static com.ad.app.notify.utils.Constants.TAG_WATCH_LATER;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.ad.app.notify.R;
import com.ad.app.notify.activities.EditorActivity;
import com.ad.app.notify.activities.MainActivity;
import com.ad.app.notify.database.NotificationDatabaseHandler;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.receiver.NotificationActionReceiver;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.TextProcessor;
import com.ad.app.notify.utils.Utils;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationModel notificationModel = (NotificationModel) intent.getSerializableExtra(NOTIFICATION_MODEL);

        String toast = intent.hasExtra(Constants.ACTION) ?
                createNotification(notificationModel) ?
                        intent.getStringExtra(Constants.ACTION).equals(Constants.ACTION_ADD) ?
                                new NotificationDatabaseHandler(this)
                                        .addNewNotification(notificationModel) ?
                                        "Added to Notify" : "addNewNotification() - Exception Found" :
                                intent.getStringExtra(Constants.ACTION)
                                        .equals(Constants.ACTION_REBOOTED) ? null :
                                        new NotificationDatabaseHandler(this)
                                                .updateExistingNotification(notificationModel) ?
                                                "Updated" : "updateExistingNotification() - Exception Found" :
                        "createNotification() : Exception Found" :
                "hasExtra() : Exception Found";

        if (toast != null)
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean createNotification(NotificationModel model) {

        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(this);

        //gets milli seconds value saved in array
        String temporaryNotes = sharedConfig.getString(getString(R.string.temporary_notes_title), "never");
        boolean isCollapsed = sharedConfig.getBoolean(getString(R.string.collapsed_view_title), true);
//        boolean isGrouped = sharedConfig.getBoolean(getString(R.string.groups_title),false);

        //Notification Builder ---------------------------------------------------------------------
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(CHANNEL_ID);
        }


        //Constant values --------------------------------------------------------------------------
        mBuilder.setSmallIcon(R.drawable.ic_notification_icon);
        mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        mBuilder.setSilent(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.build().flags = NotificationCompat.FLAG_NO_CLEAR;
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        mBuilder.setDefaults(0);
        mBuilder.setColor(Color.parseColor("#EEEEEE"));
        mBuilder.setColorized(true);
        mBuilder.setSubText(model.getNotificationTags());


        //User can change following values ---------------------------------------------------------
        if (!model.isNotificationPinned() && !temporaryNotes.equals("never"))
            mBuilder.setTimeoutAfter(Integer.parseInt(temporaryNotes));

        if (model.getNotificationCategory().equals(TAG_WATCH_LATER)) {
            mBuilder.setGroupSummary(true);
            mBuilder.setGroup(model.getNotificationCategory());
        } else {
            mBuilder.setGroupSummary(false);
        }


//        if(isGrouped){
//            mBuilder.setGroupSummary(true);
//            mBuilder.setGroup(model.getNotificationCategory());
//        }else{
//            mBuilder.setGroupSummary(false);
//        }

        //if is pinned then se auto cancel false
        mBuilder.setAutoCancel(!model.isNotificationPinned());
        mBuilder.setOngoing(model.isNotificationPinned());


        switch (model.getNotificationCategory()) {

            case TAG_WATCH_LATER:
                mBuilder.setCustomContentView(getWatchLaterRemoteView(model));
                break;

            case TAG_EMAIL:
                if (isCollapsed) {
                    mBuilder.setCustomBigContentView(getFullRemoteViews(model));
                } else {
                    RemoteViews remoteViews = getFullRemoteViews(model);
                    mBuilder.setCustomContentView(remoteViews);
                    mBuilder.setCustomBigContentView(remoteViews);
                }
                break;

            case TAG_NOTE:
                if (isCollapsed) {
                    mBuilder.setCustomBigContentView(getFullRemoteViews(model));
                } else {
                    RemoteViews remoteViews = getFullRemoteViews(model);
                    mBuilder.setCustomContentView(remoteViews);
                    mBuilder.setCustomBigContentView(remoteViews);
                }

                break;

            case TAG_PHONE_NUMBER:
                if (isCollapsed) {
                    mBuilder.setCustomBigContentView(getFullRemoteViews(model));
                } else {
                    RemoteViews remoteViews = getFullRemoteViews(model);
                    mBuilder.setCustomContentView(remoteViews);
                    mBuilder.setCustomBigContentView(remoteViews);
                }
                break;

            case TAG_URL:
                if (isCollapsed) {
                    mBuilder.setCustomBigContentView(getFullRemoteViews(model));
                } else {
                    RemoteViews remoteViews = getFullRemoteViews(model);
                    mBuilder.setCustomContentView(remoteViews);
                    mBuilder.setCustomBigContentView(remoteViews);
                }
                break;


            default:
                Toast.makeText(this, "Notification Service : Exception found", Toast.LENGTH_SHORT).show();
                break;
        }


        try {
            notificationManager.notify(model.getNotificationId(), mBuilder.build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private RemoteViews getWatchLaterRemoteView(NotificationModel model) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.layout_notification_watch_later);

        remoteViews.setTextViewText(R.id.txt_SubText_WatchLater, model.getNotificationSubText());
        remoteViews.setTextViewText(R.id.txt_Time_WatchLater,
                model.getNotificationTime() + " • " + model.getNotificationDate());
        remoteViews.setImageViewResource(R.id.ic_Pin_Watchlater, model.isNotificationPinned() ?
                R.drawable.ic_pinned :
                R.drawable.ic_unpinned);

        remoteViews.setOnClickPendingIntent(R.id.ic_Pin_Watchlater, getPinNotificationIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.txt_SubText_WatchLater,
                getSearchUrlIntent(model.getNotificationSubText()));
        return remoteViews;
    }

    private RemoteViews getFullRemoteViews(NotificationModel model) {

        String receivedText = model.getNotificationSubText();

        final String trimmedText = receivedText.length() > 200 ?
                receivedText.substring(0, 200) + "..." :
                receivedText;

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_full);
        remoteViews.setTextViewText(R.id.txt_Body, trimmedText);
        remoteViews.setTextViewText(R.id.txt_Date_Full_Notification, model.getNotificationDate());
        remoteViews.setImageViewResource(R.id.img_ActionPinNotification,
                model.isNotificationPinned() ?
                        R.drawable.ic_pinned :
                        R.drawable.ic_unpinned);

        remoteViews.setOnClickPendingIntent(R.id.img_ActionCopy, getCopyToClipboardIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionSearch, getSearchUrlIntent(receivedText));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionShare, getShareIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionMessage, getMessageIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionDialer, getDialerIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionEdit, getEditIntent(model));
        remoteViews.setOnClickPendingIntent(R.id.img_ActionPinNotification, getPinNotificationIntent(model));

        return remoteViews;
    }

    private PendingIntent getPinNotificationIntent(@NonNull NotificationModel model) {

        return PendingIntent.getBroadcast(this,
                model.getNotificationId(),
                new Intent(this, NotificationActionReceiver.class)
                        .putExtra(NOTIFICATION_MODEL, model)
                        .putExtra(Constants.NOTIFICATION_INTENT_ACTION,
                                model.isNotificationPinned() ?
                                        Constants.ACTION_REMOVE_PIN :
                                        Constants.ACTION_ATTACH_PIN),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getCopyToClipboardIntent(@NonNull NotificationModel model) {

        return PendingIntent.getBroadcast(this, model.getNotificationId(),
                new Intent(this, NotificationActionReceiver.class)
                        .putExtra(NOTIFICATION_MODEL, model)
                        .putExtra(Constants.NOTIFICATION_INTENT_ACTION,
                                Constants.ACTION_COPY),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getSearchUrlIntent(String url) {

        Intent intent;

        String extractedUrl = new TextProcessor(this).getSearchableUrl(url);

        if (new TextProcessor(this).isYoutubeUrl(extractedUrl)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        } else {
            intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, extractedUrl); // query contains search string
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return TaskStackBuilder.create(this)
                .addParentStack(MainActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(new Utils(this).getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getShareIntent(@NonNull NotificationModel model) {

        Intent intent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, model.getNotificationSubText());

        return TaskStackBuilder.create(this)
                .addParentStack(MainActivity.class)
                .addNextIntent(Intent.createChooser(intent, null))
                .getPendingIntent(model.getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getMessageIntent(@NonNull NotificationModel model) {

        if (model.getNotificationCategory().equals(TAG_PHONE_NUMBER)) {

            return PendingIntent.getActivity(this, model.getNotificationId(),
                    new Intent(Intent.ACTION_VIEW)
                            .setType("vnd.android-dir/mms-sms")
                            .putExtra("address", model.getNotificationSubText())
                            .putExtra("sms_body", ""),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else if (model.getNotificationCategory().equals(TAG_EMAIL)) {
            String to = model.getNotificationSubText();
            String subject = "";
            String body = "";

            String mailTo = "mailto:" + to +
                    "?&subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(body);

            return PendingIntent.getActivity(this, model.getNotificationId(),
                    new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse(mailTo)),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        return null;
    }

    private PendingIntent getDialerIntent(@NonNull NotificationModel model) {

        return PendingIntent.getActivity(this, model.getNotificationId(),
                new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getNotificationSubText())),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getEditIntent(@NonNull NotificationModel model) {

        return PendingIntent.getActivity(this, model.getNotificationId(),
                new Intent(this, EditorActivity.class)
                        .putExtra(NOTIFICATION_MODEL, model)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    }
}
