package com.swe.gruppe4.freespace;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by Merlin on 19.12.2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String body = remoteMessage.getNotification().getBody();
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        switch (body) {
            case "5minutes": {
                Resources res = getResources();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_stat_notify_appicon)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher_appicon))
                                .setContentTitle(res.getString(R.string.app_name))
                                .setContentText("Ihre Sitzung läuft in 5 Minuten aus");

                Intent resultIntent = new Intent(getApplicationContext(), ActiveSessionActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notif = mBuilder.build();
                notif.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(3, notif);
                break;
            }
            case "roomBlocked": {
                Resources res = getResources();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_stat_notify_appicon)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher_appicon))
                                .setContentTitle(res.getString(R.string.app_name))
                                .setContentText("In ihrem aktuellen Raum findet gleich eine Veranstaltung statt.");

                Intent resultIntent = new Intent(getApplicationContext(), ActiveSessionActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notif = mBuilder.build();
                notif.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(3, notif);
                break;
            }
            default:
                Log.d(TAG, "Unbekannte Nachricht");
                Resources res = getResources();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_stat_notify_appicon)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher_appicon))
                                .setContentTitle(res.getString(R.string.app_name))
                                .setContentText(body);

                Intent resultIntent = new Intent(getApplicationContext(), ActiveSessionActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notif = mBuilder.build();
                notif.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(3, notif);
                break;
        }

    }
}

