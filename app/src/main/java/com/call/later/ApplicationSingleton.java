package com.call.later;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.call.later.BroadCastListeners.CallStateReceiver;
import com.facebook.stetho.Stetho;

import java.util.Objects;

public class ApplicationSingleton extends Application {
    public static final String CALL_NOTIFICATION_ID = "CallNotification";
    @Override
    public void onCreate() {
        super.onCreate();
//        Intent intent = new Intent("android.intent.action.PHONE_STATE");
//        intent.setClass(this, CallStateReceiver.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CALL_NOTIFICATION_ID, "My to Calls..", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Call Later Reminders");
            channel.enableLights(true);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                initializeBroadcastReceivers(getApplicationContext());
            }
        }

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.intent.action.PHONE_STATE");
//        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
//
//        BroadcastReceiver receiver = new CallStateReceiver();
//        registerReceiver(receiver, filter);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void initializeBroadcastReceivers(Context context) {
        BroadcastReceiver phoneCallReceiver = new CallStateReceiver();
        IntentFilter phoneintentFilter = new IntentFilter();
        phoneintentFilter.addAction("android.intent.action.PHONE_STATE");
//        phoneintentFilter.addAction("android.intent.action.ACTION_NEW_OUTGOING_CALL");
        context.registerReceiver(phoneCallReceiver, phoneintentFilter);

//        BroadcastReceiver startService_Receiver = new StartServiceReceiver();
//        IntentFilter start_ServiceFilter = new IntentFilter();
//        start_ServiceFilter.addAction("com.technobees.nalamdot.START_SERVICE");
//        context.registerReceiver(startService_Receiver , start_ServiceFilter);

//
//        class PhoneCallReceiver extends BroadcastReceiver {
//            private static int lastState = TelephonyManager.CALL_STATE_IDLE;
//            private static Date callStartTime;
//            private static boolean isIncoming;
//            private static String savedNumber;  //because the passed incoming is only valid in ringing
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
////We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
//                if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
//                    savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
//                }
//                else{
//                    String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
//                    String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                    int state = 0;
//                    if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
//                        state = TelephonyManager.CALL_STATE_IDLE;
//                    }
//                    else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
//                        state = TelephonyManager.CALL_STATE_OFFHOOK;
//                    }
//                    else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
//                        state = TelephonyManager.CALL_STATE_RINGING;
//                    }
//
//                    ApplicationSingleton.CALL_RECEIVING_STATE=state;
//                }
//            }
//        }
    }
}
