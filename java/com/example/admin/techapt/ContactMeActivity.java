package com.example.admin.techapt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class ContactMeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void logOut(MenuItem item) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(ContactMeActivity.this,LoginActivity.class));
    }

    public void home(MenuItem item) {
        finish();
        startActivity(new Intent(ContactMeActivity.this,MainActivity.class));
    }
}
