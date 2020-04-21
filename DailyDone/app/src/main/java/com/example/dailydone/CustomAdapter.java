package com.example.dailydone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    String routineTitle, routineDesc, routineExecuteDate;
    Context context;
    ArrayList<StoreRoutineData> storeRoutineData;

    public CustomAdapter(Context c, ArrayList<StoreRoutineData> p) {
        context = c;
        storeRoutineData = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        routineTitle = storeRoutineData.get(position).getRoutineTitle();
        routineDesc = storeRoutineData.get(position).getRoutineDesc();
        routineExecuteDate = storeRoutineData.get(position).getRoutineExecuteDate();

        holder.textView1.setText(routineTitle);
        holder.textView2.setText(routineDesc);
        holder.textView3.setText(routineExecuteDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, FourthDialogActivity.class);
                it.putExtra("routine_title_key", routineTitle);
                it.putExtra("routine_details_key", routineDesc);
                it.putExtra("routine_Date_key", routineExecuteDate);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeRoutineData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.routineTitleID);
            textView2 = itemView.findViewById(R.id.routineDescID);
            textView3 = itemView.findViewById(R.id.routineExecuteDataID);
        }
    }
}
