package com.call.later.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.call.later.Model.CallEventItem;
import com.call.later.Views.Activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NotificationService extends IntentService {
    public NotificationService(String name) {
        super(name);
        return;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int id= Objects.requireNonNull(Objects.requireNonNull(intent).getExtras()).getInt("id");
        String typeOfAction = Objects.requireNonNull(intent.getExtras()).getString("notification_action");
        if(typeOfAction!=null && typeOfAction.equals("snooze")){
            //snooze
            CallEventItem callEventItem = ((MainActivity)getApplicationContext()).getSpecificItem(id);
            String date = callEventItem.getCalltime();
            try {
                Date date1=new SimpleDateFormat("dd-MMM-yyyy '@' h:mm a").parse(date);
                Calendar calendar= Calendar.getInstance();
                calendar.setTime(date1);
                calendar.add(Calendar.MINUTE, 5);
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy '@' h:mm a", Locale.getDefault());
                callEventItem.setCalltime(format.format(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(typeOfAction!=null && typeOfAction.equals("delete")){
            //delete
            ((MainActivity)getApplicationContext()).deleteSpecificItem(id);
        }
    }
}
