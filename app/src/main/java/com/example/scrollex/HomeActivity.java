package com.example.scrollex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devs.readmoreoption.ReadMoreOption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HomeActivity extends Activity {

    Button btnPlay1,btnPlay2;
    TextView txt1,txt2;
    ReadMoreOption readMoreOption;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);


        btnPlay1=findViewById(R.id.idPlay1);
        btnPlay2=findViewById(R.id.idPlay2);

        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);

        databaseHelper=new DatabaseHelper(this);
        File database = getApplicationContext().getDatabasePath(databaseHelper.DBName);
        if (false == database.exists()) {
            databaseHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Log.i("COPY SUCCESS ","FROM DATABASE"+copyDatabase(this));
            } else {
                Log.i("COPY DENINED ","FROM DATABASE #####"+copyDatabase(this));
                return;
            }
        }

        readMoreOption=new ReadMoreOption.Builder(this)
                .textLength(50)
                .moreLabel("See More..")
                .lessLabel("Less")
                .moreLabelColor(Color.BLUE)
                .lessLabelColor(Color.GREEN)
                .labelUnderLine(true)
                .build();

        readMoreOption.addReadMoreTo(txt2,"If you want to play game, you must know the following the facts:\nYou must start a last letter from previous word\nFor example:\nSystem: Welcome\nMg Mg: egg\nSystem: goat\n Mg Mg:taste");
        readMoreOption.addReadMoreTo(txt1,"If you want to play game,you must answer the quiz and then if you pass the step,you can play the game");

        btnPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ChooseGameActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream myInput = context.getAssets().open(databaseHelper.DBName);
            String outFileName = databaseHelper.DBLocatoin + databaseHelper.DBName;
            Log.i("Before file out","Succes...");
            OutputStream myOutput = new FileOutputStream(outFileName);
            Log.i("After file out","Succes...");
            byte[] buffer = new byte[1024];
            int length = 0;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Copy Database","Copy Success....");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
