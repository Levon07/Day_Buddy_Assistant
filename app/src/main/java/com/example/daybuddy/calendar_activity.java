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
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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


public class calendar_activity extends AppCompatActivity implements RV_Interface {

    int position;

    TextView HintText;

    ArrayList<TaskModelArr> Task_Model_Arr = new ArrayList<>();

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

    Days_RV_Adapter days_adapter = new Days_RV_Adapter(this, Days_Model, this);

    DatabaseReference myRef;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        progressBar = findViewById(R.id.progressBar);
        HintText = findViewById(R.id.HintText);
        Bundle extras1 = getIntent().getExtras();
        if (extras1 != null) {
            position = extras1.getInt("Position");
            Task_Model_Arr.get(position).TaskModel = (ArrayList<com.example.daybuddy.Task_Model>) extras1.get("TaskModelArr");
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUser = extras.getParcelable("auth");
            //The key argument here must match that used in the other activity
        }

        progressBar.setVisibility(View.VISIBLE);
        HintText.setVisibility(View.GONE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("daysModel").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                Days_Model.add(new Days_Model(queryDocumentSnapshot.getString("DocId"), queryDocumentSnapshot.getString("date"), queryDocumentSnapshot.getString("day_ow")));
                            }
                            days_adapter.notifyDataSetChanged();
                            CheckHintText();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }


        days_recyclerview = findViewById(R.id.RV_Tasks);

        days_adapter.notifyItemInserted(0);


        days_recyclerview.setAdapter(days_adapter);
        days_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        CheckTutorial();

    }

    public void CheckHintText() {
        if (Days_Model.size() > 0) {
            HintText.setVisibility(View.GONE);
        } else {
            HintText.setVisibility(View.VISIBLE);
        }
    }

    public void CheckTutorial(){
        if(Days_Model.isEmpty()){
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
                Date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date(selection));
                boolean flag = false;
                for (int i = 0; i< Days_Model.size(); i++){
                    if(Days_Model.get(i).getDate().equals(Date)){
                        flag = true;
                    }

                }
                if (!flag) {
                    Day_OW = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date(selection));
                    String id = UUID.randomUUID().toString();
                    Days_Model.add(new Days_Model(id, Date, Day_OW));
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("date", Date);
                        hashMap.put("day_ow", Day_OW);
                        hashMap.put("Position", Days_Model.size() + 1);
                        hashMap.put("DocId", id);
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
                }else {
                    Toast.makeText(calendar_activity.this, "You Already Created a TimeTable For  : " + Date, Toast.LENGTH_SHORT).show();
                }


                days_adapter.notifyDataSetChanged();
                CheckHintText();
                days_recyclerview.smoothScrollToPosition(days_adapter.getItemCount());
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("TaskModelArr", Task_Model_Arr.get(position).getTaskModel());
        intent.putExtra("Position", position);
        intent.putExtra("day", Days_Model.get(position).getDay_OW());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        intent.putExtra("id", Days_Model.get(position).id);
//        if (user != null)
//        {
//            FirebaseFirestore.getInstance().collection("daysModel")
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
//                            {
//                                String id = queryDocumentSnapshot.getId();
//                                intent.putExtra("id", id);
//                            }
//                        }
//                    });
//        }
        startActivity(intent);
    }

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
                Date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date(selection));
                boolean flag = false;
                for (int i = 0; i< Days_Model.size(); i++){
                    if(Days_Model.get(i).getDate().equals(Date)){
                        flag = true;
                    }

                }
                if (!flag){
                    Day_OW = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date(selection));
                    Days_Model.set(position, new Days_Model(Days_Model.get(position).id, Date, Day_OW));
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("date", Date);
                    hashMap.put("day_ow", Day_OW);
                    hashMap.put("Position", position);
                    hashMap.put("DocId", Days_Model.get(position).id);
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


                    days_adapter.notifyDataSetChanged();
                    CheckHintText();
                    days_recyclerview.smoothScrollToPosition(position);
                }else {
                    Toast.makeText(calendar_activity.this, "You Already Created a TimeTable For" + Date, Toast.LENGTH_SHORT).show();
                }

            }
        });
        DatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("daysModel").document(Days_Model.get(position).id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                days_adapter.notifyItemRemoved(position);
                CheckHintText();
            }
        });
        DatePicker.show(getSupportFragmentManager(), "tag");



    }

}