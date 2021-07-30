package com.haydenfarnsworth.studentsupport.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.dao.TermsDao;
import com.haydenfarnsworth.studentsupport.database.AppDatabase;
import com.haydenfarnsworth.studentsupport.database.AppRepository;
import com.haydenfarnsworth.studentsupport.entity.Term;
import com.haydenfarnsworth.studentsupport.util.Validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;


public class NewTermFragment extends Fragment {
    public long startDate = 0;
    public long endDate = 0;

    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppRepository appRepository = new AppRepository(getActivity().getApplication());
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_new_term, container, false);
        CalendarView calendarView = root.findViewById(R.id.term_date_picker);
        final EditText termName = root.findViewById(R.id.term_name_input);
        final EditText dateStart = root.findViewById(R.id.start_date);
        final EditText dateEnd = root.findViewById(R.id.end_date);
        final Button submit = root.findViewById(R.id.submit);
        /*dateStart.setShowSoftInputOnFocus(false);
        dateEnd.setShowSoftInputOnFocus(false);*/
        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> {
            String selectedDate = (i1 + 1) +"/"+ i2 +"/"+ i;
            if (dateStart.isInputMethodTarget()) {
                dateStart.setText(selectedDate);
            } else if (dateEnd.isInputMethodTarget()) {
                dateEnd.setText(selectedDate);
            }
        });


        submit.setOnClickListener(view -> {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/d/yyyy");
            Term newTerm;
            if (!termName.getText().toString().equalsIgnoreCase(getString(R.string.term_name_hint)) &&
                !dateStart.getText().toString().equalsIgnoreCase(getString(R.string.start_date_hint)) &&
                !dateEnd.getText().toString().equalsIgnoreCase(getString(R.string.end_date_hint))) {
                try {
                    Log.i(TAG, "onClick: " + new Validators().validate_date(dateStart.getText().toString()));
                    newTerm = new Term(termName.getText().toString(),
                            formatter.parse(dateStart.getText().toString()),
                            formatter.parse(dateEnd.getText().toString()));
                    appRepository.insert(newTerm);
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new TermsFragment()).commit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
