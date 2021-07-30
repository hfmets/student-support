package com.haydenfarnsworth.studentsupport.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.ViewModel.CourseViewModel;
import com.haydenfarnsworth.studentsupport.adapter.CourseAdapter;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.TermAndCourse;

import java.util.List;

public class CoursesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CourseViewModel courseViewModel;

    public CoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_courses, container, false);

        Toolbar topToolbar = (Toolbar) root.findViewById(R.id.top_toolbar);
        topToolbar.setTitle("Courses");

        courseViewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);

        recyclerView = (RecyclerView) root.findViewById(R.id.courses_recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        CourseAdapter adapter = new CourseAdapter(getActivity().getApplication(), this);
        recyclerView.setAdapter(adapter);

        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter.setCourses(courses);
            }
        });

        return root;
    }
}