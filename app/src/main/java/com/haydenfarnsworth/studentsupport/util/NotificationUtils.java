package com.haydenfarnsworth.studentsupport.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.fragment.app.Fragment;

import com.haydenfarnsworth.studentsupport.R;

public class NotificationUtils {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    int importance;
    NotificationChannel notificationChannel;
    AlarmManager am;
    PendingIntent pendingIntent;
    Intent notificationIntent;
    Fragment context;

    public NotificationUtils(Fragment context, Notification notification) {
        notificationIntent = new Intent(context.getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        this.context = context;
        pendingIntent = PendingIntent.getBroadcast(context.getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager)context.getActivity().getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleNotification(long delay) {



        NotificationManager nm = (NotificationManager)context.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_HIGH;
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "ASSESSMENT_NOTIFICATION_CHANNEL", importance);
            nm.createNotificationChannel(notificationChannel);
        }
        am.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
    }

    public void cancelNotification() {
        pendingIntent = PendingIntent.getBroadcast(context.getActivity(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);
    }

    public static Notification getNotification(String content, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        return builder.getNotification();
    }
}
