package com.haydenfarnsworth.studentsupport.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;

import java.util.List;

public class AssessmentsViewModel extends AndroidViewModel {

    AppRepository appRepository;
    LiveData<List<Assessment>> allAssessments;

    public AssessmentsViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allAssessments = appRepository.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }
}
