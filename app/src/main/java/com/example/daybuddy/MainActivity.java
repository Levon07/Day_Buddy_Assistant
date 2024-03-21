package com.example.daybuddy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewTreeViewModelKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    MaterialButton pickTime;
    MaterialButton inputText;

    ArrayList<Task_Model> Task_Model = new ArrayList<>();

    String Et_Time = "00:00";
    String St_Time = "00:00";
    String Task_text = "Task";
    RecyclerView tasks_recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        tasks_recyclerview = findViewById(R.id.RV_Tasks);

        Task_Model.add(new Task_Model(Task_text, "location", St_Time, Et_Time));

        TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model);

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
                .setTitle("Title")
                .setView(view1)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Task_Model.add(new Task_Model(Task_text,"location",St_Time,Et_Time));

                        TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(MainActivity.this, Task_Model);

                        tasks_recyclerview.setAdapter(tasks_adapter);
                        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());

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

        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Title")
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

//        Intent intent = new Intent(MainActivity.this, places.class);
//        startActivity(intent);
    }

}