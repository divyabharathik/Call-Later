package com.call.later.Views.Activities;

import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.call.later.Model.CallEventItem;
import com.call.later.R;
import com.call.later.Service.TestJobService;
import com.call.later.Views.CallItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PopupActivity extends AppCompatActivity {
    String callNumber = "";
    String callName = "";
    String callTime = "";
    Date date;
    Date customDate;
    String date_str = "";
    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy '@' h:mm a", Locale.getDefault());
    private CallItemViewModel callItemViewModel;
    private Calendar customCalendar;

//    private static Date addMinutesToDate(int minutes, Date beforeTime) {
//        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
//        long curTimeInMs = beforeTime.getTime();
//        return new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Intent intent = getIntent();
        intent.getExtras();
        callNumber = intent.getStringExtra("callNumber");
        callName = intent.getStringExtra("callName");
        callTime = intent.getStringExtra("callTime");
        TextView msg = findViewById(R.id.remainder_msg_textView);
        msg.setText("Do you want to get reminded after some time? For calling " + callName + " " + callNumber + " ?");
        ImageView close = findViewById(R.id.closeButton);
        close.setOnClickListener(v -> {
            finishAffinity();
        });
        TextView afterSetRemainder = findViewById(R.id.after_remainder_set);
        TextView text = findViewById(R.id.textView3);
        ToggleButton mins15button = findViewById(R.id.mins15button);
        ToggleButton mins30button = findViewById(R.id.mins30button);
        ToggleButton customTimebutton = findViewById(R.id.custom_timebutton);
        customTimebutton.setText("Choose");
        Button save = findViewById(R.id.saveButton);
        Button cancel = findViewById(R.id.cancelButton);
        afterSetRemainder.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        Log.d("call time", "onCreate: " + callTime);
        System.out.println("callNumber" + callNumber);
        System.out.println("callName" + callName);
        System.out.println("callTime" + callTime);
        callItemViewModel = ViewModelProviders.of(this).get(CallItemViewModel.class);

        {
            try {
                date = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault()).parse(callTime);
                System.out.println("date obj:" + date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mins15button.setOnClickListener(v -> {
            if (!mins15button.isChecked()) {
                mins15button.setBackgroundResource(R.drawable.shape_off);
                mins15button.setChecked(false);
            } else {
                mins15button.setBackgroundResource(R.drawable.shape_on);
                mins15button.setChecked(true);
                mins30button.setChecked(false);
                mins30button.setBackgroundResource(R.drawable.shape_off);
                customTimebutton.setChecked(false);
                customTimebutton.setBackgroundResource(R.drawable.shape_off);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MINUTE, 15);
                afterSetRemainder.setText(calendar.get(Calendar.HOUR) + " : " + Calendar.MINUTE + " " + (customCalendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
                System.out.println("date obj(15):" + date.toString());
                afterSetRemainder.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
            }


        });

        mins30button.setOnClickListener(v -> {
            if (!mins30button.isChecked()) {
                mins30button.setBackgroundResource(R.drawable.shape_off);
                mins30button.setChecked(false);
            } else {
                mins30button.setBackgroundResource(R.drawable.shape_on);
                mins30button.setChecked(true);
                mins15button.setChecked(false);
                mins15button.setBackgroundResource(R.drawable.shape_off);
                customTimebutton.setChecked(false);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MINUTE, 30);
                afterSetRemainder.setText(calendar.get(Calendar.HOUR) + " : " + Calendar.MINUTE + " " + (customCalendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
                customTimebutton.setBackgroundResource(R.drawable.shape_off);
                System.out.println("date obj(30):" + date.toString());
                afterSetRemainder.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
            }

        });

        customTimebutton.setOnClickListener(v -> {
            if (!customTimebutton.isChecked()) {
                customTimebutton.setBackgroundResource(R.drawable.shape_off);
                customTimebutton.setChecked(false);
            } else {
                customTimebutton.setBackgroundResource(R.drawable.shape_on);
                customTimebutton.setChecked(true);
                mins30button.setChecked(false);
                mins30button.setBackgroundResource(R.drawable.shape_off);
                mins15button.setChecked(false);
                mins15button.setBackgroundResource(R.drawable.shape_off);
                customCalendar = Calendar.getInstance();
                int hour = customCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = customCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        customCalendar.set(Calendar.MINUTE, selectedMinute);
                        customCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        date_str = format.format(customCalendar.getTime());
//                        customDate=customCalendar.getTime();
                        customTimebutton.setText(customCalendar.get(Calendar.HOUR) + " : " + selectedMinute + " " + (customCalendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
                        afterSetRemainder.setText(customCalendar.get(Calendar.HOUR) + " : " + selectedMinute + " " + (customCalendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                afterSetRemainder.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
            }

        });

        save.setOnClickListener(view -> {
            CallEventItem callEventItem = null;

            ComponentName serviceComponent = new ComponentName(getApplicationContext(), TestJobService.class);
            long id  = callItemViewModel.insert(callEventItem);
            if(id!=-1) {
                JobInfo.Builder builder = new JobInfo.Builder((int)id, serviceComponent);
                PersistableBundle bundle = new PersistableBundle();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (mins15button.isChecked()) {
                    calendar.add(Calendar.MINUTE, 15);
                    bundle.putString("dateAndTime", date.toString());
                    callEventItem = new CallEventItem(callNumber, callName, format.format(calendar.getTime()));
                    builder.setMinimumLatency(15 * 1000 * 60); // wait at least
                    builder.setOverrideDeadline(15 * 1000 * 60); // maximum delay
                } else if (mins30button.isChecked()) {
                    calendar.add(Calendar.MINUTE, 30);
                    callEventItem = new CallEventItem(callNumber, callName, format.format(calendar.getTime()));
                    bundle.putString("dateAndTime", date.toString());
                    builder.setMinimumLatency(30 * 1000 * 60); // wait at least
                    builder.setOverrideDeadline(30 * 1000 * 60); // maximum delay
                } else if (customTimebutton.isChecked()) {
                    long differenceInMillisecond = 0L;
                    differenceInMillisecond = customCalendar.getTimeInMillis() - calendar.getTimeInMillis();
                    builder.setMinimumLatency(differenceInMillisecond); // wait at least
                    builder.setOverrideDeadline(differenceInMillisecond); // maximum delay
                    callEventItem = new CallEventItem(callNumber, callName, date_str);
                    bundle.putString("dateAndTime", date_str);

                }
                bundle.putString("callName", callName == null ? "Unknown" : callName);
                bundle.putString("callNumber", callNumber);
                bundle.putInt("id",(int)id);
                builder.setExtras(bundle);
                JobScheduler jobScheduler = getApplicationContext().getSystemService(JobScheduler.class);
                List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
                for (JobInfo jobInfo : allPendingJobs) {
                    System.out.println("**********Jobinfo*******");
                    System.out.println(jobInfo.getExtras().get("key"));
                }
                jobScheduler.schedule(builder.build());
            } else {
                Toast.makeText(this, "Not inserted!", Toast.LENGTH_SHORT).show();
            }
            finishAffinity();
        });

        cancel.setOnClickListener(view -> finishAffinity());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}


