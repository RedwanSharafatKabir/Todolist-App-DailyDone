package com.example.dailydone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class FourthDialogActivity extends AppCompatActivity implements View.OnClickListener{

    Button save, delete;
    String Phone, routineTitle,routineDesc, routineExecuteDate;
    EditText taskName, taskDesc, taskDate;
    DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_dialog);

        taskName = findViewById(R.id.taskNameOutputID);
        taskDesc = findViewById(R.id.taskDetailsOutputID);
        taskDate = findViewById(R.id.executionDateOutputID);

        databaseReference = FirebaseDatabase.getInstance().getReference("Routine of Users");

        save = findViewById(R.id.updateTaskID);
        save.setOnClickListener(this);
        delete = findViewById(R.id.deleteTaskID);
        delete.setOnClickListener(this);

        taskName.setText(getIntent().getStringExtra("routine_title_key"));
        taskDesc.setText(getIntent().getStringExtra("routine_desc_key"));
        taskDate.setText(getIntent().getStringExtra("routine_date_key"));
    }

    @Override
    public void onClick(View v) {
        routineTitle = taskName.getText().toString();
        routineDesc = taskDesc.getText().toString();
        routineExecuteDate = taskDate.getText().toString();

        if(v.getId()==R.id.updateTaskID){
            if(routineTitle.isEmpty() || routineDesc.isEmpty() || routineExecuteDate.isEmpty()){
                Toast.makeText(FourthDialogActivity.this, "Fill up all required fields", Toast.LENGTH_LONG).show();
            }
            else {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.getDisplayName() != null) {
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("User Information")
                                .child(user.getDisplayName()).child("phoneObj");
                        ref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Phone = dataSnapshot.getValue(String.class);
                                DatabaseReference ref2 = databaseReference.child(Phone);
                                ref2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        storeRoutineDataMethod(Phone, routineTitle, routineDesc, routineExecuteDate);
                                        Intent it = new Intent(FourthDialogActivity.this, SecondActivity.class);
                                        startActivity(it);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(FourthDialogActivity.this, "No Data", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }
            }
        }

        if(v.getId()==R.id.deleteTaskID){
            alertDialogMethod();
        }
    }

    public void alertDialogMethod() {
        AlertDialog.Builder alertDialogBuilder;

        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Delete ?");
        alertDialogBuilder.setMessage("Are you sure you want to delete this routine !");
        alertDialogBuilder.setIcon(R.drawable.ic_delete_forever_black_24dp);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMethod();
                finish();
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deleteMethod(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() != null) {
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("User Information")
                        .child(user.getDisplayName()).child("phoneObj");
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Phone = dataSnapshot.getValue(String.class);
                        DatabaseReference ref2 = databaseReference.child(Phone).child(routineTitle);
                        ref2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    taskName.setText("");
                                    taskDesc.setText("");
                                    taskDate.setText("");
                                    Toast.makeText(getApplicationContext(), "Routine record deleted",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent it = new Intent(FourthDialogActivity.this, SecondActivity.class);
                                    startActivity(it);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Cannot be deleted",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        }
    }

    private void storeRoutineDataMethod(String Phone, String routineTitle,
                 String routineDesc, String routineExecuteDate) {
        String Key_User_Info = Phone;
        StoreRoutineData storeRoutineData = new StoreRoutineData(routineTitle, routineDesc, routineExecuteDate);
        databaseReference.child(Key_User_Info).child(routineTitle).setValue(storeRoutineData);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent it = new Intent(FourthDialogActivity.this, SecondActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
