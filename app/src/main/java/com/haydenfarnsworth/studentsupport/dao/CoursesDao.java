package com.haydenfarnsworth.studentsupport.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.CourseAndAssessment;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.util.List;

@Dao
public interface CoursesDao {
    @Query("SELECT * FROM Course")
    public LiveData<List<Course>> getCourses();

    @Query("SELECT * FROM Course")
    public List<Course> getListCourses();

    @Query("SELECT * FROM Course WHERE term_id = :term_id")
    public LiveData<List<Course>> getCoursesInTerm(int term_id);

    @Transaction
    @Query("SELECT * FROM Course")
    public List<CourseAndAssessment> getCoursesWithAssessments();

    @Query("SELECT course_name FROM Course WHERE course_id = :course_id")
    public String getCourseName(int course_id);

    /*@Query("UPDATE Course SET completed = :value WHERE course_id = :course_id")
    public void updateStatus(boolean value, int course_id);*/

    @Insert
    public void insertCourses(Course... courses);

    @Update
    public void updateCourse(Course course);

    @Query("UPDATE Course SET start_alert = :start_alert, end_alert = :end_alert WHERE course_id = :course_id")
    public void updateAlertStatus(boolean start_alert, boolean end_alert, int course_id);

    @Query("DELETE FROM Course")
    public void deleteAllCourses();

    @Query("DELETE FROM Course WHERE course_id = :course_id")
    public void deleteCourse(int course_id);

    @Query("SELECT COUNT(*) FROM Course WHERE term_id = :term_id")
    public int getNumberOfCoursesFromTerm(int term_id);
}
