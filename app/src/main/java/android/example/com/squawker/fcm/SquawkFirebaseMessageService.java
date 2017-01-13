package android.example.com.squawker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.data.SquawkContract;
import android.example.com.squawker.data.SquawkProvider;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by lyla on 12/24/16.
 */
public class SquawkFirebaseMessageService extends FirebaseMessagingService {

    private static String LOG_TAG = SquawkFirebaseMessageService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with FCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(LOG_TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(LOG_TAG,
                    "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(LOG_TAG,
                    "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            //sendNotification(remoteMessage.getNotification().getBody());
        }

        insertSquawk(remoteMessage.getData());

    }

    private void insertSquawk(Map<String, String> data) {
        ContentValues newMessage = new ContentValues();
        newMessage.put(SquawkContract.COLUMN_AUTHOR, data.get(SquawkContract.COLUMN_AUTHOR));
        newMessage.put(SquawkContract.COLUMN_MESSAGE,
                data.get(SquawkContract.COLUMN_MESSAGE).trim());
        newMessage.put(SquawkContract.COLUMN_DATE, data.get(SquawkContract.COLUMN_DATE));
        getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newMessage);
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
