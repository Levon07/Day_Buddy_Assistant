package com.example.daybuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Year_Adapter extends RecyclerView.Adapter<Year_Adapter.Y_ViewHolder> {
    Context context;
    ArrayList<Year_Model> Year_Model;

    private final RV_Interface_Year rvInterface;

    public Year_Adapter(Context context, ArrayList<Year_Model> Year_Model, RV_Interface_Year rvInterface){
        this.context = context;
        this.Year_Model = Year_Model;
        this.rvInterface = rvInterface;
    }


    @NonNull
    @Override
    public Year_Adapter.Y_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_year, parent, false);

        return new Year_Adapter.Y_ViewHolder(view, rvInterface);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull Y_ViewHolder holder, int position) {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view

        holder.Year.setText("" + Year_Model.get(position).getYear());


    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return Year_Model.size();
    }

    public static class Y_ViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from our recycler_view_row layout file
        // Kinda like in the onCreate method

        TextView Year;

        public Y_ViewHolder(@NonNull View itemView, RV_Interface_Year rvInterface) {
            super(itemView);

            Year = itemView.findViewById(R.id.Year);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rvInterface.onItemClickedYear(pos);
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
                            rvInterface.onItemLongClickYear(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
