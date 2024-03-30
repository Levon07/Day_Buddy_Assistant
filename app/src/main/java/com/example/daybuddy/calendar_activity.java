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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import retrofit.GsonConverterFactory;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit2.Retrofit;


public class calendar_activity extends AppCompatActivity {

    TextView HintText;

    private static final String TAG = "LOCATION_PICKER_TAG";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference TasksBase = database.getReference("tasks");

    MaterialButton pickTime;
    MaterialButton inputText;

    ArrayList<Days_Model> Days_Model = new ArrayList<>();

    String Date;

    String Day_OW;
    RecyclerView days_recyclerview;

    FirebaseUser mUser;

    Days_RV_Adapter days_adapter = new Days_RV_Adapter(this, Days_Model);

    DatabaseReference myRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);





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
                Days_Model = (ArrayList<com.example.daybuddy.Days_Model>) dataSnapshot.getValue(Object.class);
                Log.d(TAG, "Value is: " + Days_Model);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





        days_recyclerview = findViewById(R.id.RV_Tasks);

        days_adapter.notifyItemInserted(0);



        days_recyclerview.setAdapter(days_adapter);
        days_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        HintText = findViewById(R.id.HintText);
        CheckHintText();

    }

    public void CheckHintText(){
        if (Days_Model.size()>0){
            HintText.setVisibility(View.GONE);
        }else{
            HintText.setVisibility(View.VISIBLE);
        }
    }



    // Choose Time
    public void PickADate(View view) {
        View view1 = LayoutInflater.from(calendar_activity.this).inflate(R.layout.add_task, null);
        TextView ST_View = view1.findViewById(R.id.Start_time_view);
        MaterialDatePicker<Long> DatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        DatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date(selection));
                Day_OW = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date(selection));

                Days_Model.add(new Days_Model(Date, Day_OW));
                days_adapter.notifyItemInserted(Days_Model.size() + 1);
                CheckHintText();
                days_recyclerview.smoothScrollToPosition(days_adapter.getItemCount());
                myRef.setValue(Days_Model);

            }
        });
        DatePicker.show(getSupportFragmentManager(), "tag");


    }


    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(calendar_activity.this, LogIn.class);
        startActivity(intent);
    }



}