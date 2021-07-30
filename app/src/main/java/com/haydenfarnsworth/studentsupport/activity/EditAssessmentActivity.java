package com.haydenfarnsworth.studentsupport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.adapter.AssessmentNotesAdapter;
import com.haydenfarnsworth.studentsupport.adapter.NotesAdapter;
import com.haydenfarnsworth.studentsupport.database.AppRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditAssessmentActivity extends AppCompatActivity {
    private FloatingActionButton newNoteButton;
    private FloatingActionButton submit;
    AppRepository appRepository = new AppRepository(getApplication());
    RadioGroup radioGroup;
    Bundle extras;
    EditText aDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        LiveData<List<String>> notes = new MutableLiveData(getIntent().getExtras().getStringArrayList("notes"));
        Calendar date = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date.set(i, i1, i2);
                updateDate(date.getTime());
            }
        };
        newNoteButton = findViewById(R.id.new_note_button);
        extras = getIntent().getExtras();
        submit = findViewById(R.id.submit);
        Toolbar topToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        topToolbar.setTitle("Edit Assessment");
        radioGroup = findViewById(R.id.radio_group);
        EditText aName = ((EditText)findViewById(R.id.assessment_name));
        aDate = ((EditText)findViewById(R.id.assessment_date));

        aName.setText(extras.getString("assessment_name"));
        aDate.setText(extras.getString("assessment_date"));

        if (extras.getString("type").equalsIgnoreCase("performance")) {
            radioGroup.check(R.id.performance);
        } else if (extras.getString("type").equalsIgnoreCase("objective")) {
            radioGroup.check(R.id.objective);
        }

        aDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditAssessmentActivity.this, dateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        int assessmentId = getIntent().getExtras().getInt("assessment_id");
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EditAssessmentActivity.this);
                dialog.setContentView(R.layout.new_note_dialog);

                EditText noteText = dialog.findViewById(R.id.new_note_text);
                dialog.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!noteText.getText().toString().isEmpty()) {
                            notes.getValue().add(noteText.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                extras.putString("assessment_name", aName.getText().toString());
                extras.putString("assessment_date", aDate.getText().toString());
                extras.putStringArrayList("notes", (ArrayList<String>) notes.getValue());
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final AssessmentNotesAdapter adapter = new AssessmentNotesAdapter(this, getIntent().getExtras().getInt("assessment_id"));
        recyclerView.setAdapter(adapter);

        notes.observe(((LifecycleOwner) EditAssessmentActivity.this), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapter.setNotes(strings);
            }
        });
    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.performance:
                radioGroup.check(R.id.performance);
                extras.putString("type", "Performance");
                break;
            case R.id.objective:
                radioGroup.check(R.id.objective);
                extras.putString("type", "Objective");
                break;
        };
    }

    public void updateDate(Date date) {
        DateFormat df = new SimpleDateFormat("M/d/yyyy");
        aDate.setText(df.format(date));
    }
}