package com.example.daybuddy.Widget;

import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.daybuddy.Days_Model;
import com.example.daybuddy.R;
import com.example.daybuddy.Task_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<String> mItemsTime = new ArrayList<>();
    private List<String> mItemsTask = new ArrayList<>();
    String id;

    public RemoteViewsFactory(Context context) {
        mContext = context;
        // Initialize your data here
        int currentDateNum1 = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("daysModel").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                if (currentDateNum1 == queryDocumentSnapshot.getLong("date")){
                                    id = queryDocumentSnapshot.getString("DocId");
                                }
                            }



                            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                                                mItemsTime.add(queryDocumentSnapshot.getString("ST_Time"));
                                                mItemsTask.add(queryDocumentSnapshot.getString("Task_Text"));

                                            }

                                        }
                                    });


                        }

                    });
        }
        // Add more items as needed
    }

    @Override
    public void onCreate() {
        int currentDateNum1 = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("daysModel").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                if (currentDateNum1 == queryDocumentSnapshot.getLong("date")){
                                    id = queryDocumentSnapshot.getString("DocId");
                                }
                            }



                            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                                                mItemsTime.add(queryDocumentSnapshot.getString("ST_Time"));
                                                mItemsTask.add(queryDocumentSnapshot.getString("Task_Text"));

                                            }

                                        }
                                    });


                        }

                    });
        }
    }

    @Override
    public void onDataSetChanged() {


        int currentDateNum1 = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("daysModel").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                if (currentDateNum1 == queryDocumentSnapshot.getLong("date")){
                                    id = queryDocumentSnapshot.getString("DocId");
                                }
                            }



                            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                                                mItemsTime.add(queryDocumentSnapshot.getString("ST_Time"));
                                                mItemsTask.add(queryDocumentSnapshot.getString("Task_Text"));

                                            }

                                        }
                                    });


                        }

                    });
        }

    }

    @Override
    public void onDestroy() {
        // Clean up resources if needed
    }

    @Override
    public int getCount() {
        return mItemsTask.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_layout);
        rv.setTextViewText(R.id.Time, mItemsTime.get(position));
        rv.setTextViewText(R.id.Task, mItemsTask.get(position));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        // Return a loading RemoteViews if needed
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
