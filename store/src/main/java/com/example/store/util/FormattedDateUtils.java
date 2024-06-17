package com.example.store.util;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FormattedDateUtils {
    public String convertToString(ZonedDateTime date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm");
        String result = date.format(fmt);
        return result;
    }
}
