package edu.msu.bhushanj.exambhushanj;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by jaiwant on 11/16/2016.
 */



public class Tiles {

    private int score = 0;

    private String impValue;

    public String getImpValue() {
        return impValue;
    }

    public void setImpValue(String impValue) {
        this.impValue = impValue;
    }

    private int tileType;
    private float tileWidth;
    private float tileHeight;

    private float xLoc = 0f, yLoc = 0f;

    private Color lineColor;

    private Paint linePaint;
    private Paint textPaint;

    private float width;
    private float height;



    private int x, y;

    private int [][] grid;

    Random r;

    public int genRandIndX(){
        int x = r.nextInt(4);
        return x;
    }
    public int genRandIndY(){
        int y = r.nextInt(4);
        return y;
    }


    public void resetGame(){
        Values.value.setReset("No");

        for(int x=0; x<=3; x++){
            for(int y=0; y<=3; y++){
                grid[x][y]=0;
            }
        }

        int initialX = genRandIndX();
        int initialY = genRandIndY();

        int initialX2 = genRandIndX();
        int initialY2 = genRandIndY();

        while(initialX2==initialX||initialY==initialY2){
            initialX2=genRandIndX();
            initialY2=genRandIndY();
        }

        grid[initialX][initialY] = 2;
        grid[initialX2][initialY2] = 2;

    }

    private Context context;

    public Tiles(Context context){
        this.context = context;
        r = new Random();
        r.setSeed(System.currentTimeMillis());

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(73f);

        grid = new int[4][4];

        for(int x=0; x<=3; x++){
            for(int y=0; y<=3; y++){
                grid[x][y]=0;
            }
        }

        int initialX = genRandIndX();
        int initialY = genRandIndY();

        int initialX2 = genRandIndX();
        int initialY2 = genRandIndY();

        while(initialX2==initialX||initialY==initialY2){
            initialX2=genRandIndX();
            initialY2=genRandIndY();
        }

        //grid[0][1]=2;
        //grid[1][1]=2;

        grid[initialX][initialY] = 2;
        grid[initialX2][initialY2] = 2;

    }

    public void genRandomTile(Random r){
        int tempX = genRandIndX();
        int tempY = genRandIndY();
        while(grid[tempX][tempY]!=0){
            tempX = genRandIndX();
            tempY = genRandIndY();
        }
        grid[tempX][tempY]=2;
        score += 2;
    }




   public void leftToRight() {

       boolean isSuccess=false;
       int tempScore=0;
        for (int x = 3; x > 0; x--) {
            for (int y = 3; y > 0; y--) {
                if(grid[x][y]==0){
                    for(int i=0; i<x; i++){
                        if(grid[x-i][y]!=0){
                            grid[x-i][y]=grid[x-i-1][y];
                        }
                    }
                    //grid[x-1][y]=grid[x][y];
                    //grid[x][y]=0;
                    isSuccess = true;
                }
                if(grid[x-1][y]==grid[x][y]){
                    grid[x-1][y] = 2*grid[x][y];
                    for(int i=x; i<3; i++){
                        grid[x][y]=grid[x+1][y];
                        grid[x+1][y]=0;
                    }
                }
            }
        }
       if(isSuccess){
           genRandomTile(this.r);
           score = score + 4;
       }


    }
    public void righttoleft() {

        boolean isSuccess=false;
        int tempScore=0;
        for (int x = 3; x > 0; x--) {
            for (int y = 3; y > 0; y--) {
                if(grid[x][y]==0){
                    for(int i=0; i<x; i++){
                        if(grid[x-i][y]!=0){
                            grid[x-i][y]=grid[x-i-1][y];
                        }
                    }
                    //grid[x-1][y]=grid[x][y];
                    //grid[x][y]=0;
                    isSuccess = true;
                }
                if(grid[x-1][y]==grid[x][y]){
                    grid[x-1][y] = 2*grid[x][y];
                    for(int i=x; i<3; i++){
                        grid[x][y]=grid[x+1][y];
                        grid[x+1][y]=0;
                    }
                }
            }
        }
        if(isSuccess){
            genRandomTile(this.r);
            score = score + 4;
        }


    }

    public void bottomtoup() {
        score = score + 4;
        genRandomTile(this.r);
    }





    public void draw(Canvas canvas){

        TextView txt = (TextView)((Activity)context).findViewById(R.id.textView2);
        txt.setText(String.valueOf(score));

        if(Values.value.getReset()=="yes"){
            resetGame();
        }

        Values.value.setTotalScore(score);

        if(Values.value.getWhatType()=="lefttoright"){
            leftToRight();
        }
        else if(Values.value.getWhatType()=="righttoleft"){
            righttoleft();
        }

        for(int x=0; x<=3; x++){
            for(int y=0; y<=3; y++){
                if(grid[x][y]>0){
                    canvas.save();
                    this.x=x;
                    this.y=y;
                    drawATile(x,y,canvas,grid[x][y]);
                    canvas.restore();
                }
            }
        }
    }

    public void drawATile(int x, int y, Canvas canvas, int tileType){
        if(tileType==2){
            linePaint.setColor(Color.CYAN);
        }
        else if(tileType==4){
            linePaint.setColor(Color.BLUE);
        }
        else if(tileType==8){
            linePaint.setColor(Color.GREEN);
        }
        else if(tileType==16){
            linePaint.setColor(Color.GRAY);
        }
        else if(tileType==32){
            linePaint.setColor(Color.YELLOW);
        }
        else if(tileType==64){
            linePaint.setColor(Color.MAGENTA);
        }
        else if(tileType==128){
            linePaint.setColor(Color.RED);
        }
        else if(tileType==256) {
            int color = Color.parseColor("#a52a2a");
            linePaint.setColor(color);
        }
        else if(tileType==512){
            int color = Color.parseColor("#FFE800");
            linePaint.setColor(color);
        }
        else if(tileType==1024){
            int color = Color.parseColor("#FF94DD");
            linePaint.setColor(color);
        }
        else if(tileType==2048){
            linePaint.setColor(Color.BLACK);
        }


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(73f);

        width = canvas.getWidth();
        height = canvas.getHeight();

        tileWidth = width/4;
        tileHeight = height/4;

        String temp = String.valueOf(tileType);

        Rect bounds = new Rect();
        textPaint.getTextBounds(temp, 0, temp.length(), bounds);

        int tempx = ((int)tileWidth / 2) - (bounds.width() / 2);
        int tempy = ((int)tileHeight / 2);

        this.xLoc = this.x * tileWidth;
        this.yLoc = this.y * tileHeight;

        canvas.save();
        canvas.translate(xLoc,yLoc);



        canvas.drawRect(0,0,tileWidth,tileHeight,linePaint);
        Rect r = new Rect(0, 0, (int)tileWidth, (int)tileHeight);

        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(r, paint);

        canvas.drawText(temp, tempx,tempy, textPaint);
        canvas.restore();
    }
}
