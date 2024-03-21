package com.example.daybuddy;

public class Task_Model {

    String task_text;
    String location;
    String time_start;
    String time_end;

    public Task_Model(String task_text, String location, String time_start, String time_end) {
        this.task_text = task_text;
        this.location = location;
        this.time_start = time_start;
        this.time_end = time_end;
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
}
