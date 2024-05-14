//package com.example.daybuddy;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
//import com.example.daybuddy.databinding.ActivityMainBinding;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.dialog.MaterialAlertDialogBuilder;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.timepicker.MaterialTimePicker;
//import com.google.android.material.timepicker.TimeFormat;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import javax.xml.transform.Result;
//
//import android.content.Context;
//import androidx.annotation.NonNull;
//import androidx.work.Constraints;
//import androidx.work.NetworkType;
//import androidx.work.PeriodicWorkRequest;
//import androidx.work.WorkManager;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//
//public class MainActivity extends AppCompatActivity implements RV_Interface_Tasks {
//
//    public String ArriveDuration = null;
//
//    private ActivityMainBinding binding;
//    private AlarmManager alarmManager;
//    private PendingIntent pendingIntent;
//    private Calendar calendar;
//    TextView HintText;
//    private static final String TAG = "LOCATION_PICKER_TAG";
//
//    public String move_mode = "driving";
//
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference TasksBase = database.getReference("tasks");
//
//    MaterialButton pickTime;
//    MaterialButton inputText;
//
//    ArrayList<Task_Model> Task_Model = new ArrayList<>();
//
//    String Et_Time = "00:00";
//    String St_Time = "00:00";
//
//    String Title = null;
//    int visibility = 0;
//    Double latitude = null;
//    Double longitude = null;
//    String address = null;
//    int color = 0;
//    int St_time_M = 0;
//    int Et_time_M = 0;
//    String Task_text = "Task";
//
//    RecyclerView tasks_recyclerview;
//
//    FirebaseUser mUser;
//
//    TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model, this);
//
//    DatabaseReference myRef;
//
//    String Day_OW;
//    TextView Day_Of_Week;
//
//    View view1;
//    String id;
//    ProgressBar progressBar;
//    int Position_BackUp;
//
//    String travelMode = "walking";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        progressBar = findViewById(R.id.progressBar);
//        HintText = findViewById(R.id.HintText);
//        Day_Of_Week = findViewById(R.id.dayOfWeek);
//
//        createNotificationChannel();
//
//
//        Bundle extras1 = getIntent().getExtras();
//        if (extras1 != null) {
//
//            Day_OW = extras1.getString("day");
//            Day_Of_Week.setText(Day_OW);
//            calendar = (Calendar) extras1.get("calendar");
//            id = extras1.getString("id");
//
//        }
//        progressBar.setVisibility(View.VISIBLE);
//        HintText.setVisibility(View.GONE);
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//                                int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
//                                int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
//                                Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"),queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
//                                        queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
//                                        ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
//                            }
//                            Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
//                                @Override
//                                public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
//                                    return o1.time_start.compareTo(o2.time_start);
//                                }
//                            });
//                            tasks_adapter.notifyDataSetChanged();
//                            CheckHintText();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    });
//        }
//
//
//        tasks_recyclerview = findViewById(R.id.RV_Tasks);
//
//        tasks_adapter.notifyDataSetChanged();
//
//
//        tasks_recyclerview.setAdapter(tasks_adapter);
//        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(this));
//
//        startCheckingTraffic();
//
//
//    }
//
//    public void CheckHintText() {
//        if (Task_Model.size() > 0) {
//            HintText.setVisibility(View.GONE);
//        } else {
//            HintText.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    // Choose Time
//    public void pickStarttime(View view) {
//        TextView ST_View = view1.findViewById(R.id.Start_time_view);
//        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .setHour(12)
//                .setMinute(0)
//                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
//                .setTitleText("Pick Time")
//                .build();
//        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view2) {
//
//
//                int H = timePicker.getHour();
//                int M = timePicker.getMinute();
//                String Hour = String.valueOf(timePicker.getHour());
//                String Minute = String.valueOf(timePicker.getMinute());
//
//                if (H < 10) {
//                    Hour = "0" + H;
//                }
//                if (M < 10) {
//                    Minute = "0" + M;
//                }
//                St_time_M = H * 60 + M;
//
//
//                St_Time = Hour + ":" + Minute;
//                ST_View.setText("" + St_Time);
//
//                calendar = Calendar.getInstance();
//
//                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
//                calendar.set(Calendar.MINUTE, timePicker.getMinute());
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//            }
//        });
//        timePicker.show(getSupportFragmentManager(), "tag");
//
//
//    }
//
//
//    public void pickEndtime(View view) {
//        TextView ET_View = view1.findViewById(R.id.End_Time_view);
//        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .setHour(12)
//                .setMinute(0)
//                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
//                .setTitleText("Pick Time")
//                .build();
//        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view2) {
//
//                int H = timePicker.getHour();
//                int M = timePicker.getMinute();
//                String Hour = String.valueOf(timePicker.getHour());
//                String Minute = String.valueOf(timePicker.getMinute());
//
//                if (H < 10) {
//                    Hour = "0" + H;
//                }
//                if (M < 10) {
//                    Minute = "0" + M;
//                }
//
//                Et_time_M = H * 60 + M;
//
//                Et_Time = Hour + ":" + Minute;
//                ET_View.setText(Et_Time);
//
//
//            }
//        });
//        timePicker.show(getSupportFragmentManager(), "tag");
//
//
//    }
//
//    public void edittext(View view) {
//        View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_text_dialog_layout, null);
//        TextInputEditText editText = view2.findViewById(R.id.edittext);
//        TextView task_text = view1.findViewById(R.id.Task_text_View);
//        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
//                .setTitle("Title")
//                .setView(view2)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Task_text = String.valueOf(editText.getText());
//                        task_text.setText(Task_text);
//                        dialogInterface.dismiss();
//                    }
//                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create();
//        alertDialog.show();
//    }
//
//    private void Add_Task_To_Model() {
//        String idNew = UUID.randomUUID().toString();
//
//
//        Task_Model.add(new Task_Model(idNew, color, visibility, Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));
//        SetAlarm();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("day_ow", Day_OW);
//            hashMap.put("Task_text", Task_text);
//            hashMap.put("ST_Time", St_Time);
//            hashMap.put("ET_Time", Et_Time);
//            hashMap.put("ET_time_M", Et_time_M);
//            hashMap.put("ST_time_M", St_time_M);
//            hashMap.put("address", address);
//            hashMap.put("latitude", latitude);
//            hashMap.put("longitude", longitude);
//            hashMap.put("DocID", idNew);
//            hashMap.put("Color", color);
//            hashMap.put("Visibility", visibility);
//            hashMap.put("userId", user.getUid());
//            db.collection("daysModel").document(id).collection("taskModels").document(idNew).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            //db.collection("daysModel").document("daysModelId").collection("taskModels").add(hashMap);
//        }
//        tasks_adapter.notifyDataSetChanged();
//        CheckHintText();
//        tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
//
//
//    }
//
//
//    public void Add_Task_Final() {
//        if (!Task_Model.isEmpty()) {
//            visibility = 1;
//            LatLng origin = new LatLng(Task_Model.get(Task_Model.size() - 1).latitude, Task_Model.get(Task_Model.size() - 1).longitude);
//            LatLng destination = new LatLng(latitude, longitude);
//            DestinationTime travelTime = new DestinationTime(origin, destination, travelMode);
//            travelTime.execute();
//
//
//            Handler handler = new Handler();
//
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    int Duration = GetInt(ArriveDuration);
//                    Toast.makeText(MainActivity.this, "Duration: " + Duration, Toast.LENGTH_SHORT).show();
//                    if (St_time_M - Task_Model.get(Task_Model.size() - 1).et_time_M > Duration) {
//                        color = 0;
//                        Add_Task_To_Model();
//                        Toast.makeText(MainActivity.this, "You will be on Time", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//
//                    } else {
//                        color = 1;
//                        Add_Task_To_Model();
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(MainActivity.this, "You will be Late", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };
//            progressBar.setVisibility(View.VISIBLE);
//            handler.postDelayed(runnable, 2000);
//        } else {
//            visibility = 0;
//            Add_Task_To_Model();
//        }
//    }
//
//    public int GetInt(String a) {
//        String input = a;
//
//        // Define a pattern to match digits
//        Pattern pattern = Pattern.compile("\\d+");
//
//        // Create a matcher for the input string
//        Matcher matcher = pattern.matcher(input);
//
//        // Check if a match is found
//        if (matcher.find()) {
//            // Get the matched substring
//            String numberStr = matcher.group();
//
//            // Convert the matched substring to an integer
//            int number = Integer.parseInt(numberStr);
//
//            // Print the integer value
//            System.out.println("Extracted integer: " + number);
//            return number;
//        } else {
//            System.out.println("No integer found in the input string.");
//            return -1;
//        }
//    }
//
//    public void Add_Task(View view) {
//        view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
//        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
//                .setTitle("Add Task")
//                .setView(view1)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        if (St_time_M >= Et_time_M) {
//                            Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
//                        } else if (Task_Model.size() > 0) {
//                            if (Task_Model.get(Task_Model.size() - 1).et_time_M > St_time_M) {
//                                Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Add_Task_Final();
//                            }
//                        } else {
//                            Add_Task_Final();
//                        }
//
//
//                        dialogInterface.dismiss();
//
//                    }
//                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create();
//        alertDialog.show();
//    }
//
//    public void pickaPlace(View view) {
//
//
//        Intent intent = new Intent(MainActivity.this, places.class);
////        startActivity(intent);
//        placesResultLauncher.launch(intent);
//
//    }
//
//    public void signOut(View view) {
//        onBackPressed();
//    }
//
//    private ActivityResultLauncher<Intent> placesResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//
//                        Intent data = result.getData();
//
//                        if (data != null) {
//                            TextView ST_View = view1.findViewById(R.id.location);
//                            latitude = data.getDoubleExtra("latitude", 0.0);
//                            longitude = data.getDoubleExtra("longitude", 0.0);
//                            address = data.getStringExtra("address");
//                            Title = data.getStringExtra("title");
//                            travelMode = data.getStringExtra("travelMode");
//                            ST_View.setText(Title);
//
//
//                        } else {
//                            Log.d(TAG, "onActivityResult: cancelled");
//                            Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                }
//            }
//    );
//
//
//    public void Save(View view) {
//        finish();
//    }
//
//
//    @Override
//    public void onItemClickedTasks(int position) {
//
//    }
//
//    @Override
//    public void onItemLongClickTasks(int position) {
//
//        Position_BackUp = position;
//
//        view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_task, null);
//        TextView St_Time1 = view1.findViewById(R.id.Start_time_view);
//        TextView Et_Time1 = view1.findViewById(R.id.End_Time_view);
//        TextView TaskText1 = view1.findViewById(R.id.Task_text_View);
//        TextView Location1 = view1.findViewById(R.id.location);
//
//        St_Time1.setText(Task_Model.get(position).time_start);
//        Et_Time1.setText(Task_Model.get(position).time_end);
//        TaskText1.setText(Task_Model.get(position).task_text);
//        Location1.setText(Task_Model.get(position).location);
//        AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
//                .setTitle("Edit Task")
//                .setView(view1)
//                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                        if (St_time_M >= Et_time_M) {
//                            Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
//                        } else if (!Task_Model.isEmpty()) {
//                            if (Task_Model.get(position - 1).et_time_M > St_time_M) {
//                                Toast.makeText(MainActivity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Change_Task_Final();
//                            }
//                        } else {
//                            Change_Task_Final();
//                        }
//
//
//                        dialogInterface.dismiss();
//
//                    }
//                }).setNegativeButton("Delete Task", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(position).DocID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        Task_Model.remove(position);
//                        tasks_adapter.notifyItemRemoved(position);
//                        CheckHintText();
//                    }
//                }).create();
//        alertDialog.show();
//
//
//    }
//
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "Day Buddy";
//            String desc = "Channel for Alarm Manager";
//            int imp = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("Day Buddy", name, imp);
//            channel.setDescription(desc);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void SetAlarm() {
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
//
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void Change_Task_In_Model() {
//        String idNew = UUID.randomUUID().toString();
//
//
//        Task_Model.set(Position_BackUp, new Task_Model(idNew, color, visibility, Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));
//        SetAlarm();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("day_ow", Day_OW);
//            hashMap.put("Task_text", Task_text);
//            hashMap.put("ST_Time", St_Time);
//            hashMap.put("ET_Time", Et_Time);
//            hashMap.put("ET_time_M", Et_time_M);
//            hashMap.put("ST_time_M", St_time_M);
//            hashMap.put("address", address);
//            hashMap.put("latitude", latitude);
//            hashMap.put("longitude", longitude);
//            hashMap.put("DocID", idNew);
//            hashMap.put("Color", color);
//            hashMap.put("Visibility", visibility);
//            hashMap.put("userId", user.getUid());
//            db.collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(Position_BackUp).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            //db.collection("daysModel").document("daysModelId").collection("taskModels").add(hashMap);
//        }
//        tasks_adapter.notifyDataSetChanged();
//        CheckHintText();
//        tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
//
//
//    }
//
//
//    public void Change_Task_Final() {
//        if (!Task_Model.isEmpty()) {
//            visibility = 1;
//            LatLng origin = new LatLng(Task_Model.get(Position_BackUp - 1).latitude, Task_Model.get(Position_BackUp - 1).longitude);
//            LatLng destination = new LatLng(latitude, longitude);
//            DestinationTime travelTime = new DestinationTime(origin, destination, travelMode);
//            travelTime.execute();
//
//
//            Handler handler = new Handler();
//
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    int Duration = GetInt(ArriveDuration);
//                    Toast.makeText(MainActivity.this, "Duration: " + Duration, Toast.LENGTH_SHORT).show();
//                    if (St_time_M - Task_Model.get(Position_BackUp - 1).et_time_M > Duration) {
//
//                        color = 0;
//                        Change_Task_In_Model();
//                        Toast.makeText(MainActivity.this, "You will be on Time", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//
//                    } else {
//
//                        color = 1;
//                        Change_Task_In_Model();
//                        Toast.makeText(MainActivity.this, "You will be Late", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//
//                    }
//                }
//            };
//            progressBar.setVisibility(View.VISIBLE);
//            handler.postDelayed(runnable, 2000);
//        } else {
//
//            visibility = 0;
//            Add_Task_To_Model();
//        }
//
//    }
//
//
//    public void startCheckingTraffic(){
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        // Create a periodic work request to run every 10 minutes
//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
//                DurationCheckWorker.class, 10, TimeUnit.SECONDS)
//                .setConstraints(constraints)
//                .build();
//
//        // Enqueue the periodic work request
//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
//    }
//
//
//    class DestinationTime extends AsyncTask<Void, Void, String> {
//        private static final String TAG = "DestinationTimeCalculator";
//
//        private LatLng origin;
//        private LatLng destination;
//        private String apiKey = "AIzaSyBa-ttElPoQgDetwieuuMp360EJeXlr5RY";
//        private String travelMode;
//        private String trafficModel = "best_guess";
//
//
//        String durationText;
//
//        public DestinationTime(LatLng origin, LatLng destination, String travelMode) {
//            this.origin = origin;
//            this.destination = destination;
//            this.travelMode = travelMode;
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            String response = "";
//            try {
//                String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json" +
//                        "?origins=" + origin.latitude + "," + origin.longitude +
//                        "&destinations=" + destination.latitude + "," + destination.longitude + "&mode=" + travelMode +
//                        "&key=" + apiKey;
//
//                URL url = new URL(apiUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                InputStream inputStream = connection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//
//                response = stringBuilder.toString();
//            } catch (IOException e) {
//                Log.e(TAG, "Error fetching data: " + e.getMessage());
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            super.onPostExecute(response);
//            if (response != null) {
//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    JSONArray rows = jsonResponse.getJSONArray("rows");
//                    if (rows.length() > 0) {
//                        JSONObject row = rows.getJSONObject(0);
//                        JSONArray elements = row.getJSONArray("elements");
//                        if (elements.length() > 0) {
//                            JSONObject element = elements.getJSONObject(0);
//                            JSONObject duration = element.getJSONObject("duration");
//                            durationText = duration.getString("text");
//                            ArriveDuration = durationText;
//                            Toast.makeText(getApplicationContext(), durationText, Toast.LENGTH_SHORT).show();
//                            //Toast.makeText(, durationText, Toast.LENGTH_SHORT).show();
//                            // Use durationText as the estimated travel time
//                        }
//                    }
//                } catch (JSONException e) {
//                    Log.e(TAG, "Error parsing JSON: " + e.getMessage());
//                }
//            }
//        }
//    }
//
//    public class DurationCheckWorker extends Worker {
//
//        public DurationCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//            super(context, workerParams);
//        }
//
//        @NonNull
//        @Override
//        public Result doWork() {
//            // Call the method to check duration between locations
//            checkDuration();
//            return Result.success();
//        }
//
//        private void checkDuration() {
//            // Your code to check duration goes here
//            // This could include creating an instance of DestinationTimeCalculator and executing it
//            // You may also want to handle the results (e.g., update UI, save data, etc.)
//            Calendar calendar = Calendar.getInstance();
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            int minute = calendar.get(Calendar.MINUTE);
//
//            // Calculate total minutes since midnight
//            long totalMinutes = hour * 60 + minute;
//
//
//            Toast.makeText(MainActivity.this, ""+totalMinutes, Toast.LENGTH_SHORT).show();
//            Log.e("BACKGROUND", "checkDuration: " + totalMinutes);
////            LatLng origin = new LatLng(Task_Model.get(Position_BackUp - 1).latitude, Task_Model.get(Position_BackUp - 1).longitude);
////            LatLng destination = new LatLng(latitude, longitude);
////            DestinationTime travelTime = new DestinationTime(origin, destination, travelMode);
////            travelTime.execute();
//        }
//    }
//}
//
//
