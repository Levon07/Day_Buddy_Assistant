package com.example.daybuddy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TT_RV_Adapter extends RecyclerView.Adapter<TT_RV_Adapter.TT_ViewHolder> {
    Context context;
    ArrayList<Task_Model> Task_Model;

    private final RV_Interface rvInterface;

    public TT_RV_Adapter(Context context, ArrayList<Task_Model> Task_Model, RV_Interface rvInterface){
        this.context = context;
        this.Task_Model = Task_Model;
        this.rvInterface = rvInterface;
    }


    @NonNull
    @Override
    public TT_RV_Adapter.TT_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_tasks, parent, false);

        return new TT_RV_Adapter.TT_ViewHolder(view, rvInterface);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull TT_ViewHolder holder, int position) {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view

        holder.Time_Start.setText(Task_Model.get(position).getTime_start());
        holder.Time_End.setText(Task_Model.get(position).getTime_end());
        holder.Task_Text.setText(Task_Model.get(position).getTask_text());
        holder.Location.setText(Task_Model.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return Task_Model.size();
    }

    public static class TT_ViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from our recycler_view_row layout file
        // Kinda like in the onCreate method

        TextView Time_Start, Time_End, Task_Text, Location;
        View Card;

        public TT_ViewHolder(@NonNull View itemView, RV_Interface rvInterface) {
            super(itemView);

            Time_Start = itemView.findViewById(R.id.time_start);
            Time_End = itemView.findViewById(R.id.time_end);
            Task_Text = itemView.findViewById(R.id.task_text);
            Location = itemView.findViewById(R.id.location);
            Card = itemView.findViewById(R.id.Card);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (rvInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rvInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
