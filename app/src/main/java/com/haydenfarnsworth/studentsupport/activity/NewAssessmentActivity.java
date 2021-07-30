package com.haydenfarnsworth.studentsupport.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.adapter.NotesAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewAssessmentActivity extends AppCompatActivity {
    private EditText assessmentDateInput;
    private EditText assessmentNameInput;
    private FloatingActionButton newNoteButton;
    private FloatingActionButton submit;
    private LiveData<List<String>> notes = new MutableLiveData(new ArrayList<String>());
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RadioGroup radioGroup;
    String type = "Performance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assessment);
        Calendar date = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date.set(i, i1, i2);
                updateDate(date.getTime());
            }
        };
        radioGroup = findViewById(R.id.radio_group);
        Toolbar topToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        topToolbar.setTitle("New Assessment");
        assessmentNameInput = findViewById(R.id.assessment_name_input);
        assessmentDateInput = findViewById(R.id.assessment_date_input);
        newNoteButton = findViewById(R.id.new_note_button);
        submit = findViewById(R.id.submit);
        assessmentDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewAssessmentActivity.this, dateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(NewAssessmentActivity.this);
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
                if (!assessmentDateInput.getText().toString().isEmpty() && !assessmentNameInput.getText().toString().isEmpty()) {
                    Intent intent = new Intent();
                    Bundle extras = getIntent().getExtras();

                    extras.putString("assessment_name", assessmentNameInput.getText().toString());
                    extras.putString("assessment_date", assessmentDateInput.getText().toString());
                    extras.putStringArrayList("notes", (ArrayList<String>) notes.getValue());
                    extras.putString("type", type);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        recyclerView = findViewById(R.id.notes_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final NotesAdapter adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);

        notes.observe((LifecycleOwner)NewAssessmentActivity.this, new Observer<List<String>>() {
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
                type = "Performance";
                break;
            case R.id.objective:
                radioGroup.check(R.id.objective);
                type = "Objective";
                break;
        };
    }

    public void updateDate(Date date) {
        DateFormat df = new SimpleDateFormat("M/d/yyyy");
        assessmentDateInput.setText(df.format(date));
    }
}