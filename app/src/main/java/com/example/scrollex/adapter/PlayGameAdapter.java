package com.example.scrollex.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scrollex.R;
import com.example.scrollex.model.Subject;

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

        if(subjects.get(position).getName().equals("System:")){
            holder.textname.setTextColor(Color.BLUE);
            holder.peopleImage.setImageResource(R.mipmap.computer);
        }
        else {
            holder.textname.setTextColor(Color.GREEN);
            holder.peopleImage.setImageResource(R.mipmap.boys);
        }
        holder.textname.setText(subjects.get(position).getName());
        String txt=subjects.get(position).getText();
        SpannableString spannableString=new SpannableString(txt);
        ForegroundColorSpan sg=new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(sg,txt.length()-1,txt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.textsms.setText(spannableString);

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {


        TextView textname,textsms;
        ImageView peopleImage;
         public MyViewHolder(@NonNull View itemView) {
             super(itemView);
             peopleImage=itemView.findViewById(R.id.peopleImage);
             textname=itemView.findViewById(R.id.idname);
             textsms=itemView.findViewById(R.id.idtxt);
         }
     }
}
