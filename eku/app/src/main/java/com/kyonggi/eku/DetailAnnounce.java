package com.kyonggi.eku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class DetailAnnounce extends AppCompatActivity {

    /**
     * 공지 세부화면
     */
    long backKeyPressedTime = 0;
    private final int MODIFY_MODE = 10;
    private final int VIEW_MODE = 11;

    private final int DETAIL_BOARD_REQUEST = 0;
    private final int DETAIL_BOARD_MODIFY_REQUEST = 1;
    private final int DETAIL_BOARD_DELETE_REQUEST = 2;

    private int mode_edit = VIEW_MODE;

    TextView tv_title;
    TextView tv_content;
    TextView tv_time;
    TextView tv_writer;
    TextView tv_building;

    EditText et_title;
    EditText et_content;
    Button btn_edit_submit;
    Button btn_edit_cancle;
    LinearLayout edit_layout;

    Toolbar toolbar;

    String board_id;
    String writer_id;
    String building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_announce);

        Intent intent = getIntent();
        board_id = intent.getStringExtra("id");

        toolbar     = (Toolbar)  findViewById(R.id.detail_toolbar);
        edit_layout = (LinearLayout) findViewById(R.id.detail_button_layout);
        tv_title    = (TextView)findViewById(R.id.detail_announce_title);
        tv_content  = (TextView)findViewById(R.id.detail_announce_content);
        tv_time     = (TextView)findViewById(R.id.detail_announce_time);
        tv_building = (TextView) findViewById(R.id.detail_announce_building);
        tv_writer   = (TextView)findViewById(R.id.detail_announce_writer);

        et_title    = (EditText) findViewById(R.id.detail_announce_edit_title);
        et_content  = (EditText) findViewById(R.id.detail_announce_edit_content);

        btn_edit_submit = (Button) findViewById(R.id.detail_edit_btn_submit);
        btn_edit_cancle = (Button) findViewById(R.id.detail_edit_btn_cancle);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        request_server(board_id , DETAIL_BOARD_REQUEST);

        btn_edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode_edit == MODIFY_MODE){
                    mode_edit = VIEW_MODE;


                    tv_title.setVisibility(View.VISIBLE);
                    tv_content.setVisibility(View.VISIBLE);
                    et_title.setVisibility(View.GONE);
                    et_content.setVisibility(View.GONE);
                    edit_layout.setVisibility(View.GONE);
                    request_server(board_id , DETAIL_BOARD_MODIFY_REQUEST);
                }
            }
        });

        btn_edit_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_title.setVisibility(View.VISIBLE);
                tv_content.setVisibility(View.VISIBLE);
                et_title.setVisibility(View.GONE);
                et_content.setVisibility(View.GONE);
                edit_layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_free_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.detail_Free_menu_modify:
                Toast.makeText(getApplicationContext(),"수정",Toast.LENGTH_LONG).show();


                if(mode_edit == VIEW_MODE){
                    mode_edit = MODIFY_MODE;

                    et_title.setText(tv_title.getText().toString());
                    et_content.setText(tv_content.getText().toString());

                    tv_title.setVisibility(View.INVISIBLE);
                    tv_content.setVisibility(View.INVISIBLE);
                    et_title.setVisibility(View.VISIBLE);
                    et_content.setVisibility(View.VISIBLE);
                    edit_layout.setVisibility(View.VISIBLE);
                }

                return true;
            case R.id.detail_Free_menu_delete:
                dialog_delete();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    public void request_server(String id, int mode){
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBodyObj = new JSONObject();
        String url ="";
        if (mode == DETAIL_BOARD_REQUEST){
            try{
                jsonBodyObj.put("id",Integer.parseInt(id));
                url = "https://www.eku.kro.kr/board/info/view";
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else if( mode == DETAIL_BOARD_MODIFY_REQUEST ){
            try{
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();

                jsonBodyObj.put("id",Integer.parseInt(id));
                jsonBodyObj.put("title", title);
                jsonBodyObj.put("content", content);
                jsonBodyObj.put("building", building);

                url = "https://www.eku.kro.kr/board/info/update";
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else if (mode == DETAIL_BOARD_DELETE_REQUEST){
            try{
                jsonBodyObj.put("id",Integer.parseInt(id));
                url = "https://www.eku.kro.kr/board/info/delete";
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        final String requestBody = String.valueOf(jsonBodyObj.toString());

        if(mode == DETAIL_BOARD_MODIFY_REQUEST || mode == DETAIL_BOARD_DELETE_REQUEST){
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("---","---");
                            Log.w("//===========//","================================================");
                            Log.d("","\n"+"[COMMUNITY_BOARD_DETAIL > getRequestVolleyPOST_BODY_JSON() 메소드 : Volley POST_BODY_JSON 요청 응답]");
                            Log.d("","\n"+"["+"응답 전체 - "+String.valueOf(response.toString())+"]");
                            Log.w("//===========//","================================================");
                            Log.d("---","---");

                            if (mode == DETAIL_BOARD_MODIFY_REQUEST){
                                request_server(board_id , DETAIL_BOARD_REQUEST);
                            }else if( mode == DETAIL_BOARD_DELETE_REQUEST){
                                finish();
                                Intent intent = new Intent(getApplicationContext(), MainCommunity.class);
                                startActivity(intent);
                            }

                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.d("---","---");
                            Log.e("//===========//","================================================");
                            Log.d("","\n"+"[COMMUNITY_BOARD_DETAIL > getRequestVolleyPOST_BODY_JSON() 메소드 : Volley POST_BODY_JSON 요청 실패]");
                            Log.d("","\n"+"["+"에러 코드 - "+String.valueOf(error.toString())+"]");
                            Log.e("//===========//","================================================");
                            Log.d("---","---");
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
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

            request.setShouldCache(false);
            queue.add(request);
        }else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("---","---");
                            Log.w("//===========//","================================================");
                            Log.d("","\n"+"[COMMUNITY_BOARD_DETAIL > DETAIL_BOARD_DELETE_REQUEST 모드드 : Volly POST_BODY_JSON 요청 응답]");
                            Log.d("","\n"+"["+"응답 전체 - "+String.valueOf(response.toString())+"]");
                            Log.w("//===========//","================================================");
                            Log.d("---","---");

                            try {
                                String title    = response.getString("title");
                                String content  = response.getString("content");
                                String writer   = response.getString("writer");
                                String time     = response.getString("time");
                                building        = response.getString("building");
                                writer_id       = String.valueOf(response.getInt("writerNo"));

                                tv_title.setText(title);
                                tv_content.setText(content);
                                tv_writer.setText(writer);
                                tv_time.setText(time);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.d("---","---");
                            Log.e("//===========//","================================================");
                            Log.d("","\n"+"[COMMUNITY_BOARD_DETAIL > getRequestVolleyPOST_BODY_JSON() 메소드 : Volley POST_BODY_JSON 요청 실패]");
                            Log.d("","\n"+"["+"에러 코드 - "+String.valueOf(error.toString())+"]");
                            Log.e("//===========//","================================================");
                            Log.d("---","---");
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
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

            request.setShouldCache(false);
            queue.add(request);
        }
    }

    public void dialog_delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글을 삭제");
        builder.setMessage("삭제하시겠습니까? ");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        request_server(board_id, DETAIL_BOARD_DELETE_REQUEST);
                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}