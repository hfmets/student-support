package com.haydenfarnsworth.studentsupport.adapter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.activity.EditCourseActivity;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TermWithCoursesAdapter extends RecyclerView.Adapter<TermWithCoursesAdapter.CoursesViewHolder> {
    private List<Course> data;
    private Term term;
    Application application;
    Fragment context;
    public static final int EDIT_COURSE = 1;



    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        CardView courseCardView;
        public CoursesViewHolder(@NonNull View view) {
            super(view);
            courseCardView = view.findViewById(R.id.card_view_course);
        }
    }

    public TermWithCoursesAdapter(Term term, Application application, Fragment context) throws ExecutionException, InterruptedException {
        this.term = term;
        this.context = context;
        this.application = application;
        data = new AppRepository(application).getListCourses();
    }

    public void setCourses(List<Course> courses) {
        data = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TermWithCoursesAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_course_no_term, parent, false);
        CoursesViewHolder vh = new CoursesViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TermWithCoursesAdapter.CoursesViewHolder holder, int position) {
        Course course = data.get(position);
        DateFormat df = new SimpleDateFormat("MM/d/yyyy");
        TextView courseName = holder.courseCardView.findViewById(R.id.course_name);
        TextView courseDates = holder.courseCardView.findViewById(R.id.course_dates);
        TextView mentorName = holder.courseCardView.findViewById(R.id.mentor_name);
        courseName.setText(data.get(position).course_name);
        courseDates.setText(df.format(data.get(position).start_date) + " - " + df.format(data.get(position).end_date));
        mentorName.setText(data.get(position).course_mentor_name);

        holder.courseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();

                extras.putBoolean("edit", true);
                extras.putInt("course_id", course.course_id);
                extras.putString("course_name", course.course_name);
                extras.putString("start_date", df.format(course.start_date));
                extras.putString("end_date", df.format(course.end_date));
                extras.putString("mentor_name", course.course_mentor_name);
                extras.putString("mentor_phone", course.course_mentor_phone);
                extras.putString("mentor_email", course.course_mentor_email);
                extras.putInt("status", course.status.ordinal());
                Intent intent = new Intent(context.getContext(), EditCourseActivity.class).putExtras(extras);
                context.startActivityForResult(intent, EDIT_COURSE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}
