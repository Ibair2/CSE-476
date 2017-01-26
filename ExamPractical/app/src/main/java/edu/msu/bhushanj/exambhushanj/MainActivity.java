package edu.msu.bhushanj.exambhushanj;

import android.content.res.Resources;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private Tiles tiles;

    private TextView tv = null;

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tiles = new Tiles(getApplicationContext());
        this.gestureDetector = new GestureDetectorCompat(this,this);

        tv = (TextView)findViewById(R.id.textView2);
    }

    public void printStarter(View view) {
        //((TextView)findViewById(R.id.textView2)).setText("Your Text");
        //Values.value.setReset("yes");
        recreate();
        //(GameView)findViewById(R.id.view).invalidate();
    }

    public void reset(){
        Values.value.setReset("yes");
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private int deltaDist = 80;
    private int deltaVel = 50;

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel) {

        View v = findViewById (R.id.view);


        //Values.value.getTotalScore();

        getApplicationContext();

        if(event1.getX() - event2.getX() > deltaDist && Math.abs(xVel) > deltaVel){
            //Log.d("", "right to left");
            Values.value.setWhatType("righttoleft");
            v.invalidate();
            return true;
        } else if (event2.getX() - event1.getX() > deltaDist && Math.abs(xVel) > deltaVel){
            //Log.d("", "left to right");
            Values.value.setWhatType("lefttoright");
            v.invalidate();
            return true;
        }
        if(event1.getY() - event2.getY() > deltaDist && Math.abs(xVel) > deltaVel){
           // Log.d("", "bottom to top");
            Values.value.setWhatType("bottomtotop");
            v.invalidate();
            return false;
        } else if (event2.getY() - event1.getY() > deltaDist && Math.abs(xVel) > deltaVel){
            //Log.d("","top to bottom");
            return false;
        }


        return false;

    }
}

