package com.example.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity  extends AppCompatActivity{


    private Button btnRegister;
    private EditText email;
    private EditText password;
    private Button Login;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister=findViewById(R.id.btnRegister);
        email= findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPasword);
        Login=findViewById(R.id.btnLogin);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.pb);
        firebaseDatabase=FirebaseDatabase.getInstance();

          btnRegister.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

              }
          });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText=email.getText().toString();
                String passText=password.getText().toString();
                if(TextUtils.isEmpty(emailText))
                {
                     Toast.makeText(MainActivity.this,"Please Enter valid Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passText))
                {
                    Toast.makeText(MainActivity.this, "Please Enter a Valid Password", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(emailText,passText)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                            }
                            else{
                                    Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText=email.getText().toString();
                String passText=password.getText().toString();
                if(TextUtils.isEmpty(emailText))
                {
                      Toast.makeText(MainActivity.this,"Please Enter Some Characters",Toast.LENGTH_SHORT).show();
                        return;
                }
                if(TextUtils.isEmpty(passText))
                {
                    Toast.makeText(MainActivity.this,"Please Enter Some Characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(emailText,passText)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, firebaseAuth.getUid(), Toast.LENGTH_LONG).show();
                                    DatabaseReference mTest = FirebaseDatabase.getInstance().getReference();

                                    mTest.child("User").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Intent in=new Intent(MainActivity.this,UserData.class);
                                            if(dataSnapshot.exists()){
                                                in.putExtra("first_check", false);
                                            }
                                             else
                                            {
                                                 in.putExtra("first_check",true);
                                            }
                                            startActivity(in);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                          Toast.makeText(MainActivity.this,"ho",Toast.LENGTH_SHORT).show();
                                        }
                                    }) ;
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });

               }
        });
    }
}
