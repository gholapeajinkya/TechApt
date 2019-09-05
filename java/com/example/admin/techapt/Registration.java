package com.example.admin.techapt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    Button register;
    EditText email;
    EditText password;
    TextView alreadyReg;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        /*if(firebaseUser!=null && firebaseUser.getEmail()!="ajinkyagholape1998@hotmail.com")
        {
                Intent i = new Intent(Registration.this, MainActivity.class);
                startActivity(i);
        }*/
        email = (EditText)findViewById(R.id.email_edittext);
        password = (EditText)findViewById(R.id.password_edittext);
        alreadyReg = (TextView)findViewById(R.id.alreadyreg_textview);

        progressDialog = new ProgressDialog(this);

        alreadyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open login activity
                startActivity(new Intent(Registration.this,LoginActivity.class));
                finish();
            }
        });

        register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }


    private void registerUser() {
        String email_str = email.getText().toString();
        String password_str = password.getText().toString();

        if (TextUtils.isEmpty(email_str)) {
            Toast.makeText(getApplicationContext(), "Please Enter Email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password_str)) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password_str.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password Should Contain more than 5 characters", Toast.LENGTH_SHORT).show();
        }
        if(firebaseAuth.isSignInWithEmailLink(email_str))
        {
            Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email_str, password_str).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Registration.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
            });
        }

    }
}
