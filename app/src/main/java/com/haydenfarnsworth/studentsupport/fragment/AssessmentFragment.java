package com.haydenfarnsworth.studentsupport.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.haydenfarnsworth.studentsupport.ViewModel.AssessmentsViewModel;
import com.haydenfarnsworth.studentsupport.adapter.AssessmentsAdapter;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AssessmentFragment extends Fragment {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
    AppRepository appRepository;



    public AssessmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AssessmentsViewModel assessmentsViewModel = new ViewModelProvider(requireActivity()).get(AssessmentsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_assessment, container, false);
        Toolbar topToolbar = (Toolbar) root.findViewById(R.id.top_toolbar);
        topToolbar.setTitle("Assessments");
        appRepository = new AppRepository(getActivity().getApplication());
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.assessments_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        AssessmentsAdapter adapter = new AssessmentsAdapter(this);
        recyclerView.setAdapter(adapter);

        assessmentsViewModel.getAllAssessments().observe(getViewLifecycleOwner(), new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                adapter.setAssessments(assessments);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AssessmentsAdapter.EDIT_ASSESSMENT) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();


                try {
                    String assessmentName = extras.getString("assessment_name");
                    int assessment_id = extras.getInt("assessment_id");
                    Date assessmentDate = sdf.parse(extras.get("assessment_date").toString());
                    List<String> notes = extras.getStringArrayList("notes");
                    int courseId = extras.getInt("course_id");
                    Assessment assessment = new Assessment(assessmentName, assessmentDate, courseId, notes);
                    assessment.setAssessment_id(assessment_id);
                    assessment.setType(extras.getString("type"));
                    appRepository.updateAssessment(assessment);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}