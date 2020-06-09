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
    String Phone;
    FloatingActionButton floatingActionButton;
    Dialog dialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        dialog = new Dialog(this);

        recyclerView = findViewById(R.id.recyclerViewID);
        floatingActionButton = findViewById(R.id.floatingActionButtonID);
        floatingActionButton.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeRoutineDataArrayList = new ArrayList<StoreRoutineData>();
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
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() != null) {
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("User Information")
                        .child(user.getDisplayName()).child("phoneObj");
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Phone = dataSnapshot.getValue(String.class);
                        try {
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Routine of Users")
                                    .child(Phone);
                            ref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    storeRoutineDataArrayList.clear();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
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
                        } catch (Exception e){
                            Toast.makeText(SecondActivity.this, "No Data", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        }

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;

        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Logout ?");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.getInstance().signOut();
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
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
}
