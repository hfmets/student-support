package com.haydenfarnsworth.studentsupport.adapter;

import android.app.Application;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.fragment.CourseWithAssessmentsFragment;
import com.haydenfarnsworth.studentsupport.util.NotificationUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Calendar.getInstance;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    List<Course> data;
    AppRepository appRepository;
    Fragment context;

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView courseCardView;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCardView = itemView.findViewById(R.id.card_view_course);
        }
    }

    public CourseAdapter(Application application, Fragment context) {
        appRepository = new AppRepository(application);
        this.context = context;
    }

    public void setCourses(List<Course> courses) {
        data = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_course, parent, false);
        CourseViewHolder vh = new CourseViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        Course course = data.get(position);
        TextView courseName = holder.courseCardView.findViewById(R.id.course_name);
        TextView termName = holder.courseCardView.findViewById(R.id.term_name);
        TextView mentorName = holder.courseCardView.findViewById(R.id.mentor_name);
        TextView status = holder.courseCardView.findViewById(R.id.status);
        Switch start = holder.courseCardView.findViewById(R.id.start_alert);
        Switch end = holder.courseCardView.findViewById(R.id.end_alert);
        courseName.setText(data.get(position).course_name);
        mentorName.setText(data.get(position).course_mentor_name);
        status.setText(course.status.name());
        boolean startAlert = course.start_alert;
        boolean endAlert = course.end_alert;
        start.setChecked(course.start_alert);
        end.setChecked(course.end_alert);
//        statusSwitch.setChecked(data.get(position).completed);
        try {
            termName.setText(appRepository.getTermNameFromId(data.get(position).term_id));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appRepository.updateStatus(b, data.get(position).course_id);
            }
        });*/
        holder.courseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new CourseWithAssessmentsFragment(data.get(position).course_id)).addToBackStack(null).commit();
            }
        });

        holder.courseCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.confirm_delete_dialog);
                dialog.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appRepository.deleteCourse(data.get(position).course_id);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });

        start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                NotificationUtils notificationUtils = new NotificationUtils(context, NotificationUtils.getNotification(data.get(position).course_name + " starts today.", context.getActivity()));

                if (b && !startAlert) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(data.get(position).start_date);
                    c.add(Calendar.HOUR, 8);
                    notificationUtils.scheduleNotification(c.getTimeInMillis());
                    appRepository.updateAlertStatus(b, course.end_alert, course.course_id);
                } else if (startAlert) {
                    notificationUtils.cancelNotification();
                    appRepository.updateAlertStatus(false, course.end_alert, course.course_id);
                }
            }
        });
        end.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                NotificationUtils notificationUtils = new NotificationUtils(context, NotificationUtils.getNotification(data.get(position).course_name + " ends today.", context.getActivity()));
                appRepository.updateAlertStatus(course.start_alert, b, course.course_id);
                if (b && !endAlert) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(data.get(position).end_date);
                    c.add(Calendar.HOUR, 8);
                    notificationUtils.scheduleNotification(c.getTimeInMillis());
                    appRepository.updateAlertStatus(course.start_alert, b, course.course_id);
                } else if (endAlert) {
                    notificationUtils.cancelNotification();
                    appRepository.updateAlertStatus(course.start_alert, false, course.course_id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


}
