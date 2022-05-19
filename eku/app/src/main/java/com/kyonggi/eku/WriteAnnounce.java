package com.kyonggi.eku;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kyonggi.eku.MainCommunity;
import com.kyonggi.eku.R;
import com.kyonggi.eku.PreferenceManagers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteAnnounce extends AppCompatActivity implements View.OnClickListener {

     /*
     * 제목
     * 공지 게시판 작성
     * 기능
     * ㅈㄱㄴ
     */

    CheckBox building0, building1, building2, building3, building4,
            building5, building6, building7, building8, building9;

    EditText et_title;
    EditText et_content;

    Button btn_save;
    Button btn_cancle;

    String name         = "고지웅";
    String writer_id    = "201713924";
    String department   = "소프트웨어공학과";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_announce);

        building0   = findViewById(R.id.building0);
        building6   = findViewById(R.id.building6);
        building7   = findViewById(R.id.building7);
        building8   = findViewById(R.id.building8);
        building9   = findViewById(R.id.building9);

        et_title    = findViewById(R.id.write_announce_title);
        et_content  = findViewById(R.id.write_announce_content);
        btn_save    = findViewById(R.id.write_announce_save);
        btn_cancle  = findViewById(R.id.write_announce_close);

        btn_save.setOnClickListener(this::onClick);
        btn_cancle.setOnClickListener(this::onClick);


    }


    public String getBuilding(){
        String building = "";

        building += building0.isChecked() ? "1" : "0";;
        building += building1.isChecked() ? "1" : "0";;
        building += building2.isChecked() ? "1" : "0";;
        building += building3.isChecked() ? "1" : "0";;
        building += building4.isChecked() ? "1" : "0";;
        building += building5.isChecked() ? "1" : "0";;
        building += building6.isChecked() ? "1" : "0";;
        building += building7.isChecked() ? "1" : "0";;
        building += building8.isChecked() ? "1" : "0";;
        building += building9.isChecked() ? "1" : "0";;

        return building;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.write_announce_save:
                addBoard();
                finish();
                Intent intent1 = new Intent(getApplicationContext(), MainCommunity.class);
                startActivity(intent1);
                break;
            case R.id.write_announce_close:
                finish();
                Intent intent = new Intent(getApplicationContext(), MainCommunity.class);
                startActivity(intent);
                break;
        }
    }

    public void addBoard(){
        // volley 큐 선언 및 생성
        RequestQueue queue = Volley.newRequestQueue(this);
        // Body에 담을 JSON Object 생성 및 선언
        JSONObject jsonBodyObj = new JSONObject();
        try{
            String title    = et_title.getText().toString();
            String content  = et_content.getText().toString();

            jsonBodyObj.put("writerNo", writer_id);
            jsonBodyObj.put("department", department);
            jsonBodyObj.put("name", name);
            jsonBodyObj.put("title", title);
            jsonBodyObj.put("content", content);
            jsonBodyObj.put("building",getBuilding());

        }catch (JSONException e){
            e.printStackTrace();
        }
        // body String 선언
        final String requestBody = String.valueOf(jsonBodyObj.toString());

        // Server 주소
        String url = "https://www.eku.kro.kr/board/info/write";
        // VOLLEY 라이브러리를 이용하여 Server에 JSON Array 요청
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Request에 대한 reponse 받음
                        Log.d("---","---");
                        Log.w("//===========//","================================================");
                        Log.d("","\n"+"[FREE_COMMUNITY_BOARD > getRequestVolleyPOST_BODY_JSON() 메소드 : Volley POST_BODY_JSON 요청 응답]");
                        Log.d("","\n"+"["+"응답 전체 - "+String.valueOf(response.toString())+"]");
                        Log.w("//===========//","================================================");
                        Log.d("---","---");
                    }
                },
                // Response Error 출력시,
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("---","---");
                        Log.e("//===========//","================================================");
                        Log.d("","\n"+"[A_Main > getRequestVolleyPOST_BODY_JSON() 메소드 : Volley POST_BODY_JSON 요청 실패]");
                        Log.d("","\n"+"["+"에러 코드 - "+String.valueOf(error.toString())+"]");
                        Log.e("//===========//","================================================");
                        Log.d("---","---");
                    }
                }
        ){
            // Header Request 선언
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            // Body Request 선언
            @Override
            public byte[] getBody() {
                try {
                    if (requestBody != null && requestBody.length()>0 && !requestBody.equals("")){
                        return requestBody.getBytes("utf-8");
                    }
                    else {
                        return null;
                    }
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        // 이전 결과가 있더도 새로 요청하여 응답을 보여줌 여부
        // False
        request.setShouldCache(false);
        // Volley Request 큐에 request 삽입.
        queue.add(request);
    }
}