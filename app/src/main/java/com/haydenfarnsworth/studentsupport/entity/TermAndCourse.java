package com.haydenfarnsworth.studentsupport.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TermAndCourse {
    @Embedded public Term term;
    @Relation(entity = Course.class, parentColumn = "term_id", entityColumn = "term_id")
    public List<Course> courses;
}
