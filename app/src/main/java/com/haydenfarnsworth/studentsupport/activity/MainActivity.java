package com.haydenfarnsworth.studentsupport.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.haydenfarnsworth.studentsupport.R;
import com.haydenfarnsworth.studentsupport.fragment.AssessmentFragment;
import com.haydenfarnsworth.studentsupport.fragment.CourseWithAssessmentsFragment;
import com.haydenfarnsworth.studentsupport.fragment.CoursesFragment;
import com.haydenfarnsworth.studentsupport.fragment.HomeFragment;
import com.haydenfarnsworth.studentsupport.fragment.NewTermFragment;
import com.haydenfarnsworth.studentsupport.fragment.TermsFragment;


public class MainActivity extends AppCompatActivity implements HomeFragment.AddTermBtnPressed {

    private final String HOME = "home";
    private final String TERMS = "terms";
    private final String COURSES = "courses";
    private final String ASSESSMENTS = "assessments";
    private static final String TAG = "Clicked";
    public FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener((item) -> {
            handleNavSelection(item);
            return true;
        });
        bottom_nav.setSelectedItemId(R.id.home);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, new HomeFragment());
        transaction.addToBackStack(null);

        transaction.commit();


//        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
//        db.getTermsDao().deleteAllTerms();
    }

    public void handleNavSelection(MenuItem item) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getTitle().toString()) {
            case "Home": transaction.replace(R.id.fragment_container, new HomeFragment()); break;
            case "Terms": transaction.replace(R.id.fragment_container, new TermsFragment()); break;
            case "Courses": transaction.replace(R.id.fragment_container, new CoursesFragment()); break;
            case "Assessments": transaction.replace(R.id.fragment_container, new AssessmentFragment()); break;
            default: break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void clicked() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, new NewTermFragment());
        transaction.addToBackStack(null);

        transaction.commit();
    }
}