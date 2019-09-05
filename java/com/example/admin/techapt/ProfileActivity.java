package com.example.admin.techapt;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class ProfileActivity extends AppCompatActivity {

    EditText profile_email;
    EditText profile_first_name;
    EditText profile_last_name;
    EditText profile_phone_no;
    EditText profile_Id;

    Button saveprofile,editprofile;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    String userId;
    FirebaseDatabase mFirebaseDatabase;

    LinearLayout linearLayout;
    ProgressDialog profileprogress;

    //profile Image
    CircularImageView profileImg;
    //
    ImageView edit_profile_imageview;
    private static final int RQ = 1;
    Bitmap bitmap;
    StorageReference storageReference;
    String date;
    //SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int isCustomProfile=-1;
    ProgressDialog progressDialog;

    //Custom Profile Pic
    FirebaseStorage storage;
    StorageReference storageRef;
    Bitmap bitmap2;

    File folder;

    //Custom Dialog
    Dialog myDialog;
    EditText popupfname;
    TextView somthingTextview;
    Button popokbtn;
    Context contextpopup;
    LinearLayout popuplinearLayout;
    public ProfileActivity(Bitmap bitmap) {
        this.bitmap2 = bitmap;
    }

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeComponants();
        contextpopup = this;
        //Custom pop up
        profile_first_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (profile_first_name.getRight() - profile_first_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        try {
                            myDialog.setContentView(R.layout.activity_pop_up);
                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();

                            somthingTextview.setText("First Name");
                            popupfname.setHint("Enter first name");
                            //Toast.makeText(getApplicationContext(),"ICON PRESSED",Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            Log.e("DIALOG ERROR",e.getMessage());
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        //Custom Profile pic sharedpreferences
        profileImg = (CircularImageView)findViewById(R.id.imageView);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();
        //
        profileprogress = new ProgressDialog(this);
        profileprogress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog = new ProgressDialog(this);
        //
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//get Current User
        userId = firebaseUser.getUid();
        profile_email.setText(firebaseUser.getEmail().toString());
        profile_Id.setText(firebaseUser.getUid().toString());
        edit_profile_imageview = findViewById(R.id.edit_profile_image);
        edit_profile_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_PICK);
                in.setType("image/*");
                startActivityForResult(in, RQ);
            }
        });
        //for reading from databasse
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("UserInformation");
        //done reading

        //
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        storageRef = storage.getReferenceFromUrl("gs://technical-aptitude.appspot.com/images").child("profiles").child(firebaseUser.getEmail()).child("/profile.jpg");
        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ".TechApt");
        if (sharedPreferences.getInt("isCustomProfile",-1)==1) {
            if(!folder.exists())
            {
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            //Toast.makeText(getApplicationContext(),"downloaded profile picture successfully\nSize: "+bitmap.getByteCount(),Toast.LENGTH_SHORT).show();
                            //profileImg = (CircularImageView) headerView.findViewById(R.id.imageView);
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
            //storageRef = storage.getReference("images").child("profiles").child(firebaseUser.getEmail()).child("/profile.jpg");
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
                       //profileImg = (CircularImageView) headerView.findViewById(R.id.imageView);
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

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profile_first_name.isEnabled() == false)
                    profile_first_name.setEnabled(true);
                if (profile_last_name.isEnabled() == false)
                    profile_last_name.setEnabled(true);
                if (profile_phone_no.isEnabled() == false)
                    profile_phone_no.setEnabled(true);
                linearLayout.removeView(editprofile);
                linearLayout.addView(saveprofile);
            }
        });
        saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profile_first_name_str = profile_first_name.getText().toString();
                String profile_last_name_str = profile_last_name.getText().toString();
                String profile_phone_no_str = profile_phone_no.getText().toString();
                if (TextUtils.isEmpty(profile_first_name_str)) {
                    Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(profile_last_name_str)) {
                    Toast.makeText(getApplicationContext(), "Please Enter last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(profile_phone_no_str)) {
                    Toast.makeText(getApplicationContext(), "Please Enter phone No", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    setUserProfile();
                }
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readFirstName(dataSnapshot);
                readLastName(dataSnapshot);
                readPhoneNo(dataSnapshot);
                //readProfile(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readFirstName(DataSnapshot dataSnapshot)
    {
        try
        {
            profileprogress.show();
            String fname = "";
            ProfileClass profileClass = new ProfileClass();
            DataSnapshot dataSnapshot1 = dataSnapshot.child(userId);
            profileClass = dataSnapshot1.getValue(ProfileClass.class);
            fname = profileClass.getFirst_name();
            profile_first_name.setText(fname);
            profileprogress.dismiss();
        }
        catch (Exception e){
            Log.e("Message",e.getMessage());
            Toast.makeText(getApplicationContext(), "You haven't updated your first name", Toast.LENGTH_SHORT).show();
        }
    }

    private void readLastName(DataSnapshot dataSnapshot)
    {
        try
        {
            profileprogress.show();
            String lname = "";
            ProfileClass profileClass = new ProfileClass();
            DataSnapshot dataSnapshot1 = dataSnapshot.child(userId);
            profileClass = dataSnapshot1.getValue(ProfileClass.class);
            lname = profileClass.getLast_name();
            profile_last_name.setText(lname);
            profileprogress.dismiss();
        }
        catch (Exception e){
            Log.e("Message",e.getMessage());
            Toast.makeText(getApplicationContext(), "You haven't updated your last name", Toast.LENGTH_SHORT).show();
        }
    }

    private void readPhoneNo(DataSnapshot dataSnapshot)
    {
        try
        {
            profileprogress.show();
            String phoneno = "";
            ProfileClass profileClass = new ProfileClass();
            DataSnapshot dataSnapshot1 = dataSnapshot.child(userId);
            profileClass = dataSnapshot1.getValue(ProfileClass.class);
            phoneno = profileClass.getPhone_no();
            profile_phone_no.setText(phoneno);
            profileprogress.dismiss();
        }
        catch (Exception e){
            Log.e("Message",e.getMessage());
            Toast.makeText(getApplicationContext(), "You haven't updated your phone no", Toast.LENGTH_SHORT).show();
        }
    }

    private void readProfile(DataSnapshot dataSnapshot) {
        try
        {
            //profileprogress.show();
            Log.e("UID:",userId);
            String fname = "";
            String lname = "";
            String phoneno = "";
            ProfileClass profileClass = new ProfileClass();
            DataSnapshot dataSnapshot1 = dataSnapshot.child(userId);
            profileClass = dataSnapshot1.getValue(ProfileClass.class);
            fname = profileClass.getFirst_name();
            //Log.e("First Name: ",fname);
            lname = profileClass.getLast_name();
            Log.e("Last Name: ",lname);
            phoneno = profileClass.getPhone_no();
            //Log.e("Phone No: ",phoneno);
            //Log.e("UIDDD: ",profileClass.getUid());

            profile_first_name.setText(fname);
            profile_last_name.setText(lname);
            profile_phone_no.setText(phoneno);
            //profileprogress.dismiss();
        }
        catch (Exception e){
            Log.e("Message",e.getMessage());
            Toast.makeText(getApplicationContext(), "You haven't updated your profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUserProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String fname = profile_first_name.getText().toString();
        String lname = profile_last_name.getText().toString();
        String phoneno = profile_phone_no.getText().toString();
        ProfileClass profileClass = new ProfileClass(fname,lname,phoneno,firebaseUser.getEmail().toString(),firebaseUser.getUid().toString());
        databaseReference.child("UserInformation").child(firebaseUser.getUid()).setValue(profileClass);
        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
    }

    private void initializeComponants()
    {
        myDialog = new Dialog(ProfileActivity.this);
        myDialog.setContentView(R.layout.activity_pop_up);

        popupfname = new EditText(this);
        popupfname.setPadding(20, 20, 20, 20);
        popuplinearLayout = (LinearLayout)myDialog.findViewById(R.id.editpopuplayout);
        popupfname.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupfname.setHint("Enter First Name");
        popuplinearLayout.addView(popupfname);
        popokbtn = (Button) myDialog.findViewById(R.id.okbtn);

        somthingTextview = myDialog.findViewById(R.id.somthing);

        profile_email = (EditText) findViewById(R.id.profile_email);
        profile_first_name = (EditText) findViewById(R.id.profile_first_name);

        profile_last_name = (EditText) findViewById(R.id.profile_last_name);
        profile_phone_no = (EditText) findViewById(R.id.profile_phone_no);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getInt("isNightModeOn",-1)==1)
        {
            profile_first_name.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_edit_white_24dp, 0);
            profile_last_name.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_edit_white_24dp, 0);
            profile_phone_no.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_edit_white_24dp, 0);
        }
        else {
            profile_first_name.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_edit_black_24dp, 0);
            profile_last_name.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_edit_black_24dp, 0);
            profile_phone_no.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_edit_black_24dp, 0);
        }

        profile_Id = (EditText) findViewById(R.id.profile_id);
        saveprofile = new Button(this);//(Button) findViewById(R.id.save_profile);
        saveprofile.setText("Save Profile");
        linearLayout = (LinearLayout)findViewById(R.id.edit_save_layout);

        saveprofile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        saveprofile.setTextColor(Color.WHITE);
        saveprofile.setTypeface(null, Typeface.BOLD);
        saveprofile.setBackgroundResource(R.drawable.buttonborder);
        saveprofile.setPadding(15,0,15,0);
        editprofile = (Button) findViewById(R.id.edit_profile);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void logOut(MenuItem item) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
    }
    public void home(MenuItem item) {
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    }

    public void cancelClicked(View view) {
        myDialog.dismiss();
    }

    public void okClicked(View view) {
        if(popupfname.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            ProfileClass profileClass = new ProfileClass(popupfname.getText().toString(),firebaseUser.getUid());
            databaseReference.child("UserInformation").child(firebaseUser.getUid()).setValue(profileClass);
            myDialog.dismiss();
        }
        Toast.makeText(getApplicationContext(),popupfname.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        InputStream stream = null;
        progressDialog.show();
        if(requestCode == RQ && resultCode == Activity.RESULT_OK){
            try{
                if(bitmap != null){
                    bitmap.recycle();
                }
                Uri uri = data.getData();
                File file= new File(uri.getPath());
                file.getName();
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);


                //StorageReference mstorageReference =  storageReference.child("images/profiles/"+userId+"/");
                StorageReference mstorageReference =  storageReference.child("images").child("profiles").child(firebaseUser.getEmail()).child("/profile.jpg");
                mstorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        editor.putInt("isCustomProfile", 1);
                        editor.commit();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Unable to upload...",Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

                try {
                    folder = new File(Environment.getExternalStorageDirectory(), ".TechApt");
                    if(!folder.exists())
                    {
                        folder.mkdir();
                    }
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.TechApt", "profile.jpg");
                    //if (!file.exists()) {
                        Log.d("path", file.toString());
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    //}
                }catch (Exception e){}

                profileImg.setImageBitmap(bitmap);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'at'HH:mm:ss");
                date = sdf.format(System.currentTimeMillis());
                //mstorageReference =  storageReference.child("images/users/"+userId+"/"+date+".jpg");
                mstorageReference =  storageReference.child("images").child("users").child(firebaseUser.getEmail()).child(date+".jpg");
                mstorageReference.putFile(uri);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            finally {
                if(stream != null)
                    try{
                        stream.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
            }
        }
    }
}
