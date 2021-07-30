package com.haydenfarnsworth.studentsupport.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.haydenfarnsworth.studentsupport.entity.Assessment;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.util.List;

@Dao
public interface AssessmentsDao {
    @Query("SELECT * FROM Assessment")
    public LiveData<List<Assessment>> getAssessments();

    @Query("SELECT * FROM Assessment WHERE course_id = :course_id")
    public LiveData<List<Assessment>> getAssessmentsFromCourse(int course_id);

    @Insert
    public void insertAssessments(Assessment... assessments);

    @Delete
    public void deleteAssessment(Assessment assessment);

    @Query("UPDATE Assessment SET alert = :alert WHERE assessment_id = :assessment_id")
    public void updateAssessmentAlert(boolean alert, int assessment_id);

    @Query("SELECT notes FROM Assessment WHERE assessment_id = :assessment_id")
    public LiveData<List<String>> getAllNotesFromAssessment(int assessment_id);

    @Query("SELECT notes FROM Assessment WHERE assessment_id = :assessment_id")
    public List<String> getStringNotesFromAssessment(int assessment_id);

    @Query("UPDATE Assessment SET notes = :noteList WHERE assessment_id = :assessment_id")
    public void updateNotes(List<String> noteList, int assessment_id);

    @Update
    public void updateAssessment(Assessment assessment);
}
