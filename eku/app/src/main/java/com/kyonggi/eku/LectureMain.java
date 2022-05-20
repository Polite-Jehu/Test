package com.kyonggi.eku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.kyonggi.eku.presenter.lecture.LecturePresenter;
import com.kyonggi.eku.utils.SendTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LectureMain extends AppCompatActivity {

    /*
     * 강의평가 메인
     *
     */
    ImageButton imageButton;
    ImageButton imageButton1;
    LinearLayout sc;
    String[] showBuilding = {"1강의동", "2강의동", "3강의동", "4강의동", "5강의동", "6강의동", "7강의동", "8강의동", "9강의동", "제2공학관"};
    int buildingSelected = 0;
    int[] building = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    AlertDialog buildingSelectDialog;
    long backKeyPressedTime;
    private LecturePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_main);

        presenter = new LecturePresenter(this, this);

        SwipeRefreshLayout swipe = findViewById(R.id.Lecture_Main_Swipe);
        swipe.setOnRefreshListener(
                () -> {
                    Log.i("TAG", "onRefresh called from SwipeRefreshLayout");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sc = (LinearLayout) findViewById(R.id.Lecture_Main_scroll);
                            sc.removeAllViews();
                            LoadMain();
                            swipe.setRefreshing(false);
                        }
                    }, 500);
                });

        /*
        imageButton = (ImageButton) findViewById(R.id.Lecture_Main_WriteButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformation info = new UserInformation(getApplicationContext());
                if (!info.fromPhoneVerify(getApplicationContext())) {
                    //presenter.signIn();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LectureWrite.class);
                    startActivity(intent);
                }
            }
        });
         */

        EditText searchText = (EditText) findViewById(R.id.Lecture_Main_searchtext);
        LoadMain();


        imageButton1 = (ImageButton) findViewById(R.id.Lecture_Main_searchButton);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchText.getText().toString();
                sc = (LinearLayout) findViewById(R.id.Lecture_Main_scroll);
                sc.removeAllViews();
                Handler handler = new Handler(getMainLooper()){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        Log.i("c", (String) msg.obj);
                        String responseResult = (String) msg.obj;
                        try {
                            JSONArray LectureArray = new JSONArray(responseResult);
                            for (int i = 0; i < LectureArray.length(); i++) {
                                JSONObject LectureObject = LectureArray.getJSONObject(i);
                                String title = LectureObject.getString("lectureName");
                                String professor = LectureObject.getString("professor");
                                String rating = String.valueOf(LectureObject.getDouble("star"));
                                search_Lecture(title, professor, rating);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                HashMap<String, Object> temp = new HashMap<>();
                temp.put("keyword",search);
                try {
                    SendTool.requestForJson("/critic/search", temp, handler);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void search_Lecture(String Title, String professor, String rating) {
        sc = (LinearLayout) findViewById(R.id.Lecture_Main_scroll);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LectureItem lectureitem = new LectureItem(getApplicationContext(), Title,professor,rating);
        lectureitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LectureDetail.class);
                intent.putExtra("Name", Title);
                intent.putExtra("Prof", professor);
                startActivity(intent);
                finish();
            }
        });
        sc.addView(lectureitem);
    }


    public void write_Lecture(String Title, String professor, String rating, String content, int Lectureid) {
        sc = (LinearLayout) findViewById(R.id.Lecture_Main_scroll);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        String writer="고지웅";
        LectureItem lectureitem = new LectureItem(getApplicationContext(), Title,professor,rating,writer);
        lectureitem.setId(Lectureid);
        lectureitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LectureDetail.class);
                intent.putExtra("Name", Title);
                intent.putExtra("Prof", professor);
                startActivity(intent);
                finish();
            }
        });
        sc.addView(lectureitem);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            finish();
            //Toast.makeText(this, "뒤로 가기 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
        }
    }

    public void LoadMain(){
        Handler handler =  new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String responseResult = (String) msg.obj;
                try {
                    JSONArray LectureArray = new JSONArray(responseResult);
                    for (int i = 0; i < LectureArray.length(); i++) {
                        JSONObject LectureObject = LectureArray.getJSONObject(i);
                        String rating = LectureObject.getString("star");
                        int LectureId = Integer.parseInt(LectureObject.getString("cid"));
                        String content = LectureObject.getString("content");
                        Gson a = new Gson();
                        Lecture lecture1 = a.fromJson(LectureObject.getString("lecture"), Lecture.class);
                        String title = lecture1.getLectureName();
                        String professor = lecture1.getProfessor();

                        write_Lecture(title, professor, rating, content, LectureId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        HashMap<String, Object> temp = new HashMap<>();
        try {
            SendTool.requestForJson("/critic/read", temp, handler);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}