package com.colval_agenda;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;

import com.github.tibolte.colvalcalendar.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by macbookpro on 17-05-03.
 */

public class Notifications {

    private Context context;


    public Notifications(Context ctx)
    {
        context = ctx;
    }

    public void createNotificationAtDate(int id, Date date, String title, String desc)
    {
        AlarmManager alarms = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Receiver receiver = new Receiver();
        IntentFilter filter = new IntentFilter("ALARM_ACTION");
        context.registerReceiver(receiver, filter);

        Intent intent = new Intent("ALARM_ACTION");
        intent.putExtra("nId", id);
        intent.putExtra("nTitle", title);
        intent.putExtra("nDesc", desc);
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, intent, id);
        // I choose 3s after the launch of my application
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long in = (date.getTime() - Calendar.getInstance().getTime().getTime()) / 1000;
         alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), operation);

    }

    public void createNotification(int id, String title, String desc)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(title)
                        .setContentText(desc);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }

    public void deleteNotification(int id)
    {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }
}

class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("nId", 000);
        String title = intent.getStringExtra("nTitle");
        String desc = intent.getStringExtra("nDesc");
        Notifications n = new Notifications(context);
        n.createNotification(id, title, desc);
    }
}

