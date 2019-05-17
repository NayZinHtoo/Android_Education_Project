package com.example.scrollex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MultiplayActivity extends AppCompatActivity {

    ImageView gotoSetting;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer_layout);
        gotoSetting=findViewById(R.id.goToSettings);

        gotoSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Open Wifi settings
                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });
    }
}
