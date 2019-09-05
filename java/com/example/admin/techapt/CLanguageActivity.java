package com.example.admin.techapt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CLanguageActivity extends AppCompatActivity {


    DatabaseReference databaseReference,reference;
    FirebaseAuth firebaseAuth;
    Button Insert_Que_Button,nextbtn,prebtn;
    FirebaseDatabase firebaseDatabase;
    ArrayList <InsertCLanguageQue>arrayList;
    int index = 0;
    long c=0;

    Button created;
    LinearLayout linearLayout;
    EditText que_edittext,opA_edittext,opB_edittext,opC_edittext,opD_edittext,ans_edittext,queno_edittext;
    TextView que_count_clang;
    String language_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clanguage);

        language_name = getIntent().getStringExtra("LanguageName");
        que_edittext = (EditText)findViewById(R.id.question);
        opA_edittext = (EditText)findViewById(R.id.op_A);
        opB_edittext = (EditText)findViewById(R.id.op_B);
        opC_edittext = (EditText)findViewById(R.id.op_C);
        opD_edittext = (EditText)findViewById(R.id.op_D);
        ans_edittext = (EditText)findViewById(R.id.answer);
        queno_edittext = (EditText)findViewById(R.id.question_no);
        que_count_clang = (TextView)findViewById(R.id.que_count_clang);
        created = new Button(this);
        created.setText("Insert");
        created.setTextColor(Color.WHITE);
        created.setTypeface(null, Typeface.BOLD);
        created.setBackgroundResource(R.drawable.buttonborder);
        created.setPadding(15,0,15,0);
        created.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearLayout = findViewById(R.id.layout);
        //Adding C Language MCQ Questions
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference(language_name);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Insert_Que_Button = (Button)findViewById(R.id.insert_que);
        Insert_Que_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                que_edittext.setEnabled(true);
                queno_edittext.setEnabled(true);
                opA_edittext.setEnabled(true);
                opB_edittext.setEnabled(true);
                opC_edittext.setEnabled(true);
                opD_edittext.setEnabled(true);
                ans_edittext.setEnabled(true);

                que_edittext.setText("");
                queno_edittext.setText("");;
                opA_edittext.setText("");
                opB_edittext.setText("");
                opC_edittext.setText("");
                opD_edittext.setText("");
                ans_edittext.setText("");

                linearLayout.removeView(Insert_Que_Button);
                linearLayout.addView(created);
                created.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String que = que_edittext.getText().toString();
                        String A = opA_edittext.getText().toString();
                        String B = opB_edittext.getText().toString();
                        String C = opC_edittext.getText().toString();
                        String D = opD_edittext.getText().toString();
                        String ans = ans_edittext.getText().toString();
                        String q_no = queno_edittext.getText().toString();

                        if(TextUtils.isEmpty(que)){
                            Toast.makeText(getApplicationContext(), "Please Enter Question", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(A)){
                            Toast.makeText(getApplicationContext(), "Please Enter Option A", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(B)){
                            Toast.makeText(getApplicationContext(), "Please Enter Option B", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(C)){
                            Toast.makeText(getApplicationContext(), "Please Enter Option C", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(D)){
                            Toast.makeText(getApplicationContext(), "Please Enter Option D", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(ans)){
                            Toast.makeText(getApplicationContext(), "Please Enter Answer", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(q_no+"")){
                            Toast.makeText(getApplicationContext(), "Please Enter Question Number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            InsertCLanguageQue insertCLanguageQue = new InsertCLanguageQue(que, A, B, C, D, ans, Integer.parseInt(q_no));
                            databaseReference.child(language_name).child("" + q_no).setValue(insertCLanguageQue);
                            Toast.makeText(getApplicationContext(), "Question Uploaded", Toast.LENGTH_SHORT).show();
                            que_edittext.setEnabled(false);
                            queno_edittext.setEnabled(false);
                            opA_edittext.setEnabled(false);
                            opB_edittext.setEnabled(false);
                            opC_edittext.setEnabled(false);
                            opD_edittext.setEnabled(false);
                            ans_edittext.setEnabled(false);
                        }
                    }
                });
            }
        });
        //for reading All Question
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference(language_name);
        nextbtn = (Button)findViewById(R.id.next);
        prebtn = (Button)findViewById(R.id.pre);
        arrayList = new ArrayList<InsertCLanguageQue>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c = dataSnapshot.getChildrenCount();
                for(long l=1;l<=c;l++)
                {
                    InsertCLanguageQue insertCLanguageQue = dataSnapshot.child(l+"").getValue(InsertCLanguageQue.class);
                    arrayList.add(insertCLanguageQue);
                    if(l==1)
                    {
                        queno_edittext.setText(insertCLanguageQue.getQuestion_no() + "");
                        que_edittext.setText(insertCLanguageQue.getQuestion());
                        opA_edittext.setText(insertCLanguageQue.getA());
                        opB_edittext.setText(insertCLanguageQue.getB());
                        opC_edittext.setText(insertCLanguageQue.getC());
                        opD_edittext.setText(insertCLanguageQue.getD());
                        ans_edittext.setText(insertCLanguageQue.getAnswer());
                        que_count_clang.setText(insertCLanguageQue.getQuestion_no()+"/"+c);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    index += 1;
                    Log.e("Value C: ",c+"");
                    if (index > Integer.parseInt(c+"")-1) {
                        index = (Integer.parseInt(c+"")-1);
                        Log.e("index/count",index+"/"+c);
                    }
                    InsertCLanguageQue insertCLanguageQue = arrayList.get(index);
                    que_count_clang.setText(insertCLanguageQue.getQuestion_no()+"/"+c);
                    queno_edittext.setText(insertCLanguageQue.getQuestion_no() + "");
                    que_edittext.setText(insertCLanguageQue.getQuestion());
                    opA_edittext.setText(insertCLanguageQue.getA());
                    opB_edittext.setText(insertCLanguageQue.getB());
                    opC_edittext.setText(insertCLanguageQue.getC());
                    opD_edittext.setText(insertCLanguageQue.getD());
                    ans_edittext.setText(insertCLanguageQue.getAnswer());
                }
                catch (Exception e){Log.e("Error Message: ",e.getMessage());}
            }
        });

        prebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (index < 1) {
                        Log.e("index/count",index+"/"+c);
                        index = 1;
                    }
                    index -= 1;
                    InsertCLanguageQue insertCLanguageQue = arrayList.get(index);
                    que_count_clang.setText(insertCLanguageQue.getQuestion_no()+"/"+c);
                    queno_edittext.setText(insertCLanguageQue.getQuestion_no() + "");
                    que_edittext.setText(insertCLanguageQue.getQuestion());
                    opA_edittext.setText(insertCLanguageQue.getA());
                    opB_edittext.setText(insertCLanguageQue.getB());
                    opC_edittext.setText(insertCLanguageQue.getC());
                    opD_edittext.setText(insertCLanguageQue.getD());
                    ans_edittext.setText(insertCLanguageQue.getAnswer());
                }
                catch (Exception e){Log.e("Error Message: ",e.getMessage());}
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void logOut(MenuItem item) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(CLanguageActivity.this,LoginActivity.class));
    }
    public void home(MenuItem item) {
        finish();
        startActivity(new Intent(CLanguageActivity.this,MainActivity.class));
    }
}
