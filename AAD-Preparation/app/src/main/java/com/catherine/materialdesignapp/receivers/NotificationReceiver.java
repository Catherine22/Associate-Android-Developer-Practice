package com.catherine.materialdesignapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.utils.OccupiedActions;

public class NotificationReceiver extends BroadcastReceiver {
    public final static String TAG = NotificationReceiver.class.getSimpleName();

    public NotificationReceiver() {
        Log.i(TAG, "init");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Update the notification
        Log.i(TAG, "onReceive");
        Log.i(TAG, intent.toString());
        String action = intent.getAction();
        if (OccupiedActions.ACTION_POSITIVE_CLICK.equals(action)) {
            Log.d(TAG, "ACTION_POSITIVE_CLICK");
        } else if (OccupiedActions.ACTION_NEGATIVE_CLICK.equals(action)) {
            Log.d(TAG, "ACTION_NEGATIVE_CLICK");
        } else if (OccupiedActions.ACTION_REPLAY.equals(action)) {
            String title = "Your message from notification";
            String body = intent.getStringExtra(OccupiedActions.NOTIFICATION_REPLY_KEY);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share)));
        }
    }
}