package com.example.admin.techapt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {


    TextView test_name_textview;
    TextView date_textview;
    TextView marks_textview;
    TextView total_questions_textview;
    //
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef, myRefOption, myRefQue;
    FirebaseUser firebaseUser;
    String date;
    //
    ListView listView;
    ArrayList <InsertCLanguageQue>arrayList;
    ArrayList <InsertCLanguageQue>arrayListView;
    ArrayList <StoreOption> arrayOption;
    ArrayAdapter arrayAdapter;

    TextView question_textview;
    RadioButton opa,opb,opc,opd;
    TextView answer_textview;
    StoreResult storeResult;
    String languagestrdb;
    String language_name_date;

    //Progressbar
    ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //progressBar = findViewById(R.id.resultprogressbar);
        //progressBar.setVisibility(View.VISIBLE);
        test_name_textview = (TextView)findViewById(R.id.testname);
        date_textview = (TextView)findViewById(R.id.testdate);
        marks_textview = (TextView)findViewById(R.id.correctanswers);
        total_questions_textview = (TextView)findViewById(R.id.totalque);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//get Current User

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date = sdf.format(System.currentTimeMillis());

        languagestrdb = getIntent().getStringExtra("Languagedb");
        //Log.e("Language Database",languagestrdb);
        language_name_date = getIntent().getStringExtra("Language&date");
        //Log.e("Language Name/date",language_name_date);
        myRef = mFirebaseDatabase.getReference();//Result
        myRefQue = mFirebaseDatabase.getReference(languagestrdb);
        myRefOption = mFirebaseDatabase.getReference("SolvedTests").child(firebaseUser.getUid()).child(language_name_date).child("Option");
        //Log.e("Option Key",myRefOption.getKey());
        arrayList = new ArrayList<InsertCLanguageQue>();
        arrayOption = new ArrayList<StoreOption>();
        //
        listView = (ListView)findViewById(R.id.list_item);
        arrayListView = new ArrayList<InsertCLanguageQue>();

        progressBar = new ProgressDialog(this);
        progressBar.show();
        myRef.addValueEventListener(new ValueEventListener() {//RESULT DISPLAYS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Log.e("Date snap: ", dataSnapshot.getChildrenCount() + "");
                    storeResult = dataSnapshot.child("SolvedTests").child(firebaseUser.getUid()).child(language_name_date).child("Result").getValue(StoreResult.class);
                    test_name_textview.setText(storeResult.getTest_name());
                    date_textview.setText(storeResult.getDate());
                    marks_textview.setText(storeResult.getMarks() + "");
                    total_questions_textview.setText(storeResult.getTotal_questions() + "");
                }catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        myRefOption.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Log.e("Option Count/key", dataSnapshot.getChildrenCount() + "/"+dataSnapshot.getKey());
                    long c = dataSnapshot.getChildrenCount();
                    for(long l=1;l<=c;l++) {
                        StoreOption storeOption = dataSnapshot.child(l + "").getValue(StoreOption.class);
                        Log.e("Option No."+l+"",storeOption.getOption());
                        arrayOption.add(storeOption);
                    }
                }
                catch (Exception e){Log.e("Error",e.getMessage());}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        myRefQue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long c = dataSnapshot.getChildrenCount();
                for(long l=1;l<=c;l++)
                {
                    InsertCLanguageQue insertCLanguageQue = dataSnapshot.child(l+"").getValue(InsertCLanguageQue.class);
                    arrayList.add(insertCLanguageQue);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        QustionAdapter qustionAdapter = new QustionAdapter(arrayList,arrayOption,this);
        listView.setAdapter(qustionAdapter);
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        progressBar.hide();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void logOut(MenuItem item) {
        finish();
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(ResultActivity.this,LoginActivity.class));
    }

    public void home(MenuItem item) {
        finish();
        startActivity(new Intent(ResultActivity.this,MainActivity.class));
    }

}
