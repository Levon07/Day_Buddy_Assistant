package com.example.daybuddy;

import java.util.ArrayList;
import java.util.Calendar;

public class Days_Model {

    String id;
    String Date;
    String Day_OW;
    int Year;
    int color = 0;
    int Month;



    Calendar calendar;

    public Days_Model(String id, int color ,int Year, int Month,  String Date, String day_OW, Calendar calendar) {
        this.id = id;
        this.Date = Date;
        this.Day_OW = day_OW;
        this.calendar = calendar;
        this.color = color;
        this.Year = Year;
        this.Month = Month;
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


    public int getColor() {
        return color;
    }

    public int getYear() {
        return Year;
    }
}
