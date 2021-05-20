package com.alexch.newwordlearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    String userlogin, userpass, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();



        CheckBox newu = (CheckBox) findViewById(R.id.newuser);
        EditText name = (EditText) findViewById(R.id.username);
        EditText login = (EditText) findViewById(R.id.email);
        EditText pass = (EditText) findViewById(R.id.pass);
        Button auth = (Button) findViewById(R.id.auth);
        Button reg = (Button) findViewById(R.id.reg);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, PersonalStats.class);
            startActivity(intent);
            finish();
        }


        newu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    name.setVisibility(View.VISIBLE);
                    reg.setVisibility(View.VISIBLE);
                }else{
                    name.setVisibility(View.INVISIBLE);
                    reg.setVisibility(View.INVISIBLE);
                }
            }
        });


        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin=login.getText().toString();
                userpass=pass.getText().toString();
                authuser(userlogin,userpass);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username= name.getText().toString();
                userlogin=login.getText().toString();
                userpass=pass.getText().toString();
                reguser(userlogin,userpass,username);
            }
        });

    }

    private void reguser(String email, String password, String username) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    myRef.child("Users").child(user.getUid()).child("Name").setValue(username);
                    Intent inten = new Intent(MainActivity.this, PersonalStats.class);
                    startActivity(inten);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    private void authuser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, PersonalStats.class); startActivity(intent); finish();
                } else {
                    Toast.makeText(MainActivity.this, "ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}