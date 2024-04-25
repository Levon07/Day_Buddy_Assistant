package com.example.daybuddy;

import java.util.ArrayList;
import java.util.Calendar;

public class Days_Model {

    String id;
    String Date;
    String Day_OW;



    Calendar calendar;

    public Days_Model(String id,String Date, String day_OW, Calendar calendar) {
        this.id = id;
        this.Date = Date;
        this.Day_OW = day_OW;
        this.calendar = calendar;

    }

    public String getDate() {
        return Date;
    }


    public String getDay_OW() {
        return Day_OW;
    }

    public String getId() {
        return id;
    }

    public Calendar getCalendar() {
        return calendar;
    }



}
