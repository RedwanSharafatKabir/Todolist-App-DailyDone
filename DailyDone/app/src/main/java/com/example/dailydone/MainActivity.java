package com.example.dailydone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog dialog;
    EditText emailInput, phone;
    Button enter;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailInputID);
        phone = findViewById(R.id.phoneInputID);
        enter = findViewById(R.id.enterID);
        enter.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User Information");
    }

    @Override
    public void onClick(View v) {
        final String emailObj = emailInput.getText().toString();
        final String phoneObj = phone.getText().toString();

        if(v.getId()==R.id.enterID) {
            dialog = ProgressDialog.show(MainActivity.this, "Loading",
                    "Please wait.......", true);

            if (emailObj.equals("")) {
                emailInput.setError("Fill this field");
                dialog.dismiss();
                return;
            }

            if (phoneObj.equals("")) {
                phone.setError("Fill this field");
                dialog.dismiss();
                return;
            }

            if (emailObj.length() > 15) {
                emailInput.setError("Username is too long.\nMaximum number of characters of name is 15");
                dialog.dismiss();
                return;
            }

            else {
                email = emailObj;
                password = phoneObj;
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            storeUserData(email, password);
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Intent it = new Intent(MainActivity.this, SecondActivity.class);
                                        startActivity(it);

                                        emailInput.setText("");
                                        phone.setText("");
                                    } else {
                                        dialog.dismiss();
                                        Toast t = Toast.makeText(MainActivity.this, "Error : " +
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                    }
                                }
                            });

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            Intent it = new Intent(MainActivity.this, SecondActivity.class);
                                            startActivity(it);

                                            emailInput.setText("");
                                            phone.setText("");
                                        } else {
                                            dialog.dismiss();
                                            Toast t = Toast.makeText(MainActivity.this, "Error : " +
                                                    task.getException().getMessage(), Toast.LENGTH_LONG);
                                            t.setGravity(Gravity.CENTER, 0, 0);
                                            t.show();
                                        }
                                    }
                                });

                            } else {
                                Toast t = Toast.makeText(MainActivity.this, "Authentication failed. Error : "
                                        + task.getException().getMessage(), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();
                            }
                        }
                    }
                });
            }
        }
    }

    private void storeUserData(String emailObj, String phoneObj) {
        String displayname = phoneObj;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            UserProfileChangeRequest profile;
            profile= new UserProfileChangeRequest.Builder().setDisplayName(displayname).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {}
            });
        }

        String Key_User_Info = phoneObj;
        StoreData storeData = new StoreData(emailObj, phoneObj);
        databaseReference.child(Key_User_Info).setValue(storeData);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;

        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Exit ?");
        alertDialogBuilder.setMessage("Are you sure you want to exit this application !");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
                if( getIntent().getBooleanExtra("Exit me", false)){
                    finish();
                }
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
}
