package com.call.later.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.call.later.R;
import com.call.later.Views.Activities.MainActivity;

import static com.call.later.ApplicationSingleton.CALL_NOTIFICATION_ID;

public class TestJobService extends JobService {
    int id = 0;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e("TestJobService st", "" + System.currentTimeMillis());
        String number = jobParameters.getExtras().get("callNumber").toString();
        Context context = this;
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
        //call now
        Intent call_phoneintent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        PendingIntent call_dialerpendingIntent = PendingIntent.getActivity(context, 1, call_phoneintent, 0);
        //snooze
        Intent snooze_intent = new Intent(context, NotificationService.class);
        snooze_intent.putExtra("id", id);
        snooze_intent.putExtra("notification_action", "snooze");
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(context, 1, snooze_intent, 0);
        //delete
        Intent delete_intent = new Intent(context, NotificationService.class);
        delete_intent.putExtra("id", id);
        delete_intent.putExtra("notification_action", "delete");
        PendingIntent delete_pendingIntent = PendingIntent.getActivity(context, 1, delete_intent, 0);

        PersistableBundle persistableBundle = jobParameters.getExtras();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CALL_NOTIFICATION_ID);
        id = jobParameters.getExtras().getInt("id");
        NotificationCompat.Action callAction = new NotificationCompat.Action.Builder(R.drawable.ic_call_black_24dp, "Call Now", call_dialerpendingIntent).build();
        NotificationCompat.Action snoozeAction = new NotificationCompat.Action.Builder(R.drawable.ic_snooze_black_24dp, "Snooze", snoozePendingIntent).build();
        NotificationCompat.Action deleteAction = new NotificationCompat.Action.Builder(R.drawable.ic_delete_black_24dp, "Delete", delete_pendingIntent).build();
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle("Call Reminders!");
//        inboxStyle.addLine("Multiple call reminders");
        builder.setContentTitle("Call Remainder")
                .setStyle(new NotificationCompat.InboxStyle())
                .setContentText("Call " + jobParameters.getExtras().get("callName") + "(" + number + ")")
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .addAction(callAction)
                .addAction(snoozeAction)
                .addAction(deleteAction)
                .setWhen(System.currentTimeMillis())
                .setChannelId(CALL_NOTIFICATION_ID)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setGroup("rr")
                .setColor(ContextCompat.getColor(context, R.color.colorAccent));
        Notification notification = builder.build();
        int df = persistableBundle.getInt("id");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify("cc", df, notification);


        jobFinished(jobParameters, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e("TestJobService so", "" + System.currentTimeMillis());
        jobFinished(jobParameters, false);
//        Toast.makeText(this, "Job completed"+System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        return false;
    }


}
