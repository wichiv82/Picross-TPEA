package com.example.picross;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonStart;
    Button buttonDownload;
    Button buttonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();

    }

    public void goToLevelsScreen(View view){
        Intent intent = new Intent(this, LevelsScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToDownloadScreen(View view){
        Intent intent = new Intent(this, DownloadLevelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    public void addListenerOnButton() {
        buttonStart = findViewById(R.id.buttonStart);
        buttonDownload = findViewById(R.id.buttonDownload);
        buttonQuit = findViewById(R.id.buttonQuit);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                goToLevelsScreen(arg0);
            }
        });


        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                goToDownloadScreen(arg0);
            }
        });

        buttonQuit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


    }

}
