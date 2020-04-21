package com.example.dailydone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThirdDialogActivity extends AppCompatDialogFragment implements View.OnClickListener{

    EditText task, description, date;
    Button save;
    String Phone;
    DatabaseReference databaseReference;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_third_dialog, null);
        builder.setView(view).setTitle("Create new task")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        task = view.findViewById(R.id.taskNameInputID);
        description = view.findViewById(R.id.taskDetailsInputID);
        date = view.findViewById(R.id.executionDateInputID);

        save = view.findViewById(R.id.saveTaskID);
        save.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Routine of Users");

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle!=null){
            String value2 = bundle.getString("phone_key_name");
            Phone = value2;
        }

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String routineTitle = task.getText().toString();
        String routineDesc = description.getText().toString();
        String routineExecuteDate = date.getText().toString();

        if(v.getId()==R.id.saveTaskID){
            if(routineTitle.isEmpty() || routineDesc.isEmpty() || routineExecuteDate.isEmpty()){
                Toast.makeText(getActivity(), "Fill up all required fields", Toast.LENGTH_LONG).show();
            }
            else {
                storeRoutineDataMethod(Phone, routineTitle, routineDesc, routineExecuteDate);
                task.setText("");
                description.setText("");
                date.setText("");
            }

//            Intent it = new Intent(getActivity(), FourthDialogActivity.class);
//            it.putExtra("phone_key_name", Phone);
//            startActivityForResult(it, 1);
        }
    }

    private void storeRoutineDataMethod(String Phone, String routineTitle, String routineDesc, String routineExecuteDate) {
        String Key_User_Info = Phone;
        StoreRoutineData storeRoutineData = new StoreRoutineData(routineTitle, routineDesc, routineExecuteDate);
        databaseReference.child(Key_User_Info).child(routineTitle).setValue(storeRoutineData);

//        SecondActivity sl = (SecondActivity) getActivity();
//        sl.fl(routineTitle);
    }
}
