package fr.vtarreau.borntocode.crowdrescue;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import fr.necmergitur.borntocode.crowdrescue.crowdrescue.R;

/**
 * Created by Vincent on 17/01/2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message"); //type = description
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        //available/response ->id, has_stuff

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PUSH", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_cast_dark)
                .setContentTitle("Alerte")
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notification = new Notification(R.drawable.ic_cast_dark, message,);

        //Intent notificationIntent = new Intent(this, MainActivity.class);

        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                //| Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //PendingIntent intent = PendingIntent.getActivity(this, 0,
                //notificationIntent, 0);

        //notification.setLatestEventInfo(context, title, message, intent);
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //notificationManager.notify(0, notification);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
