package com.haydenfarnsworth.studentsupport.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.ViewModel.TermWithCoursesViewModel;
import com.haydenfarnsworth.studentsupport.activity.EditCourseActivity;
import com.haydenfarnsworth.studentsupport.activity.NewCourseActivity;
import com.haydenfarnsworth.studentsupport.adapter.TermWithCoursesAdapter;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class TermWithCoursesFragment extends Fragment {


    private Term term;
    static final int CREATE_NEW_COURSE = 0;
    Bundle newCourseInfo = new Bundle();
    static final String COURSE_INFO = "com.app.action.COURSE_INFO";
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
    AppRepository appRepository;
    public TermWithCoursesAdapter adapter;


    public TermWithCoursesFragment(Term term) {
        this.term = term;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_term_with_courses, container, false);
        appRepository = new AppRepository(getActivity().getApplication());
        Toolbar topToolbar = (Toolbar) root.findViewById(R.id.top_toolbar);
        topToolbar.setTitle(term.term_name);

        TermWithCoursesViewModel termWithCoursesViewModel = new ViewModelProvider(requireActivity()).get(TermWithCoursesViewModel.class);
        termWithCoursesViewModel.setCoursesInTerm(term.term_id);

        RecyclerView recyclerView = root.findViewById(R.id.courses_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        try {
            adapter = new TermWithCoursesAdapter(term, getActivity().getApplication(), this);
            recyclerView.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        termWithCoursesViewModel.getCoursesInTerm().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter.setCourses(courses);
            }
        });

        root.findViewById(R.id.floatingActionButton).setOnClickListener((view -> {
            newCourseInfo.putString("course_name", null);
            newCourseInfo.putString("start_date", null);
            newCourseInfo.putString("end_date", null);
            newCourseInfo.putString("mentor_name", null);
            newCourseInfo.putString("mentor_phone", null);
            newCourseInfo.putString("mentor_email", null);
            newCourseInfo.putInt("status", 0);
            newCourseInfo.putBoolean("edit", false);
            Intent intent = new Intent(this.getContext(), EditCourseActivity.class).putExtra(COURSE_INFO, newCourseInfo);
            startActivityForResult(intent, CREATE_NEW_COURSE);
        }));
        return root;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_NEW_COURSE || requestCode == TermWithCoursesAdapter.EDIT_COURSE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                try {
                    String courseName = extras.getString("course_name");
                    Date startDate = sdf.parse(extras.getString("start_date"));
                    Date endDate = sdf.parse(extras.getString("end_date"));
                    String mentorName = extras.getString("mentor_name");
                    String mentorPhone = extras.getString("mentor_phone");
                    String mentorEmail = extras.getString("mentor_email");
                    int status = extras.getInt("status");
                    Log.d(TAG, "onActivityResult: " + status);
                    Course newCourse = new Course(courseName, term.term_id, startDate, endDate, Course.Status.values()[status], mentorName, mentorPhone, mentorEmail);
                    if (requestCode == TermWithCoursesAdapter.EDIT_COURSE) {
                        newCourse.setCourse_id(extras.getInt("course_id"));
                        appRepository.updateCourse(newCourse);
                    } else {
                        appRepository.insert(newCourse);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}