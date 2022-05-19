package com.kyonggi.eku.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kyonggi.eku.ComminityItem;
import com.kyonggi.eku.DetailAnnounce;
import com.kyonggi.eku.R;

import java.util.ArrayList;

public class ComminityRecyclerAdapter extends RecyclerView.Adapter<ComminityRecyclerAdapter.ViewHolder> {
    private ArrayList<ComminityItem> arrayList = null;

    public ComminityRecyclerAdapter(ArrayList<ComminityItem> arrayList){
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.community_item, parent, false);
        ComminityRecyclerAdapter.ViewHolder vh = new ComminityRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComminityItem item = arrayList.get(position);

        holder.tv_c_title.setText(item.getc_title());
        holder.tv_c_writer.setText(item.getc_writer());
        holder.tv_c_comments.setText(item.getc_comment());
        holder.tv_c_time.setText(item.getc_time());
        holder.tv_c_views.setText(item.getc_views());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_c_title;
        TextView tv_c_writer;
        TextView tv_c_comments;
        TextView tv_c_time;
        TextView tv_c_views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_c_title     = (TextView) itemView.findViewById(R.id.Community_item_title);
            tv_c_writer    = (TextView) itemView.findViewById(R.id.Community_item_writer);
            tv_c_comments  = (TextView) itemView.findViewById(R.id.Community_item_comments);
            tv_c_time      = (TextView) itemView.findViewById(R.id.Community_item_time);
            tv_c_views     = (TextView) itemView.findViewById(R.id.Community_item_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        ((Activity) itemView.getContext()).finish();
                        Intent intent = new Intent(itemView.getContext(), DetailAnnounce.class);
                        intent.putExtra("id", arrayList.get(pos).getc_id());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
