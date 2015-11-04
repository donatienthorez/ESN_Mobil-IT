package org.esn.mobilit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertor {

    public static Date getDate(String stringDate, String dateFormat) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.parse(stringDate);
    }
}
