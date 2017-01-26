package edu.msu.simunovi.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

/**
 * A representation of the playing area
 */
public class PlayingArea{
    /**
     * Width of the playing area (integer number of cells)
     */
    private int width;

    private PlayingAreaView playingAreaView;

    public static int drawText;



    /**
     * Height of the playing area (integer number of cells)
     */
    private int height;

    /**
     * Storage for the pipes
     * First level: X, second level Y
     */
    private Pipe [][] pipes;

    public PlayingArea(Context context) {
    }

    /**
     * Construct a playing area
     * @param width Width (integer number of cells)
     * @param playingAreaView
     * @param height Height (integer number of cells)
     */

    public void setPlayingAreaView(PlayingAreaView playingAreaView) {
        this.playingAreaView = playingAreaView;
    }

    public PlayingArea(int width,  int height) {
        if (width == 5){
            this.width = 5;
            this.height = 5;
        }else if (width == 10) {
            this.width = 10;
            this.height = 10;
        }else{
            this.width = 20;
            this.height = 20;
        }

        drawText = this.width;
        // Allocate the playing area
        // Java automatically initializes all of the locations to null
        pipes = new Pipe[width][height];
    }



    /**
     * Get the playing area height
     * @return Height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the playing area width
     * @return Width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the pipe at a given location.
     * This will return null if outside the playing area.
     * @param x X location
     * @param y Y location
     * @return Reference to Pipe object or null if none exists
     */
    public Pipe getPipe(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }

        return pipes[x][y];
    }

    /**
     * Add a pipe to the playing area
     * @param pipe Pipe to add
     * @param x X location
     * @param y Y location
     */
    public void add(Pipe pipe, int x, int y) {
        pipes[x][y] = pipe;
        pipe.set(this, x, y);
    }

    /**
     * Search to determine if this pipe has no leaks
     * @param start Starting pipe to search from
     * @return true if no leaks
     */
    public boolean search(Pipe start) {
        /*
         * Set the visited flags to false
         */
        for(Pipe[] row: pipes) {
            for(Pipe pipe : row) {
                if (pipe != null) {
                    pipe.setVisited(false);
                }
            }
        }

        /*
         * The pipe itself does the actual search
         */
        return start.search();
    }

    public void draw(Canvas canvas, float blockSize) {
        canvas.save();

        int playingAreaSize = (int)(width * blockSize);

        /*
         * Draw the outline of the playing field for now
         */
        //canvas.drawRect(0, 0, playingAreaSize, playingAreaSize, debugPaint);

        /*
         * Draw each pipe
         */
        for(Pipe[] row : pipes) {
            for(Pipe pipe : row) {
                if(pipe != null) {
                    pipe.drawPipe(canvas);
                }
            }
        }

        canvas.restore();
    }



}
