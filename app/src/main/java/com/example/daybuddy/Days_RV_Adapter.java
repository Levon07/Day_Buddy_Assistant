package com.example.daybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Days_RV_Adapter extends RecyclerView.Adapter<Days_RV_Adapter.Days_ViewHolder> {
    Context context;
    ArrayList<Days_Model> Days_Model;
    private final RV_Interface rvInterface;

    public Days_RV_Adapter(Context context, ArrayList<Days_Model> Days_Model, RV_Interface rvInterface){
        this.context = context;
        this.Days_Model = Days_Model;
        this.rvInterface = rvInterface;
    }


    @NonNull
    @Override
    public Days_RV_Adapter.Days_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_days, parent, false);

        return new Days_RV_Adapter.Days_ViewHolder(view, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Days_RV_Adapter.Days_ViewHolder holder, int position) {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view

        holder.Date.setText(Days_Model.get(position).getDate());
        holder.Day_OW.setText(Days_Model.get(position).getDay_OW());
        if(Days_Model.get(position).color == 0) {
            holder.Background.setBackground(ContextCompat.getDrawable(context, R.drawable.button1));
        }else{
            holder.Background.setBackground(ContextCompat.getDrawable(context, R.drawable.button));
        }
    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return Days_Model.size();
    }

    public static class Days_ViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from our recycler_view_row layout file
        // Kinda like in the onCreate method

        TextView Date, Day_OW;
        ConstraintLayout Background;

        public Days_ViewHolder(@NonNull View itemView, RV_Interface rvInterface) {
            super(itemView);

            Date = itemView.findViewById(R.id.Date);
            Day_OW = itemView.findViewById(R.id.Day_OW);
            Background = itemView.findViewById(R.id.Background);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rvInterface.onItemClicked(pos);
                        }
                    }
                }
            });

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
