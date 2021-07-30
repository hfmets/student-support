package com.haydenfarnsworth.studentsupport.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id", onDelete = CASCADE))
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    public int assessment_id;
    public String assessment_name;
    public Date assessment_date;
    public int course_id;
    public List<String> notes;
    public boolean alert;
    public String type;

    public Assessment(String assessment_name, Date assessment_date, int course_id, List<String> notes) {
        this.assessment_name = assessment_name;
        this.assessment_date = assessment_date;
        this.course_id = course_id;
        this.notes = notes;
    }

    public void setAssessment_id(int id){
        assessment_id = id;
    }
    public void setType(String type) { this.type = type; }
}
