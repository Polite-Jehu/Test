package com.kyonggi.eku;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WriteAnnounce extends AppCompatActivity {
    /*
     *
     * 제목
     * 공지 게시판 작성
     * 기능
     * ㅈㄱㄴ
     * */
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_announce);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Button saveButton = (Button) findViewById(R.id.write_announce_save);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainCommunity.class);
                int count = PreferenceManagers.getInt(getApplicationContext(), "announce_count");
                // Toast.makeText(getApplicationContext(),String.valueOf(count), Toast.LENGTH_SHORT).show();
                count++;
                PreferenceManagers.setInt(getApplicationContext(),"announce_count", count);


                String title = "announce_title"+count;
                EditText text = findViewById(R.id.write_announce_title);
                String titletext = text.getText().toString();
                PreferenceManagers.setString(getApplicationContext(), title, titletext);

                String content = "announce_content"+count;
                text = findViewById(R.id.write_announce_content);
                String contenttext = text.getText().toString();
                PreferenceManagers.setString(getApplicationContext(), content, contenttext);

                String writer = "announce_writer"+count;
                String writertext = "고지웅";
                PreferenceManagers.setString(getApplicationContext(), writer, writertext);

                String building = "";
                String temp = "";
                CheckBox building0 = findViewById(R.id.building0);
                temp = building0.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building1 = findViewById(R.id.building1);
                temp = building1.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building2 = findViewById(R.id.building2);
                temp = building2.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building3 = findViewById(R.id.building3);
                temp = building3.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building4 = findViewById(R.id.building4);
                temp = building4.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building5 = findViewById(R.id.building5);
                temp = building5.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building6 = findViewById(R.id.building6);
                temp = building6.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building7 = findViewById(R.id.building7);
                temp = building7.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building8 = findViewById(R.id.building8);
                temp = building8.isChecked() ? "1" : "0";
                building += temp;
                CheckBox building9 = findViewById(R.id.building9);
                temp = building9.isChecked() ? "1" : "0";
                building += temp;


                String buildingtext = "announce_building"+count;
                PreferenceManagers.setString(getApplicationContext(), building, buildingtext);

                /* 백에서 시간을 저장
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd hh-mm");
                String time = timeFormat.format(date);
                PreferenceManagers.setString(getApplicationContext(), "announce_time" + count, time);
                 */

                Handler handler = new Handler() {
                    public void handleMessage(@NonNull Message msg){
                        switch (msg.what){
                            case 0 :
                                String responseResult = (String) msg.obj;
                                Toast.makeText(getApplicationContext(),responseResult,Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                SendTool sendTool = new SendTool(handler);
                HashMap<String,String> temp2 = new HashMap<>();


                temp2.put("content","서버전송 공지 test");
                temp2.put("writer","201713924");
                temp2.put("articleID","8");

                try {
                    sendTool.request(" www.eku.kro.kr/comment/info/write","POST",temp2);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                activityResultLauncher.launch(intent);
                finish();

            }
        });
        Button closeButton = (Button) findViewById(R.id.write_announce_close);
        closeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainCommunity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}