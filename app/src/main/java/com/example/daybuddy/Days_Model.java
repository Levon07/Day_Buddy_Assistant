package com.example.daybuddy;

public class Days_Model {


    String Date;
    String Day_OW;

    public Days_Model(String Date, String day_OW) {
        this.Date = Date;
        this.Day_OW = day_OW;
    }

    public String getDate() {
        return Date;
    }


    public String getDay_OW() {
        return Day_OW;
    }


}
