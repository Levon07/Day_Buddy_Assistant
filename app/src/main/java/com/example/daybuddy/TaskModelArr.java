package com.example.daybuddy;

import java.util.ArrayList;

public class TaskModelArr {
    ArrayList<Task_Model> TaskModel = new ArrayList<>();



    public TaskModelArr(ArrayList<Task_Model> TaskModel){
        this.TaskModel = TaskModel;
    }

    public ArrayList<Task_Model> getTaskModel() {
        return TaskModel;
    }


}
