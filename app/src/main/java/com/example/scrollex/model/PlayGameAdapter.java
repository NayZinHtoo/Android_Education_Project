package com.example.scrollex.model;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scrollex.R;

import java.util.ArrayList;
import java.util.List;

public class PlayGameAdapter extends RecyclerView.Adapter<PlayGameAdapter.MyViewHolder> {
    String name;
    int pic;
    List<Subject> subjects=new ArrayList<Subject>();
    Context context;

    public PlayGameAdapter(Context context, List<Subject> subjectList){
        this.context=context;
        this.subjects=subjectList;

    }

    @NonNull
    @Override
    public PlayGameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayGameAdapter.MyViewHolder holder, int position) {

        if(subjects.get(position).getName().equals("System")){
            holder.textname.setTextColor(Color.RED);
        }
        else {
            holder.textname.setTextColor(Color.GREEN);
        }
        holder.textname.setText(subjects.get(position).getName());
        holder.textsms.setText(subjects.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {


        TextView textname,textsms;
         public MyViewHolder(@NonNull View itemView) {
             super(itemView);
             textname=itemView.findViewById(R.id.idname);
             textsms=itemView.findViewById(R.id.idtxt);
         }
     }
}
