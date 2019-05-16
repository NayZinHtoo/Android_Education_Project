package com.example.scrollex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrollex.model.PlayGameAdapter;
import com.example.scrollex.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class PlayingGameActivity extends AppCompatActivity {

    EditText ed_message;
    Button btnSend;
    RecyclerView rCycle;
    GridLayoutManager grid;

    Subject subject;
    List<Subject> subjectList;
    PlayGameAdapter playGameAdapter;

    String name="";
    String player_message="";

    DatabaseHelper databaseHelper;

    Boolean check=false;
    int player_Score=0;
    int systemScore=0;

    TextView txtA,txtB,txt_countdown;
    String counter="";
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playinggame_layout);
        ed_message=findViewById(R.id.edMessage);
        btnSend=findViewById(R.id.btnSend);

        txtA=findViewById(R.id.txt_scoreA);
        txt_countdown=findViewById(R.id.txt_countdown);
        txtB=findViewById(R.id.txt_scoreB);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        counter=intent.getStringExtra("min");
         count=Integer.parseInt(counter);


        rCycle=findViewById(R.id.idRecycle);
        grid=new GridLayoutManager(PlayingGameActivity.this,1);
        //grid.setReverseLayout(true);
        rCycle.setLayoutManager(grid);



        subjectList=new ArrayList<Subject>();
        subject=new Subject("System:","Welcome");
        subjectList.add(subject);
        BindData(subjectList);

        databaseHelper=new DatabaseHelper(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_message=ed_message.getText().toString();

                if(player_message.equals("")){
                    ed_message.setError("Please fill your word");
                }

                else {

                    String txtSend = subjectList.get(subjectList.size() - 1).getText();
                    char chSend = txtSend.charAt(txtSend.length() - 1);
                    char chReceive = player_message.charAt(0);

                    if (Character.toLowerCase(chSend) == Character.toLowerCase(chReceive)) {
                        check = databaseHelper.checkWord(player_message);
                        if (check) {
                            if (CheckList(player_message)) {
                                Toast.makeText(getApplicationContext(), "Please Try Again!! "+player_message+ " has in previous words", Toast.LENGTH_LONG).show();
                            } else {
                                player_Score += player_message.length();
                                txtA.setText(Integer.toString(player_Score));
                                subject = new Subject(name + ":", player_message);
                                subjectList.add(subject);
                                BindData(subjectList);

                                List<Subject> list = databaseHelper.getWord(player_message.charAt(player_message.length() - 1));
                                for (int i = 0; i < list.size(); i++) {
                                    String dictword = list.get(i).getText().toString();
                                    if (CheckList(dictword)) {
                                        Log.i(dictword, " has in previous words");
                                    } else {
                                        systemScore += dictword.length();
                                        txtB.setText(Integer.toString(systemScore));
                                        subject = new Subject("System:", dictword);
                                        subjectList.add(subject);
                                        BindData(subjectList);
                                        break;
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), player_message + " is meaningless", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error!!!! You should start " + chSend, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        new CountDownTimer(count*1000, 1000){
            public void onTick(long millisUntilFinished){
                txt_countdown.setText(String.valueOf(count));
                count--;
            }
            public  void onFinish(){
                txt_countdown.setText("0");
                count=Integer.parseInt(counter);
                if(player_Score>systemScore){
                    AlertDialog dialogshow=dialogwin();
                    dialogshow.show();
                    dialogshow.setCancelable(false);
                }
                else {
                    AlertDialog dialogshow=dialoglose();
                    dialogshow.show();
                    dialogshow.setCancelable(false);
                }


            }
        }.start();

    }

    public AlertDialog dialogwin(){
         AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("********")
                .setMessage("You Win")

               .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        count=Integer.parseInt(counter);
                        subjectList.clear();
                        subjectList=new ArrayList<Subject>();
                        subject=new Subject("System:","Begin");
                        subjectList.add(subject);
                        BindData(subjectList);
                        player_Score=0;
                        systemScore=0;
                        txtA.setText(player_Score+"");
                        txtB.setText(systemScore+"");
                        new CountDownTimer(count*1000, 1000){
                            public void onTick(long millisUntilFinished){
                                txt_countdown.setText(String.valueOf(count));
                                count--;
                            }
                            public  void onFinish(){
                                txt_countdown.setText("0");
                                count=Integer.parseInt(counter);
                                if(player_Score>systemScore){
                                    AlertDialog dialogshow=dialogwin();
                                    dialogshow.show();
                                    dialogshow.setCancelable(false);
                                }
                                else {
                                    AlertDialog dialogshow=dialoglose();
                                    dialogshow.show();
                                    dialogshow.setCancelable(false);
                                }


                            }
                        }.start();
                    }
                }).setNegativeButton("Home", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(PlayingGameActivity.this,HomeActivity.class);
                        startActivity(intent);
                     }
                 })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
         return dialog;
    }
    public AlertDialog dialoglose(){
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("********")
                .setMessage("You Lose")

                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        count=Integer.parseInt(counter);
                        subjectList.clear();
                        subjectList=new ArrayList<Subject>();
                        subject=new Subject("System:","Begin");
                        subjectList.add(subject);
                        BindData(subjectList);
                        player_Score=0;
                        systemScore=0;
                        txtA.setText(player_Score+"");
                        txtB.setText(systemScore+"");
                        new CountDownTimer(count*1000, 1000){
                            public void onTick(long millisUntilFinished){
                                txt_countdown.setText(String.valueOf(count));
                                count--;
                            }
                            public  void onFinish(){
                                txt_countdown.setText("0");
                                count=Integer.parseInt(counter);
                                if(player_Score>systemScore){
                                    AlertDialog dialogshow=dialogwin();
                                    dialogshow.show();
                                    dialogshow.setCancelable(false);
                                }
                                else {
                                    AlertDialog dialogshow=dialoglose();
                                    dialogshow.show();
                                    dialogshow.setCancelable(false);
                                }


                            }
                        }.start();
                    }
                }).setNegativeButton("Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(PlayingGameActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        return dialog;
    }
    private Boolean CheckList(String message){
        Boolean hasList=false;
        aa:
        for(int i=0;i<subjectList.size();i++){
            if(subjectList.get(i).getText().equals(message)){
                hasList=true;
                break aa;
            }
        }
        return hasList;
    }
    private void BindData(List<Subject> subjects){
        playGameAdapter =new PlayGameAdapter(PlayingGameActivity.this,subjects);
        playGameAdapter.notifyDataSetChanged();
        rCycle.setAdapter(playGameAdapter);
        rCycle.smoothScrollToPosition(subjectList.size()-1);
        ed_message.setText("");
        String n=subjectList.get(subjectList.size()-1).getText();
        ed_message.setHint("You must start "+n.charAt(n.length()-1));

    }
}