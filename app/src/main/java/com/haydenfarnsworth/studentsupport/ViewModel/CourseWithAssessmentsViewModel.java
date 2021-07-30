package com.haydenfarnsworth.studentsupport.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;

import java.util.List;

public class CourseWithAssessmentsViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Assessment>> allAssessments;
    private LiveData<List<Assessment>> assessmentsFromCourse;

    public CourseWithAssessmentsViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allAssessments = appRepository.getAllAssessments();
    }

    public void setAssessmentsInCourse(int course_id) {
        assessmentsFromCourse = appRepository.getAssessmentsFromCourse(course_id);
    }

    public LiveData<List<Assessment>> getAssessmentsFromCourse() { return assessmentsFromCourse; }
}
