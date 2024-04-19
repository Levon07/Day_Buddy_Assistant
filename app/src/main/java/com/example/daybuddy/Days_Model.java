package com.example.daybuddy;

import java.util.ArrayList;

public class Days_Model {

    String id;
    String Date;
    String Day_OW;

    public Days_Model(String id,String Date, String day_OW) {
        this.id = id;
        this.Date = Date;
        this.Day_OW = day_OW;

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



}
