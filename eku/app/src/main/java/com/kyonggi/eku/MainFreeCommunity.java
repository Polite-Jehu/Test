package com.kyonggi.eku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.kyonggi.eku.utils.adapters.FreeComminityRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 *제목
 * 자유게시판
 *
 *내용
 * 자유게시판판* *
 *
 */
public class MainFreeCommunity extends AppCompatActivity {
    String[] showBuilding = {"6강의동","7강의동","8강의동","9강의동","제2공학관"};
    int buildingSelected = 0;
    int[] building = {6,7,8,9,0};
    AlertDialog buildingSelectDialog;
    long backKeyPressedTime;

    // 위젯 선언
    private RecyclerView rc_fc_board;
    // 게시판 목록 정보를 담을 Array List 선언
    private ArrayList<FreeComminityItem> arrayList;
    // 리사이클러 뷰 어댐터
    private FreeComminityRecyclerAdapter freeComminityRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_free_community);

        final DrawerLayout drawerLayout = findViewById(R.id.FreeCommunity_drawerLayout);

        // 리사이클러 뷰 위젯 연결
        rc_fc_board = (RecyclerView) findViewById(R.id.FreeCommunity_RecyclerView);

        // 게시판 목록을 담을 Array List 생성
        arrayList = new ArrayList<FreeComminityItem>();
        // Server에서 게시판 목록 가져옴
        getBoardList();

        // 리사이클러 뷰 어댑터 생성
        freeComminityRecyclerAdapter = new FreeComminityRecyclerAdapter(arrayList);
        // 리사이클러 뷰에 어댑터 선언
        rc_fc_board.setAdapter(freeComminityRecyclerAdapter);
        // 리사이클러뷰 레이아웃 선언
        rc_fc_board.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.FreeCommunity_Menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.FreeCommunity_navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch(id) {
                    case R.id.Home:
                        intent = new Intent(getApplicationContext(), MainBoard.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.Announce:
                        intent = new Intent(getApplicationContext(), MainCommunity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.Free:
                        intent = new Intent(getApplicationContext(), MainFreeCommunity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.lectureMain:
                        intent = new Intent(getApplicationContext(), LectureMain.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.ToDo:
                        intent = new Intent(getApplicationContext(), TodoActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.TimeTable:
                        intent = new Intent(getApplicationContext(), ScheduleTable.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });

        TextView BuildingButton = (TextView) findViewById(R.id.FreeCommunity_Spinner);
        BuildingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildingSelectDialog.show();
            }
        });
        buildingSelectDialog = new AlertDialog.Builder(MainFreeCommunity.this)
                .setSingleChoiceItems(showBuilding, buildingSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buildingSelected = i;
                    }
                })
                .setTitle("강의동")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BuildingButton.setText(showBuilding[buildingSelected]);
                    }
                })
                .setNegativeButton("취소", null)
                .create();

        ImageButton imageButton = (ImageButton)findViewById(R.id.FreeCommunity_Write);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteFreeCommunity.class);
                startActivity(intent);
                finish();
            }
        });

        EditText searchText = (EditText)findViewById(R.id.FreeCommunity_Searchtext);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int s, int s1, int s2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int s, int s1, int s2) {
                LinearLayout sc = (LinearLayout)findViewById(R.id.FreeCommunity_scroll);
                sc.removeAllViews();
                ///바꾸셈
                int count = PreferenceManagers.getInt(getApplicationContext(), "FreeCommunity_count");
                for (int i = count; i >= 1; i--) {
                    if (PreferenceManagers.getString(getApplicationContext(), "FreeCommunity_title" + String.valueOf(i)).contains(charSequence)) {

                        String str = "FreeCommunity_title" + i;
                        String title = PreferenceManagers.getString(getApplicationContext(), str);

                        str = "FreeCommunity_writer" + i;
                        String professor = PreferenceManagers.getString(getApplicationContext(), str);

                        ///강의동 체크

                        write_Lecture(title, professor, i);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SwipeRefreshLayout swipe = findViewById(R.id.FreeCommunity_Swipe);
        swipe.setOnRefreshListener(
                () -> {
                    Log.i("TAG", "onRefresh called from SwipeRefreshLayout");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipe.setRefreshing(false);
                        }
                    }, 500);
                });
        ImageButton imageButton1 = (ImageButton)findViewById(R.id.FreeCommunity_Search);
        imageButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //?????????????????
            }
        });
        Button moveButton = (Button)findViewById(R.id.FreeCommunity_announce_button);
        moveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainCommunity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        //initialize();
        int count = PreferenceManagers.getInt(getApplicationContext(), "FreeCommunity_count");
        if (count==-1){
            PreferenceManagers.setInt(getApplicationContext(), "FreeCommunity_count", 0);
        }
        //Toast.makeText(getApplicationContext(),"자유작성"+count, Toast.LENGTH_SHORT).show();
        if (count >0){
            for (int i = count;i>=1;i--){
                if(!PreferenceManagers.getString(getApplicationContext(),"FreeCommunity_title"+String.valueOf(i)).equals("")) {
                    String str = "FreeCommunity_title" + i;
                    String title = PreferenceManagers.getString(getApplicationContext(), str);

                    str = "FreeCommunity_writer" + i;
                    String professor = PreferenceManagers.getString(getApplicationContext(), str);

                    write_Lecture(title, professor, i);
                }
            }
        }
    }

    // Serser에서 게시판 목록을 가져오는 메소드
    public void getBoardList(){
        // volley 큐 선언 및 생성
        RequestQueue queue = Volley.newRequestQueue(this);
        // Body에 담을 JSON Object 생성 및 선언
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("","");
        }catch (JSONException e){
            e.printStackTrace();
        }
        // body String 선언
        final String requestBody = String.valueOf(jsonBodyObj.toString());

        // Server 주소
        String url = "https://www.eku.kro.kr/board/free/lists";
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

                        try{
                            // Json Array 의 각 데이터를 파싱
                            // Array List 에 삽입
                            // 리사이클러 뷰 어댑터 갱신
                            for(int i=0;i<response.length();i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id       = jsonObject.getString("id");
                                String title    = jsonObject.getString("title");
                                String writer   = jsonObject.getString("writer") + " " +jsonObject.getString("no");
                                String time     = jsonObject.getString("time");
                                String views    = jsonObject.getString("view");
                                String comments = jsonObject.getString("comments");

                                addItem(id,title, comments, writer, time, views);
                                freeComminityRecyclerAdapter.notifyDataSetChanged();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
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
            public Map<String, String> getHeaders() throws AuthFailureError{
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

    // Array List 에 게시판 내용 Item 삽입
    public void addItem(String id,String title, String comments, String writer, String time, String views){
        FreeComminityItem item = new FreeComminityItem(id,title,writer,comments,time,views);
        /*
        item.setFc_title(title);
        item.setFc_comment(comments);
        item.setFc_writer(writer);
        item.setFc_time(time);
        item.setFc_views(views);*/
        arrayList.add(item);
    }

    public void write_Lecture(String Title, String writer, int count){
        LinearLayout sc = (LinearLayout)findViewById(R.id.FreeCommunity_scroll);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout .setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams linearLayoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        linearLayout.setId(count);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailFreeCommunity.class);
                int Lid = view.getId();
                intent.putExtra("FreeCommunity_key",Lid);
                // Toast.makeText(getApplicationContext(),String.valueOf(Lid), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();;

            }
        });



        TextView textView = new TextView(getApplicationContext());
        textView.setText(Title);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25.0f);
        textView.setLayoutParams(linearLayoutParams);
        textView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        linearLayout.addView(textView);


        TextView professorView = new TextView(getApplicationContext());
        professorView.setText(writer);
        professorView.setGravity(Gravity.RIGHT);
        professorView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17.0f);
        professorView.setLayoutParams(linearLayoutParams);
        professorView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        linearLayout.addView(professorView);

        sc.addView(linearLayout);


    }
    public void initialize() { //초기화
        int i = PreferenceManagers.getInt(getApplicationContext(), "FreeCommunity_count");
        for (int j=0;j<=i;j++){
            PreferenceManagers.removeKey(getApplicationContext(),"FreeCommunity_name"+j);
            PreferenceManagers.removeKey(getApplicationContext(),"FreeCommunity_writer"+j);
        }
        PreferenceManagers.setInt(getApplicationContext(), "FreeCommunity_count", 0);
        Toast.makeText(getApplicationContext(),"초기화 켜져있어요", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
        }
    }
}