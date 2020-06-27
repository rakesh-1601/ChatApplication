package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username,email,password;
    Button register;

    FirebaseAuth auth;
    DatabaseReference reference;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username = findViewById(R.id.username);
        email    = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        constraintLayout = findViewById(R.id.cs);

        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_username = username.getText().toString();
                String t_email = email.getText().toString();
                String t_password = password.getText().toString();

                if(TextUtils.isEmpty(t_username) || TextUtils.isEmpty(t_email) || TextUtils.isEmpty(t_password)) {
                    Snackbar
                            .make(constraintLayout, "Enter all fields", Snackbar.LENGTH_LONG)
                            .show();

                }else if(t_password.length()<6){
                    Snackbar
                            .make(constraintLayout, "password must be atleast of length 6", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    registerUser(t_username, t_email, t_password);
                }
            }
        });
    }

    private void registerUser(final String username, String email, String password){
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Regsitering user..");
        pd.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            User user  = new User(userid,username,"default","offline",username.toLowerCase());

                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        pd.dismiss();
                                        finish();
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Snackbar
                                    .make(constraintLayout, "Cant Register with this Email and Password", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
}
