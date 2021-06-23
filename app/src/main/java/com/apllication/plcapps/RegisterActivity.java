package com.apllication.plcapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fStore;
    private ProgressDialog mLoading;
    private TextView login;
    private EditText email, nama, password, konfirmasi;
    private Button registrasi;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String emailUser, namaUser, passwordUser, konfirmasiUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initial();
    }

    private void initial() {
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        login = findViewById(R.id.tV_login);
        email = findViewById(R.id.eT_email);
        nama = findViewById(R.id.eT_nama);
        password = findViewById(R.id.eT_password);
        konfirmasi = findViewById(R.id.eT_konfirmasi_pass);
        registrasi = findViewById(R.id.btn_daftar);

        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialRegist();
            }
        });
    }

    private void initialRegist() {

        emailUser = email.getText().toString();
        namaUser = nama.getText().toString();
        passwordUser = password.getText().toString();
        konfirmasiUser = konfirmasi.getText().toString();

        if (emailUser.isEmpty()) {
            email.setError("Masukan Email Terlebih Dahulu");
            email.setFocusable(true);
        } else if (!emailUser.matches(emailPattern)) {
            email.setError("Masukan Format Email yang Benar");
            email.setFocusable(true);
        } else if (namaUser.isEmpty()) {
            nama.setError("Masukan Nama Terlebih Dahulu");
            nama.setFocusable(true);
        } else if (passwordUser.isEmpty()) {
            password.setError("Masukan Password Anda");
            password.setFocusable(true);
        } else if (passwordUser.length() < 8) {
            password.setError("Masukan Password minimal 8");
            password.setFocusable(true);
        } else if (konfirmasiUser.isEmpty()) {
            konfirmasi.setError("Masukan Confirm Password Terlebih Dahulu");
            konfirmasi.setFocusable(true);
        } else if (!konfirmasiUser.equals(passwordUser)) {
            konfirmasi.setError("Masukan Password yang Sama");
            konfirmasi.setFocusable(true);
        } else {
            registrasiUser(emailUser, passwordUser);
        }

    }

    private void registrasiUser(String emailUser, String passwordUser) {
        mLoading.show();
        firebaseAuth.createUserWithEmailAndPassword(emailUser, passwordUser)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            String uid = user.getUid();
                            DocumentReference documentReference = fStore.collection("user").document(uid);


                            HashMap hashMap = new HashMap();
                            String emailmap = user.getEmail();

                            hashMap.put("email", emailmap);
                            hashMap.put("uid", uid);
                            hashMap.put("nama", namaUser);
                            hashMap.put("email", emailUser);


                            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mLoading.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Berhasil Daftar Dengan Email\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}