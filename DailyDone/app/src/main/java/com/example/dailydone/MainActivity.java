package com.example.dailydone;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText name, phone;
    Button enter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.nameInputID);
        phone = findViewById(R.id.phoneInputID);
        enter = findViewById(R.id.enterID);
        enter.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("User Information");
    }

    @Override
    public void onClick(View v) {
        final String nameObj = name.getText().toString();
        final String phoneObj = phone.getText().toString();

        if(nameObj.equals("")){
            name.setError("Fill this field");
        }

        if(phoneObj.equals("")){
            phone.setError("Fill this field");
        }

        if(nameObj.length() > 15){
            name.setError("Username is too long.\nMaximum number of characters of name is 15");
            name.setText("");
        }

        if(nameObj.length() <= 15){
            storeUserData(phoneObj, nameObj);

            String Keyphone = "phone_key_name";
            String KeyName = "name_key_name";
            Intent it = new Intent(MainActivity.this, SecondActivity.class);
            it.putExtra(Keyphone, phoneObj);
            it.putExtra(KeyName, nameObj);

            name.setText("");
            phone.setText("");
            startActivityForResult(it, 1);
        }
    }

    private void storeUserData(String phoneObj, String nameObj) {
        String Key_User_Info = phoneObj;
        StoreData storeData = new StoreData(phoneObj, nameObj);
        databaseReference.child(Key_User_Info).setValue(storeData);
    }
}
