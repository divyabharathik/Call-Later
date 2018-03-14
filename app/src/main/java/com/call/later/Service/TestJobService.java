package com.call.later.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.call.later.R;
import com.call.later.Views.Activities.MainActivity;

public class TestJobService extends JobService {
    int id=0;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e("TestJobService st",""+System.currentTimeMillis());
        String number = jobParameters.getExtras().get("callNumber").toString();
        Context context=this;
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
        //call now
        Intent call_phoneintent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        PendingIntent call_dialerpendingIntent = PendingIntent.getActivity(context, 1, call_phoneintent, 0);
        //snooze
        Intent snooze_intent = new Intent(context,NotificationService.class);
        snooze_intent.putExtra("id",id);
        snooze_intent.putExtra("notification_action","snooze");
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(context, 1, snooze_intent, 0);
        //delete
        Intent delete_intent = new Intent(context,NotificationService.class);
        delete_intent.putExtra("id",id);
        delete_intent.putExtra("notification_action","delete");
        PendingIntent delete_pendingIntent = PendingIntent.getActivity(context, 1, delete_intent, 0);

        PersistableBundle persistableBundle =jobParameters.getExtras();
//        Toast.makeText(context, "Job Started"+System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"CallNotification");

        id=jobParameters.getExtras().getInt("id");

        builder.setContentTitle("Call Remainder")
                .setContentText("Call " + jobParameters.getExtras().get("callName")+ "("+number+")")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .addAction(R.drawable.ic_call_black_24dp,"Call Now",call_dialerpendingIntent)
                .addAction(R.drawable.ic_snooze_black_24dp,"Snooze",snoozePendingIntent)
                .addAction(R.drawable.ic_delete_black_24dp,"Delete",delete_pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int df=persistableBundle.getInt("id");
        notificationManager.notify(df, builder.build());

        
        jobFinished(jobParameters,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e("TestJobService so",""+System.currentTimeMillis());
        jobFinished(jobParameters,false);
//        Toast.makeText(this, "Job completed"+System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        return false;
    }


}
