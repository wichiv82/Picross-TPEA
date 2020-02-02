package com.example.picross;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Game game;
    private Button saveButton;
    private Button loadButton;
    private Button quitButton;
    ImageView image_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.myCustomView);

        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);
        quitButton = findViewById(R.id.quitButton);
        image_ok = findViewById(R.id.ok_img);

        Bundle bundle = getIntent().getExtras();
        String level = bundle.getString("level");
        String launch = bundle.getString("launch");

        game = getGame(level);


        if(launch.equals("CONTINUE")) {
            game.load();
        }

        gameView.setImg_ok(image_ok);
        gameView.setGame(game);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                game.save();
            }

        });

        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                game.load();
                gameView.postInvalidate();
            }

        });

        quitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quitGame();
            }

        });
    }

    @Override
    public void onBackPressed() {
        quitGame();
    }

    public void quitGame(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Quitter la partie")
                .setMessage("Êtes-vous sûr de vouloir quitter la partie ?")
                .setPositiveButton("Quitter", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(GameActivity.this, LevelsScreenActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }


    public Game getGame(String filename){
        ArrayList<ArrayList<Integer>> liste1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> liste2 = new ArrayList<>();

        File fichier = new File( getFilesDir() + "/Levels/" +  filename) ;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fichier));
            String line;
            int num = 1;

            while ((line = reader.readLine()) != null){
                if(line.equals("-")){
                    num++;
                    continue;
                }
                String[] nombres = line.split("\\.");
                ArrayList<Integer> l = new ArrayList<>();
                for (int i=0; i<nombres.length; i++)
                    l.add(Integer.parseInt(nombres[i].trim()));

                Collections.reverse(l);

                if(num == 1) {
                    liste1.add(l);
                }else {
                    liste2.add(l);
                }
            }

            ArrayList<Integer>[] left = new ArrayList[liste1.size()];
            for(int i=0; i< left.length; i++)
                left[i] = liste1.get(i);

            ArrayList<Integer>[] top = new ArrayList[liste2.size()];
            for(int i=0; i< top.length; i++)
                top[i] = liste2.get(i);

            return new Game(top.length, left.length, top, left, filename, this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
