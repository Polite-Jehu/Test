package com.kyonggi.eku.presenter.board;

import static com.kyonggi.eku.view.board.activity.ActivityBoard.BOARD_FREE;
import static com.kyonggi.eku.view.board.activity.ActivityBoard.BOARD_INFO;
import static com.kyonggi.eku.view.board.activity.ActivityBoard.INIT;
import static com.kyonggi.eku.view.board.activity.ActivityBoard.LOAD_OLD;
import static com.kyonggi.eku.view.board.activity.ActivityBoard.LOAD_RECENT;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kyonggi.eku.UserInformation;
import com.kyonggi.eku.WriteAnnounce;
import com.kyonggi.eku.WriteFreeCommunity;
import com.kyonggi.eku.model.BoardPreview;
import com.kyonggi.eku.model.FreeBoardPreview;
import com.kyonggi.eku.model.InfoBoardPreview;
import com.kyonggi.eku.utils.SendTool;
import com.kyonggi.eku.utils.callbacks.OnResponseListeners;
import com.kyonggi.eku.view.board.activity.ActivityFreeBoardSearch;
import com.kyonggi.eku.view.board.activity.ActivityInfoBoardSearch;
import com.kyonggi.eku.view.signIn.ActivitySignIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoardPresenter {
    private static final String TAG = "FreeBoardPresenter";
    private final Context context;
    private final OnResponseListeners activity;
    private UserInformation userInformation;

    public BoardPresenter(Context context, OnResponseListeners activity) {
        this.context = context;
        this.activity = activity;
        userInformation = new UserInformation(context);
    }

    public void getInfoBoardArticles(String buildingNumber){
        HashMap<String, Object> request = new HashMap<>();
        request.put("lectureBuilding", buildingNumber);
        SendTool.requestForJson("/board/info/lists", request, getHandler(BOARD_INFO, INIT));
    }

    public void getFreeBoardArticles(){
        SendTool.request("/board/free/lists", getHandler(BOARD_FREE, INIT));
    }

    private Handler getHandler(String board, String purpose) {
        return new Handler(Looper.getMainLooper()) {
            public void handleMessage(@NonNull Message msg) {
                int code = msg.what;
                String response = (String) msg.obj;
                Log.d(TAG, "handleMessage: " + code);
                if (code == SendTool.HTTP_OK) {
                    switch (board) {
                        case BOARD_FREE:
                            List<FreeBoardPreview> freeBoardPreviews = SendTool.parseToList(response, FreeBoardPreview[].class);
                            activity.onSuccess(freeBoardPreviews, purpose);
                            break;
                        case BOARD_INFO:
                            List<InfoBoardPreview> infoBoardPreviews = SendTool.parseToList(response, InfoBoardPreview[].class);
                            activity.onSuccess(infoBoardPreviews, purpose);
                    }
                } else {
                    activity.onFailed();
                }
            }
        };
    }

    public List<InfoBoardPreview> convertToInfoBoard(List<? extends BoardPreview> source) {
        ArrayList<InfoBoardPreview> list = new ArrayList<>();
        for (BoardPreview preview : source) {
            list.add((InfoBoardPreview) preview);
        }
        return list;
    }

    public List<FreeBoardPreview> convertToFreeBoard(List<? extends BoardPreview> source) {
        ArrayList<FreeBoardPreview> list = new ArrayList<>();
        for (BoardPreview preview : source) {
            list.add((FreeBoardPreview) preview);
        }
        return list;
    }

    public void updateInfoBoard(long id, String building) {
        HashMap<String, Object> request = new HashMap<>();
        request.put("id", id);
        request.put("lectureBuilding", building);
        SendTool.requestForJson("/board/info/recent", request, getHandler(BOARD_INFO, LOAD_RECENT));
    }

    public void loadMoreInfoArticles(long no, String building) {
        HashMap<String, Object> request = new HashMap<>();
        request.put("id", no);
        request.put("lectureBuilding", building);
        SendTool.requestForJson("/board/info/load", request, getHandler(BOARD_INFO, LOAD_OLD));
    }

    public void loadMoreFreeArticles(long id) {
        HashMap<String, Object> request = new HashMap<>();
        request.put("id", id);
        SendTool.requestForJson("/board/free/load", request, getHandler(BOARD_FREE, LOAD_OLD));
    }

    public void updateFreeBoard(long id) {
        HashMap<String, Object> request = new HashMap<>();
        request.put("id", id);
        SendTool.requestForJson("/board/free/recent", request, getHandler(BOARD_FREE, LOAD_RECENT));
    }

    public boolean isAuthenticated(){
        return userInformation.fromPhoneVerify(context);
    }

    public void signIn(){
        Intent intent = new Intent(context, ActivitySignIn.class);
        context.startActivity(intent);
    }

    public void writeInfoBoard(){
        Intent intent = new Intent(context, WriteAnnounce.class);
        context.startActivity(intent);
    }

    public void writeFreeBoard(){
        Intent intent = new Intent(context, WriteFreeCommunity.class);
        context.startActivity(intent);
    }

    public void search(String currentMode) {
        if (currentMode.equals(BOARD_FREE)){
            Intent intent = new Intent(context, ActivityFreeBoardSearch.class);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, ActivityInfoBoardSearch.class);
            context.startActivity(intent);
        }
    }
}
