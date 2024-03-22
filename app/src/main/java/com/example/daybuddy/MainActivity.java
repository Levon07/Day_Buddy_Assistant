package com.example.daybuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference TasksBase = database.getReference("tasks");

    MaterialButton pickTime;
    MaterialButton inputText;

    ArrayList<Task_Model> Task_Model = new ArrayList<>();

    String Et_Time = "00:00";
    String St_Time = "00:00";

    int St_time_M = 0;
    int Et_time_M = 0;
    String Task_text = "Task";
    RecyclerView tasks_recyclerview;

    FirebaseUser mUser;

    TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUser = extras.getParcelable("auth");
            //The key argument here must match that used in the other activity
        }



        tasks_recyclerview = findViewById(R.id.RV_Tasks);

        Task_Model.add(new Task_Model(Task_text, "location", St_Time, Et_Time, St_time_M, Et_time_M));

        tasks_adapter.notifyItemInserted(0);



        tasks_recyclerview.setAdapter(tasks_adapter);
        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }



    // Choose Time
    public void pickStarttime(View view) {
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        TextView ST_View = view1.findViewById(R.id.Start_time_view);
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTitleText("Pick Time")
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {



                int H = timePicker.getHour();
                int M = timePicker.getMinute();
                String Hour = String.valueOf(timePicker.getHour());
                String Minute = String.valueOf(timePicker.getMinute());

                if(H < 10) {
                    Hour = "0" + H;
                }
                if(M < 10) {
                    Minute = "0" + M;
                }
                St_time_M = H*60 + M;


                St_Time = Hour + ":" + Minute;
                ST_View.setText(St_Time);

            }
        });
        timePicker.show(getSupportFragmentManager(), "tag");


    }


    public void pickEndtime(View view) {
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        TextView ET_View = view1.findViewById(R.id.End_Time_view);
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTitleText("Pick Time")
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {

                int H = timePicker.getHour();
                int M = timePicker.getMinute();
                String Hour = String.valueOf(timePicker.getHour());
                String Minute = String.valueOf(timePicker.getMinute());

                if(H < 10) {
                    Hour = "0" + H;
                }
                if(M < 10) {
                    Minute = "0" + M;
                }

                Et_time_M = H*60 + M;

                Et_Time = Hour + ":" + Minute;
                ET_View.setText(Et_Time);


            }
        });
        timePicker.show(getSupportFragmentManager(), "tag");


    }

    public void edittext(View view) {
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_text_dialog_layout, null);
        TextInputEditText editText = view1.findViewById(R.id.edittext);
        View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        TextView task_text = view2.findViewById(R.id.Task_text_View);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Title")
                .setView(view1)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Task_text = String.valueOf(editText.getText());
                        task_text.setText(Task_text);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    public void Add_Task(View view) {
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        TextView editText = view1.findViewById(R.id.Start_time_view);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Add Task")
                .setView(view1)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(St_time_M>=Et_time_M){
                            Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
                        } else if (Task_Model.get(Task_Model.size()-1).et_time_M > St_time_M) {
                            Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
                        }else {
                            Task_Model.add(new Task_Model(Task_text, "location", St_Time, Et_Time, St_time_M, Et_time_M));

                            tasks_adapter.notifyItemInserted(Task_Model.size() + 1);

//                        TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(MainActivity.this, Task_Model);
//
//                        tasks_recyclerview.setAdapter(tasks_adapter);
//                        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
                            tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
                        }


                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    public void pickaPlace(View view){

        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_places, null);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Choose the Task Place")
                .setView(view1)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
    }

}