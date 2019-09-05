package com.example.admin.techapt;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Pop_up extends AppCompatActivity {


    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.activity_pop_up);

    }

    public void okClicked(View view) {
        Toast.makeText(getApplicationContext(), "cadfdde", Toast.LENGTH_SHORT).show();
    }

    public void cancelClicked(View view) {
        Toast.makeText(getApplicationContext(), "dadafAFF", Toast.LENGTH_SHORT).show();
    }
}
