package com.kyonggi.eku.presenter.signIn;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kyonggi.eku.UserInformation;
import com.kyonggi.eku.utils.SendTool;
import com.kyonggi.eku.view.signIn.ActivitySignIn;
import com.kyonggi.eku.view.signUp.activity.ActivitySignUpCamera;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInPresenter {

    private static final String TAG = "LoginPresenter";
    private Handler handler;
    private final Context context;
    private final ActivitySignIn activity;
    private final UserInformation userInformation;

    public SignInPresenter(Context context, ActivitySignIn activity) {
        this.context = context;
        this.userInformation = new UserInformation(context);
        this.activity = activity;
    }

    public void signIn(String email, String password) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        SendTool.requestForJson("/signIn", user, getHandler(email, password));
    }

    private Handler getHandler(String email, String password) {
        if (handler == null) {
            this.handler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(@NonNull Message msg) {
                    String response = (String) msg.obj;
                    Log.d(TAG, "handleMessage: "+response);
                    switch (response) {
                        case SERVER_ERROR:
                            Toast.makeText(context, "서버 에러 발생", Toast.LENGTH_SHORT).show();
                            break;
                        case PASSWORD_INVALID:
                            Toast.makeText(context, "이메일 혹은 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        case NOT_AUTHORIZED:
                            Toast.makeText(context, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        case NOT_REGISTERED:
                            Toast.makeText(context, "가입되어 있지 않은 계정입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int student_no = jsonObject.getInt("studNo");
                                String st_student_no = String.valueOf(student_no);
                                String department = jsonObject.getString("department");
                                userInformation.toPhone(context, email, password, st_student_no, department, true, true);
                                activity.finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            };
        }
        return handler;
    }

    public void signUp() {
        Intent intent = new Intent(context, ActivitySignUpCamera.class);
        context.startActivity(intent);
    }

    private final String NOT_AUTHORIZED = "Not Authorized.";
    private final String NOT_REGISTERED = "Not Registered.";
    private final String PASSWORD_INVALID = "Password Not Matching.";
    private final String SERVER_ERROR = "Server In Error.";
}

