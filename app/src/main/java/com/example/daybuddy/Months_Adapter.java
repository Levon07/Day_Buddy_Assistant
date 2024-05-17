package com.example.daybuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Months_Adapter extends RecyclerView.Adapter<Months_Adapter.M_ViewHolder> {
    Context context;
    ArrayList<Month_Model> Month_Model;

    private final RV_Interface_Months rvInterface;

    public Months_Adapter(Context context, ArrayList<Month_Model> Month_Model, RV_Interface_Months rvInterface){
        this.context = context;
        this.Month_Model = Month_Model;
        this.rvInterface = rvInterface;
    }


    @NonNull
    @Override
    public Months_Adapter.M_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_months, parent, false);

        return new Months_Adapter.M_ViewHolder(view, rvInterface);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull M_ViewHolder holder, int position) {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view

        holder.Month.setText(Month_Model.get(position).getMonth());


    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return Month_Model.size();
    }

    public static class M_ViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from our recycler_view_row layout file
        // Kinda like in the onCreate method

        TextView Month;

        public M_ViewHolder(@NonNull View itemView, RV_Interface_Months rvInterface) {
            super(itemView);

            Month = itemView.findViewById(R.id.Month);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rvInterface.onItemClickedMonth(pos);
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
                            rvInterface.onItemLongClickMonth(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
