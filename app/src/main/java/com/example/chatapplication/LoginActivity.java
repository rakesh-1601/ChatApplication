package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login;

    FirebaseAuth auth;
    TextView forgetpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        forgetpassword = findViewById(R.id.forget_password);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });

        email    = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        final ConstraintLayout constraintLayout = findViewById(R.id.cs);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_email = email.getText().toString();
                String t_password = password.getText().toString();
                if(t_email.equals("")||t_password.equals("")){
                    Snackbar.make(constraintLayout, "All fields are required", Snackbar.LENGTH_LONG).show();
                }else {
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Logging you in..");
                    pd.show();
                    auth.signInWithEmailAndPassword(t_email, t_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        pd.dismiss();
                                        finish();
                                    } else {
                                        Snackbar.make(constraintLayout, "Authentication Failed", Snackbar.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }
                                }

                            });
                }
            }
        });

    }
}
