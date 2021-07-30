package com.haydenfarnsworth.studentsupport.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Course;

import java.util.List;

public class TermWithCoursesViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> coursesInTerm;

    public TermWithCoursesViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allCourses = appRepository.getAllCourses();
    }

    public void setCoursesInTerm(int term_id) {
        coursesInTerm = appRepository.getCoursesFromTerm(term_id);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Course>> getCoursesInTerm() { return coursesInTerm; }

    public void insert(Course course) {
        appRepository.insert(course);
    }
}
