package com.haydenfarnsworth.studentsupport.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {
    Pattern date_pattern = Pattern.compile("^(0?[1-9]|1[0-2])[/](0?[1-9]|[12]\\d|3[01])[/](19|20)\\d{2}$");

    public boolean validate_date(String date) {
        return date_pattern.matcher(date).matches();
    }
}
