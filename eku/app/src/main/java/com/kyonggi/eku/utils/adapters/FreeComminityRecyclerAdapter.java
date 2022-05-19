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

import com.kyonggi.eku.DetailFreeCommunity;
import com.kyonggi.eku.FreeComminityItem;
import com.kyonggi.eku.R;

import java.util.ArrayList;

public class FreeComminityRecyclerAdapter extends RecyclerView.Adapter<FreeComminityRecyclerAdapter.ViewHolder> {
    private ArrayList<FreeComminityItem> arrayList = null;

    public FreeComminityRecyclerAdapter(ArrayList<FreeComminityItem> arrayList){
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.free_community_item, parent, false);
        FreeComminityRecyclerAdapter.ViewHolder vh = new FreeComminityRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FreeComminityItem item = arrayList.get(position);

        holder.tv_fc_title.setText(item.getFc_title());
        holder.tv_fc_writer.setText(item.getFc_writer());
        holder.tv_fc_comments.setText(item.getFc_comment());
        holder.tv_fc_time.setText(item.getFc_time());
        holder.tv_fc_views.setText(item.getFc_views());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_fc_title;
        TextView tv_fc_writer;
        TextView tv_fc_comments;
        TextView tv_fc_time;
        TextView tv_fc_views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_fc_title     = (TextView) itemView.findViewById(R.id.FreeCommunity_item_title);
            tv_fc_writer    = (TextView) itemView.findViewById(R.id.FreeCommunity_item_writer);
            tv_fc_comments  = (TextView) itemView.findViewById(R.id.FreeCommunity_item_comments);
            tv_fc_time      = (TextView) itemView.findViewById(R.id.FreeCommunity_item_time);
            tv_fc_views     = (TextView) itemView.findViewById(R.id.FreeCommunity_item_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        ((Activity) itemView.getContext()).finish();
                        Intent intent = new Intent(itemView.getContext(), DetailFreeCommunity.class);
                        intent.putExtra("id", arrayList.get(pos).getFc_id());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}