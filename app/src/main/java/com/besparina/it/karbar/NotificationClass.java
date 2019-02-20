package com.besparina.it.karbar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

public class NotificationClass {
    private NotificationManager notificationManager;
    private NotificationManager notifManager;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode;
    private String OrderCode;
    private String Title;
    private String Detils;
	public void Notificationm(Context context, String Title, String Detils,String OrderCode,int notificationID,Class<?> Cls) {
        dbh = new DatabaseHelper(context);
        try {

            dbh.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbh.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }

        this.OrderCode=OrderCode;
        this.Title=Title;
        this.Detils=Detils;
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM login", null);
        for (int i = 0; i < coursors.getCount(); i++) {
            coursors.moveToNext();

            karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
        }
        try {	if (db.isOpen()) {	db.close();		if(!coursors.isClosed())
            coursors.close();}}	catch (Exception ex){	}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification(context, Cls);
        } else {
            long[] v = {500, 1000};
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, String.valueOf(notificationID));
            mBuilder.setSmallIcon(R.drawable.logo);
            mBuilder.setContentTitle(Title);
            mBuilder.setContentText(Detils);
            mBuilder.setVibrate(v);
            mBuilder.setSound(uri);
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            mBuilder.setAutoCancel(true);
            Intent intent = new Intent(context, Cls);
            intent.putExtra("karbarCode", karbarCode);
            intent.putExtra("OrderCode", OrderCode);
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
            mBuilder.setContentIntent(pIntent);
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // notificationID allows you to update the notification later on.
            notificationManager.notify(notificationID, mBuilder.build());
        }
    }
    public void createNotification(Context context,Class<?> Cls) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = context.getString(R.string.default_notification_channel_title); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context,Cls);
            intent.putExtra("karbarCode", karbarCode);
            intent.putExtra("OrderCode", OrderCode);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(Title)                            // required
                    .setSmallIcon(R.drawable.logo)   // required
                    .setContentText(Detils) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(Title)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, Cls);
            intent.putExtra("karbarCode", karbarCode);
            intent.putExtra("OrderCode", OrderCode);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(Title)                            // required
                    .setSmallIcon(R.drawable.logo)   // required
                    .setContentText(Detils) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(Title)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }
}

