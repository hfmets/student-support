package com.haydenfarnsworth.studentsupport.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.haydenfarnsworth.studentsupport.dao.AssessmentsDao;
import com.haydenfarnsworth.studentsupport.dao.CoursesDao;
import com.haydenfarnsworth.studentsupport.dao.TermsDao;
import com.haydenfarnsworth.studentsupport.entity.Assessment;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.Term;
import com.haydenfarnsworth.studentsupport.entity.TermAndCourse;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AppRepository {
    private TermsDao termsDao;
    private CoursesDao coursesDao;
    private AssessmentsDao assessmentsDao;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Assessment>> allAssessments;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        termsDao = db.getTermsDao();
        coursesDao = db.getCoursesDao();
        assessmentsDao = db.getAssessmentsDao();
        allTerms = termsDao.getTerms();
        allCourses = coursesDao.getCourses();
        allAssessments = assessmentsDao.getAssessments();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }
    public LiveData<List<Course>> getAllCourses() { return allCourses; }
    public LiveData<List<Assessment>> getAllAssessments() { return allAssessments; }
    public LiveData<List<Course>> getCoursesFromTerm(int term_id) { return coursesDao.getCoursesInTerm(term_id); }
    public LiveData<List<TermAndCourse>> getTermsWithCourses() { return termsDao.getTermsWithCourses(); }
    public LiveData<List<Assessment>> getAssessmentsFromCourse(int course_id) { return assessmentsDao.getAssessmentsFromCourse(course_id); }
    public LiveData<List<String>> getNotesFromAssessment(int assessment_id) { return assessmentsDao.getAllNotesFromAssessment(assessment_id); }

    public void updateAssessment(Assessment assessment) {
        AppDatabase.database_executor.execute(() -> {
            assessmentsDao.updateAssessment(assessment);
        });
    }

    public void updateCourse(Course course) {
        AppDatabase.database_executor.execute(() -> {
            coursesDao.updateCourse(course);
        });
    }

    public void updateAlertStatus(boolean start, boolean end, int courseId) {
        AppDatabase.database_executor.execute(() -> {
            coursesDao.updateAlertStatus(start, end, courseId);
        });
    }

    public void updateAssessmentAlert(boolean alert, int assessment_id) {
        AppDatabase.database_executor.execute(() -> {
            assessmentsDao.updateAssessmentAlert(alert, assessment_id);
        });
    }

    public void deleteAssessment(Assessment assessment) {
        AppDatabase.database_executor.execute(() -> {
            assessmentsDao.deleteAssessment(assessment);
        });
    }

    public void deleteTerm(int termId) {
        AppDatabase.database_executor.execute(() -> {
            termsDao.deleteTerm(termId);
        });
    }

    public void deleteCourse(int courseId) {
        AppDatabase.database_executor.execute(() -> {
            coursesDao.deleteCourse(courseId);
        });
    }


    public String getTermNameFromId(int term_id) throws ExecutionException, InterruptedException {
        Future<String> result = AppDatabase.database_executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return termsDao.getTermNameFromId(term_id);
            }
        });
        return result.get();
    }

    public int getNumberOfCoursesInTerm(int term_id) throws ExecutionException, InterruptedException {
        Future<Integer> result = AppDatabase.database_executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return coursesDao.getNumberOfCoursesFromTerm(term_id);
            }
        });
        return result.get();
    }

    public  String getCourseName(int course_id) throws ExecutionException, InterruptedException {
        Future<String> result = AppDatabase.database_executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return coursesDao.getCourseName(course_id);
            }
        });
        return result.get();
    }

    public List<String> getStringNotesFromAssessment(int assessment_id) throws ExecutionException, InterruptedException {
        Future<List<String>> result = AppDatabase.database_executor.submit(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return assessmentsDao.getStringNotesFromAssessment(assessment_id);
            }
        });
        return result.get();
    }

    public void updateNotes(List<String> notes, int assessment_id) {
        AppDatabase.database_executor.execute(() -> {
            assessmentsDao.updateNotes(notes, assessment_id);
        });
    }

    /*public void updateStatus(boolean value, int course_id) {
        AppDatabase.database_executor.execute(() -> {
            coursesDao.updateStatus(value, course_id);
        });
    }*/

    public List<Course> getListCourses() throws ExecutionException, InterruptedException {
        Future<List<Course>> result = AppDatabase.database_executor.submit(new Callable<List<Course>>() {
            @Override
            public List<Course> call() throws Exception {
                return coursesDao.getListCourses();
            }
        });
        return result.get();
    }

    public void insert(Term term) {
        AppDatabase.database_executor.execute(() -> {
            termsDao.insertTerm(term);
        });
    }

    public void insert(Course course) {
        AppDatabase.database_executor.execute(() -> {
            coursesDao.insertCourses(course);
        });
    }

    public void insert(Assessment assessment) {
        AppDatabase.database_executor.execute(() -> {
            assessmentsDao.insertAssessments(assessment);
        });
    }
}
