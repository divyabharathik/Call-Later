package com.call.later.Views.Activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Index;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.call.later.Model.CallEventItem;
import com.call.later.R;
import com.call.later.Views.Adapters.CallListAdapter;
import com.call.later.Views.CallItemViewModel;

import java.util.List;

import static android.Manifest.permission.PROCESS_OUTGOING_CALLS;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {

    private static final int PHONE_STATUS_PERSMISSION_REQUEST = 3;
    private CallListAdapter adapter;
    private CallItemViewModel callItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeWithPermission();
        adapter = new CallListAdapter(this);
        callItemViewModel = ViewModelProviders.of(this).get(CallItemViewModel.class);
        callItemViewModel.getAllEventsLiveData().observe(this, callEventItems -> {
            adapter.setCallEventItemList(callEventItems);
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        callItemViewModel = ViewModelProviders.of(this).get(CallItemViewModel.class);
        callItemViewModel.getAllEventsLiveData().observe(this, new Observer<List<CallEventItem>>() {
            @Override
            public void onChanged(@Nullable final List<CallEventItem> callEventItems) {
                // Update the cached copy of the words in the adapter.
                adapter.setCallEventItemList(callEventItems);
            }
        });
    }


    void initializeWithPermission() {
        String[] permissions = new String[]{PROCESS_OUTGOING_CALLS, READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE};
        if (ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) +
                ContextCompat.checkSelfPermission(this, READ_CALL_LOG) +
                ContextCompat.checkSelfPermission(this, PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CALL_LOG) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PROCESS_OUTGOING_CALLS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Requesting Permission")
                        .setMessage("We need your permission to access your phone call status")
                        .setPositiveButton("Ok, got it!", (dialogInterface, i) -> ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{READ_CALL_LOG},
                                PHONE_STATUS_PERSMISSION_REQUEST))
                        .setNegativeButton("I'm Unpredictable", (di, i) -> {
                            di.dismiss();
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                READ_CALL_LOG,
                                READ_PHONE_STATE,
                                PROCESS_OUTGOING_CALLS},
                        PHONE_STATUS_PERSMISSION_REQUEST);
            }
        } else {
            sayMessage("Thank you for granting permission to access your phone call status", Snackbar.LENGTH_LONG, false);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void deleteItem(CallEventItem callEventItem) {
        callItemViewModel.delete(callEventItem);
    }
    public void insertItem(CallEventItem callEventItem) {
        callItemViewModel.insert(callEventItem);
    }
    public void deleteSpecificItem(int id){
       callItemViewModel.deleteSpecificItem(id);
    }
    public CallEventItem getSpecificItem(int id){
        return callItemViewModel.getSpecificItem(id);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
//        if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                && grantResults[1] == PackageManager.PERMISSION_GRANTED
//                && grantResults[2] == PackageManager.PERMISSION_GRANTED
//                ) {
        switch (requestCode) {
            case PHONE_STATUS_PERSMISSION_REQUEST:
                if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        ) {
                    //say thanks for granting permission
                    sayMessage("Thank you for granting permission to access your phone call status", Snackbar.LENGTH_LONG, false);
                } else {
                    sayMessage("Permission Denied for Location Access!", Snackbar.LENGTH_INDEFINITE, true);
                }
        }
    }

    void sayMessage(String message, int lengthOfSnackbar, boolean tryagain) {
        Snackbar.make(findViewById(R.id.recycler_view), message, lengthOfSnackbar).setAction(lengthOfSnackbar == -2 ? "Retry?" : "Ok", view -> {
            if (lengthOfSnackbar == Snackbar.LENGTH_INDEFINITE) {
                if (tryagain) initializeWithPermission();
            }
        }).show();
    }
}
