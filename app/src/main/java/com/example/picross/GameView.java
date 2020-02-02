package com.example.picross;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class GameView extends View {
    private Rect background;
    private Paint paint;
    private Game game;
    private ImageView img_ok ;
    private Context context;

    private final int debutGrilleX = 300;
    private final int debutGrilleY = 400;

    // dimensions de chaque cellule
    private float celluleTailleX;
    private float celluleTailleY;

    // Contient les coordonnees de chaque cellule a dessiner dans onDraw()
    Rect cellule;

    public GameView(Context context){
        super(context);
        this.context = context;
        init();
    }


    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        init();
    }

    void init(){
        this.background = new Rect();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.cellule = new Rect();


    }

    void setImg_ok(ImageView i){
        this.img_ok = i;
        this.img_ok.setVisibility(View.GONE);
    }

    void setGame(Game g){
        this.game = g;
        if(this.game.isWon())
            this.img_ok.setVisibility(View.VISIBLE);
        postInvalidate();
    }

    /**
     * Renvoie les coordonnees de la cellule dans la grille sur lequel un clic est effectue
     * @param x Coordonnees X du clic
     * @param y Coordonnees Y du clic
     * @return Coordonnes de la cellule
     */
    public Point getCelluleFromCoordinates(float x, float y){
        x = x - debutGrilleX;
        y = y - debutGrilleY;

        if(x < 0 || y < 0)
            return null;

        int celluleX = (int) (x/celluleTailleX);
        int celluleY = (int) (y/celluleTailleY);

        if(celluleX >= game.getLargeur() || celluleY >= game.getHauteur())
            return null;

        return new Point(celluleX, celluleY);
    }

    public int getCelluleColor(EnumEtatCase etat){
        if(etat.equals(EnumEtatCase.NOIRE))
            return Color.BLACK;
        if(etat.equals(EnumEtatCase.BLANCHE))
            return Color.WHITE;
        if(etat.equals(EnumEtatCase.MARQUE))
            return Color.GRAY;

        return Color.GREEN;
    }

    @Override
    public void onDraw(Canvas canvas){
        if(game == null)
            return;

        background.left = 0;
        background.top = 0;
        background.right = getWidth();
        background.bottom = getHeight();

        int nbColonnes = this.game.getLargeur();
        int nbLignes = this.game.getHauteur();

        celluleTailleX =  (getWidth()-debutGrilleX)/nbColonnes;
        celluleTailleY =  (getHeight()-debutGrilleY)/nbLignes;

        float defaultStroke = paint.getStrokeWidth();

        paint.setColor(getResources().getColor(R.color.beige));
        canvas.drawRect(background, paint);

        for (int i=0; i < nbColonnes ; i++){
            for (int j=0; j < nbLignes ; j++){

                cellule.left =(int) (i * celluleTailleX + debutGrilleX);
                cellule.top = (int) (j * celluleTailleY + debutGrilleY);
                cellule.right = (int) (cellule.left + celluleTailleX);
                cellule.bottom = (int) (cellule.top + celluleTailleY);

                paint.setColor(getCelluleColor(game.getEtatCase(i, j)));
                canvas.drawRect(cellule, paint);
            }
        }

        paint.setStrokeWidth( (defaultStroke +1) * 5);
        paint.setColor(Color.BLACK);
        for(int i=0; i < nbColonnes; i++) {
            canvas.drawLine( (debutGrilleX + celluleTailleX * i), 0, (debutGrilleX + celluleTailleX * i), getHeight(), paint);
        }

        for(int i=0; i < nbLignes; i++) {
            canvas.drawLine(0, (debutGrilleY + celluleTailleY * i), getWidth(), (debutGrilleY + celluleTailleY * i), paint);
        }
        paint.setStrokeWidth(defaultStroke);


        ArrayList<Integer>[] top_indices = game.getTop_indices();
        ArrayList<Integer>[] left_indices = game.getLeft_indices();

        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        for(int i=0; i < top_indices.length; i++){
            for(int j=0; j < top_indices[i].size(); j++) {
                canvas.drawText(
                        top_indices[i].get(j).toString(),
                        (debutGrilleX + (celluleTailleX/2 - paint.getTextSize()/3) + i * celluleTailleX) ,
                        debutGrilleY - 10 - j * paint.getTextSize() ,
                        paint
                );
            }
        }
        for(int i=0; i < left_indices.length; i++){
            for(int j=0; j < left_indices[i].size(); j++) {
                canvas.drawText(
                        left_indices[i].get(j).toString(),
                        debutGrilleX - 40 - j * 40 ,
                        debutGrilleY + (celluleTailleY/3 *2 )  + i * celluleTailleY,
                        paint
                );
            }
        }
    }

    @Override
    public boolean onTouchEvent( MotionEvent event){
        if(game == null || game.isWon()) {
            this.img_ok.setVisibility(View.VISIBLE);
            performClick();
            return false;
        }

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_UP :
                Point p = getCelluleFromCoordinates(x, y);
                if(p != null) {
                    game.setEtatCase(p.x, p.y, game.getEtatCase(p.x, p.y).getNextEtat());
                    if(game.isCompleted()){
                        game.setWon(true);
                        this.img_ok.setVisibility(View.VISIBLE);
                        askSaveEndGame();
                    }
                }
                break;

            default :
                break;
        }

        performClick();
        postInvalidate();

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void askSaveEndGame(){
        new AlertDialog.Builder(this.context)
                .setIcon(android.R.drawable.star_big_on)
                .setTitle("Dessin Résolu !")
                .setMessage("Souhaitez-vous sauvegarder ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        game.save();
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }


}
