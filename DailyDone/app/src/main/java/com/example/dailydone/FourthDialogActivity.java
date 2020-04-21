package com.example.dailydone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FourthDialogActivity extends AppCompatActivity implements View.OnClickListener{

    TextView taskName;
    Button save, delete;
    String Phone;
    EditText taskDesc, taskDate;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_dialog);

        taskName = findViewById(R.id.taskNameOutputID);
        taskDesc = findViewById(R.id.taskDetailsOutputID);
        taskDate = findViewById(R.id.executionDateOutputID);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Phone = bundle.getString("phone_key_name");
            Phone = "01723076992";

            taskName.setText(bundle.getString("routine_title_key"));
            taskDesc.setText(bundle.getString("routine_details_key"));
            taskDate.setText(bundle.getString("routine_Date_key"));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Routine of Users");

        save = findViewById(R.id.updateTaskID);
        save.setOnClickListener(this);
        delete = findViewById(R.id.deleteTaskID);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String routineTitle = taskName.getText().toString();
        String routineDesc = taskDesc.getText().toString();
        String routineExecuteDate = taskDate.getText().toString();

        if(v.getId()==R.id.updateTaskID){
            if(routineTitle.isEmpty() || routineDesc.isEmpty() || routineExecuteDate.isEmpty()){
                Toast.makeText(FourthDialogActivity.this, "Fill up all required fields", Toast.LENGTH_LONG).show();
            }
            else {
                storeRoutineDataMethod(Phone, routineTitle, routineDesc, routineExecuteDate);
                Intent it = new Intent(FourthDialogActivity.this, SecondActivity.class);
                it.putExtra("phone_key_name", Phone);
                startActivityForResult(it, 1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                taskDesc.setText("");
                taskDate.setText("");
            }
        }

        if(v.getId()==R.id.deleteTaskID){
            databaseReference.child(Phone).child(routineTitle).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        taskName.setText("");
                        taskDesc.setText("");
                        taskDate.setText("");
                        Intent it = new Intent(FourthDialogActivity.this, SecondActivity.class);
                        it.putExtra("phone_key_name", Phone);
                        startActivityForResult(it, 1);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot be deleted", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void storeRoutineDataMethod(String Phone, String routineTitle, String routineDesc, String routineExecuteDate) {
        String Key_User_Info = Phone;
        StoreRoutineData storeRoutineData = new StoreRoutineData(routineTitle, routineDesc, routineExecuteDate);
        databaseReference.child(Key_User_Info).child(routineTitle).setValue(storeRoutineData);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(FourthDialogActivity.this, SecondActivity.class);
        it.putExtra("phone_key_name", Phone);
        startActivityForResult(it, 1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
