package com.haydenfarnsworth.studentsupport.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CourseAndAssessment {
    @Embedded public Course course;
    @Relation(entity = Assessment.class, parentColumn = "course_id", entityColumn = "course_id")
    public List<Assessment> assessments;
}
