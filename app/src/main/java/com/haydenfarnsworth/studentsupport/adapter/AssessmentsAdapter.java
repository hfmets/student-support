package com.haydenfarnsworth.studentsupport.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.activity.EditAssessmentActivity;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Assessment;
import com.haydenfarnsworth.studentsupport.fragment.AssessmentFragment;
import com.haydenfarnsworth.studentsupport.util.NotificationPublisher;
import com.haydenfarnsworth.studentsupport.util.NotificationUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.AssessmentViewHolder> {
    List<Assessment> data;
    Fragment context;
    public static String ASSESSMENT_INFO = "com.app.action.ASSESSMENT_INFO";
    public static int EDIT_ASSESSMENT = 0;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    int importance;
    NotificationChannel notificationChannel;
    AlarmManager am;
    PendingIntent pendingIntent;
    Intent notificationIntent;

    public static class AssessmentViewHolder extends RecyclerView.ViewHolder {
        CardView assessmentCardView;
        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentCardView = itemView.findViewById(R.id.assessment_card_view);
        }
    }

    public AssessmentsAdapter(Fragment context) {
        this.context = context;
    }

    public void setAssessments(List<Assessment> assessments) {
        data = assessments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_assessment_with_toggle, parent, false);
        AssessmentViewHolder vh = new AssessmentViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        AppRepository appRepository = new AppRepository(context.getActivity().getApplication());
        Assessment assessment = data.get(position);
        DateFormat df = new SimpleDateFormat("M/d/yyyy");
        ((TextView) holder.assessmentCardView.findViewById(R.id.assessment_name)).setText(data.get(position).assessment_name);
        ((TextView) holder.assessmentCardView.findViewById(R.id.assessment_date)).setText(df.format(data.get(position).assessment_date));
        Switch alertSwitch = holder.assessmentCardView.findViewById(R.id.alerts_switch);
        alertSwitch.setChecked(assessment.alert);
        try {
            ((TextView) holder.assessmentCardView.findViewById(R.id.course)).setText(appRepository.getCourseName(data.get(position).course_id));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                NotificationUtils notificationUtils = new NotificationUtils(context, NotificationUtils.getNotification(data.get(position).assessment_name + " is scheduled for today.", context.getActivity()));
                if (b && !assessment.alert) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(data.get(position).assessment_date);
                    c.add(Calendar.HOUR, 8);
                    notificationUtils.scheduleNotification(c.getTimeInMillis());
                    appRepository.updateAssessmentAlert(b, assessment.assessment_id);
                } else if (assessment.alert) {
                    notificationUtils.cancelNotification();
                    appRepository.updateAssessmentAlert(false, assessment.assessment_id);
                }
            }
        });

        holder.assessmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putInt("assessment_id", data.get(position).assessment_id);
                extras.putString("assessment_name", data.get(position).assessment_name);
                extras.putString("assessment_date", df.format(data.get(position).assessment_date));
                extras.putStringArrayList("notes", (ArrayList<String>) data.get(position).notes);
                extras.putString("type", data.get(position).type);
                extras.putInt("course_id", data.get(position).course_id);
                Intent intent = new Intent(context.getContext(), EditAssessmentActivity.class).putExtras(extras);
                context.startActivityForResult(intent, EDIT_ASSESSMENT);
            }
        });

        holder.assessmentCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.confirm_delete_dialog);
                dialog.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appRepository.deleteAssessment(data.get(position));
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    
}
