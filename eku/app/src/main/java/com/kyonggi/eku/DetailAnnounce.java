package com.kyonggi.eku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DetailAnnounce extends AppCompatActivity {

    /**
     * 공지 세부화면
     */
    String[] items = {"1강의동","2강의동","3강의동","4강의동","5강의동","6강의동","7강의동","8강의동","9강의동","제2공학관"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_announce);

        Spinner spinner = (Spinner)findViewById(R.id.detail_announce_Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //선택
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //없음
            }
        });

        Intent intent = getIntent();
        int Lid = intent.getIntExtra("announce_key",-1);

        TextView textView = (TextView)findViewById(R.id.detail_announce_title);
        String text = PreferenceManagers.getString(getApplicationContext(), "announce_title"+Lid);
        textView.setText(text);

        textView = (TextView)findViewById(R.id.detail_announce_content);
        text = PreferenceManagers.getString(getApplicationContext(), "announce_content"+Lid);
        textView.setText(text);

        textView = (TextView)findViewById(R.id.detail_announce_writer);
        text = PreferenceManagers.getString(getApplicationContext(), "announce_writer"+Lid);
        textView.setText(text);

        textView = (TextView)findViewById(R.id.detail_announce_time);
        text = PreferenceManagers.getString(getApplicationContext(), "announce_time"+Lid);
        textView.setText(text);

        textView = (TextView)findViewById(R.id.detail_announce_building);
        String building = PreferenceManagers.getString(getApplicationContext(), "announce_building"+Lid);
        String buildingtemp = "";
        for(int j = 0; j < building.length(); j++) {

            if (building.charAt(j)=='1')
                buildingtemp += String.valueOf(j);
        }
        textView.setText(buildingtemp);

        LinearLayout buttonLayout =  (LinearLayout) findViewById(R.id.detail_announce_button);
        buttonLayout.setGravity(Gravity.CENTER);


        if (PreferenceManagers.getString(getApplicationContext(), "announce_writer"+Lid).equals("고일석")){
            Button modifyButton = new Button(getApplicationContext());
            modifyButton.setText("수정");
            modifyButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"수정되었습니다.", Toast.LENGTH_SHORT).show();


                }
            });
            Button deleteButton = new Button(getApplicationContext());
            deleteButton.setText("삭제");
            deleteButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            buttonLayout.addView(modifyButton);
            buttonLayout.addView(deleteButton);
        }
        Button closeButton = new Button(getApplicationContext());
        closeButton.setText("닫기");
        closeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainCommunity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonLayout.addView(closeButton);

    }

}