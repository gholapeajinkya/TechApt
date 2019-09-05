package com.example.admin.techapt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class SolvedTests extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference myreftest;
    FirebaseDatabase testdatabase;

    //ListView
    ListView listView;
    ArrayList <String>arrayList;
    ArrayList <String> arrayOption;
    SolvedTestsAdapter testadapter;

    ProgressDialog progressBar;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved_tests);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        testdatabase = FirebaseDatabase.getInstance();

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please Wait...");
        myreftest = testdatabase.getReference("SolvedTests").child(firebaseUser.getUid());
        listView = (ListView)findViewById(R.id.solvedtestlist);
        arrayList = new ArrayList<String>();
        arrayOption = new ArrayList<String>();


        myreftest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("COUNT/KEY",dataSnapshot.getChildrenCount()+"/"+dataSnapshot.getKey());
                //progressBar.show();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String s[] = child.getKey().split("@");;
                    arrayList.add(s[0]);
                    arrayOption.add(s[1]);
                    //Log.e("test/data",arrayList.size()+"/"+arrayOption.size());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
        testadapter = new SolvedTestsAdapter(arrayList,arrayOption,this);
        listView.setAdapter(testadapter);
        //progressBar.dismiss();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext()," Result of "+arrayList.get(i)+" at date "+arrayOption.get(i),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SolvedTests.this,ResultActivity.class);
                intent.putExtra("Language&date",arrayList.get(i)+"@"+arrayOption.get(i));
                switch(arrayList.get(i))
                {
                    case "C Language":
                    {
                        intent.putExtra("Languagedb","CLanguageQuestionsSimple");
                        break;
                    }
                    case "Java Language":
                    {
                        intent.putExtra("Languagedb","JavaQuestionsSimple");
                        break;
                    }
                    case "Data Structure":
                    {
                        intent.putExtra("Languagedb","DataStructureQuestionsSimple");
                        break;
                    }
                    case "Networking":
                    {
                        intent.putExtra("Languagedb","NetworkingQuestionsSimple");
                        break;
                    }
                    case "Database":
                    {
                        intent.putExtra("Languagedb","DatabaseQuestionsSimple");
                        break;
                    }
                    case "Operating System":
                    {
                        intent.putExtra("Languagedb","OperatingsystemQuestionsSimple");
                        break;
                    }
                }
                startActivity(intent);
            }
        });

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                int checkedCount = listView.getCheckedItemCount();
                actionMode.setTitle(checkedCount + " Selected");
                testadapter.toggleSelection(i);

            }
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.solvedtestsmenu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = testadapter.getSelectedIds();

                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                SolvedTests selecteditem = (SolvedTests)testadapter.getItem(selected.keyAt(i));

                            }
                        }
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                testadapter.removeSelection();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void logOut(MenuItem item) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(SolvedTests.this,LoginActivity.class));
    }

    public void home(MenuItem item) {
        finish();
        startActivity(new Intent(SolvedTests.this,MainActivity.class));
    }
    public void searchSolvedTests(MenuItem item) {
        Toast.makeText(getApplicationContext(),"Clicked On Search",Toast.LENGTH_SHORT).show();
    }
}
