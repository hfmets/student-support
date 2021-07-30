package com.haydenfarnsworth.studentsupport.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.TermAndCourse;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<TermAndCourse>> allTermsAndCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allCourses = appRepository.getAllCourses();
        allTermsAndCourses = appRepository.getTermsWithCourses();
    }

    public LiveData<List<TermAndCourse>> getAllTermsAndCourses() {
        return allTermsAndCourses;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
}
