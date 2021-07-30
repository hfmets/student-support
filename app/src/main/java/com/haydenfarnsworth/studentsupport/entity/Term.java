package com.haydenfarnsworth.studentsupport.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.Date;
import java.util.List;

@Entity
public class Term {
    @PrimaryKey(autoGenerate = true)
    public int term_id;
    public String term_name;
    public Date term_start;
    public Date term_end;

    public Term(String term_name, Date term_start, Date term_end) {
        this.term_name = term_name;
        this.term_start = term_start;
        this.term_end = term_end;
    }
}