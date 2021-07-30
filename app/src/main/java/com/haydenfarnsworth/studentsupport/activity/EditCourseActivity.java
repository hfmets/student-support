package com.haydenfarnsworth.studentsupport.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.entity.Course;

public class EditCourseActivity extends AppCompatActivity {
    RadioGroup radioGroup = null;
    int statusId = Course.Status.INPROGRESS.ordinal();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Bundle intentExtras = getIntent().getExtras();
        int inProgress = 0;
        int completed = Course.Status.COMPLETED.ordinal();
        int dropped = Course.Status.DROPPED.ordinal();
        int planned = Course.Status.PLANNED.ordinal();

        CalendarView calendarView = findViewById(R.id.calendarView);
        final EditText dateStart = findViewById(R.id.course_start_date);
        final EditText dateEnd = findViewById(R.id.course_end_date);
        final EditText courseName = findViewById(R.id.course_name_input);
        final EditText mentorName = findViewById(R.id.mentor_name_input);
        final EditText mentorPhone = findViewById(R.id.course_mentor_phone_input);
        final EditText mentorEmail = findViewById(R.id.course_mentor_email_input);
        radioGroup = findViewById(R.id.radioGroup);

        dateStart.setShowSoftInputOnFocus(false);
        dateEnd.setShowSoftInputOnFocus(false);

        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> {
            String selectedDate = (i1 + 1) +"/"+ i2 +"/"+ i;
            if (dateStart.isInputMethodTarget()) {
                dateStart.setText(selectedDate);
            } else if (dateEnd.isInputMethodTarget()) {
                dateEnd.setText(selectedDate);
            }
        });

        if (getIntent().getExtras().getBoolean("edit")) {
            dateStart.setText(intentExtras.getString("start_date"));
            dateEnd.setText(intentExtras.getString("end_date"));
            courseName.setText(intentExtras.getString("course_name"));
            mentorName.setText(intentExtras.getString("mentor_name"));
            mentorPhone.setText(intentExtras.getString("mentor_phone"));
            mentorEmail.setText(intentExtras.getString("mentor_email"));
            switch (Course.Status.values()[intentExtras.getInt("status")]) {
                case INPROGRESS:
                    radioGroup.check(R.id.status_in_progress);
                    break;
                case COMPLETED:
                    radioGroup.check(R.id.status_completed);
                    break;
                case DROPPED:
                    radioGroup.check(R.id.status_dropped);
                    break;
                case PLANNED:
                    radioGroup.check(R.id.status_planned);
                    break;
            };
        }

        findViewById(R.id.submit).setOnClickListener((view -> {
            Intent intent = new Intent();


            intentExtras.putString("course_name", courseName.getText().toString());
            intentExtras.putString("start_date", dateStart.getText().toString());
            intentExtras.putString("end_date", dateEnd.getText().toString());
            intentExtras.putString("mentor_name", mentorName.getText().toString());
            intentExtras.putString("mentor_phone", mentorPhone.getText().toString());
            intentExtras.putString("mentor_email", mentorEmail.getText().toString());
            intentExtras.putInt("status", statusId);

            intent.putExtras(intentExtras);
            setResult(RESULT_OK, intent);
            finish();
        }));
    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.status_in_progress:
                if (checked)
                    radioGroup.check(R.id.status_in_progress);
                statusId = Course.Status.INPROGRESS.ordinal();
                break;
            case R.id.status_completed:
                if (checked)
                    radioGroup.check(R.id.status_completed);
                statusId = Course.Status.COMPLETED.ordinal();
                break;
            case R.id.status_dropped:
                if (checked)
                    radioGroup.check(R.id.status_dropped);
                statusId = Course.Status.DROPPED.ordinal();
                break;
            case R.id.status_planned:
                if (checked)
                    radioGroup.check(R.id.status_planned);
                statusId = Course.Status.PLANNED.ordinal();
                break;
        }
    }


}