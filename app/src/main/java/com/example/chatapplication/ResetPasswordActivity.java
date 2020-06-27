package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText email;
    Button reset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        reset = findViewById(R.id.reset);
        final ConstraintLayout constraintLayout = findViewById(R.id.cs);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s  =email.getText().toString();
                if(s.equals("")){
                    Snackbar
                            .make(constraintLayout, "All fields are required", Snackbar.LENGTH_LONG)
                            .show();
                }else {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Snackbar
                                        .make(constraintLayout, "Check Email Address", Snackbar.LENGTH_LONG)
                                        .show();
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            }else{
                                Snackbar
                                        .make(constraintLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }
            }
        });

    }
}
