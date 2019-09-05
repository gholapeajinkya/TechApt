package com.example.admin.techapt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CLanguageQuestionsActivity extends AppCompatActivity {

    TextView question_textview,question_count;
    RadioButton optionA[],optionB[],optionC[],optionD[];
    Button next_que,pre_que;
    Button submit_ans;

    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef, myRefOption, myRefResult;
    FirebaseUser firebaseUser;
    DataSnapshot dataSnap;
    ArrayList <InsertCLanguageQue> arrayList;
    //long i;
    int index = 0;
    long c;
    long size;
    LinearLayout linearLayout,finishtestlayout;
    RadioGroup radioGroup[];
    String compare_answer;
    int correct_answers=0;
    Button finish_btn;
    String date;
    String language_questions;
    String LANGUAGE;
    Context context;
    //SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clanguage_questions);
        context = this;
        language_questions = getIntent().getStringExtra("Languagedb");
        /*
        if (language_questions.equals("CLanguageQuestionsSimple"))
        {
            LANGUAGE = "C Language";
        }
        else if (language_questions.equals("JavaQuestionsSimple"))
        {
            LANGUAGE = "Java Language";
        }
        else if (language_questions.equals("DataStructureQuestionsSimple"))
        {
            LANGUAGE = "Data Structure";
        }
        else if (language_questions.equals("NetworkingQuestionsSimple"))
        {
            LANGUAGE = "Networking";
        }
        else if (language_questions.equals("DatabaseQuestionsSimple"))
        {
            LANGUAGE = "Database";
        }
        else if (language_questions.equals("OperatingsystemQuestionsSimple"))
        {
            LANGUAGE = "Operating System";
        }
        /*switch(language_questions)
        {
            case "CLanguageQuestionsSimple": {
                LANGUAGE = "C Language";
            }
            case "JavaLanguageQuestionsSimple": {
                LANGUAGE = "Java Language";
            }
        }*/

        switch(language_questions)
        {
            case "CLanguageQuestionsSimple":
            {
                LANGUAGE = "C Language";
                break;
            }
            case "JavaQuestionsSimple":
            {
                LANGUAGE = "Java Language";
                break;
            }
            case "DataStructureQuestionsSimple":
            {
                LANGUAGE = "Data Structure";
                break;
            }
            case "NetworkingQuestionsSimple":
            {
                LANGUAGE = "Networking";
                break;
            }
            case "OperatingsystemQuestionsSimple":
            {
                LANGUAGE = "Operating System";
                break;
            }
            case "DatabaseQuestionsSimple":
            {
                LANGUAGE = "Database";
                break;
            }
        }

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        question_textview = (TextView)findViewById(R.id.question);
        question_count = (TextView)findViewById(R.id.que_count);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();
        next_que = (Button)findViewById(R.id.next_button);
        if(sharedPreferences.getInt("isNightModeOn",-1)==1)
        {
            next_que.setBackgroundResource(R.drawable.ic_navigate_next_white_24dp);
        }
        pre_que = (Button)findViewById(R.id.pre_button);
        if(sharedPreferences.getInt("isNightModeOn",-1)==1)
        {
            pre_que.setBackgroundResource(R.drawable.ic_navigate_before_white_24dp);
        }
        submit_ans = (Button)findViewById(R.id.submit_answer);
        linearLayout = (LinearLayout)findViewById(R.id.radio_button_layout);

        //PROGRESS BAR SHOULD START FROM HERE
        spinner.setVisibility(View.VISIBLE);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference(language_questions);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//get Current User

        arrayList = new ArrayList<InsertCLanguageQue>();

        myRefOption = FirebaseDatabase.getInstance().getReference();
        myRefResult = FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date = sdf.format(System.currentTimeMillis());

        finish_btn = new Button(this);//(Button) findViewById(R.id.save_profile);
        finish_btn.setText("Finish Test");
        finishtestlayout = (LinearLayout)findViewById(R.id.finishtest);
        finish_btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        finish_btn.setTextColor(Color.WHITE);
        finish_btn.setTypeface(null, Typeface.BOLD);
        finish_btn.setBackgroundResource(R.drawable.buttonborder);
        finish_btn.setPadding(20,0,20,0);
        submit_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertCLanguageQue insertCLanguageQue = arrayList.get(index);
                if(optionA[index].isChecked())
                {
                    compare_answer = optionA[index].getText().toString();
                }
                if(optionB[index].isChecked())
                {
                    compare_answer = optionB[index].getText().toString();
                }
                if(optionC[index].isChecked())
                {
                    compare_answer = optionC[index].getText().toString();
                }
                if(optionD[index].isChecked())
                {
                    compare_answer = optionD[index].getText().toString();
                }
                if (insertCLanguageQue.getAnswer().equals(compare_answer))
                {
                    correct_answers++;
                    //Toast.makeText(getApplicationContext(),"You Are Correct",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(),"You Are Wrong",Toast.LENGTH_SHORT).show();
                    //Log.e("Answer/correct answer",compare_answer+"/"+insertCLanguageQue.getAnswer());
                }
                Toast.makeText(getApplicationContext(),"You Choose "+compare_answer,Toast.LENGTH_SHORT).show();
                StoreOption storeOption = new StoreOption(index+1,compare_answer);
                myRefOption.child("SolvedTests").child(firebaseUser.getUid()).child(LANGUAGE+"@"+date).child("Option").child((index+1)+"").setValue(storeOption);
            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Date: ",date);
                StoreResult storeResult = new StoreResult(LANGUAGE,date,correct_answers,Integer.parseInt(c+""));
                myRefResult.child("SolvedTests").child(firebaseUser.getUid()).child(LANGUAGE+"@"+date).child("Result").setValue(storeResult);
                Intent i = new Intent(CLanguageQuestionsActivity.this,ResultActivity.class);
                i.putExtra("Language&date",LANGUAGE+"@"+date);
                i.putExtra("Languagedb",language_questions);
                finish();
                startActivity(i);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;
                size = dataSnapshot.getChildrenCount();
                Log.e("questions_size/key",size+"/"+dataSnapshot.getKey());
                getQuestions(dataSnapshot);
                radioGroup = new RadioGroup[Integer.parseInt(size+"")];
                optionA = new RadioButton[Integer.parseInt(size+"")];
                optionB = new RadioButton[Integer.parseInt(size+"")];
                optionC = new RadioButton[Integer.parseInt(size+"")];
                optionD = new RadioButton[Integer.parseInt(size+"")];

                for(int i=0;i<Integer.parseInt(c+"");i++)
                {
                    optionA[i] = new RadioButton(context);
                    optionB[i] = new RadioButton(context);
                    optionC[i] = new RadioButton(context);
                    optionD[i] = new RadioButton(context);
                    radioGroup[i] = new RadioGroup(context);
                    if(i==0)
                    {
                        InsertCLanguageQue insertCLanguageQue = arrayList.get(i);
                        optionA[i].setText(insertCLanguageQue.getA());
                        optionB[i].setText(insertCLanguageQue.getB());
                        optionC[i].setText(insertCLanguageQue.getC());
                        optionD[i].setText(insertCLanguageQue.getD());
                        radioGroup[i].addView(optionA[i]);
                        radioGroup[i].addView(optionB[i]);
                        radioGroup[i].addView(optionC[i]);
                        radioGroup[i].addView(optionD[i]);
                        linearLayout.addView(radioGroup[i]);
                    }
                }
                question_count.setText((index+1)+"/"+size);
                //PROGRESS BAR SHOULD END FROM HERE
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        next_que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    if(index+1+1 == Integer.parseInt(c+""))
                    {
                        finishtestlayout.addView(finish_btn);
                    }
                    radioGroup[index].removeView(optionA[index]);
                    radioGroup[index].removeView(optionB[index]);
                    radioGroup[index].removeView(optionC[index]);
                    radioGroup[index].removeView(optionD[index]);
                    index += 1;
                    if (index > Integer.parseInt(c+"")-1) {
                        index = (Integer.parseInt(c+"")-1);
                    }
                    InsertCLanguageQue insertCLanguageQue = arrayList.get(index);
                    question_count.setText(insertCLanguageQue.getQuestion_no()+"/"+size);

                    question_textview.setText("Q."+insertCLanguageQue.getQuestion_no()+" "+insertCLanguageQue.getQuestion());
                    Log.e(LANGUAGE,insertCLanguageQue.getQuestion());
                    optionA[index].setText(insertCLanguageQue.getA());
                    optionB[index].setText(insertCLanguageQue.getB());
                    optionC[index].setText(insertCLanguageQue.getC());
                    optionD[index].setText(insertCLanguageQue.getD());

                    radioGroup[index].addView(optionA[index]);
                    radioGroup[index].addView(optionB[index]);
                    radioGroup[index].addView(optionC[index]);
                    radioGroup[index].addView(optionD[index]);
                    linearLayout.addView(radioGroup[index]);
                }
                catch (Exception e){
                    Log.e("Error Message: ",e.getMessage());
                }
            }
        });
        pre_que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finishtestlayout.removeView(finish_btn);
                    if (index < 1) {
                        //Log.e("index/count",index+"/"+c);
                        index = 1;
                    }
                    radioGroup[index].removeView(optionA[index]);
                    radioGroup[index].removeView(optionB[index]);
                    radioGroup[index].removeView(optionC[index]);
                    radioGroup[index].removeView(optionD[index]);

                    index -= 1;
                    InsertCLanguageQue insertCLanguageQue = arrayList.get(index);
                    question_count.setText(insertCLanguageQue.getQuestion_no()+"/"+size);
                    question_textview.setText("Q."+insertCLanguageQue.getQuestion_no()+" "+insertCLanguageQue.getQuestion());
                    radioGroup[index].addView(optionA[index]);
                    radioGroup[index].addView(optionB[index]);
                    radioGroup[index].addView(optionC[index]);
                    radioGroup[index].addView(optionD[index]);
                    linearLayout.addView(radioGroup[index]);

                }
                catch (Exception e){
                    //Log.e("Error Message: ",e.getMessage());
                    }
            }
        });
    }

    private void getQuestions(DataSnapshot dataSnapshot) {

        //Log.e("Count: ", dataSnapshot.getChildrenCount() + "");
        //Log.e("Key: ", dataSnapshot.getKey());
        c = dataSnapshot.getChildrenCount();
        //progressDialog.setMessage("Please Wait....");
        for(long l=1;l<=c;l++)
        {
            InsertCLanguageQue insertCLanguageQue = dataSnapshot.child(l+"").getValue(InsertCLanguageQue.class);
            arrayList.add(insertCLanguageQue);
            if(l==1)
            {
                question_textview.setText("Q."+insertCLanguageQue.getQuestion_no()+" "+insertCLanguageQue.getQuestion());
                Log.e(LANGUAGE,insertCLanguageQue.getQuestion());

            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void logOut(MenuItem item) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(CLanguageQuestionsActivity.this,LoginActivity.class));
    }
    public void home(MenuItem item) {
        finish();
        startActivity(new Intent(CLanguageQuestionsActivity.this,MainActivity.class));
    }
}
