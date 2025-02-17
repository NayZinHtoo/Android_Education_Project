package com.example.scrollex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChooseGameActivity extends AppCompatActivity {

    Button btnSingleGame,btnMultiGame;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_game);

        btnMultiGame=findViewById(R.id.btn_multigame);
        btnSingleGame=findViewById(R.id.btn_single);

        btnSingleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        btnMultiGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MultiplayActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ChooseGameActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChooseGameActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        final EditText edMinutes = (EditText) promptView.findViewById(R.id.edmin);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String name=editText.getText().toString();
                        String min=edMinutes.getText().toString();

                        if(name.equals("")&&min.equals("")){
                            editText.setError("Please fill your Name!");
                            edMinutes.setError("Please fill minutes!");
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), PlayingGameActivity.class);
                            intent.putExtra("name",name );
                            intent.putExtra("min",min);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
