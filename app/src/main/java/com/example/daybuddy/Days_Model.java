package com.example.daybuddy;

public class Days_Model {


    String Date;
    String Day_OW;
    Task_Model taskModel;

    public Days_Model(String Date, String day_OW, Task_Model taskModel) {
        this.Date = Date;
        this.Day_OW = day_OW;
        this.taskModel = taskModel;
    }

    public String getDate() {
        return Date;
    }


    public String getDay_OW() {
        return Day_OW;
    }



}
