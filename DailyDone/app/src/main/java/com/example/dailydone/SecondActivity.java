package com.example.dailydone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    ArrayList<StoreRoutineData> storeRoutineDataArrayList;
    CustomAdapter customAdapter;
    String Phone, Name;
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        dialog = new Dialog(this);

        recyclerView = findViewById(R.id.recyclerViewID);
        floatingActionButton = findViewById(R.id.floatingActionButtonID);
        floatingActionButton.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeRoutineDataArrayList = new ArrayList<StoreRoutineData>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Routine of Users");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Phone = bundle.getString("phone_key_name");
            Name = bundle.getString("name_key_name");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.floatingActionButtonID){
            ThirdDialogActivity thirdDialogActivity = new ThirdDialogActivity();
            thirdDialogActivity.show(getSupportFragmentManager(), "Sample dialog");
        }
    }

    @Override
    protected void onStart() {
        databaseReference.child(Phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeRoutineDataArrayList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    StoreRoutineData storeRoutineData = dataSnapshot1.getValue(StoreRoutineData.class);
                    storeRoutineDataArrayList.add(storeRoutineData);
                }
                Collections.reverse(storeRoutineDataArrayList);
                customAdapter = new CustomAdapter(SecondActivity.this, storeRoutineDataArrayList);
                recyclerView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, "No Data", Toast.LENGTH_LONG).show();
            }
        });
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;

        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Are you sure to exit ?");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent it = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    public void fl(String Title){
//        DatabaseReference userRef1 = databaseReference.child(Phone).child(Title).child("routineTitle");
//        userRef1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                textView2.setText(" " + dataSnapshot.getValue(String.class));
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });
//    }
}
