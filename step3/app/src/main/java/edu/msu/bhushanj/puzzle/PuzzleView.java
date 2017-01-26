package edu.msu.bhushanj.puzzle;

import android.content.Context;
import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Custom view class for our Puzzle.
 */
public class PuzzleView extends View {


    /**
     * The actual puzzle
     */
    private Puzzle puzzle;


    public PuzzleView(Context context) {
        super(context);
        init(null, 0);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return puzzle.onTouchEvent(this, event);
    }

    private void init(AttributeSet attrs, int defStyle) {
        puzzle = new Puzzle(getContext());


    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        puzzle.saveInstanceState(bundle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        puzzle.draw(canvas);
    }

    /**
     * Load the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        puzzle.loadInstanceState(bundle);
    }


}