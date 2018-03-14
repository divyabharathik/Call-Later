package com.call.later.Views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.call.later.Model.CallEventItem;
import com.call.later.Persistance.CallLaterRepository;

import java.util.List;

public class CallItemViewModel extends AndroidViewModel {
    private CallLaterRepository callLaterRepository;

    private LiveData<List<CallEventItem>> allEventsLiveData;

    public CallItemViewModel (Application application) {
        super(application);
        callLaterRepository = new CallLaterRepository(application);
        allEventsLiveData = callLaterRepository.getAllEventsLiveData();
    }

    public LiveData<List<CallEventItem>> getAllEventsLiveData() { return allEventsLiveData; }

    public long insert(@NonNull CallEventItem callEventItem) {
        return callLaterRepository.insert(callEventItem);

    }
    public void delete(CallEventItem callEventItem) { callLaterRepository.delete(callEventItem); }
    public void deleteSpecificItem(long id) { callLaterRepository.deleteSpecificItem(id); }
    public void updateSpecificItem(long id) { callLaterRepository.updateSpecificItem(id); }
    public CallEventItem getSpecificItem(long id){ return callLaterRepository.getSpecificItem(id);}

}
