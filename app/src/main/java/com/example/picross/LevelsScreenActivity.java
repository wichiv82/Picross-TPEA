package com.example.picross;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class LevelsScreenActivity extends AppCompatActivity {
    private ListView listView;
    private File levelFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_screen_activity);

        levelFolder = new File( getFilesDir() + "/Levels");

        if (!levelFolder.exists()) {
            levelFolder.mkdir();
        }

        listView = findViewById(R.id.listViewLevelsScreen);

        afficherNiveaux();
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(LevelsScreenActivity.this, MainActivity.class));
        finish();

    }

    public void afficherNiveaux(){
        ArrayList<String> files = new ArrayList<>();
        try {
            File[] fichiers = levelFolder.listFiles();

            for(File f : fichiers)
                files.add(f.getName());

        } catch (Exception e){
            e.printStackTrace();
        }

        listView.setAdapter(new LevelsScreen(files, this) );

    }



}
