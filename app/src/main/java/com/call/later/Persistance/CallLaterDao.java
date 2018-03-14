package com.call.later.Persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import com.call.later.Model.CallEventItem;

import java.util.List;

@Dao
public interface CallLaterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CallEventItem callEventItem);

    @Query("DELETE FROM call_remainder")
    void deleteAll();

    @Query("SELECT * from call_remainder ORDER BY id ASC")
    LiveData<List<CallEventItem>> getAllEvents();

    @Query("DELETE from call_remainder WHERE id = :id")
    void deleteItemForGivenId(long id);

    @Query("Select * from call_remainder WHERE id = :id")
    CallEventItem getItemForGivenId(long id);

    @Update
    void update(CallEventItem callEventItem);

    @Delete
    void delete(CallEventItem callEventItem);

}
