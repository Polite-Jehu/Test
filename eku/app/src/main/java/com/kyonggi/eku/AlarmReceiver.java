package com.kyonggi.eku;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    private String channelId="alarm_channel";
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Intent goIntent = new Intent(context, TodoActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(goIntent);
        PendingIntent busRoutePendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);

        final NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentTitle("오늘의 할일을 확인해보세요")
                .setContentText("당신의 할일을 오전의 eku와 시작하세요..!")
                .setContentIntent(busRoutePendingIntent);


        final NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int id=(int)System.currentTimeMillis();

        notificationManager.notify(id,notificationBuilder.build());

    }
}