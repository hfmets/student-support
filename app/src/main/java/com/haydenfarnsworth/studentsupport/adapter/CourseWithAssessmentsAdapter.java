package com.haydenfarnsworth.studentsupport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.entity.Assessment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CourseWithAssessmentsAdapter extends RecyclerView.Adapter<CourseWithAssessmentsAdapter.AssessmentViewHolder> {
    private List<Assessment> data;

    public static class AssessmentViewHolder extends RecyclerView.ViewHolder {
        CardView assessmentCardView;
        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentCardView = itemView.findViewById(R.id.assessment_card_view);
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        data = assessments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_assessment, parent, false);
        AssessmentViewHolder vh = new AssessmentViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        DateFormat df = new SimpleDateFormat("M/d/yyyy");
        ((TextView) holder.assessmentCardView.findViewById(R.id.assessment_name)).setText(data.get(position).assessment_name);
        ((TextView) holder.assessmentCardView.findViewById(R.id.assessment_date)).setText(df.format(data.get(position).assessment_date));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}
