package com.haydenfarnsworth.studentsupport.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Term.class, parentColumns = "term_id", childColumns = "term_id", onDelete = CASCADE))
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int course_id;
    public String course_name;
    public int term_id;
    public Date start_date;
    public Date end_date;
    public Status status;
    public String course_mentor_name;
    public String course_mentor_phone;
    public String course_mentor_email;
    public boolean start_alert;
    public boolean end_alert;


    public Course(String course_name, int term_id, Date start_date, Date end_date, Status status, String course_mentor_name, String course_mentor_phone, String course_mentor_email) {
        this.course_name = course_name;
        this.term_id = term_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.course_mentor_name = course_mentor_name;
        this.course_mentor_phone = course_mentor_phone;
        this.course_mentor_email = course_mentor_email;
    }

    public enum Status {
        INPROGRESS,
        COMPLETED,
        DROPPED,
        PLANNED
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
