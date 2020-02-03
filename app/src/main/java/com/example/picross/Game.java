package com.example.picross;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Game {
    private EnumEtatCase [][] grille;
    private ArrayList<Integer>[] top_indices;
    private ArrayList<Integer>[] left_indices;
    private String name;
    private Context context;
    private boolean won;


    Game(int colonnes, int lignes, ArrayList<Integer>[] top, ArrayList<Integer>[]left, String name, Context context){
        this.grille = new EnumEtatCase[colonnes][lignes];
        for (int i=0; i<colonnes; i++){
            for (int j=0; j<lignes; j++){
                grille[i][j] = EnumEtatCase.BLANCHE;
            }
        }
        this.top_indices = top;
        this.left_indices = left;

        this.name = name;
        this.context = context;
        this.won = false;
    }

    int getLargeur(){
        if(grille == null)
            return 0;
        return grille.length;
    }

    int getHauteur(){
        if(grille == null || grille[0] == null)
            return 0;
        return grille[0].length;
    }

    boolean isWon(){
        return this.won;
    }

    void setWon(boolean won){
        this.won = won;
        if(this.won) {
            clearCases();
        }
    }

    EnumEtatCase getEtatCase(int x, int y){
        return grille[x][y];
    }

    void setEtatCase(int x, int y, EnumEtatCase etat){
        grille[x][y] = etat;
    }

    void clearCases(){
        for(int i=0; i<grille.length; i++){
            for (int j=0; j<grille[i].length; j++){
                if(grille[i][j] == EnumEtatCase.MARQUE)
                    grille[i][j] = EnumEtatCase.BLANCHE;
            }
        }
    }

    ArrayList<Integer>[] getTop_indices(){
        return top_indices;
    }

    ArrayList<Integer>[] getLeft_indices(){
        return left_indices;
    }


    void load(){
        File file = new File( context.getFilesDir() + "/Progression/" + this.name );

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int ligne = 0;

            while ((line = br.readLine()) != null) {

                String[] cases = line.split("(?!^)");

                int colonne = 0;
                for(String c : cases){
                    grille[colonne][ligne] = EnumEtatCase.valueOf(Integer.parseInt(c));
                    colonne++;
                }
                ligne++;
            }
            br.close();

            if(isCompleted())
                setWon(true);
            else
                setWon(false);
        }
        catch (IOException e) {}

    }

    void save(){
        String contenu = "";
        for(int j=0; j<grille.length; j++){
            for(int i=0; i<grille[j].length; i++){
                contenu += grille[i][j].getEtat();
            }
            contenu += "\n";
        }

        File file = new File(context.getFilesDir(), "Progression");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File f = new File(file, this.name);
            FileWriter writer = new FileWriter(f);
            writer.append(contenu);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    boolean isCompleted(){
        for(int i=0; i<grille.length; i++){
            int nbCasesNoires = 0;
            int indice_numero = 0;
            for (int j=grille[i].length-1; j>=0; j--){
                if(grille[i][j] == EnumEtatCase.NOIRE) {
                    nbCasesNoires++;
                    if(j == 0){
                        if(indice_numero >= top_indices[i].size()) {
                            return false;
                        }
                        if (nbCasesNoires != top_indices[i].get(indice_numero)) {
                            return false;
                        }else {
                            nbCasesNoires = 0;
                            indice_numero++;
                        }
                    }
                }else if(grille[i][j] == EnumEtatCase.BLANCHE || grille[i][j] == EnumEtatCase.MARQUE){
                    if(nbCasesNoires > 0) {
                        if(indice_numero >= top_indices[i].size()) {
                            return false;
                        }
                        if (nbCasesNoires != top_indices[i].get(indice_numero)) {
                            return false;
                        }else {
                            nbCasesNoires = 0;
                            indice_numero++;
                        }
                    }
                }
            }

            if(indice_numero != top_indices[i].size() && top_indices[i].get(0) != 0) {
                return false;
            }
        }

        for (int j=0; j<grille[0].length; j++){
            int nbCasesNoires = 0;
            int indice_numero = 0;

            for(int i=grille.length-1; i>= 0; i--){
                if(grille[i][j] == EnumEtatCase.NOIRE) {
                    nbCasesNoires++;
                    if(i == 0){
                        if(indice_numero >= left_indices[j].size()) {
                            return false;
                        }
                        if (nbCasesNoires != left_indices[j].get(indice_numero)) {
                            return false;
                        }else {
                            nbCasesNoires = 0;
                            indice_numero++;
                        }
                    }
                }else if(grille[i][j] == EnumEtatCase.BLANCHE || grille[i][j] == EnumEtatCase.MARQUE){
                    if(nbCasesNoires > 0) {
                        if(indice_numero >= left_indices[j].size()) {
                            return false;
                        }
                        if (nbCasesNoires != left_indices[j].get(indice_numero)) {
                            return false;
                        }else {
                            nbCasesNoires = 0;
                            indice_numero++;
                        }
                    }
                }
            }
            if(indice_numero != left_indices[j].size() && left_indices[j].get(0) != 0) {
                return false;
            }
        }
        return true;
    }
}
