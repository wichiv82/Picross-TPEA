package com.example.picross;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class DownloadLevelActivity extends AppCompatActivity {

    private String lien = "https://raw.githubusercontent.com/wichiv82/Dessins-Picross/master/";
    private String contenu = "";

    private File levelFolder;
    private ArrayList<String> missingFiles = new ArrayList<>();

    TextView downloadMessage;
    Button downloadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        this.downloadMessage = findViewById(R.id.downloadText);
        this.downloadButton = findViewById(R.id.downloadButton);

        this.downloadButton.setVisibility(View.GONE);

        this.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                downloadFile();
            }
        });

        levelFolder = new File( getFilesDir() + "/Levels");

        if (!levelFolder.exists()) {
            levelFolder.mkdir();
        }

        this.missingFiles = getMissingFiles();

        setDownloadMessage(this.missingFiles.size());

    }

    void setDownloadMessage(int nbMissingFiles){

        if(nbMissingFiles <= 0){
            this.downloadMessage.setText("Aucun fichier en ligne retrouvé. Vous êtes à jour !");
            this.downloadButton.setVisibility(View.GONE);
        } else {
            this.downloadMessage.setText(nbMissingFiles + " fichier(s) retrouvé(s) en ligne. Voulez-vous le(s) télécharger ?");
            this.downloadButton.setVisibility(View.VISIBLE);
        }
    }

    ArrayList<String> getMissingFiles(){

        HashSet<String> online_files = getOnlineFiles();
        HashSet<String> online_files_copy = (HashSet<String>) online_files.clone();

        HashSet<String> local_files = getLocalFiles();

        online_files_copy.removeAll(local_files);

        return new ArrayList<>(online_files_copy);
    }


    HashSet<String> getLocalFiles(){
        ArrayList<String> files = new ArrayList<>();

        File[] fichiers = levelFolder.listFiles();
        for(File f : fichiers)
            files.add(f.getName());

        return new HashSet<>(files);
    }

    HashSet<String> getOnlineFiles(){
        final ArrayList<String> result = new ArrayList<>();

        Thread t = new Thread(new Runnable(){

            public void run(){

                try {

                    URL url = new URL(lien + "originSource.txt");

                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(30000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line;
                    while ((line = in.readLine()) != null) {
                        result.add(line);
                    }
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();

        try {
            t.join();
        } catch(Exception e){
            e.printStackTrace();
        }


        return new HashSet<>(result);
    }

    void downloadFile(){

       Thread t =  new Thread(new Runnable(){

            public void run(){
                for(String name : missingFiles) {
                    try {

                        URL url = new URL(lien + name);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(30000);

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        String line;
                        contenu = "";

                        while ((line = in.readLine()) != null) {
                            contenu += line + "\n";
                        }
                        in.close();

                        writeFileInLevelFolder(name, contenu);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();

        try {
            t.join();
            setDownloadMessage(0);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    void writeFileInLevelFolder(String name, String data){
        try{
            File f = new File(levelFolder, name);
            FileWriter writer = new FileWriter(f);
            writer.append(data);
            writer.flush();
            writer.close();
            this.getMissingFiles().remove(name);

        }catch (Exception e){
            e.printStackTrace();

        }
    }

}
