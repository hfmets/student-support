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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.ViewModel.CourseWithAssessmentsViewModel;
import com.haydenfarnsworth.studentsupport.activity.NewAssessmentActivity;
import com.haydenfarnsworth.studentsupport.adapter.CourseWithAssessmentsAdapter;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CourseWithAssessmentsFragment extends Fragment {
    static final int CREATE_NEW_ASSESSMENT = 0;
    static final String ASSESSMENT_INFO = "com.app.action.ASSESSMENT_INFO";
    private int course_id;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
    private Bundle newAssessmentInfo = new Bundle();
    AppRepository appRepository;


    public CourseWithAssessmentsFragment(int course_id) {
        this.course_id = course_id;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_course_with_assessments, container, false);
        appRepository = new AppRepository(getActivity().getApplication());
        Toolbar topToolbar = (Toolbar) root.findViewById(R.id.top_toolbar);
        topToolbar.setTitle("Assessments");

        FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAssessmentInfo.putString("assessment_name", null);
                newAssessmentInfo.putString("assessment_date", null);
                newAssessmentInfo.putStringArrayList("notes", null);
                newAssessmentInfo.putString("type", null);
                Intent intent = new Intent(root.getContext(), NewAssessmentActivity.class).putExtra(ASSESSMENT_INFO, newAssessmentInfo);
                startActivityForResult(intent, CREATE_NEW_ASSESSMENT);
            }
        });

        CourseWithAssessmentsViewModel assessmentViewModel = new ViewModelProvider(requireActivity()).get(CourseWithAssessmentsViewModel.class);
        assessmentViewModel.setAssessmentsInCourse(course_id);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.assessments_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        CourseWithAssessmentsAdapter adapter = new CourseWithAssessmentsAdapter();
        recyclerView.setAdapter(adapter);

        assessmentViewModel.getAssessmentsFromCourse().observe(getViewLifecycleOwner(), new Observer<List<Assessment>>() {
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

        if (requestCode == CREATE_NEW_ASSESSMENT) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Bundle extras = data.getExtras();

                try {
                    String assessmentName = extras.get("assessment_name").toString();
                    Date assessmentDate = sdf.parse(extras.get("assessment_date").toString());
                    List<String> notes = extras.getStringArrayList("notes");
                    Assessment newAssessment = new Assessment(assessmentName, assessmentDate, course_id, notes);
                    newAssessment.setType(extras.getString("type"));
                    appRepository.insert(newAssessment);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}