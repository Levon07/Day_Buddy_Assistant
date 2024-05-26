package com.example.daybuddy.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.daybuddy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WidgetService extends RemoteViewsService {
    private Context mContext;
    private List<String> mItemsTime = new ArrayList<>();
    private List<String> mItemsTask = new ArrayList<>();
    String id;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {
                mContext = getApplicationContext();

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
        };
    }
}
