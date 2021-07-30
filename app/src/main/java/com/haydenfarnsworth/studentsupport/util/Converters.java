package com.haydenfarnsworth.studentsupport.util;

import androidx.room.TypeConverter;

import com.haydenfarnsworth.studentsupport.entity.Course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<String> fromStrings(String values) {
        List<String> list = new ArrayList<>();

        String[] array = values.split("/,/");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(s);
            }
        }
        return list;
    }

    @TypeConverter
    public static String listToStrings(List<String> list) {
        String values = "";
        for (String s : list) {
            values += "/,/" + s;
        }
        return values;
    }

    @TypeConverter
    public static Course.Status fromInt(int value) {
        return Course.Status.values()[value];
    }

    @TypeConverter
    public static int statusToInt (Course.Status status) {
        return status.ordinal();
    }
}
