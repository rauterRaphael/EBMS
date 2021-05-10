package com.f.ebms.ui.system;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SystemViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SystemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is se dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}