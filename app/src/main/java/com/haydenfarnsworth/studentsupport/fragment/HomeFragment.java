package com.haydenfarnsworth.studentsupport.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.Term;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    DateFormat df = new SimpleDateFormat("MM/d/yyyy");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AppRepository appRepository = new AppRepository(this.getActivity().getApplication());
        TextView date = view.findViewById(R.id.date);
        TextView terms = view.findViewById(R.id.terms);
        TextView courses = view.findViewById(R.id.courses);
        TextView assessment = view.findViewById(R.id.assessments);
        Toolbar topToolbar = view.findViewById(R.id.top_toolbar);
        topToolbar.setTitle("Home");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        date.setText("Today's date is " + df.format(c.getTime()));

        appRepository.getAllTerms().observe(getViewLifecycleOwner(), new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> termsList) {
                terms.setText("You have " + termsList.size() + " terms.");
            }
        });

        appRepository.getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> coursesList) {
                courses.setText("You have " + coursesList.size() + " courses.");
            }
        });

        appRepository.getAllAssessments().observe(getViewLifecycleOwner(), new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessmentsList) {
                assessment.setText("You have " + assessmentsList.size() + " assessments.");
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (AddTermBtnPressed) context;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    AddTermBtnPressed callback;


    public interface AddTermBtnPressed {
        public void clicked();
    }
}
