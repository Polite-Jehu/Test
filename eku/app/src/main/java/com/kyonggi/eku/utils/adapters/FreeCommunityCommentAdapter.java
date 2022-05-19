package com.kyonggi.eku.utils.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kyonggi.eku.FreeCommunityCommentItem;
import com.kyonggi.eku.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FreeCommunityCommentAdapter extends BaseAdapter {
    private final int EDIT_MODE     = 0;
    private final int DELETE_MODE   = 1;
    private ArrayList<FreeCommunityCommentItem> arrayList;
    private Context context;


    public FreeCommunityCommentAdapter(ArrayList<FreeCommunityCommentItem> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        FreeCommunityCommentItem freeCommunityCommentItem = arrayList.get(i);

        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.free_community_comment_item, viewGroup, false);
        }

        TextView tv_writer      = view.findViewById(R.id.detail_Free_comment_writer);
        TextView tv_time        = view.findViewById(R.id.detail_Free_comment_time);
        TextView tv_content     = view.findViewById(R.id.detail_Free_comment_comment);
        EditText et_editText    = view.findViewById(R.id.detail_Free_comment_edit);
        Button btn_edit         = view.findViewById(R.id.detail_Free_comment_btn_edit);
        Button btn_delete       = view.findViewById(R.id.detail_Free_comment_btn_delete);
        Button btn_submit       = view.findViewById(R.id.detail_Free_comment_btn_edit_submit);
        Button btn_cancle       = view.findViewById(R.id.detail_Free_comment_btn_edit_cancle);
        LinearLayout linear_btn = view.findViewById(R.id.detail_Free_comment_button_layout);
        LinearLayout linear_edit= view.findViewById(R.id.detail_Free_comment_edit_layout);

        String writer   = freeCommunityCommentItem.getWriter();
        String time     = freeCommunityCommentItem.getTime();
        String comment  = freeCommunityCommentItem.getComment();
        String id       = freeCommunityCommentItem.getComment_id();

        tv_writer.setText(writer);
        tv_time.setText(time);
        tv_content.setText(comment);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_tv = tv_content.getText().toString();
                et_editText.setText(edit_tv);

                tv_content.setVisibility(View.GONE);
                et_editText.setVisibility(View.VISIBLE);

                linear_btn.setVisibility(View.GONE);
                linear_edit.setVisibility(View.VISIBLE);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.remove(i);
                request(id,null,DELETE_MODE);
                notifyDataSetChanged();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_text = et_editText.getText().toString();


                request(id,edit_text,EDIT_MODE);

                tv_content.setText(edit_text);
                freeCommunityCommentItem.setComment(edit_text);
                notifyDataSetChanged();

                tv_content.setVisibility(View.VISIBLE);
                et_editText.setVisibility(View.GONE);

                linear_btn.setVisibility(View.VISIBLE);
                linear_edit.setVisibility(View.GONE);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_btn.setVisibility(View.VISIBLE);
                linear_edit.setVisibility(View.GONE);

                et_editText.setVisibility(View.GONE);
                tv_content.setVisibility(View.VISIBLE);
                et_editText.setText("");
            }
        });
        return view;
    }

    public void request(String id,String comment, int mode){
        JSONObject jsonBodyObj = new JSONObject();
        String url ="https://www.eku.kro.kr/board/free/view";
        if(mode == EDIT_MODE){
            try{
                jsonBodyObj.put("commentId",Integer.parseInt(id));
                jsonBodyObj.put("content", comment);
                url ="https://www.eku.kro.kr/comment/free/update";
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else if(mode == DELETE_MODE){
            try{
                jsonBodyObj.put("commentId",Integer.parseInt(id));
                url ="https://www.eku.kro.kr/comment/free/delete";
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        String requestBody = String.valueOf(jsonBodyObj.toString());
        request(requestBody, url);
    }

    public void request(String requestBody, String url){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("---","---");
                        Log.w("//===========//","================================================");
                        Log.d("","\n"+"[FREE_COMMUNITY_COMMENT > getRequestVolleyPOST_BODY_JSON() 메소드 : Volley POST_BODY_JSON 요청 응답]");
                        Log.d("","\n"+"["+"응답 전체 - "+String.valueOf(response.toString())+"]");
                        Log.w("//===========//","================================================");
                        Log.d("---","---");
                    }
                },
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
