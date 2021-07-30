package com.haydenfarnsworth.studentsupport.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.haydenfarnsworth.studentsupport.entity.Assessment;
import com.haydenfarnsworth.studentsupport.dao.AssessmentsDao;
import com.haydenfarnsworth.studentsupport.dao.CoursesDao;
import com.haydenfarnsworth.studentsupport.dao.TermsDao;
import com.haydenfarnsworth.studentsupport.entity.Course;
import com.haydenfarnsworth.studentsupport.entity.Term;
import com.haydenfarnsworth.studentsupport.util.Converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase{

    public abstract TermsDao getTermsDao();
    abstract CoursesDao getCoursesDao();
    abstract AssessmentsDao getAssessmentsDao();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService database_executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if(instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").addCallback(roomDatabaseCallback).build();
                }
            }
        }
        return instance;
    }

    public static DateFormat df = new SimpleDateFormat("M/d/yyyy");
    public static Term term;
    public static Course course;

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            database_executor.execute(() -> {
                instance.getTermsDao().deleteAllTerms();
                instance.getCoursesDao().deleteAllCourses();
                try {
                    term = new Term("Term 1", df.parse("12/17/2020"), df.parse("1/20/2021"));
                    long termId = instance.getTermsDao().insertTerm(term);
                    course = new Course("Karate", (int)termId, df.parse("12/17/2020"), df.parse("1/20/2021"), Course.Status.INPROGRESS, "Splinter", "8172349045", "splinter@tmnt.com");
                    instance.getCoursesDao().insertCourses(course);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    };
}
