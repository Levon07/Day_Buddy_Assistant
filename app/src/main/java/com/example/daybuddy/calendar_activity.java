package com.example.daybuddy;

import static java.sql.DriverManager.println;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import java.sql.Time;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daybuddy.chatgpt.ChatGPTActivity;
import com.example.daybuddy.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class calendar_activity extends AppCompatActivity implements RV_Interface, RV_Interface_Tasks, RV_Interface_Months, RV_Interface_Year {

    int NowTime;

    TextView Time1;
    TextView Time2;


    int position;
    private Calendar calendar;

    ConstraintLayout HintText;

    ArrayList<TaskModelArr> Task_Model_Arr = new ArrayList<>();
    ArrayList<Month_Model> Month_Model = new ArrayList<>();
    Months_Adapter month_adapter = new Months_Adapter(this, Month_Model, this);

    ArrayList<Year_Model> Year_Model = new ArrayList<>();
    Year_Adapter year_adapter = new Year_Adapter(this, Year_Model, this);

    private static final String TAG = "LOCATION_PICKER_TAG";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference DaysBase = database.getReference("tasks");
    String idCopy;


    ArrayList<Days_Model> Days_Model = new ArrayList<>();
    ArrayList<Days_Model> Days_ModelY = new ArrayList<>();
    ArrayList<Days_Model> Days_ModelALL = new ArrayList<>();


    String Date;

    String Day_OW;
    RecyclerView days_recyclerview;

    FirebaseUser mUser;

    Days_RV_Adapter days_adapter = new Days_RV_Adapter(this, Days_Model, this);

    DatabaseReference myRef;
    ProgressBar progressBar;
    TextView Date_Today;


    ///////MAIN_ACTIVITY_CLASS////////////////////////////

    public String ArriveDuration = null;

    private ActivityMainBinding binding;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar1;
    private static final String TAG1 = "LOCATION_PICKER_TAG";

    public String move_mode = "driving";

    DatabaseReference TasksBase = database.getReference("tasks");

    MaterialButton pickTime;
    MaterialButton inputText;
    int color_days = 0;

    ArrayList<Task_Model> Task_Model = new ArrayList<>();
    ArrayList<Task_Model> Task_Model1 = new ArrayList<>();

    ImageView arrow;

    String Et_Time = "00:00";
    String St_Time = "00:00";

    String Title = null;
    int visibility = 0;
    Double latitude = null;
    Double longitude = null;
    String address = null;
    int color = 0;
    int checkColor = 0;
    int St_time_M = 0;
    int Et_time_M = 0;
    String Task_text = "Task";
    TextView taskCompleted;
    int Month;

    RecyclerView tasks_recyclerview;


    TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model, this);

    ConstraintLayout HintTextTask;

    String Day_OW1;

    int positionCopy = 0;
    TextView Day_Of_Week;

    View view1;
    String id;
    int Position_BackUp;
    ProgressBar progressBarTask;
    TextView pendingTaskCount;

    String travelMode = "walking";


    ////////////////////////////////////////////


    ZonedDateTime currentTime;
    LocalDate currentDate;
    String formattedDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
    int Year;
    TextView Month_TV;

    TextView TextTaskText;

    ConstraintLayout liveUpdate;
    ConstraintLayout AI;
    RecyclerView Months_RV;
    RecyclerView Years_RV;

    String[] Months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int[] years = {1964, 1965, 1966, 1967, 1968, 1969, 1970, 1971, 1972, 1973, 1974, 1975, 1976, 1977, 1978, 1979, 1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020,
            2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032, 2033, 2034, 2035, 2036, 2037, 2038, 2039, 2040, 2041, 2042, 2043, 2044, 2045, 2046, 2047, 2048, 2049, 2050, 2051, 2052, 2053, 2054, 2055, 2056, 2057, 2058, 2059, 2060
            , 2061, 2062, 2063, 2064, 2065, 2066, 2067, 2068, 2069, 2070, 2071, 2072, 2073, 2074, 2075, 2076, 2077, 2078, 2079, 2080, 2081, 2082, 2083, 2084, 2085, 2086, 2087, 2088, 2089, 2090, 2091, 2092, 2093, 2094, 2095, 2096, 2097, 2098, 2099, 2100, 2101, 2102, 2103, 2104, 2105, 2106, 2107, 2108, 2109, 2110, 2111, 2112,
            2113, 2114, 2115, 2116, 2117, 2118, 2119, 2120, 2121, 2122, 2123, 2124, 2125, 2126, 2127, 2128, 2129, 2130, 2131};
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    ConstraintLayout Month_lay;
    ConstraintLayout YM;
    TextView Year_TV;
    ImageView yarrows;


    int NowYearNOW, NowMonthNOW, currentDateNum1;
    int height;
    ConstraintLayout topPanel;
    String currentTimeM;
    String currentTimeH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        NowYearNOW = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        NowMonthNOW = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));

        yarrows = findViewById(R.id.YArrows);
        Year_TV = findViewById(R.id.Year_TV);
        YM = findViewById(R.id.Year_Lay);
        Month_lay = findViewById(R.id.Month_RV_Layout);
        AI = findViewById(R.id.AI);
        arrow = findViewById(R.id.arrow);
        progressBar = findViewById(R.id.progressBar);
        HintText = findViewById(R.id.HintText);
        HintTextTask = findViewById(R.id.HintTextTask);
        progressBarTask = findViewById(R.id.progressBarTask);
        Bundle extras1 = getIntent().getExtras();
        taskCompleted = findViewById(R.id.taskCompleted);
        pendingTaskCount = findViewById(R.id.pendingTasksCount);
        liveUpdate = findViewById(R.id.liveUpdate);
        Month_TV = findViewById(R.id.Month_TV);
        topPanel = findViewById(R.id.topPanel);

        currentDateNum1 = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
        currentTimeH = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        currentTimeM = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
        int NowTimeHour = Integer.parseInt(currentTimeH);
        int NowTimeMinute = Integer.parseInt(currentTimeM);
        NowTime = NowTimeHour * 60 + NowTimeMinute;

        Download();

        for (String month : Months) {
            Month_Model.add(new Month_Model(month));
        }

        for (int year : years) {
            Year_Model.add(new Year_Model(year));
        }

        TextTaskText = findViewById(R.id.TaskText);


        Time1 = findViewById(R.id.Time_1);
        Date_Today = findViewById(R.id.Date_Today);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUser = extras.getParcelable("auth");

        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        progressBar.setVisibility(View.VISIBLE);
        HintText.setVisibility(View.GONE);


        days_recyclerview = findViewById(R.id.RV_Days);


        days_recyclerview.setAdapter(days_adapter);

        Months_RV = findViewById(R.id.Month_RV);

        Months_RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Months_RV.setAdapter(month_adapter);


        Years_RV = findViewById(R.id.Year_RV);

        Years_RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Years_RV.setAdapter(year_adapter);


        //days_recyclerview.setLayoutManager(new LinearLayoutManager(this));


        tasks_recyclerview = findViewById(R.id.RV_Tasks);

        tasks_adapter.notifyDataSetChanged();


        tasks_recyclerview.setAdapter(tasks_adapter);
        tasks_recyclerview.setLayoutManager(new LinearLayoutManager(this));


        CheckHintTasksText();


    }


    int NowYear;
    int NowMonth;

    public void Download() {


        NowYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        NowMonth = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));

        Month_TV.setText(Months[NowMonth - 1]);
        Year_TV.setText("" + NowYear);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("daysModel").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                Days_ModelALL.add(new Days_Model(queryDocumentSnapshot.getString("DocId"), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Year", int.class), queryDocumentSnapshot.get("Month", int.class), queryDocumentSnapshot.getString("date"), queryDocumentSnapshot.getString("day_ow"), queryDocumentSnapshot.get("calendar", Calendar.class)));

                            }

                            for (int i = 0; i < Days_ModelALL.size(); i++) {
                                int NumDate = GetInt(Days_ModelALL.get(i).Date);
                                int NumYear = Days_ModelALL.get(i).Year;
                                int NumMonth = Days_ModelALL.get(i).Month;


                                if (NumDate < currentDateNum1 && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                                    Days_ModelALL.get(i).color = 2;

                                } else {

                                    Days_ModelALL.get(i).color = 0;

                                }
                            }


                            Days_ModelY.clear();
                            for (Days_Model daysModelALL : Days_ModelALL) {

                                if (daysModelALL.Year == NowYear) {
                                    Days_ModelY.add(new Days_Model(daysModelALL.id, daysModelALL.color, daysModelALL.Year, daysModelALL.Month, daysModelALL.Date, daysModelALL.Day_OW, daysModelALL.calendar));
                                }
                            }


                            Days_Model.clear();
                            for (Days_Model daysModel : Days_ModelY) {

                                if (daysModel.Month == NowMonth) {
                                    Days_Model.add(new Days_Model(daysModel.id, daysModel.color, daysModel.Year, daysModel.Month, daysModel.Date, daysModel.Day_OW, daysModel.calendar));
                                }
                            }

                            for (int zz = 0; zz < Months.length; zz++) {
                                if ((zz + 1) == NowMonth) {
                                    Months_RV.scrollToPosition(zz);
                                    Months_RV.suppressLayout(true);
                                    Years_RV.suppressLayout(true);
                                }
                            }


                            Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                                @Override
                                public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                                    return o1.Date.compareTo(o2.Date);
                                }
                            });
                            days_adapter.notifyDataSetChanged();
                            CheckHintText();
                            progressBar.setVisibility(View.GONE);
                            CheckTutorial();
                            if (!Days_Model.isEmpty()) {
                                idCopy = Days_Model.get(0).id;
                            }


                            /////////////////////

                            SetCurrentActivityView();


                            ///////////////////////////////////


                            if (!Days_Model.isEmpty()) {
                                id = Days_Model.get(0).id;
                                Days_Model.get(0).color = 1;
                                days_adapter.notifyDataSetChanged();


                                FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                                    int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                                    Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                                            queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                                            ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));

//                                                    if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                                                        Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                                    } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                                                        if (Task_Model.get(Task_Model.size() - 1).et_time_M < NowTime) {
//                                                            Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                                        } else {
//                                                            Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                                        }
//                                                    } else {
//                                                        Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                                    }


                                                }
                                                Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
                                                    @Override
                                                    public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                                        return o1.time_start.compareTo(o2.time_start);
                                                    }
                                                });
                                                tasks_adapter.notifyDataSetChanged();
                                                CheckHintTasksText();
                                                progressBarTask.setVisibility(View.GONE);


                                            }
                                        });


                                ////////////////////////////
                            } else {
                                progressBarTask.setVisibility(View.GONE);

                            }


                        }

                    });
        }


    }


    int i = 0;
    int j = 0;
    int dateNum;
    int currentDateNum;
    int countCheck = 0;
    int count = 0;
    String countCheckStr;
    String countStr;
    String id1;


    public void SetCheckedPendingView() {
        if (!Days_ModelALL.isEmpty()) {

            id1 = Days_ModelALL.get(i).id;
            dateNum = GetInt(Days_ModelALL.get(i).Date);


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseFirestore.getInstance().collection("daysModel").document(id1).collection("taskModels").whereEqualTo("userId", user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    count++;
                                    int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                    int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                    Task_Model1.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                            queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                            ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));

                                    Log.e("FOR123", "onSuccess: Task " + count);
                                    Log.e("FOR123", "Date num " + dateNum);
                                    Log.e("FOR123", "currentDateNum " + currentDateNum);


                                    if (Task_Model1.get(Task_Model1.size() - 1).checkColor == 1) {
                                        countCheck++;
                                    }
                                }
                                if (i < Days_ModelALL.size() - 1) {
                                    i++;
                                    SetCheckedPendingView();
                                } else {


                                    countCheckStr = String.valueOf(countCheck);
                                    taskCompleted.setText(countCheckStr);
                                    countStr = String.valueOf(count - countCheck);
                                    pendingTaskCount.setText(countStr);
                                }


                            }
                        });


            }
        }


    }


    public void SetCurrentActivityView() {


        String currentTimeH = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String currentTimeM = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        currentDateNum = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        String Datedate = currentDate.toString();
        countCheck = 0;
        count = 0;
        i = 0;


        for (int i = 0; i < Days_ModelALL.size(); i++) {

            int NumDate = GetInt(Days_ModelALL.get(i).Date);


            int NumYear = Days_ModelALL.get(i).Year;
            int NumMonth = Days_ModelALL.get(i).Month;


            if (NumDate < currentDateNum && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                Days_ModelALL.get(i).color = 2;

            }


            if (Objects.equals(Days_ModelALL.get(i).Date, Datedate)) {
                Date_Today.setText(Datedate);


                String id = Days_ModelALL.get(i).id;

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                        int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                        int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                        Task_Model1.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                                queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                                ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
                                    }


                                    int NowTimeHour = Integer.parseInt(currentTimeH.toString());
                                    int NowTimeMinute = Integer.parseInt(currentTimeM.toString());
                                    NowTime = NowTimeHour * 60 + NowTimeMinute;


                                    for (int j = 0; j < Task_Model1.size(); j++) {
                                        if (Task_Model1.get(j).st_time_M <= NowTime && Task_Model1.get(j).et_time_M >= NowTime) {

                                            Time1.setText(Task_Model1.get(j).time_start);
                                            String text = Task_Model1.get(j).task_text;

                                            if (text.length() < 6) {

                                                TextTaskText.setText(text);

                                            } else if (text.length() < 10) {
                                                TextTaskText.setTextSize(30);
                                                TextTaskText.setText(text);
                                            } else {
                                                TextTaskText.setTextSize(20);
                                                TextTaskText.setText(text);
                                            }

                                        }
                                    }


                                }
                            });


                }
            }

        }


        SetCheckedPendingView();


    }


    public void CheckHintText() {
        if (Days_Model.size() > 0) {
            HintText.setVisibility(View.GONE);
        } else {
            HintText.setVisibility(View.VISIBLE);
        }
    }

    public void CheckHintTasksText() {
        if (Task_Model.size() > 0) {
            HintTextTask.setVisibility(View.GONE);
        } else {
            HintTextTask.setVisibility(View.VISIBLE);
        }
    }

    public void CheckTutorial() {
        if (Days_ModelALL.isEmpty()) {
            View view = LayoutInflater.from(calendar_activity.this).inflate(R.layout.guide_text, null);
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(calendar_activity.this)
                    .setTitle("How To Use Day Buddy")
                    .setView(view)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            alertDialog.show();
        }
    }


    // Choose Time
    public void PickADate(View view) {
        MaterialDatePicker<Long> DatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        DatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                calendar = Calendar.getInstance();
                Date date = new Date(selection);
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.YEAR, date.getYear());
                calendar.set(Calendar.DATE, date.getDate());
                Date = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date(selection));
                int dateNum = Integer.parseInt(Date);
                Year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date(selection)));
                Month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date(selection)));
                boolean flag = false;
                for (int i = 0; i < Days_ModelALL.size(); i++) {
                    if (Days_ModelALL.get(i).getYear() == Year) {
                        if (Days_ModelALL.get(i).getMonth() == Month) {
                            if (Days_ModelALL.get(i).getDate().equals(Date)) {
                                flag = true;
                            }
                        }
                    }

                }
                if (Year < NowYearNOW) {
                    flag = true;
                } else if (Year == NowYearNOW) {
                    if (Month < NowMonthNOW) {
                        flag = true;
                    } else if (Month == NowMonthNOW) {
                        if (dateNum < currentDateNum1) {
                            flag = true;
                        }

                    }
                }
                if (!flag) {
                    Day_OW = new SimpleDateFormat("E", Locale.getDefault()).format(new Date(selection));
                    id = UUID.randomUUID().toString();
                    Days_ModelALL.add(new Days_Model(id, color_days, Year, Month, Date, Day_OW, calendar));
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("date", Date);
                        hashMap.put("day_ow", Day_OW);
                        hashMap.put("Position", Days_Model.size() + 1);
                        hashMap.put("Color", color_days);
                        hashMap.put("DocId", id);
                        hashMap.put("Year", Year);
                        hashMap.put("Month", Month);
                        hashMap.put("userId", user.getUid());
                        db.collection("daysModel").document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                        //db.collection("daysModel").document("daysModelId").collection("tasks").add(hashMap);
                    }


                    for (int i = 0; i < Days_ModelALL.size(); i++) {
                        int NumDate = GetInt(Days_ModelALL.get(i).Date);
                        int NumYear = Days_ModelALL.get(i).Year;
                        int NumMonth = Days_ModelALL.get(i).Month;


                        if (NumDate < currentDateNum && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                            Days_ModelALL.get(i).color = 2;

                        } else {

                            Days_ModelALL.get(i).color = 0;

                        }
                    }

                    Days_ModelY.clear();
                    for (Days_Model daysModelALL : Days_ModelALL) {

                        if (daysModelALL.Year == NowYear) {
                            Days_ModelY.add(new Days_Model(daysModelALL.id, daysModelALL.color, daysModelALL.Year, daysModelALL.Month, daysModelALL.Date, daysModelALL.Day_OW, daysModelALL.calendar));
                        }
                    }

                    Days_Model.clear();
                    for (Days_Model daysModel : Days_ModelY) {

                        if (daysModel.Month == NowMonth) {
                            Days_Model.add(new Days_Model(daysModel.id, daysModel.color, daysModel.Year, daysModel.Month, daysModel.Date, daysModel.Day_OW, daysModel.calendar));
                        }
                    }


                    if (!Days_Model.isEmpty()) {

                        Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                            @Override
                            public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                                return o1.Date.compareTo(o2.Date);
                            }
                        });

                        days_adapter.notifyDataSetChanged();
                        idCopy = id;

                        for (int i = 0; i < Days_Model.size(); i++) {

                            if (Objects.equals(Days_Model.get(i).id, id)) {

                                positionCopy = Days_Model.indexOf(Days_Model.get(i));

                                Days_Model.get(i).color = 1;
                                idCopy = id;

                                days_recyclerview.smoothScrollToPosition(Days_Model.indexOf(Days_Model.get(i)));

                            }

                        }

                        CheckHintText();


                        Task_Model.clear();


                        CheckHintText();


                        Task_Model.clear();
                        if (user != null) {
                            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                                int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                                Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                                        queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                                        ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
                                            }
                                            Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
                                                @Override
                                                public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                                    return o1.time_start.compareTo(o2.time_start);
                                                }
                                            });
                                            tasks_adapter.notifyDataSetChanged();
                                            CheckHintTasksText();
                                            progressBarTask.setVisibility(View.GONE);

                                            SetCurrentActivityView();
                                        }
                                    });


                        }

                    }
                    CheckHintText();


                } else {
                    Toast.makeText(calendar_activity.this, "You Already Created a TimeTable For  : " + Date + ", Or Passed That Day", Toast.LENGTH_SHORT).show();
                }


            }
        });
        DatePicker.show(getSupportFragmentManager(), "tag");


    }


    public void signOut(View view) {

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(calendar_activity.this)
                .setTitle("Sign Out")
                .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(calendar_activity.this, LogIn.class);
                        startActivity(intent);
                        finish();


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();


    }


    @Override
    public void onItemClicked(int position) {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//        intent.putExtra("Position", position);
//        intent.putExtra("day", Days_Model.get(position).getDay_OW());
//        intent.putExtra("calendar", Days_Model.get(position).getCalendar());
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        intent.putExtra("id", Days_Model.get(position).id);
//
//        startActivity(intent);


        id = Days_Model.get(position).id;

        positionCopy = position;

        if (Objects.equals(id, idCopy)) {


            Intent intent = new Intent(calendar_activity.this, map_waypoints.class);
            intent.putExtra("id", id);
            startActivity(intent);

        } else {
            for (int i = 0; i < Days_ModelALL.size(); i++) {
                int NumDate = GetInt(Days_ModelALL.get(i).Date);
                int NumYear = Days_ModelALL.get(i).Year;
                int NumMonth = Days_ModelALL.get(i).Month;


                if (NumDate < currentDateNum && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                    Days_ModelALL.get(i).color = 2;

                } else {

                    Days_ModelALL.get(i).color = 0;

                }
            }
            Days_ModelY.clear();
            for (Days_Model daysModelALL : Days_ModelALL) {

                if (daysModelALL.Year == NowYear) {
                    Days_ModelY.add(new Days_Model(daysModelALL.id, daysModelALL.color, daysModelALL.Year, daysModelALL.Month, daysModelALL.Date, daysModelALL.Day_OW, daysModelALL.calendar));
                }
            }


            Days_Model.clear();
            for (Days_Model daysModel : Days_ModelY) {

                if (daysModel.Month == NowMonth) {
                    Days_Model.add(new Days_Model(daysModel.id, daysModel.color, daysModel.Year, daysModel.Month, daysModel.Date, daysModel.Day_OW, daysModel.calendar));
                }
            }

            Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                @Override
                public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                    return o1.Date.compareTo(o2.Date);
                }
            });


            Days_Model.get(position).color = 1;

            days_adapter.notifyDataSetChanged();
            Task_Model.clear();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                    int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                    Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                            queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                            ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));

//                                    if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                                        Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                    } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                                        if (Task_Model.get(Task_Model.size() - 1).et_time_M < NowTime) {
//                                            Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                        } else {
//                                            Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                        }
//                                    } else {
//                                        Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                    }
                                }
                                idCopy = id;
                                Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
                                    @Override
                                    public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                        return o1.time_start.compareTo(o2.time_start);
                                    }
                                });
                                tasks_adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                CheckHintTasksText();
                            }
                        });
            }


        }
    }

    String id2;
    int positionDelete;


    ArrayList<Task_Model> taskModels_delete = new ArrayList<>();

    @Override
    public void onItemLongClick(int position) {

        MaterialDatePicker<Long> DatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setPositiveButtonText("Save Changes")
                .setNegativeButtonText("Delete Day")
                .build();

        DatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date(selection));
                Year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date(selection)));
                boolean flag = false;
                for (int i = 0; i < Days_Model.size(); i++) {
                    if (Days_Model.get(i).getDate().equals(Date)) {
                        flag = true;
                    }

                }
                if (!flag) {
                    Day_OW = new SimpleDateFormat("EE", Locale.getDefault()).format(new Date(selection));
                    Days_Model.set(position, new Days_Model(Days_Model.get(position).id, color_days, Year, Month, Date, Day_OW, calendar));
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("date", Date);
                        hashMap.put("day_ow", Day_OW);
                        hashMap.put("Position", position);
                        hashMap.put("Color", color_days);
                        hashMap.put("Year", Year);
                        hashMap.put("Month", Month);
                        hashMap.put("DocId", Days_Model.get(position).id);
                        hashMap.put("calendar", Days_Model.get(position).calendar);
                        hashMap.put("userId", user.getUid());


                        db.collection("daysModel").document(Days_Model.get(position).id).update(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("aaaa", e.getMessage());
                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        //db.collection("daysModel").document("daysModelId").collection("tasks").add(hashMap);
                    }


                    for (int i = 0; i < Days_ModelALL.size(); i++) {
                        int NumDate = GetInt(Days_ModelALL.get(i).Date);
                        int NumYear = Days_ModelALL.get(i).Year;
                        int NumMonth = Days_ModelALL.get(i).Month;


                        if (NumDate < currentDateNum && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                            Days_ModelALL.get(i).color = 2;

                        } else {

                            Days_ModelALL.get(i).color = 0;

                        }
                    }
                    Days_ModelY.clear();
                    for (Days_Model daysModelALL : Days_ModelALL) {

                        if (daysModelALL.Year == NowYear) {
                            Days_ModelY.add(new Days_Model(daysModelALL.id, daysModelALL.color, daysModelALL.Year, daysModelALL.Month, daysModelALL.Date, daysModelALL.Day_OW, daysModelALL.calendar));
                        }
                    }

                    Days_Model.clear();
                    for (Days_Model daysModel : Days_ModelY) {

                        if (daysModel.Month == NowMonth) {
                            Days_Model.add(new Days_Model(daysModel.id, daysModel.color, daysModel.Year, daysModel.Month, daysModel.Date, daysModel.Day_OW, daysModel.calendar));
                        }
                    }


                    if (!Days_Model.isEmpty()) {
                        for (int i = 0; i < Days_Model.size(); i++) {

                            int NumDate = GetInt(Days_Model.get(i).Date);
                            int NumYear = Days_Model.get(i).Year;
                            int NumMonth = Days_Model.get(i).Month;


                            if (NumDate < currentDateNum && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                                Days_Model.get(i).color = 2;

                            } else {

                                Days_Model.get(i).color = 0;

                            }

                            if (Objects.equals(Days_Model.get(i).id, id)) {

                                Days_Model.get(i).color = 0;
                            }

                        }


                        Days_Model.get(Days_Model.size() - 1).color = 1;
                        id = Days_Model.get(Days_Model.size() - 1).id;
                        idCopy = id;
                        Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                            @Override
                            public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                                return o1.Date.compareTo(o2.Date);
                            }
                        });
                        for (int i = 0; i < Days_Model.size(); i++) {


                            if (Objects.equals(Days_Model.get(i).id, id)) {

                                positionCopy = Days_Model.indexOf(Days_Model.get(i));

                                days_recyclerview.smoothScrollToPosition(Days_Model.indexOf(Days_Model.get(i)));

                            }

                        }

                        CheckHintText();


                        Task_Model.clear();
                        if (user != null) {
                            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                                int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                                Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                                        queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                                        ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
                                            }
                                            Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
                                                @Override
                                                public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                                    return o1.time_start.compareTo(o2.time_start);
                                                }
                                            });
                                            tasks_adapter.notifyDataSetChanged();
                                            CheckHintTasksText();
                                            progressBarTask.setVisibility(View.GONE);

                                            SetCurrentActivityView();
                                        }
                                    });
                        }


                        CheckHintText();
                    }


                } else {
                    Toast.makeText(calendar_activity.this, "You Already Created a TimeTable For" + Date, Toast.LENGTH_SHORT).show();
                }

            }
        });
        DatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int zz = 0; zz < Days_ModelALL.size(); zz++) {
                    if (Objects.equals(Days_ModelALL.get(zz).id, Days_Model.get(position).id)) {
                        id2 = Days_Model.get(position).id;
                        positionDelete = zz;
                        break;
                    }
                }


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseFirestore.getInstance().collection("daysModel").document(id2).collection("taskModels").whereEqualTo("userId", user.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                        int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                        int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                        taskModels_delete.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                                queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                                ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));


                                    }


                                    delete_collection = 0;
                                    delete_collection(id2);
                                    FirebaseFirestore.getInstance().collection("daysModel").document(id2).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(calendar_activity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(calendar_activity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Days_Model.remove(position);
                                    Days_ModelALL.remove(positionDelete);
                                    days_adapter.notifyItemRemoved(position);
                                    CheckHintText();
                                    SetCurrentActivityView();
                                    Task_Model.clear();
                                    tasks_adapter.notifyDataSetChanged();
                                    CheckHintTasksText();

                                    if (Days_Model.size() > 1) {
                                        id = Days_Model.get(Days_Model.size() - 1).id;
                                        for (int i = 0; i < Days_ModelALL.size(); i++) {
                                            int NumDate = GetInt(Days_ModelALL.get(i).Date);
                                            int NumYear = Days_ModelALL.get(i).Year;
                                            int NumMonth = Days_ModelALL.get(i).Month;


                                            if (NumDate < currentDateNum && NumYear <= NowYearNOW && NumMonth <= NowMonthNOW) {

                                                Days_ModelALL.get(i).color = 2;

                                            } else {

                                                Days_ModelALL.get(i).color = 0;

                                            }
                                        }


                                        days_adapter.notifyDataSetChanged();

                                        Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                                            @Override
                                            public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                                                return o1.Date.compareTo(o2.Date);
                                            }
                                        });

                                        days_adapter.notifyDataSetChanged();
                                        CheckHintText();
                                        if (Days_Model.size() > 1) {
                                            days_recyclerview.smoothScrollToPosition(Days_Model.indexOf(Days_Model.get(Days_Model.size() - 1)));
                                        }

                                        id = Days_Model.get(Days_Model.size() - 1).id;
                                        Days_Model.get(Days_Model.size() - 1).color = 1;

                                        Task_Model.clear();
                                        if (user != null) {
                                            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                                int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                                                int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                                                Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                                                        queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                                                        ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
                                                            }
                                                            Task_Model.sort(new Comparator<com.example.daybuddy.Task_Model>() {
                                                                @Override
                                                                public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                                                    return o1.time_start.compareTo(o2.time_start);
                                                                }
                                                            });
                                                            tasks_adapter.notifyDataSetChanged();
                                                            CheckHintTasksText();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    }


                                }
                            });
                }


            }
        });
        DatePicker.show(getSupportFragmentManager(), "tag");


    }

    int delete_collection = 0;

    public void delete_collection(String idd) {
        if (!taskModels_delete.isEmpty()) {
            FirebaseFirestore.getInstance().collection("daysModel").document(idd).collection("taskMmodels")
                    .document(taskModels_delete.get(delete_collection).DocID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            delete_collection(idd);
                        }
                    });
        }

    }


    ////////////////////////////MAIN_ACTIVITY_CLASS///////////////////////////////////


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

                if (H < 10) {
                    Hour = "0" + H;
                }
                if (M < 10) {
                    Minute = "0" + M;
                }
                St_time_M = H * 60 + M;


                St_Time = Hour + ":" + Minute;
                ST_View.setText("" + St_Time);

                calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, Days_Model.get(positionCopy).Year);
                calendar.set(Calendar.MONTH, Days_Model.get(positionCopy).Month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, GetInt(Days_Model.get(positionCopy).Date));
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

                if (H < 10) {
                    Hour = "0" + H;
                }
                if (M < 10) {
                    Minute = "0" + M;
                }

                Et_time_M = H * 60 + M;

                Et_Time = Hour + ":" + Minute;
                ET_View.setText(Et_Time);


            }
        });
        timePicker.show(getSupportFragmentManager(), "tag");


    }

    public void edittext(View view) {
        View view2 = LayoutInflater.from(calendar_activity.this).inflate(R.layout.input_text_dialog_layout, null);
        TextInputEditText editText = view2.findViewById(R.id.edittext);
        TextView task_text = view1.findViewById(R.id.Task_text_View);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(calendar_activity.this)
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


    public String Task_Text_for_notify;

    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to set the exact alarm
            } else {
                // Permission denied, gracefully degrade your app's experience
            }
        }
    }


    private void Add_Task_To_Model() {

        boolean flag = false;

        if (!Days_Model.isEmpty()) {

            if (Days_Model.get(positionCopy).Year < NowYearNOW) {
                flag = true;
            } else if (Days_Model.get(positionCopy).Year == NowYearNOW) {
                if (Days_Model.get(positionCopy).Month < NowMonthNOW) {
                    flag = true;
                } else if (Days_Model.get(positionCopy).Month == NowMonthNOW) {
                    if (Integer.parseInt(Days_Model.get(positionCopy).Date) < currentDateNum1) {
                        flag = true;
                    }

                }
            }

            if (!flag) {


                String idNew = UUID.randomUUID().toString();

                checkColor = 0;


                Task_Model.add(new Task_Model(idNew, checkColor, color, visibility, Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));
//                SetAlarm();
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
                    hashMap.put("DocID", idNew);
                    hashMap.put("Color", color);
                    hashMap.put("CheckColor", checkColor);
                    hashMap.put("Visibility", visibility);
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


                Task_Text_for_notify = Task_text;


                setNotificationAlarm(Task_Text_for_notify);
                alarm(calendar);


                tasks_adapter.notifyDataSetChanged();
                CheckHintTasksText();
                tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());


                SetCurrentActivityView();


            } else {
                Toast.makeText(this, "You Already Passed that Day", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void Add_Task_Final() {
        if (!Task_Model.isEmpty()) {
//            if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                checkColor = 1;
//            } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                if (Et_time_M < NowTime) {
//                    checkColor = 1;
//                } else {
//                    checkColor = 0;
//                }
//            } else {
//                checkColor = 0;
//            }
            visibility = 1;
            LatLng origin = new LatLng(Task_Model.get(Task_Model.size() - 1).latitude, Task_Model.get(Task_Model.size() - 1).longitude);
            LatLng destination = new LatLng(latitude, longitude);
            long departureTime = calendar.getTimeInMillis() / 1000;
            calendar_activity.DestinationTime travelTime = new calendar_activity.DestinationTime(origin, destination, travelMode);
            travelTime.execute();


            Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int Duration = getTime(ArriveDuration);
                    if (St_time_M - Task_Model.get(Task_Model.size() - 1).et_time_M > Duration) {
                        color = 0;
                        Add_Task_To_Model();
                        Toast.makeText(calendar_activity.this, "You will be on Time", Toast.LENGTH_SHORT).show();
                        progressBarTask.setVisibility(View.GONE);

                    } else {
                        color = 1;
                        Add_Task_To_Model();
                        progressBarTask.setVisibility(View.GONE);
                        Toast.makeText(calendar_activity.this, "You will be Late", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            progressBarTask.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, 2000);
        } else {
//            if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                checkColor = 1;
//            } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                if (Et_time_M < NowTime) {
//                    checkColor = 1;
//                } else {
//                    checkColor = 0;
//                }
//            } else {
//                checkColor = 0;
//            }
            visibility = 0;
            Add_Task_To_Model();
        }
    }

    public int GetInt(String a) {
        String input = a;

        // Define a pattern to match digits
        Pattern pattern = Pattern.compile("\\d+");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);

        // Check if a match is found
        if (matcher.find()) {
            // Get the matched substring
            String numberStr = matcher.group();

            // Convert the matched substring to an integer
            int number = Integer.parseInt(numberStr);

            // Print the integer value
            System.out.println("Extracted integer: " + number);
            return number;
        } else {
            System.out.println("No integer found in the input string.");
            return -1;
        }
    }

    public void Add_Task(View view) {
        if (!Days_Model.isEmpty()) {
            view1 = LayoutInflater.from(calendar_activity.this).inflate(R.layout.add_task, null);
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(calendar_activity.this)
                    .setTitle("Add Task")
                    .setView(view1)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            if (longitude == null) {
                                Toast.makeText(calendar_activity.this, "Select Locations", Toast.LENGTH_SHORT).show();
                            } else {


                                if (St_time_M >= Et_time_M) {
                                    Toast.makeText(calendar_activity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
                                } else if (Task_Model.size() > 0) {
                                    if (Task_Model.get(Task_Model.size() - 1).et_time_M > St_time_M) {
                                        Toast.makeText(calendar_activity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Add_Task_Final();
                                    }
                                } else {
                                    Add_Task_Final();
                                }


                                dialogInterface.dismiss();

                            }

                        }
                    }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            alertDialog.show();
        } else {
            Toast.makeText(calendar_activity.this, "Add Day Before adding Task", Toast.LENGTH_SHORT).show();
        }
    }

    public void pickaPlace(View view) {


        Intent intent = new Intent(calendar_activity.this, places.class);
//        startActivity(intent);
        placesResultLauncher.launch(intent);

    }

    public void alarm(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver1.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }


    private ActivityResultLauncher<Intent> placesResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        if (data != null) {
                            TextView ST_View = view1.findViewById(R.id.location);
                            latitude = data.getDoubleExtra("latitude", 0.0);
                            longitude = data.getDoubleExtra("longitude", 0.0);
                            address = data.getStringExtra("address");
                            Title = data.getStringExtra("title");
                            travelMode = data.getStringExtra("travelMode");
                            ST_View.setText(Title);


                        } else {
                            Log.d(TAG, "onActivityResult: cancelled");
                            Toast.makeText(calendar_activity.this, "Cancelled", Toast.LENGTH_SHORT).show();


                        }
                    }
                }
            }
    );


    void ifDate(int position) {

        if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {

            if (Task_Model.get(position).checkColor == 1) {
                Task_Model.get(position).checkColor = 0;
                countCheck--;
                countCheckStr = String.valueOf(countCheck);
                taskCompleted.setText(countCheckStr);
                countStr = String.valueOf(count - countCheck);
                pendingTaskCount.setText(countStr);
            } else {
                Task_Model.get(position).checkColor = 1;
                countCheck++;
                countCheckStr = String.valueOf(countCheck);
                taskCompleted.setText(countCheckStr);
                countStr = String.valueOf(count - countCheck);
                pendingTaskCount.setText(countStr);
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("day_ow", Day_OW);
                hashMap.put("Task_text", Task_Model.get(position).task_text);
                hashMap.put("ST_Time", Task_Model.get(position).time_start);
                hashMap.put("ET_Time", Task_Model.get(position).time_end);
                hashMap.put("ET_time_M", Task_Model.get(position).et_time_M);
                hashMap.put("ST_time_M", Task_Model.get(position).st_time_M);
                hashMap.put("address", Task_Model.get(position).location);
                hashMap.put("latitude", Task_Model.get(position).latitude);
                hashMap.put("longitude", Task_Model.get(position).longitude);
                hashMap.put("DocID", Task_Model.get(position).DocID);
                hashMap.put("Color", Task_Model.get(position).color);
                hashMap.put("CheckColor", Task_Model.get(position).checkColor);
                hashMap.put("Visibility", Task_Model.get(position).visibility);
                hashMap.put("userId", user.getUid());
                db.collection("daysModel").document(idCopy).collection("taskModels").document(Task_Model.get(position).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            }


        } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
            if (Task_Model.get(position).et_time_M < NowTime) {
                if (Task_Model.get(position).checkColor == 1) {
                    Task_Model.get(position).checkColor = 0;
                    countCheck--;
                    countCheckStr = String.valueOf(countCheck);
                    taskCompleted.setText(countCheckStr);
                    countStr = String.valueOf(count - countCheck);
                    pendingTaskCount.setText(countStr);
                } else {
                    Task_Model.get(position).checkColor = 1;
                    countCheck++;
                    countCheckStr = String.valueOf(countCheck);
                    taskCompleted.setText(countCheckStr);
                    countStr = String.valueOf(count - countCheck);
                    pendingTaskCount.setText(countStr);

                }


                FirebaseFirestore db = FirebaseFirestore.getInstance();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("day_ow", Day_OW);
                    hashMap.put("Task_text", Task_Model.get(position).task_text);
                    hashMap.put("ST_Time", Task_Model.get(position).time_start);
                    hashMap.put("ET_Time", Task_Model.get(position).time_end);
                    hashMap.put("ET_time_M", Task_Model.get(position).et_time_M);
                    hashMap.put("ST_time_M", Task_Model.get(position).st_time_M);
                    hashMap.put("address", Task_Model.get(position).location);
                    hashMap.put("latitude", Task_Model.get(position).latitude);
                    hashMap.put("longitude", Task_Model.get(position).longitude);
                    hashMap.put("DocID", Task_Model.get(position).DocID);
                    hashMap.put("Color", Task_Model.get(position).color);
                    hashMap.put("CheckColor", Task_Model.get(position).checkColor);
                    hashMap.put("Visibility", Task_Model.get(position).visibility);
                    hashMap.put("userId", user.getUid());
                    db.collection("daysModel").document(idCopy).collection("taskModels").document(Task_Model.get(position).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                }


            } else {
                Task_Model.get(position).checkColor = 0;
            }


        } else {
            Task_Model.get(position).checkColor = 0;
        }

    }


    @Override
    public void onItemClickedTasks(int position) {

        if (Days_Model.get(positionCopy).Month == NowMonthNOW) {
            ifDate(position);
        } else if (Days_Model.get(positionCopy).Month < NowMonthNOW) {
            if (Task_Model.get(position).checkColor == 1) {
                Task_Model.get(position).checkColor = 0;
                countCheck--;
                countCheckStr = String.valueOf(countCheck);
                taskCompleted.setText(countCheckStr);
                countStr = String.valueOf(count - countCheck);
                pendingTaskCount.setText(countStr);
            } else {
                Task_Model.get(position).checkColor = 1;
                countCheck++;
                countCheckStr = String.valueOf(countCheck);
                taskCompleted.setText(countCheckStr);
                countStr = String.valueOf(count - countCheck);
                pendingTaskCount.setText(countStr);
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("day_ow", Day_OW);
                hashMap.put("Task_text", Task_Model.get(position).task_text);
                hashMap.put("ST_Time", Task_Model.get(position).time_start);
                hashMap.put("ET_Time", Task_Model.get(position).time_end);
                hashMap.put("ET_time_M", Task_Model.get(position).et_time_M);
                hashMap.put("ST_time_M", Task_Model.get(position).st_time_M);
                hashMap.put("address", Task_Model.get(position).location);
                hashMap.put("latitude", Task_Model.get(position).latitude);
                hashMap.put("longitude", Task_Model.get(position).longitude);
                hashMap.put("DocID", Task_Model.get(position).DocID);
                hashMap.put("Color", Task_Model.get(position).color);
                hashMap.put("CheckColor", Task_Model.get(position).checkColor);
                hashMap.put("Visibility", Task_Model.get(position).visibility);
                hashMap.put("userId", user.getUid());
                db.collection("daysModel").document(idCopy).collection("taskModels").document(Task_Model.get(position).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            }
        } else {
            Toast.makeText(this, "You can't check this task yet", Toast.LENGTH_SHORT).show();
        }


        tasks_adapter.notifyDataSetChanged();


    }

    @Override
    public void onItemLongClickTasks(int position) {


        St_time_M = Task_Model.get(position).st_time_M;
        Et_time_M = Task_Model.get(position).et_time_M;
        checkColor = Task_Model.get(position).checkColor;
        color = Task_Model.get(position).color;
        visibility = Task_Model.get(position).visibility;
        Task_text = Task_Model.get(position).task_text;
        St_Time = Task_Model.get(position).time_start;
        Et_Time = Task_Model.get(position).time_end;
        latitude = Task_Model.get(position).latitude;
        longitude = Task_Model.get(position).longitude;
        address = Task_Model.get(position).location;

        calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, Days_Model.get(positionCopy).Year);
        calendar.set(Calendar.MONTH, Days_Model.get(positionCopy).Month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, GetInt(Days_Model.get(positionCopy).Date));
        calendar.set(Calendar.HOUR_OF_DAY, Task_Model.get(position).st_time_M / 60);
        calendar.set(Calendar.MINUTE, Task_Model.get(position).st_time_M % 60);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Position_BackUp = position;

        view1 = LayoutInflater.from(calendar_activity.this).inflate(R.layout.add_task, null);
        TextView St_Time1 = view1.findViewById(R.id.Start_time_view);
        TextView Et_Time1 = view1.findViewById(R.id.End_Time_view);
        TextView TaskText1 = view1.findViewById(R.id.Task_text_View);
        TextView Location1 = view1.findViewById(R.id.location);

        St_Time1.setText(Task_Model.get(position).time_start);
        Et_Time1.setText(Task_Model.get(position).time_end);
        TaskText1.setText(Task_Model.get(position).task_text);
        Location1.setText(Task_Model.get(position).location);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(calendar_activity.this)
                .setTitle("Edit Task")
                .setView(view1)
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Task_Model.size() > 1) {


                            if (St_time_M >= Et_time_M) {
                                Toast.makeText(calendar_activity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();
                            } else if (!Task_Model.isEmpty()) {
                                if (Task_Model.get(position - 1).et_time_M > St_time_M) {
                                    Toast.makeText(calendar_activity.this, "Wrong start and end times", Toast.LENGTH_SHORT).show();

                                } else {
                                    Change_Task_Final();
                                }
                            } else {
                                Change_Task_Final();
                            }
                        } else {
                            Change_Task_Final();
                        }


                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("Delete Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(position).DocID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(calendar_activity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(calendar_activity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (position == 0) {
                            if (Task_Model.size() > 1) {
                                Task_Model.get(position + 1).visibility = 0;
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("day_ow", Day_OW);
                                    hashMap.put("Task_text", Task_Model.get(position + 1).task_text);
                                    hashMap.put("ST_Time", Task_Model.get(position + 1).time_start);
                                    hashMap.put("ET_Time", Task_Model.get(position + 1).time_end);
                                    hashMap.put("ET_time_M", Task_Model.get(position + 1).et_time_M);
                                    hashMap.put("ST_time_M", Task_Model.get(position + 1).st_time_M);
                                    hashMap.put("address", Task_Model.get(position + 1).location);
                                    hashMap.put("latitude", Task_Model.get(position + 1).latitude);
                                    hashMap.put("longitude", Task_Model.get(position + 1).longitude);
                                    hashMap.put("DocID", Task_Model.get(position + 1).DocID);
                                    hashMap.put("Color", Task_Model.get(position + 1).color);
                                    hashMap.put("CheckColor", checkColor);
                                    hashMap.put("Visibility", Task_Model.get(position + 1).visibility);
                                    hashMap.put("userId", user.getUid());
                                    db.collection("daysModel").document(idCopy).collection("taskModels").document(Task_Model.get(position + 1).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                }
                            }
                            tasks_adapter.notifyDataSetChanged();
                            Task_Model.remove(position);
                            tasks_adapter.notifyItemRemoved(position);
                            CheckHintTasksText();
                            SetCurrentActivityView();

                        } else if (Task_Model.size() - 1 > position) {
                            if (Task_Model.size() > 1) {
                                LatLng origin = new LatLng(Task_Model.get(position - 1).latitude, Task_Model.get(position - 1).longitude);
                                LatLng destination = new LatLng(Task_Model.get(position + 1).latitude, Task_Model.get(position + 1).longitude);
                                long departureTime = calendar.getTimeInMillis() / 1000;
                                DestinationTime travelTime = new DestinationTime(origin, destination, travelMode);
                                travelTime.execute();


                                Handler handler = new Handler();

                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        int Duration = getTime(ArriveDuration);
                                        if (Task_Model.get(position + 1).st_time_M - Task_Model.get(position - 1).et_time_M > Duration) {
                                            Task_Model.get(position + 1).color = 0;
                                            Add_Task_To_Model();
                                            Toast.makeText(calendar_activity.this, "You will be on Time", Toast.LENGTH_SHORT).show();
                                            progressBarTask.setVisibility(View.GONE);
                                            Task_Model.remove(position);
                                            tasks_adapter.notifyItemRemoved(position);
                                            CheckHintTasksText();
                                            SetCurrentActivityView();


                                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (user != null) {
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("day_ow", Day_OW);
                                                hashMap.put("Task_text", Task_Model.get(position + 1).task_text);
                                                hashMap.put("ST_Time", Task_Model.get(position + 1).time_start);
                                                hashMap.put("ET_Time", Task_Model.get(position + 1).time_end);
                                                hashMap.put("ET_time_M", Task_Model.get(position + 1).et_time_M);
                                                hashMap.put("ST_time_M", Task_Model.get(position + 1).st_time_M);
                                                hashMap.put("address", Task_Model.get(position + 1).location);
                                                hashMap.put("latitude", Task_Model.get(position + 1).latitude);
                                                hashMap.put("longitude", Task_Model.get(position + 1).longitude);
                                                hashMap.put("DocID", Task_Model.get(position + 1).DocID);
                                                hashMap.put("Color", Task_Model.get(position + 1).color);
                                                hashMap.put("CheckColor", Task_Model.get(position + 1).checkColor);
                                                hashMap.put("Visibility", Task_Model.get(position + 1).visibility);
                                                hashMap.put("userId", user.getUid());
                                                db.collection("daysModel").document(idCopy).collection("taskModels").document(Task_Model.get(position + 1).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                            }


                                        } else {
                                            Task_Model.get(position + 1).color = 1;
                                            Add_Task_To_Model();
                                            progressBarTask.setVisibility(View.GONE);
                                            Toast.makeText(calendar_activity.this, "You will be Late", Toast.LENGTH_SHORT).show();
                                            Task_Model.remove(position);
                                            tasks_adapter.notifyItemRemoved(position);
                                            CheckHintTasksText();
                                            SetCurrentActivityView();


                                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (user != null) {
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("day_ow", Day_OW);
                                                hashMap.put("Task_text", Task_Model.get(position + 1).task_text);
                                                hashMap.put("ST_Time", Task_Model.get(position + 1).time_start);
                                                hashMap.put("ET_Time", Task_Model.get(position + 1).time_end);
                                                hashMap.put("ET_time_M", Task_Model.get(position + 1).et_time_M);
                                                hashMap.put("ST_time_M", Task_Model.get(position + 1).st_time_M);
                                                hashMap.put("address", Task_Model.get(position + 1).location);
                                                hashMap.put("latitude", Task_Model.get(position + 1).latitude);
                                                hashMap.put("longitude", Task_Model.get(position + 1).longitude);
                                                hashMap.put("DocID", Task_Model.get(position + 1).DocID);
                                                hashMap.put("Color", Task_Model.get(position + 1).color);
                                                hashMap.put("CheckColor", Task_Model.get(position + 1).checkColor);
                                                hashMap.put("Visibility", Task_Model.get(position + 1).visibility);
                                                hashMap.put("userId", user.getUid());
                                                db.collection("daysModel").document(idCopy).collection("taskModels").document(Task_Model.get(position + 1).DocID).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                            }


                                        }
                                    }
                                };
                                progressBarTask.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable, 2000);


                            }
                        } else {
                            Task_Model.remove(position);
                            tasks_adapter.notifyItemRemoved(position);
                            CheckHintTasksText();
                            SetCurrentActivityView();
                        }
                    }
                }).create();
        alertDialog.show();


    }

    public int getTime(String time) {
        String timeStr = time;

        if (timeStr != null) {
            // Split the string by spaces and extract hours and minutes
            String[] timeParts = timeStr.split("\\s+");
            int days = 0, hours = 0, minutes = 0;

            for (int i = 0; i < timeParts.length; i += 2) {
                int value = Integer.parseInt(timeParts[i]);
                String unit = timeParts[i + 1];

                if (unit.equals("days") || unit.equals("day")) {
                    days = value;
                } else if (unit.equals("hours") || unit.equals("hour")) {
                    hours = value;
                } else if (unit.equals("mins") || unit.equals("min")) {
                    minutes = value;
                }
            }

            // Calculate total time in minutes
            int totalMinutes = days * 24 * 60 + hours * 60 + minutes;

            return totalMinutes;
        } else {
            return 0;
        }

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Day Buddy";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Day Buddy", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void SetAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(calendar_activity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(calendar_activity.this, 0, intent, PendingIntent.FLAG_MUTABLE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void setNotificationAlarm(String notificationText) {

        // Create an Intent to launch the BroadcastReceiver
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.NOTIFICATION_TEXT_KEY, notificationText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }


    boolean flagx;

    private void Change_Task_In_Model() {
        String idNew = UUID.randomUUID().toString();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(Position_BackUp).DocID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


                if (Days_Model.get(positionCopy).Year < NowYearNOW) {
                    flagx = true;
                } else if (Days_Model.get(positionCopy).Year == NowYearNOW) {
                    if (Days_Model.get(positionCopy).Month < NowMonthNOW) {
                        flagx = true;
                    } else if (Days_Model.get(positionCopy).Month == NowMonthNOW) {
                        if (Integer.parseInt(Days_Model.get(positionCopy).Date) < currentDateNum1) {
                            flagx = true;
                        }

                    }
                }

                if (!flagx) {

                    Task_Model.set(Position_BackUp, new Task_Model(idNew, checkColor, color, visibility, Task_text, address, St_Time, Et_Time, St_time_M, Et_time_M, latitude, longitude));
//                SetAlarm();

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
                        hashMap.put("DocID", idNew);
                        hashMap.put("Color", color);
                        hashMap.put("CheckColor", checkColor);
                        hashMap.put("Visibility", visibility);
                        hashMap.put("userId", user.getUid());
                        db.collection("daysModel").document(id).collection("taskModels").document(Task_Model.get(Position_BackUp).DocID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    CheckHintTasksText();
                    tasks_recyclerview.smoothScrollToPosition(tasks_adapter.getItemCount());
                    SetCurrentActivityView();

                    Task_Text_for_notify = Task_text;


                    setNotificationAlarm(Task_Text_for_notify);
                } else {
                    Toast.makeText(calendar_activity.this, "You Already Passed that Day", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    public void Change_Task_Final() {
        if (Task_Model.size() > 1) {
//            if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                checkColor = 1;
//            } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                if (Et_time_M < NowTime) {
//                    checkColor = 1;
//                } else {
//                    checkColor = 0;
//                }
//            } else {
//                checkColor = 0;
//            }
            visibility = 1;

            LatLng origin = new LatLng(Task_Model.get(Position_BackUp - 1).latitude, Task_Model.get(Position_BackUp - 1).longitude);
            LatLng destination = new LatLng(latitude, longitude);
            long departureTime = calendar.getTimeInMillis() / 1000;
            calendar_activity.DestinationTime travelTime = new calendar_activity.DestinationTime(origin, destination, travelMode);
            travelTime.execute();


            Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int Duration = GetInt(ArriveDuration);
                    if (St_time_M - Task_Model.get(Position_BackUp - 1).et_time_M > Duration) {

                        color = 0;
                        Change_Task_In_Model();
                        Toast.makeText(calendar_activity.this, "You will be on Time", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    } else {

                        color = 1;
                        Change_Task_In_Model();
                        Toast.makeText(calendar_activity.this, "You will be Late", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);


                    }
                }
            };
            progressBar.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, 2000);
        } else {
            visibility = 0;
            Change_Task_In_Model();
//            if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                checkColor = 1;
//            } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                if (Et_time_M < NowTime) {
//                    checkColor = 1;
//                } else {
//                    checkColor = 0;
//                }
//            } else {
//                checkColor = 0;
//            }


        }

    }


    public void startCheckingTraffic() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a periodic work request to run every 10 minutes
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                calendar_activity.DurationCheckWorker.class, 10, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        // Enqueue the periodic work request
        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }

    public void ChatGPT(View view) {
        Intent intent = new Intent(calendar_activity.this, ChatGPTActivity.class);

        startActivity(intent);

    }

    boolean taskViewAreUp = false;
    int oldHeightL;
    int oldHeight;
    int newHeight;
    int oldHeightA;

    public void moveUp(View view) {


        //int oldHeightL = 750;

        int newHeightL = 0;


        int newHeightA = 0;


        Handler handler = new Handler();


        Log.e("MOVE UP", "moveUp: ");

        if (!taskViewAreUp) {
            liveUpdate.setVisibility(View.GONE);
            AI.setVisibility(View.GONE);

            taskViewAreUp = true;
            oldHeightL = liveUpdate.getHeight() - 1;
            oldHeight = tasks_recyclerview.getHeight() - 1;
            newHeight = height - topPanel.getHeight() - 1 - 889;//889
            oldHeightA = AI.getHeight() - 1;


            ValueAnimator animator = ValueAnimator.ofInt(oldHeight, newHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = tasks_recyclerview.getLayoutParams();
                    layoutParams.height = value;
                    tasks_recyclerview.setLayoutParams(layoutParams);
                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();


            ValueAnimator animator1 = ValueAnimator.ofInt(oldHeightL, newHeightL);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = liveUpdate.getLayoutParams();
                    layoutParams.height = value;
                    liveUpdate.setLayoutParams(layoutParams);
                }
            });
            animator1.setDuration(500); // Set the duration of the animation in milliseconds
            animator1.start();


            ValueAnimator animator2 = ValueAnimator.ofInt(oldHeightA, newHeightA);
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = AI.getLayoutParams();
                    layoutParams.height = value;
                    AI.setLayoutParams(layoutParams);
                }
            });
            animator2.setDuration(500); // Set the duration of the animation in milliseconds
            animator2.start();


            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(arrow, "rotation", 180, 360);
            rotateAnimator.setDuration(500); // Duration in milliseconds (1 second in this example)
            rotateAnimator.start();


        } else {
            liveUpdate.setVisibility(View.VISIBLE);
            AI.setVisibility(View.VISIBLE);
            taskViewAreUp = false;


            ValueAnimator animator = ValueAnimator.ofInt(newHeight, oldHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = tasks_recyclerview.getLayoutParams();
                    layoutParams.height = value;
                    tasks_recyclerview.setLayoutParams(layoutParams);
                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();


            ValueAnimator animator1 = ValueAnimator.ofInt(newHeightL, oldHeightL);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = liveUpdate.getLayoutParams();
                    layoutParams.height = value;
                    liveUpdate.setLayoutParams(layoutParams);
                }
            });
            animator1.setDuration(500); // Set the duration of the animation in milliseconds
            animator1.start();


            ValueAnimator animator2 = ValueAnimator.ofInt(newHeightA, oldHeightA);
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = AI.getLayoutParams();
                    layoutParams.height = value;
                    AI.setLayoutParams(layoutParams);
                }
            });
            animator2.setDuration(500); // Set the duration of the animation in milliseconds
            animator2.start();


            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(arrow, "rotation", 0, 180);
            rotateAnimator.setDuration(500); // Duration in milliseconds (1 second in this example)
            rotateAnimator.start();
        }

    }

    @Override
    public void onItemClickedMonth(int position) {

        if (monthViewAreUp) {
            Task_Model.clear();
            tasks_adapter.notifyDataSetChanged();
            CheckHintTasksText();

            NowMonth = position + 1;
            Month_TV.setText(Month_Model.get(position).Month);


            Days_ModelY.clear();
            for (Days_Model daysModelALL : Days_ModelALL) {

                if (daysModelALL.Year == NowYear) {
                    Days_ModelY.add(new Days_Model(daysModelALL.id, daysModelALL.color, daysModelALL.Year, daysModelALL.Month, daysModelALL.Date, daysModelALL.Day_OW, daysModelALL.calendar));
                }
            }


            Days_Model.clear();
            for (Days_Model daysModel : Days_ModelY) {

                if (daysModel.Month == NowMonth) {
                    Days_Model.add(new Days_Model(daysModel.id, daysModel.color, daysModel.Year, daysModel.Month, daysModel.Date, daysModel.Day_OW, daysModel.calendar));
                }
            }

            for (int zz = 0; zz < Months.length; zz++) {
                if ((zz + 1) == NowMonth) {
                    Months_RV.scrollToPosition(zz);
                    Months_RV.suppressLayout(true);
                }
            }


            Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                @Override
                public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                    return o1.Date.compareTo(o2.Date);
                }
            });
            days_adapter.notifyDataSetChanged();
            CheckHintText();
            progressBar.setVisibility(View.GONE);
            CheckTutorial();
            if (!Days_Model.isEmpty()) {
                idCopy = Days_Model.get(0).id;
            }


            /////////////////////

            SetCurrentActivityView();


            ///////////////////////////////////


            if (!Days_Model.isEmpty()) {
                id = Days_Model.get(0).id;
                Days_Model.get(0).color = 1;
                days_adapter.notifyDataSetChanged();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                    int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                    Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                            queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                            ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));

//                                    if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                                        Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                    } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                                        if (Task_Model.get(Task_Model.size() - 1).et_time_M < NowTime) {
//                                            Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                        } else {
//                                            Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                        }
//                                    } else {
//                                        Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                    }
                                }
                                Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
                                    @Override
                                    public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                        return o1.time_start.compareTo(o2.time_start);
                                    }
                                });
                                tasks_adapter.notifyDataSetChanged();
                                CheckHintTasksText();
                                progressBarTask.setVisibility(View.GONE);


                            }
                        });


                ////////////////////////////
            } else {
                progressBarTask.setVisibility(View.GONE);

            }


            monthViewAreUp = false;
            Months_RV.scrollToPosition(position);
            int newHeight = 600;
            int oldHeight = 115;

            ValueAnimator animator = ValueAnimator.ofInt(newHeight, oldHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = Month_lay.getLayoutParams();
                    layoutParams.height = value;
                    Month_lay.setLayoutParams(layoutParams);
                    Months_RV.suppressLayout(true);

                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();
            Months_RV.setClickable(false);
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Months_RV.setVisibility(View.INVISIBLE);
                    Month_TV.setVisibility(View.VISIBLE);
                }
            };

            handler.postDelayed(runnable, 500);


        }
    }

    @Override
    public void onItemLongClickMonth(int position) {

    }


    boolean monthViewAreUp = false;

    public void Open_Month(View view) {

        int newHeight = 600;
        int oldHeight = 115;


        if (!monthViewAreUp) {
            monthViewAreUp = true;
            Months_RV.setClickable(true);
            Months_RV.setVisibility(View.VISIBLE);
            Month_TV.setVisibility(View.INVISIBLE);


            ValueAnimator animator = ValueAnimator.ofInt(oldHeight, newHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = Month_lay.getLayoutParams();
                    layoutParams.height = value;
                    Month_lay.setLayoutParams(layoutParams);
                    Months_RV.suppressLayout(false);
                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();


        } else {


            monthViewAreUp = false;
            Months_RV.setClickable(false);


            ValueAnimator animator = ValueAnimator.ofInt(newHeight, oldHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = Month_lay.getLayoutParams();
                    layoutParams.height = value;
                    Month_lay.setLayoutParams(layoutParams);
                    Months_RV.suppressLayout(true);

                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Months_RV.setVisibility(View.INVISIBLE);
                    Month_TV.setVisibility(View.VISIBLE);
                }
            };

            handler.postDelayed(runnable, 500);


        }


    }

    @Override
    public void onItemClickedYear(int position) {


        if (YearsViewAreUp) {
            Task_Model.clear();
            tasks_adapter.notifyDataSetChanged();
            CheckHintTasksText();

            NowYear = Year_Model.get(position).Year;
            Year_TV.setText("" + Year_Model.get(position).Year);


            Days_ModelY.clear();
            for (Days_Model daysModelALL : Days_ModelALL) {

                if (daysModelALL.Year == NowYear) {
                    Days_ModelY.add(new Days_Model(daysModelALL.id, daysModelALL.color, daysModelALL.Year, daysModelALL.Month, daysModelALL.Date, daysModelALL.Day_OW, daysModelALL.calendar));
                }
            }


            Days_Model.clear();
            for (Days_Model daysModel : Days_ModelY) {

                if (daysModel.Month == NowMonth) {
                    Days_Model.add(new Days_Model(daysModel.id, daysModel.color, daysModel.Year, daysModel.Month, daysModel.Date, daysModel.Day_OW, daysModel.calendar));
                }
            }


            Collections.sort(Days_Model, new Comparator<com.example.daybuddy.Days_Model>() {
                @Override
                public int compare(com.example.daybuddy.Days_Model o1, com.example.daybuddy.Days_Model o2) {
                    return o1.Date.compareTo(o2.Date);
                }
            });
            days_adapter.notifyDataSetChanged();
            CheckHintText();
            progressBar.setVisibility(View.GONE);
            CheckTutorial();
            if (!Days_Model.isEmpty()) {
                idCopy = Days_Model.get(0).id;
            }


            /////////////////////

            SetCurrentActivityView();


            ///////////////////////////////////


            if (!Days_Model.isEmpty()) {
                id = Days_Model.get(0).id;
                Days_Model.get(0).color = 1;
                days_adapter.notifyDataSetChanged();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                    int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                    Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("CheckColor", int.class), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                            queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                            ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));

//                                    if (GetInt(Days_Model.get(positionCopy).Date) < currentDateNum) {
//                                        Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                    } else if (GetInt(Days_Model.get(positionCopy).Date) == currentDateNum) {
//                                        if (Task_Model.get(Task_Model.size() - 1).et_time_M < NowTime) {
//                                            Task_Model.get(Task_Model.size() - 1).checkColor = 1;
//                                        } else {
//                                            Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                        }
//                                    } else {
//                                        Task_Model.get(Task_Model.size() - 1).checkColor = 0;
//                                    }
                                }
                                Collections.sort(Task_Model, new Comparator<com.example.daybuddy.Task_Model>() {
                                    @Override
                                    public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                        return o1.time_start.compareTo(o2.time_start);
                                    }
                                });
                                tasks_adapter.notifyDataSetChanged();
                                CheckHintTasksText();
                                progressBarTask.setVisibility(View.GONE);


                            }
                        });


                ////////////////////////////
            } else {
                progressBarTask.setVisibility(View.GONE);

            }


            YearsViewAreUp = false;
            Years_RV.scrollToPosition(position);
            int newHeight = 600;
            int oldHeight = 115;

            ValueAnimator animator = ValueAnimator.ofInt(newHeight, oldHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = YM.getLayoutParams();
                    layoutParams.height = value;
                    YM.setLayoutParams(layoutParams);
                    Years_RV.suppressLayout(true);

                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();
            Years_RV.setClickable(false);
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Years_RV.setVisibility(View.INVISIBLE);
                    Year_TV.setVisibility(View.VISIBLE);
                }
            };

            handler.postDelayed(runnable, 500);


        }


    }

    @Override
    public void onItemLongClickYear(int position) {

    }

    boolean YearsViewAreUp = false;

    public void Open_Year(View view) {


        int newHeight = 600;
        int oldHeight = 115;


        if (!YearsViewAreUp) {
            YearsViewAreUp = true;

            Years_RV.setClickable(true);
            Years_RV.setVisibility(View.VISIBLE);
            Year_TV.setVisibility(View.INVISIBLE);


            ValueAnimator animator = ValueAnimator.ofInt(oldHeight, newHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = YM.getLayoutParams();
                    layoutParams.height = value;
                    YM.setLayoutParams(layoutParams);
                    Years_RV.suppressLayout(false);
                    Years_RV.scrollToPosition(59);
                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();


        } else {


            YearsViewAreUp = false;
            Years_RV.setClickable(false);


            ValueAnimator animator = ValueAnimator.ofInt(newHeight, oldHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = YM.getLayoutParams();
                    layoutParams.height = value;
                    YM.setLayoutParams(layoutParams);
                    Years_RV.suppressLayout(true);

                }
            });
            animator.setDuration(500); // Set the duration of the animation in milliseconds
            animator.start();

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    Years_RV.setVisibility(View.INVISIBLE);

                    Year_TV.setVisibility(View.VISIBLE);

                }
            };

            handler.postDelayed(runnable, 500);


        }
    }

    public void Charts(View view) {
        Intent intent = new Intent(calendar_activity.this, activity_pie_char.class);
        intent.putExtra("countCheck", countCheck);
        intent.putExtra("count", count);
        startActivity(intent);
    }


    class DestinationTime extends AsyncTask<Void, Void, String> {
        private static final String TAG = "DestinationTimeCalculator";

        private LatLng origin;
        private LatLng destination;
        private String apiKey = "AIzaSyBa-ttElPoQgDetwieuuMp360EJeXlr5RY";
        private String travelMode;
        private String trafficModel = "best_guess";


        String durationText;

        public DestinationTime(LatLng origin, LatLng destination, String travelMode) {
            this.origin = origin;
            this.destination = destination;
            this.travelMode = travelMode;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json" +
                        "?origins=" + origin.latitude + "," + origin.longitude +
                        "&destinations=" + destination.latitude + "," + destination.longitude + "&mode=" + travelMode +
                        "&key=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                response = stringBuilder.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching data: " + e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray rows = jsonResponse.getJSONArray("rows");
                    if (rows.length() > 0) {
                        JSONObject row = rows.getJSONObject(0);
                        JSONArray elements = row.getJSONArray("elements");
                        if (elements.length() > 0) {
                            JSONObject element = elements.getJSONObject(0);
                            JSONObject duration = element.getJSONObject("duration");
                            durationText = duration.getString("text");
                            ArriveDuration = durationText;
                            //Toast.makeText(, durationText, Toast.LENGTH_SHORT).show();
                            // Use durationText as the estimated travel time
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                }
            }
        }
    }

    public class DurationCheckWorker extends Worker {

        public DurationCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            // Call the method to check duration between locations
            checkDuration();
            return Result.success();
        }

        private void checkDuration() {
            // Your code to check duration goes here
            // This could include creating an instance of DestinationTimeCalculator and executing it
            // You may also want to handle the results (e.g., update UI, save data, etc.)
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Calculate total minutes since midnight
            long totalMinutes = hour * 60 + minute;


            Log.e("BACKGROUND", "checkDuration: " + totalMinutes);
//            LatLng origin = new LatLng(Task_Model.get(Position_BackUp - 1).latitude, Task_Model.get(Position_BackUp - 1).longitude);
//            LatLng destination = new LatLng(latitude, longitude);
//            DestinationTime travelTime = new DestinationTime(origin, destination, travelMode);
//            travelTime.execute();
        }
    }


    ////////////////////////////////////////////////////////////////////

}