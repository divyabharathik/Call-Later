package com.call.later.BroadCastListeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.call.later.Views.Activities.PopupActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallStateReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.w("intent " , intent.getAction().toString());

        if (intent.getAction().equals("android.intent.action.ACTION_NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }
    }


    public void onCallStateChanged(Context context, int state, String number) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
//                String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 1";
//                Uri callUri = Uri.parse("content://call_log/calls");
//                Cursor curLog = context.getContentResolver().query(callUri, null, null, null, strOrder);
////                Cursor curLog = CallLogHelper.getAllCallLogs(context.getContentResolver());
//                while (curLog.moveToNext()) {
//                    String callNumber = curLog.getString(curLog
//                            .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
//                    System.out.println("callNumber:"+callNumber);
//
//
//                    String callName = curLog
//                            .getString(curLog
//                                    .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
//                    if (callName == null) {
//                        System.out.println("callName:"+"Unknown");
//                    } else
//                        System.out.println("callName:"+callName);
//
//                    String callDate = curLog.getString(curLog
//                            .getColumnIndex(android.provider.CallLog.Calls.DATE));
//                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//                    String dateString = formatter.format(new Date(Long
//                            .parseLong(callDate)));
//                    System.out.println("Date:"+dateString);
//
//                    String callType = curLog.getString(curLog
//                            .getColumnIndex(android.provider.CallLog.Calls.TYPE));
//                    if (callType.equals("1")) {
//                        System.out.println("Call type:"+"Incoming");
//                    } else
//                        System.out.println("Call type:"+"Outgoing");
//
//                    String duration = curLog.getString(curLog
//                            .getColumnIndex(android.provider.CallLog.Calls.DURATION));
//                    System.out.println("Call duration:"+duration);
//                }
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                }
                else if(isIncoming){

                }
                else{
                    String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 1";
                    Uri callUri = Uri.parse("content://call_log/calls");
                    Cursor curLog = context.getContentResolver().query(callUri, null, null, null, strOrder);
                    String callNumber ="";
                    String callName ="";
                    String callDate ="";
                    String dateString ="";
                    String callType ="";
                    String duration ="";
                    while (curLog.moveToNext()) {
                        callNumber = curLog.getString(curLog
                                .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                        callName = curLog
                                .getString(curLog
                                        .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                        callDate = curLog.getString(curLog
                                .getColumnIndex(android.provider.CallLog.Calls.DATE));
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        dateString = formatter.format(new Date(Long
                                .parseLong(callDate)));
                        callType = curLog.getString(curLog
                                .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                        duration = curLog.getString(curLog
                                .getColumnIndex(android.provider.CallLog.Calls.DURATION));
                        System.out.println("callNumber:"+callNumber);
                        if (callName == null) {
                            System.out.println("callName changed:"+"Unknown");
                        } else
                            System.out.println("callName changed:"+callName);
                        System.out.println("Date:"+dateString);
                        if (callType.equals("1")) {
                            System.out.println("Call type:"+"Incoming");
                        } else {
                            System.out.println("Call type:" + "Outgoing");
                        }
                        System.out.println("Call duration:"+duration);
                    }
                    if(duration.equals("0") && callType.equals("2")) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("Requesting Permission")
//                                .setMessage("Do you want to call " + callName + " again ?")
//                                .setPositiveButton("15 mins", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        Toast.makeText(context, "After 15 mins", Toast.LENGTH_SHORT).show();
//                                    }
//                                }).setPositiveButton("30 mins", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                                Toast.makeText(context, "After 30 mins", Toast.LENGTH_SHORT).show();
//                            }
//                        }).setPositiveButton("custom", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                                Toast.makeText(context, "choose time ", Toast.LENGTH_SHORT).show();
//                            }
//                        }).show();
                        //
                        // TODO: transparent activity for pop up window
//                        Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
                        System.out.println("callNumber:"+callNumber);
                        if (callName == null) {
                            System.out.println("callName changed:"+"Unknown");
                        } else
                            System.out.println("callName changed:"+callName);
                        System.out.println("Date:"+dateString);
                        if (callType.equals("1")) {
                            System.out.println("Call type:"+"Incoming");
                        } else {
                            System.out.println("Call type:" + "Outgoing");
                        }
                        System.out.println("Call duration:"+duration);
                        Intent intent = new Intent(context,PopupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("callNumber",callNumber);
                        intent.putExtra("callName",callName);
                        intent.putExtra("callTime",dateString);
                                context.getApplicationContext().startActivity(intent);

                    }

                }

                break;
        }
        lastState = state;
    }
}