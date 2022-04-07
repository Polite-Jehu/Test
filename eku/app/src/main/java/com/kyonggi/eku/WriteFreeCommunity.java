package com.kyonggi.eku;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteFreeCommunity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_free_community);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Button saveButton = (Button) findViewById(R.id.write_free_save);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainFreeCommunity.class);
                int count = PreferenceManagers.getInt(getApplicationContext(), "FreeCommunity_count");
                // Toast.makeText(getApplicationContext(),String.valueOf(count), Toast.LENGTH_SHORT).show();
                count++;
                PreferenceManagers.setInt(getApplicationContext(),"FreeCommunity_count", count);

                String title = "FreeCommunity_title"+count;
                EditText text = findViewById(R.id.write_free_title);
                String titletext = text.getText().toString();
                PreferenceManagers.setString(getApplicationContext(), title, titletext);

                String content = "FreeCommunity_content"+count;
                text = findViewById(R.id.write_free_content);
                String contenttext = text.getText().toString();
                PreferenceManagers.setString(getApplicationContext(), content, contenttext);

                String writer = "FreeCommunity_writer"+count;
                String writertext = "고지웅";
                PreferenceManagers.setString(getApplicationContext(), writer, writertext);

                String building = "";
                String temp = "";
                CheckBox building0 = findViewById(R.id.free_building0);
                temp = building0.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building1 = findViewById(R.id.free_building1);
                temp = building1.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building2 = findViewById(R.id.free_building2);
                temp = building2.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building3 = findViewById(R.id.free_building3);
                temp = building3.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building4 = findViewById(R.id.free_building4);
                temp = building4.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building5 = findViewById(R.id.free_building5);
                temp = building5.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building6 = findViewById(R.id.free_building6);
                temp = building6.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building7 = findViewById(R.id.free_building7);
                temp = building7.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building8 = findViewById(R.id.free_building8);
                temp = building8.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building9 = findViewById(R.id.free_building9);
                temp = building9.isChecked() ? "1" : "0";
                building += temp;


                String buildingtext = "FreeCommunity_building"+count;
                Toast.makeText(getApplicationContext(),buildingtext, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),building, Toast.LENGTH_SHORT).show();
                PreferenceManagers.setString(getApplicationContext(), building, buildingtext);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd hh-mm");
                String time = timeFormat.format(date);
                PreferenceManagers.setString(getApplicationContext(), "FreeCommunity_time" + count, time);

                activityResultLauncher.launch(intent);
                finish();

            }
        });
        Button closeButton = (Button) findViewById(R.id.write_free_close);
        closeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainFreeCommunity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}