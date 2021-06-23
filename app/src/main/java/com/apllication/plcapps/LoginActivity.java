package com.apllication.plcapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login,btn_daftar;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoading;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.eT_email);
        password = findViewById(R.id.eT_password);
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        mAuth = FirebaseAuth.getInstance();
        btn_login = findViewById(R.id.btn_login);
        btn_daftar = findViewById(R.id.btn_daftar);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialLogin();
            }
        });
    }

    private void initialLogin() {
        String email_user = email.getText().toString();
        final String password_user = password.getText().toString();
        if (email_user.isEmpty()) {
            email.setError("Masukan Email");
            email.setFocusable(true);
        } else if (password_user.isEmpty()) {
            password.setError("Masukan Password");
            password.setFocusable(true);
        } else if (!email_user.matches(emailPattern)) {
            email.setError("Masukan Email yang Valid");
            email.setFocusable(true);
        } else {
            login(email_user, password_user);
        }
    }

    private void login(String email_user, String password_user) {
        mLoading.show();

        mAuth.signInWithEmailAndPassword(email_user, password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("nama", user.getDisplayName());
                    Log.d("cobaNama", "onComplete: " +user.getDisplayName());
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mLoading.dismiss();
                Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
