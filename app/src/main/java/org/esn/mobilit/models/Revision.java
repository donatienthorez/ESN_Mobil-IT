package org.esn.mobilit.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Revision {

    private String id;
    private String date;

    public Revision(String id, String date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

