package com.haydenfarnsworth.studentsupport.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.haydenfarnsworth.studentsupport.entity.Term;
import com.haydenfarnsworth.studentsupport.entity.TermAndCourse;

import java.util.List;

@Dao
public interface TermsDao {
    @Query("SELECT * FROM Term")
    public LiveData<List<Term>> getTerms();

    @Transaction
    @Query("SELECT * FROM Term")
    public LiveData<List<TermAndCourse>> getTermsWithCourses();

    @Query("SELECT term_name FROM Term WHERE term_id = :term_id")
    public String getTermNameFromId(int term_id);

    @Insert
    public long insertTerm(Term term);

    @Query("DELETE FROM Term")
    public void deleteAllTerms();

    @Query("DELETE FROM Term WHERE term_id = :term_id")
    public void deleteTerm(int term_id);
}
