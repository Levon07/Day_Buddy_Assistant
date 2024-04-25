package com.example.daybuddy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.daybuddy.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import retrofit.GsonConverterFactory;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements RV_Interface {

    private ActivityMainBinding binding;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
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

    String Title = null;

    Double latitude = null;
    Double longitude = null;
    String address = null;

    int St_time_M = 0;
    int Et_time_M = 0;
    String Task_text = "Task";
    RecyclerView tasks_recyclerview;

    FirebaseUser mUser;

    TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model, this);

    DatabaseReference myRef;
    int position;
    String Day_OW;
    TextView Day_Of_Week;

    View view1;
    String id;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        HintText = findViewById(R.id.HintText);
        Day_Of_Week = findViewById(R.id.dayOfWeek);

        createNotificationChannel();


        Bundle extras1 = getIntent().getExtras();
   if(extras1 != null) {

            Day_OW = extras1.getString("day");
            Day_Of_Week.setText(Day_OW);
            calendar = (Calendar) extras1.get("calendar");
            id = extras1.getString("id");

        }
        progressBar.setVisibility(View.VISIBLE);
        HintText.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                            {
                                int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                        queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM ,
                                        ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
                            }
                            tasks_adapter.notifyDataSetChanged();
                            CheckHintText();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }






        tasks_recyclerview = findViewById(R.id.RV_Tasks);

        tasks_adapter.notifyDataSetChanged();



        tasks_recyclerview.setAdapter(tasks_adapter);
        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(this));




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
                ST_View.setText("" + St_Time);

                calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
        timePicker.show(getSupportFragmentManager(), "tag");


    }


    public void pickEndtime(View view) {
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
        View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_text_dialog_layout, null);
        TextInputEditText editText = view2.findViewById(R.id.edittext);
        TextView task_text = view1.findViewById(R.id.Task_text_View);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Title")
                .setView(view2)
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

    private void Add_Task_To_Model(){
        String idNew = UUID.randomUUID().toString();
        Task_Model.add(new Task_Model(idNew, Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));
        SetAlarm();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("day_ow", Day_OW);
            hashMap.put("Task_text", Task_text);
            hashMap.put("ST_Time", St_Time);
            hashMap.put("ET_Time", Et_Time);
            hashMap.put("ET_time_M", Et_time_M);
            hashMap.put("ST_time_M", St_time_M);
            hashMap.put("address", address);
            hashMap.put("latitude", latitude);
            hashMap.put("longitude", longitude);
            hashMap.put("DocID", idNew);
            hashMap.put("userId", user.getUid());
            db.collection("daysModel").document(id).collection("taskModels").document(idNew).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });

            //db.collection("daysModel").document("daysModelId").collection("taskModels").add(hashMap);
        }
        tasks_adapter.notifyDataSetChanged();
        CheckHintText();
        tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
        if (Task_Model.size() > 1){
            LatLng origin = new LatLng(Task_Model.get(Task_Model.size()-2).latitude, Task_Model.get(Task_Model.size()-2).longitude);
            LatLng destination = new LatLng(Task_Model.get(Task_Model.size()-1).latitude, Task_Model.get(Task_Model.size()-1).longitude);
            DestinationTimeCalculator travelTime = new DestinationTimeCalculator(origin, destination);
            String result = String.valueOf(travelTime.execute());
            Toast.makeText(this,result, Toast.LENGTH_SHORT).show();
            Log.e("LEVON", result);
        }
    }

    public void Add_Task(View view) {
        view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
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
                                Add_Task_To_Model();
                            }
                        }else {
                            Add_Task_To_Model();
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


        Intent intent = new Intent(MainActivity.this, places.class);
//        startActivity(intent);
        placesResultLauncher.launch(intent);

    }

    public void signOut(View view){
        onBackPressed();
    }

    private ActivityResultLauncher<Intent> placesResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){

                        Intent data = result.getData();

                        if (data != null){
                            TextView ST_View = view1.findViewById(R.id.location);
                            latitude = data.getDoubleExtra("latitude", 0.0);
                            longitude = data.getDoubleExtra("longitude",0.0);
                            address = data.getStringExtra("address");
                            Title = data.getStringExtra("title");
                            ST_View.setText(Title);


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
     finish();
    }


    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

        view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
        TextView St_Time1 = view1.findViewById(R.id.Start_time_view);
        TextView Et_Time1 = view1.findViewById(R.id.End_Time_view);
        TextView TaskText1 = view1.findViewById(R.id.Task_text_View);
        TextView Location1 = view1.findViewById(R.id.location);

        St_Time1.setText(Task_Model.get(position).time_start);
        Et_Time1.setText(Task_Model.get(position).time_end);
        TaskText1.setText(Task_Model.get(position).task_text);
        Location1.setText(Task_Model.get(position).location);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Edit Task")
                .setView(view1)
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                                Task_Model.set(position, new Task_Model(Task_Model.get(position).DocID, Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));
                                SetAlarm();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("day_ow", Day_OW);
                                    hashMap.put("Task_text", Task_text);
                                    hashMap.put("ST_Time", St_Time);
                                    hashMap.put("ET_Time", Et_Time);
                                    hashMap.put("ET_time_M", Et_time_M);
                                    hashMap.put("ST_time_M", St_time_M);
                                    hashMap.put("address", address);
                                    hashMap.put("latitude", latitude);
                                    hashMap.put("longitude", longitude);
                                    hashMap.put("DocID", Task_Model.get(position).DocID);
                                    hashMap.put("userId", user.getUid());
                                    db.collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(position).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    //db.collection("daysModel").document("daysModelId").collection("tasks").add(hashMap);
                                }
                                tasks_adapter.notifyDataSetChanged();
                                CheckHintText();
                                tasks_recyclerview.smoothScrollToPosition(position);




                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("Delete Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(position).DocID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Task_Model.remove(position);
                        tasks_adapter.notifyItemRemoved(position);
                        CheckHintText();
                    }
                }).create();
        alertDialog.show();


    }


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Day Buddy";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Day Buddy", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void SetAlarm(){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }
}


