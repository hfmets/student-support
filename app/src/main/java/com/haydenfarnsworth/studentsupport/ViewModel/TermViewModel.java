package com.haydenfarnsworth.studentsupport.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allTerms = appRepository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public void insert(Term term) {
        appRepository.insert(term);
    }
}
