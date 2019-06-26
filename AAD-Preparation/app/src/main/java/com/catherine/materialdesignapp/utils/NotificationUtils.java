package com.catherine.materialdesignapp.utils;

import android.app.*;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.activities.MainActivity;
import com.catherine.materialdesignapp.models.ChannelInfo;

public class NotificationUtils extends ContextWrapper {
    private ChannelInfo channelInfo;
    private NotificationManager notificationManager;

    public NotificationUtils(Context context) {
        super(context);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    public NotificationUtils(Context context, ChannelInfo channelInfo) {
        super(context);
        createChannels(channelInfo);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannels(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(channelInfo.channelId,
                channelInfo.channelName, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        androidChannel.setShowBadge(true);
        getNotificationManager().createNotificationChannel(androidChannel);
    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /**
     * Push a notification on whatever version devices
     *
     * @param title          comes from Firebase cloud messing
     * @param body           comes from Firebase cloud messing
     * @param notificationId user defined
     */
    public void sendNotification(String title, String body, int notificationId) {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                    channelInfo.channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker("This is a default ticker")
                    .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            getNotificationManager().notify(notificationId, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker("This is a default ticker")
                    .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            getNotificationManager().notify(notificationId, builder.build());
        }
    }

    public void sendHandsUpNotification(String title, String body, int notificationId) {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // action
        Intent positive = new Intent(OccupiedActions.ACTION_UPDATE_NOTIFICATION);
        positive.putExtra(OccupiedActions.NOTIFICATION_ID, notificationId);
        positive.setAction(OccupiedActions.ACTION_POSITIVE_CLICK);
        PendingIntent positiveIntent = PendingIntent.getBroadcast(this, notificationId, positive, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent negative = new Intent(OccupiedActions.ACTION_UPDATE_NOTIFICATION);
        negative.putExtra(OccupiedActions.NOTIFICATION_ID, notificationId);
        negative.setAction(OccupiedActions.ACTION_NEGATIVE_CLICK);
        PendingIntent negativeIntent = PendingIntent.getBroadcast(this, notificationId, negative, PendingIntent.FLAG_CANCEL_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                    channelInfo.channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker("This is a default ticker")
                    .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_schedule_black_24dp, "Positive", positiveIntent)
                    .addAction(R.drawable.ic_schedule_black_24dp, "Negative", negativeIntent)
                    .setContentIntent(pendingIntent);
            getNotificationManager().notify(notificationId, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker("This is a default ticker")
                    .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_schedule_black_24dp, "Positive", positiveIntent)
                    .addAction(R.drawable.ic_schedule_black_24dp, "Negative", negativeIntent)
                    .setContentIntent(pendingIntent);
            getNotificationManager().notify(notificationId, builder.build());
        }
    }

    public void sendNotificationWithResponse(String title, String body, int notificationId) {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(OccupiedActions.ACTION_UPDATE_NOTIFICATION);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // action
        Intent reply = new Intent(OccupiedActions.ACTION_UPDATE_NOTIFICATION);
        reply.putExtra(OccupiedActions.NOTIFICATION_ID, notificationId);
        reply.setAction(OccupiedActions.ACTION_REPLAY);
        PendingIntent replyRendingIntent = PendingIntent.getBroadcast(this, notificationId, reply, PendingIntent.FLAG_CANCEL_CURRENT);
        RemoteInput remoteInput = new RemoteInput.Builder(OccupiedActions.NOTIFICATION_REPLY_KEY)
                .setLabel("Type Something")
                .build();
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_schedule_black_24dp, "Reply", replyRendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                    channelInfo.channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker("This is a default ticker")
                    .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                    .setAutoCancel(true)
                    .addAction(action)
                    .setContentIntent(pendingIntent);
            getNotificationManager().notify(notificationId, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker("This is a default ticker")
                    .setSmallIcon(R.drawable.ic_restaurant_black_24dp)
                    .setAutoCancel(true)
                    .addAction(action)
                    .setContentIntent(pendingIntent);
            getNotificationManager().notify(notificationId, builder.build());
        }
    }


    /**
     * Create a notification for foreground services
     *
     * @return Notification
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public Notification getNotificationForForegroundServices() {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_view);
        contentView.setImageViewResource(R.id.iv_icon, R.drawable.ic_music_note_black_24dp);
        contentView.setTextViewText(R.id.tv_title, "Foreground Service Title");

        return new Notification.Builder(getApplicationContext(), channelInfo.channelId)
                .setContentTitle("Foreground Service Title")
                .setContentText("Foreground Service content")
                .setTicker("Foreground Service Ticker")
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
//                .setCustomContentView(contentView)
                .build();
    }
}