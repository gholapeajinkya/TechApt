package com.example.admin.techapt;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class SettingsActivity extends AppCompatActivity {

    SwitchCompat night_mode;
    TextView theme;
    boolean isnightmodeon=false;
    Context context;
    AppCompatAutoCompleteTextView textView;
    //SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int isNightModeOn=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();
        if (sharedPreferences.contains("isNightModeOn")) {
            isNightModeOn = sharedPreferences.getInt("isNightModeOn", -1);
        }

        night_mode = findViewById(R.id.nightmode);
        if(isNightModeOn==1)
            night_mode.setChecked(true);
        theme = findViewById(R.id.theme_textview);
        night_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    nightMode(b);
            }
        });
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Choose color")
                        .initialColor(Color.RED)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                               // changeBackgroundColor(selectedColor);
                                Toast.makeText(getApplicationContext(),Integer.toHexString(selectedColor)+"",Toast.LENGTH_SHORT).show();
                                context.setTheme(R.style.newTheme);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
    }


    private void nightMode(boolean b) {
        night_mode.setChecked(b);
        if(b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(getApplicationContext(), "Night Mode On", Toast.LENGTH_SHORT).show();
            editor.putInt("isNightModeOn", 1);
            editor.commit();
            //startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(getApplicationContext(), "Night Mode Off", Toast.LENGTH_SHORT).show();
            editor.putInt("isNightModeOn", 0);
            editor.commit();

            //startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        }
    }
}
