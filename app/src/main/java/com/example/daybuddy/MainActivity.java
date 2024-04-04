package com.example.daybuddy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import retrofit.GsonConverterFactory;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    TextView HintText;
    private static final String TAG = "LOCATION_PICKER_TAG";

    public String move_mode = "driving";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference TasksBase = database.getReference("tasks");

    MaterialButton pickTime;
    MaterialButton inputText;

    ArrayList<Task_Model> Task_Model = new ArrayList<>();

    String Et_Time = "00:00";
    String St_Time = "00:00";

    Double latitude = null;
    Double longitude = null;
    String address = null;

    int St_time_M = 0;
    int Et_time_M = 0;
    String Task_text = "Task";
    RecyclerView tasks_recyclerview;

    FirebaseUser mUser;

    TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model);

    DatabaseReference myRef;
    int position;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Bundle extras1 = getIntent().getExtras();
        if(extras1 == null) {
            Task_Model = (ArrayList<com.example.daybuddy.Task_Model>) extras1.get("TaskModelArr");
            position = extras1.getInt("Position");
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUser = extras.getParcelable("auth");
            //The key argument here must match that used in the other activity
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://day-buddy-default-rtdb.firebaseio.com/");

        myRef = database.getReference("Task_Model");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Task_Model = (ArrayList<com.example.daybuddy.Task_Model>) dataSnapshot.getValue(Object.class);
                Log.d(TAG, "Value is: " + Task_Model);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





        tasks_recyclerview = findViewById(R.id.RV_Tasks);
        int pos = Task_Model.size()-1;
        Toast.makeText(this, ""+ pos, Toast.LENGTH_SHORT).show();

        tasks_adapter.notifyItemInserted(pos);



        tasks_recyclerview.setAdapter(tasks_adapter);
        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        HintText = findViewById(R.id.HintText);
        CheckHintText();

    }

    public void CheckHintText(){
        if (Task_Model.size()>0){
            HintText.setVisibility(View.GONE);
        }else{
            HintText.setVisibility(View.VISIBLE);
        }
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
                        } else if (Task_Model.size()>0) {
                            if (Task_Model.get(Task_Model.size() - 1).et_time_M > St_time_M) {
                                Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();

                            } else {
                                Task_Model.add(new Task_Model(Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));

                                tasks_adapter.notifyItemInserted(Task_Model.size()+2);
                                CheckHintText();
                                tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
                            }
                        }else {

                            Task_Model.add(new Task_Model(Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));

                            tasks_adapter.notifyItemInserted(Task_Model.size() + 1);
                            CheckHintText();
                            tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
                            myRef.setValue(Task_Model);
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
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        TextView ST_View = view1.findViewById(R.id.location);


        Intent intent = new Intent(MainActivity.this, places.class);
//        startActivity(intent);
        placesResultLauncher.launch(intent);

    }

    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
    }

    private ActivityResultLauncher<Intent> placesResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){

                        Intent data = result.getData();

                        if (data != null){

                            latitude = data.getDoubleExtra("latitude", 0.0);
                            longitude = data.getDoubleExtra("longitude",0.0);
                            address = data.getStringExtra("address");

                        }else{
                            Log.d(TAG, "onActivityResult: cancelled");
                            Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                            
                        }
                    }
                }
            }
    );

    public void direction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String origin = "" + Task_Model.get(Task_Model.size()-1).latitude + ", " + Task_Model.get(Task_Model.size()-1).longitude;
        String destination = "" + latitude + ", " + longitude;
        String Url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("origin", origin)
                .appendQueryParameter("destination", destination)
                .appendQueryParameter("mode", move_mode)
                .appendQueryParameter("key", getString(R.string.google_api_key))
                .toString();
    }

    public void Save(View view){
        Intent intent = new Intent(MainActivity.this, calendar_activity.class);
        intent.putExtra("TaskModelArr", Task_Model);
        intent.putExtra("Position", position);
        setResult(RESULT_OK, intent);

        finish();
    }



}