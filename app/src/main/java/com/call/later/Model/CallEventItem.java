package com.call.later.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "call_remainder" , indices = {@Index(value = {"callNumber"}, unique = true)})
public class CallEventItem {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "callNumber")
    @NonNull
    private String callNumber;
    private String callName;
    private String calltime;

    public CallEventItem(@NonNull String callNumber, String callName, String calltime) {
        this.callNumber = callNumber;
        this.callName = callName;
        this.calltime = calltime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(@NonNull String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }
}
