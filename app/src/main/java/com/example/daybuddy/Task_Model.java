package com.example.daybuddy;

import android.graphics.drawable.Drawable;
import android.opengl.Visibility;

public class Task_Model {

    String task_text;
    String location;
    String time_start;
    String time_end;

    int st_time_M;
    int et_time_M;

    Double latitude;
    Double longitude;
    String DocID;
    int color;
    int checkColor;
    int visibility;



    public Task_Model(String DocID,int checkColor, int color, int visibility, String task_text, String location, String time_start, String time_end, int st_time_M, int et_time_M, Double latitude, Double longitude) {
        this.task_text = task_text;
        this.location = location;
        this.time_start = time_start;
        this.time_end = time_end;
        this.st_time_M = st_time_M;
        this.et_time_M = et_time_M;
        this.latitude = latitude;
        this.longitude = longitude;
        this.DocID = DocID;
        this.color = color;
        this.visibility = visibility;
        this.checkColor = checkColor;

    }

    public String getTask_text() {
        return task_text;
    }

    public String getLocation() {
        return location;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public int getColor() {
        return color;
    }

    public int getVisibility() {
        return visibility;
    }
    public int getCheckColor() {
        return checkColor;
    }
}
