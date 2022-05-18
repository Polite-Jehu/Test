package com.kyonggi.eku;

import static android.widget.LinearLayout.*;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.gridlayout.widget.GridLayout;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kyonggi.eku.view.signIn.ActivitySignIn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
public class ScheduleTable extends AppCompatActivity {

    /*
     *
     * 제목
     * 시간표
     * 기능
     * 모양만
     * */
    String[] showBuilding = {"1강의동", "2강의동", "3강의동", "4강의동", "5강의동", "6강의동", "7강의동", "8강의동", "9강의동", "제2공학관"};
    int buildingSelected = 0;
    int[] building = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

    AlertDialog buildingSelectDialog;
    long backKeyPressedTime;
    EditText password;
    JSONArray getLecture = new JSONArray();
    int[] input_time = {0, 0, 0, 0, 0, 0, 0, 0};
    String day = "";

    boolean[][] check = {{false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}};
    int studentNumber = -1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);
        UserInformation userInformation = new UserInformation(getApplicationContext());


        GridLayout lp = (GridLayout) findViewById(R.id.gridLayout);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        int w = lp.getWidth();
        int h = lp.getHeight();
        password = findViewById(R.id.Schedule_password);
        /**
         * 세션 확인
         */
        if (!(userInformation.fromPhoneVerify(getApplicationContext()))) {
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ActivitySignIn.class);
            startActivity(intent);
            finish();
        } else {
            try {
                studentNumber = Integer.valueOf(userInformation.fromPhoneStudentNo(getApplicationContext()));
            } catch (Exception e) {
                studentNumber = -1;
            }
        }


/*


*
 *
 *Lecture를 불러와서
 *배치하고 초기값 설정하고
                * 중복값 방지를 위해서 미리 설정
*/

    //   PreferenceManagers.removeKey(getApplicationContext(),"list");
        String tableString = PreferenceManagers.getString(getApplicationContext(), "list");
        Toast.makeText(getApplicationContext(),tableString,Toast.LENGTH_LONG).show();
        try {
            getLecture = new JSONArray(tableString);

        } catch (JSONException e) {
            getLecture = new JSONArray();
        }
        for (int i = 0; i < getLecture.length(); i++) {
            try {
                JSONObject jsonObject = getLecture.getJSONObject(i);

                int start = 0;
                int startButtonPosition = 0;
                String lectureTime = jsonObject.getString("lecture_time");

                String[] lectureTimeArray = lectureTime.split("");
                
                if (lectureTimeArray[0].equals("월")) {
                    start = 1;
                    for (int k = 1; k < lectureTimeArray.length; k++) {
                        check[0][Integer.valueOf(lectureTimeArray[k]) - 1] = true;
                    }

                } else if (lectureTimeArray[0].equals("화")) {
                    start = 2;
                    for (int k = 1; k < lectureTimeArray.length; k++) {
                        check[1][Integer.valueOf(lectureTimeArray[k]) - 1] = true;
                    }
                } else if (lectureTimeArray[0].equals("수")) {
                    start = 3;
                    for (int k = 1; k < lectureTimeArray.length; k++) {
                        check[2][Integer.valueOf(lectureTimeArray[k]) - 1] = true;
                    }
                } else if (lectureTimeArray[0].equals("목")) {
                    start = 4;
                    for (int k = 1; k < lectureTimeArray.length; k++) {
                        check[3][Integer.valueOf(lectureTimeArray[k]) - 1] = true;
                    }
                } else if (lectureTimeArray[0].equals("금")) {
                    start = 5;
                    for (int k = 1; k < lectureTimeArray.length; k++) {
                        check[4][Integer.valueOf(lectureTimeArray[k]) - 1] = true;
                    }
                }

                startButtonPosition = Integer.valueOf(lectureTimeArray[1]);
                int temp = Integer.valueOf(lectureTimeArray[lectureTimeArray.length - 1]);
                int end = temp - startButtonPosition + 1;
                Button button2 = new Button(this.getApplicationContext());
                float size = 50.0f * end;

                String stLectureName = jsonObject.getString("lecture_name");
                String stLectureTime = jsonObject.getString("lecture_time");
                String stProfessor = jsonObject.getString("professor");
                String stLectureRoom = jsonObject.getString("lecture_room");

                StringBuilder sb = new StringBuilder();
                sb.append(stLectureName);
                sb.append("\n");
                sb.append(stLectureTime);
                sb.append("\n");
                sb.append(stProfessor);
                sb.append("\n");
                sb.append(stLectureRoom);
                button2.setHeight((int)dp2px(size));
                button2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleTable.this);
                        StringBuilder st = new StringBuilder();
                        st.append("강의명 : ");
                        st.append(stLectureName);
                        st.append("\n");
                        st.append("강의시간 : ");
                        st.append(stLectureTime);
                        st.append("\n");
                        st.append("교수명 : ");
                        st.append(stProfessor);
                        st.append("\n");
                        st.append("강의 장소 : ");
                        st.append(stLectureRoom);
                        builder.setTitle("강의 정보").setMessage(st.toString());

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int deleteIndex=0;
                                for(int e=0;e<getLecture.length();e++)
                                {
                                    JSONObject tempJSONObject = null;
                                    try {
                                        tempJSONObject = getLecture.getJSONObject(e);
                                        if(stLectureName.equals(tempJSONObject.getString("lecture_name")))
                                        {
                                            deleteIndex=e;
                                            break;
                                        }
                                    } catch (JSONException jsonException) {
                                        jsonException.printStackTrace();
                                    }

                                }
                                getLecture.remove(deleteIndex);
                                PreferenceManagers.removeKey(getApplicationContext(),"list");
                                PreferenceManagers.setString(getApplicationContext(),"list",getLecture.toString());
                                Intent intent = new Intent(getApplicationContext(), ScheduleTable.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }


                });

                button2.setText(sb.toString());
                button2.setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);

                GridLayout.LayoutParams gridLayoutWitch = new GridLayout.LayoutParams();
              /*  Log.e("start : ", String.valueOf(start));
                Log.e("startButtonPosition", String.valueOf(startButtonPosition)) ;*/
                gridLayoutWitch.rowSpec = GridLayout.spec(startButtonPosition,end);
                gridLayoutWitch.columnSpec = GridLayout.spec(start);
                gridLayoutWitch.width = (int)dp2px(68.0f);
                lp.addView(button2, gridLayoutWitch);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        TextView BuildingButton = (TextView) findViewById(R.id.schedule_Spinner);
        BuildingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                buildingSelectDialog.show();
            }
        });
        buildingSelectDialog = new AlertDialog.Builder(ScheduleTable.this)
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

        final DrawerLayout drawerLayout = findViewById(R.id.schedule_drawerLayout);

        findViewById(R.id.schedule_Menu).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.schedule_navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
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

        Button UpButton = (Button) findViewById(R.id.schedule_up_button);
        UpButton.setOnClickListener(new OnClickListener() {
            int upLectureCount = getLecture.length();
            @Override
            public void onClick(View view) {
                String pwd = password.getText().toString();
                if(pwd.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    for(int u=0;u<upLectureCount;u++)
                    {
                        try {
                            JSONObject upJSON = getLecture.getJSONObject(u);
                            upJSON.put("password",Integer.valueOf(password.getText().toString()));
                            getLecture.put(u,upJSON);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    PreferenceManagers.removeKey(getApplicationContext(),"list");

                    PreferenceManagers.setString(getApplicationContext(),"list",getLecture.toString());
                    HashMap<String,Object> temp = new HashMap<>();
                    try {

                        com.kyonggi.eku.utils.SendTool.requestForTimeTable("/schedule/write",getLecture,new Handler());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"전송완료",Toast.LENGTH_SHORT).show();

                }


            }

        });

        Button DownButton = (Button) findViewById(R.id.schedule_down_button);
        DownButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_down,null);
                final EditText hakbunText = (EditText) dialogView.findViewById(R.id.DownStudentNo);
                final EditText bibunText = (EditText) dialogView.findViewById(R.id.DownPassword);

                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleTable.this);
                builder.setView(dialogView);

                builder.setPositiveButton("다운로드", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Handler handler = new Handler(){
                            public void handleMessage(@NonNull Message msg){
                                Log.i("a",(String)msg.obj);

                                String responseResult=(String)msg.obj;

                                if(responseResult != null) {
                                    try {
                                        JSONArray downFile = new JSONArray(responseResult);
                                        int lengDownFile = downFile.length();
                                        for(int p=0;p<lengDownFile;p++)
                                        {
                                            JSONObject tempJS = (JSONObject) downFile.get(p);
                                            tempJS.put("studNo",Integer.valueOf(userInformation.fromPhoneStudentNo(getApplicationContext())));
                                        }
                                        getLecture = downFile;
                                        PreferenceManagers.setString(getApplicationContext(),"list",downFile.toString());
                                        Intent intent = new Intent(getApplicationContext(), ScheduleTable.class);
                                        startActivity(intent);
                                        finish();

                                    } catch (JSONException e) {
                                        Toast.makeText(getBaseContext(),"해당하는 시간표가 없습니다...",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    Toast.makeText(getBaseContext(),"해당하는 시간표가 없습니다...",Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        Integer hakbun=0;
                        Integer bibun=0;

                        try{
                            hakbun = Integer.valueOf(hakbunText.getText().toString());
                            bibun = Integer.valueOf(bibunText.getText().toString());
                        }
                        catch(Exception e)
                        {
                            hakbun=0;
                            bibun=0;
                        }
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("studNo",hakbun);
                            jsonObject.put("password",bibun);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),"잘못된 입력값이 있습니다.",Toast.LENGTH_SHORT).show();
                        }
                        try {
                            com.kyonggi.eku.utils.SendTool.downForTimeTable("/schedule/view",jsonObject,handler);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),"서버 에러입니다.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }



        });

        Button AddButton = (Button) findViewById(R.id.schedule_add_button);
        AddButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ScheduleTable.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_schedule);
                EditText dialog_title = dialog.findViewById(R.id.timeTableTitle);
                EditText dialog_Professor = dialog.findViewById(R.id.timeTableProfessor);
                EditText dialog_Building = dialog.findViewById(R.id.timeTableBuilding);

                RadioButton mon = (RadioButton) dialog.findViewById(R.id.TimeTableMon);
                RadioButton tue = (RadioButton) dialog.findViewById(R.id.TimeTableTue);
                RadioButton wed = (RadioButton) dialog.findViewById(R.id.TimeTableWed);
                RadioButton thu = (RadioButton) dialog.findViewById(R.id.TimeTableThu);
                RadioButton fri = (RadioButton) dialog.findViewById(R.id.TimeTableFri);
                RadioGroup gro = (RadioGroup) dialog.findViewById(R.id.timeTableRadioGroup);
                gro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.TimeTableMon) {
                            day = "월";
                        } else if (i == R.id.TimeTableTue) {
                            day = "화";
                        } else if (i == R.id.TimeTableWed) {
                            day = "수";
                        } else if (i == R.id.TimeTableThu) {
                            day = "목";
                        }
                        if (i == R.id.TimeTableFri) {
                            day = "금";
                        }
                    }
                });



                /*
                 *시간표 확인할때
                 *1 교시 =>0 번입니다..
                 **/
                CheckBox checkBox1 = (CheckBox) dialog.findViewById(R.id.TimeTable_1);
                checkBox1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[0] = 1;
                        } else {
                            input_time[0] = 0;
                        }
                    }
                });
                CheckBox checkBox2 = (CheckBox) dialog.findViewById(R.id.TimeTable_2);
                checkBox2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[1] = 1;
                        } else {
                            input_time[1] = 0;
                        }
                    }
                });
                CheckBox checkBox3 = (CheckBox) dialog.findViewById(R.id.TimeTable_3);
                checkBox3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[2] = 1;
                        } else {
                            input_time[2] = 0;
                        }
                    }
                });
                CheckBox checkBox4 = (CheckBox) dialog.findViewById(R.id.TimeTable_4);
                checkBox4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[3] = 1;
                        } else {
                            input_time[3] = 0;
                        }
                    }
                });
                CheckBox checkBox5 = (CheckBox) dialog.findViewById(R.id.TimeTable_5);
                checkBox5.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[4] = 1;
                        } else {
                            input_time[4] = 0;
                        }
                    }
                });
                CheckBox checkBox6 = (CheckBox) dialog.findViewById(R.id.TimeTable_6);
                checkBox6.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[5] = 1;
                        } else {
                            input_time[5] = 0;
                        }
                    }
                });
                CheckBox checkBox7 = (CheckBox) dialog.findViewById(R.id.TimeTable_7);
                checkBox7.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[6] = 1;
                        } else {
                            input_time[6] = 0;
                        }
                    }
                });
                CheckBox checkBox8 = (CheckBox) dialog.findViewById(R.id.TimeTable_8);
                checkBox8.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            input_time[7] = 1;
                        } else {
                            input_time[7] = 0;
                        }
                    }
                });
                Button btn_ok = dialog.findViewById(R.id.TimeTable_ok);
                btn_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String temp = "";
                        for (int i : input_time) {
                            temp += i;
                        }
                        temp += dialog_title.getText().toString();
                        temp += dialog_Building.getText().toString();
                        temp += dialog_Professor.getText().toString();
                        temp += day;
                        dialog.dismiss();

                        boolean checked = false;
                        if (day.equals("월")) {
                            for (int i = 0; i < input_time.length; i++) {
                                if (input_time[i] == 1 && check[0][i] == true) {
                                    checked = true;
                                }
                            }
                        } else if (day.equals("화")) {
                            for (int i = 0; i < input_time.length; i++) {
                                if (input_time[i] == 1 && check[1][i] == true) {
                                    checked = true;
                                }
                            }

                        } else if (day.equals("수")) {
                            for (int i = 0; i < input_time.length; i++) {
                                if (input_time[i] == 1 && check[2][i] == true) {
                                    checked = true;
                                }
                            }

                        } else if (day.equals("목")) {
                            for (int i = 0; i < input_time.length; i++) {
                                if (input_time[i] == 1 && check[3][i] == true) {
                                    checked = true;
                                }
                            }
                        } else if (day.equals("금")) {
                            for (int i = 0; i < input_time.length; i++) {
                                if (input_time[i] == 1 && check[4][i] == true) {
                                    checked = true;
                                }
                            }
                        }

                        if (checked) {
                            Toast.makeText(getApplicationContext(), "중복되는 값이 있습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                String lecture_time = "";
                                lecture_time += day;
                                for (int i = 0; i < input_time.length; i++) {
                                    if (input_time[i] == 1) {
                                        int j = i;
                                        lecture_time += (j + 1);
                                    }
                                }
                                jsonObject.put("lecture_time", lecture_time);
                                jsonObject.put("lecture_room", dialog_Building.getText().toString());
                                jsonObject.put("professor", dialog_Professor.getText().toString());
                                jsonObject.put("lecture_name", dialog_title.getText().toString());
                                jsonObject.put("studNo", Integer.valueOf(userInformation.fromPhoneStudentNo(getApplicationContext())));
                                if(password.getText().toString().equals(""))
                                {
                                    jsonObject.put("password", 0);
                                }
                                else{
                                    jsonObject.put("password", Integer.valueOf(password.getText().toString()));
                                }
                                if (getLecture == null) {
                                    getLecture = new JSONArray();
                                }
                                getLecture.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
/*
                         *inputTime 초기화
                         **/
                        for (int i = 0; i < input_time.length; i++) {
                            input_time[i] = 0;
                        }
                        Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
                        HashMap hashMap = new HashMap();
                        hashMap.put("list", getLecture);
                     //   SendTool.requestForJson("/schedule/write", hashMap, new Handler());
                        PreferenceManagers.setString(getApplicationContext(), "list", getLecture.toString());
                        Intent intent = new Intent(getApplicationContext(), ScheduleTable.class);
                        startActivity(intent);
                        finish();
                    }
                });
                Button btn_cancel = dialog.findViewById(R.id.TimeTable_cancel);
                btn_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    //dp-> px
    public float dp2px(float dp) {
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    //px을 dp로 변환 (px을 입력받아 dp를 리턴)
    public float px2dp(float px) {
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    //뒤로가기 추가
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