package edu.msu.bhushanj.exambhushanj;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private GestureDetector gestureScanner;


    private Tiles tiles;


    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        tiles = new Tiles(getContext());
    }


    public boolean onFling(MotionEvent event1,
                           MotionEvent event2, float velocityX, float velocityY) {
        //add("onFling " + velocityX + ", " + velocityY);
        Toast.makeText(getContext(), "this is my Toast message!!! =)",
                Toast.LENGTH_LONG).show();
        return true;

    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        Rect r = new Rect(1, 1, canvas.getWidth(), getHeight());

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(r, paint);

        tiles.draw(canvas);
    }
}
