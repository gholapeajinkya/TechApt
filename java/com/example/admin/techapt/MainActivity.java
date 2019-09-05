package com.example.admin.techapt;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference databaseReference;
    String email="";
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView navtitle, navtitle2;
    CardView CLANG,JAVALANG,DS, NET,DB,OS;
    CircularImageView profileImg;
    //
    ImageView clangImageview,javalangImageView,dsImageView,netImageView,dbImageView,osImageView;
    TextView clangTextview,javalangTextView,dsTextView,netTextView,dbTextView,osTextView;

    //SOLVED TESTSLISTVIEW
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList <String> arrayOption;
    DatabaseReference myreftest;
    FirebaseDatabase testdatabase;
    SolvedTestsAdapter testadapter;

    //IMAGE SLIDER
    ViewPager viewPager;
    int images[] = {R.drawable.image_4,R.drawable.image_5};
    ImageSliderAdapter imageSliderAdapter;

    //INTERNET CONNECTION AVALIABLE OR NOT
    boolean connected = false;
    //SETTINGS
    SwitchPreference switchPreference;
    //SharedPreferences
    SharedPreferences sharedPreferences;


    SharedPreferences.Editor editor;
    //Snackbar
    ScrollView scrollView;
    //Custom Profile Pic
    FirebaseStorage storage;
    StorageReference storageRef;
    Bitmap bitmap;
    File folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else
            {
                scrollView = findViewById(R.id.mainactivity_scrollview);
                sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                editor = sharedPreferences.edit();
                if (sharedPreferences.getInt("isNightModeOn",-1)==1) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                //Snackbar.make(scrollView,"Message",12),Snackbar.LENGTH_SHORT);
                viewPager = (ViewPager)findViewById(R.id.viewPager);
                imageSliderAdapter = new ImageSliderAdapter(this,images);
                viewPager.setAdapter(imageSliderAdapter);
                firebaseUser = firebaseAuth.getCurrentUser();
                listView = (ListView)findViewById(R.id.solvedtestlist);

                testdatabase = FirebaseDatabase.getInstance();
                myreftest = testdatabase.getReference("SolvedTests").child(firebaseUser.getUid());
                arrayList = new ArrayList<String>();
                arrayOption = new ArrayList<String>();
                myreftest.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e("COUNT/KEY",dataSnapshot.getChildrenCount()+"/"+dataSnapshot.getKey());
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String s[] = child.getKey().split("@");;
                            arrayList.add(s[0]);
                            arrayOption.add(s[1]);
                            //Log.e("test/data",arrayList.size()+"/"+arrayOption.size());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                testadapter = new SolvedTestsAdapter(arrayList,arrayOption,this);
                //listView.setAdapter(testadapter);ERROR OCCURED BECAUSE OF THIS LINE
            clangImageview = (ImageView) findViewById(R.id.clanguage_imageview);
            clangImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                        Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                        i.putExtra("LanguageName","CLanguageQuestionsSimple");
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                        i.putExtra("Languagedb","CLanguageQuestionsSimple");
                        startActivity(i);
                    }
                }
            });
            clangTextview = (TextView) findViewById(R.id.clanguage_textview);
            clangTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                        Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                        i.putExtra("LanguageName","CLanguageQuestionsSimple");
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                        i.putExtra("Languagedb","CLanguageQuestionsSimple");
                        startActivity(i);
                    }
                }
            });
            CLANG = (CardView) findViewById(R.id.c_cardview);
            CLANG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                        Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                        i.putExtra("LanguageName","CLanguageQuestionsSimple");
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                        i.putExtra("Languagedb","CLanguageQuestionsSimple");
                        startActivity(i);
                    }
                }
            });

            //FOR JAVA LANGUAGE
                javalangImageView = (ImageView) findViewById(R.id.javalanguage_imageview);
                javalangImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","JavaQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","JavaQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                javalangTextView = (TextView) findViewById(R.id.javalanguage_textview);
                javalangTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","JavaQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","JavaQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                JAVALANG = (CardView) findViewById(R.id.java_cardview);
                JAVALANG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","JavaQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","JavaQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                //TILL THIS FOR JAVA LANGUAGE

                //FOR DATA STRUCTURE
                dsImageView = (ImageView) findViewById(R.id.datastructure_imageview);
                dsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","DataStructureQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","DataStructureQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                dsTextView = (TextView) findViewById(R.id.datastructure_textview);
                dsTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","DataStructureQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","DataStructureQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                DS = (CardView) findViewById(R.id.datastructure_cardview);
                DS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","DataStructureQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","DataStructureQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });

                //FOR NETWORKING
                netImageView = (ImageView) findViewById(R.id.networking_imageview);
                netImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","NetworkingQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","NetworkingQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                netTextView = (TextView) findViewById(R.id.networking_textview);
                netTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","NetworkingQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","NetworkingQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                NET = (CardView) findViewById(R.id.networking_cardview);
                NET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","NetworkingQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","NetworkingQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });

                //For database
                dbImageView = (ImageView) findViewById(R.id.database_imageview);
                dbImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","DatabaseQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","DatabaseQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                dbTextView = (TextView) findViewById(R.id.database_textview);
                dbTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","DatabaseQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","DatabaseQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                DB = (CardView) findViewById(R.id.database_cardview);
                DB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","DatabaseQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","DatabaseQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                //for Operating System
                osImageView = (ImageView) findViewById(R.id.operatingsystem_imageview);
                osImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","OperatingsystemQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","OperatingsystemQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                osTextView = (TextView) findViewById(R.id.operatingsystem_textview);
                osTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","OperatingsystemQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","OperatingsystemQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                OS = (CardView) findViewById(R.id.operatingsystem_cardview);
                OS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com")) {
                            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
                            i.putExtra("LanguageName","OperatingsystemQuestionsSimple");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
                            i.putExtra("Languagedb","OperatingsystemQuestionsSimple");
                            startActivity(i);
                        }
                    }
                });
                //
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, ""+sharedPreferences.getInt("isNightModeOn",12), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            final View headerView = navigationView.getHeaderView(0);
            profileImg = (CircularImageView) headerView.findViewById(R.id.imageView);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            email = getIntent().getStringExtra("Email");
            folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ".TechApt");
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReferenceFromUrl("gs://technical-aptitude.appspot.com/images").child("profiles").child(firebaseUser.getEmail()).child("/profile.jpg");
            if (sharedPreferences.getInt("isCustomProfile",-1)==1) {
                if(!folder.exists())
                {
                    try {
                        final File localFile = File.createTempFile("images", "jpg");
                        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                profileImg.setImageBitmap(bitmap);
                                ProfileActivity p = new ProfileActivity(bitmap);

                                folder.mkdir();
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.TechApt", "profile.jpg");
                                //if (!file.exists()) {
                                Log.d("path", file.toString());
                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                    fos.flush();
                                    fos.close();
                                    Toast.makeText(getApplicationContext(),"Pic Stored...",Toast.LENGTH_LONG).show();
                                } catch (java.io.IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(),"Unable to set profile picture",Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException e ) {}
                }
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/.TechApt/profile.jpg");
                profileImg.setImageBitmap(bitmap);
            }
            else if(!folder.exists())
            {
                try {
                final File localFile = File.createTempFile("images", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        profileImg.setImageBitmap(bitmap);
                        ProfileActivity p = new ProfileActivity(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),"Unable to set profile picture",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e ) {
                    Glide.with(this)
                            .load(firebaseUser.getPhotoUrl())
                            .into(profileImg);
                }
            }
            else {
                //profileImg = (CircularImageView) headerView.findViewById(R.id.imageView);
                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(profileImg);
            }
            navtitle = (TextView) headerView.findViewById(R.id.nav_title);
            navtitle.setText(firebaseUser.getDisplayName());
            navtitle2 = (TextView) headerView.findViewById(R.id.nav_title2);
            navtitle2.setText(firebaseUser.getEmail());
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_profile) {
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signin) {
            startActivity(new Intent(MainActivity.this,Registration.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        } else if (id == R.id.nav_share) {
            shareIt();
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this,ContactMeActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ClickedOnProfile(MenuItem item) {
        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
        i.putExtra("Email",email);
        startActivity(i);
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TechApt");
        String link;
        //link= "https://drive.google.com/open?id=14U8c22FVoSgjhGCvRCKiqfTsWkar5fRi";
        link = "https://drive.google.com/open?id=1I6bELMYpmpTnvc1bwoOrtx2hlhfLJgtd";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Technical Aptitude with TechApt click here to "+link);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void ClickedOnSolvedTests(MenuItem item) {
        Intent i = new Intent(MainActivity.this,SolvedTests.class);
        startActivity(i);
    }

    public void cLanguage(View view) {
        if(firebaseUser.getEmail().equals("ajinkyagholape1998@hotmail.com"))
        {
            Intent i = new Intent(MainActivity.this, CLanguageActivity.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(MainActivity.this, CLanguageQuestionsActivity.class);
            startActivity(i);
        }
    }

    public void logOut(MenuItem item) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    public void home(MenuItem item) {
        finish();
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }
}
