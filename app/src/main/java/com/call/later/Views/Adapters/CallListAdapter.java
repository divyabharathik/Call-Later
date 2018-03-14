package com.call.later.Views.Adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.call.later.Model.CallEventItem;
import com.call.later.Persistance.CallLaterDao;
import com.call.later.R;
import com.call.later.Views.Activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_EVENT = 1;
    static String updatedtime = "";
    private final LayoutInflater mInflater;
    private List<CallEventItem> callEventItemList;
    private Context context;
    public CallListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_EMPTY) {
            itemView = mInflater.inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(itemView);
        } else {
            itemView = mInflater.inflate(R.layout.call_event_item, parent, false);
            return new CallItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy '@' h:mm a", Locale.getDefault());
        if (callEventItemList != null && callEventItemList.size() != 0) {



            CallEventItem callEventItem = callEventItemList.get(position);
            CallItemViewHolder callholder = (CallItemViewHolder) holder;
            callholder.nameTV.setText(callEventItem.getCallName());
            callholder.numDay.setText(callEventItem.getCallNumber());
            callholder.timeTV.setText(callEventItem.getCalltime());
            callholder.reScheduleBtn.setOnClickListener(v->{
                String name=callEventItem.getCallName();
                String num=callEventItem.getCallNumber();
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(((CallItemViewHolder) holder).reScheduleBtn.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        updatedtime=selectedHour + ":" + selectedMinute;
                        cal.set(Calendar.MINUTE, selectedMinute);
                        cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                        CallEventItem updatedCallItemEvent = new CallEventItem(num,name,format.format(cal.getTime()));
                        ((MainActivity)context).deleteItem(callEventItemList.get(position));
                        ((MainActivity)context).insertItem(updatedCallItemEvent);
                        callholder.timeTV.setText(updatedCallItemEvent.getCalltime());
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            });
            callholder.deleteBtn.setOnClickListener(v->{

                ((MainActivity)context).deleteItem(callEventItemList.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        if (callEventItemList != null && callEventItemList.size() != 0)
            return callEventItemList.size();
        else return 1;
    }

    public void setCallEventItemList(List<CallEventItem> callEventItemList) {
        this.callEventItemList = callEventItemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (callEventItemList == null || callEventItemList.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_EVENT;
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class CallItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.timeTV)
        TextView timeTV;
        @BindView(R.id.numDay)
        TextView numDay;
        @BindView(R.id.reScheduleBtn)
        Button reScheduleBtn;
        @BindView(R.id.deleteBtn)
        Button deleteBtn;

        public CallItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
