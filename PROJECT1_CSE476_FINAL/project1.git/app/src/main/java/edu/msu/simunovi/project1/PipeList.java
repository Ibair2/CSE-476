package edu.msu.simunovi.project1;

/**
 * Created by jaiwant on 10/23/2016.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;


public class PipeList implements Serializable {

    /**
     * Number of pipes in the bank
     */
    public final int ListSize = 5;

    /**
     * Array of pipes in the bank
     */
    private Pipe[] pipes = new Pipe[ListSize];

    /**
     * The pipe that is being dragged
     */
    private Pipe dragging = null;

    /**
     * Random number generator used to select new pipes for the bank
     */
    private static Random random = new Random(System.nanoTime());

    /**
     * The context that has the pipe drawables
     */
    private transient Context context;

    /**
     * Paint object used to draw the pipe bank rectangle
     */
    private transient Paint PipePaintObj;

    /**
     * Pipe various measurements
     */
    private boolean PipeHor;
    private float PipeSpace;
    private float PipeSize;



    /**
     * Constructor for the PipeBank
     * @param context Context passed to Pipes when generating new pipes
     */
    public PipeList(Context context) {
        this.context = context;
        /**
        pipes[0] = getRandomPipe();
        pipes[1] = getRandomPipe();
        pipes[2] = getRandomPipe();
        pipes[3] = getRandomPipe();
        pipes[4] = getRandomPipe();
        **/
        PipePaintObj = new Paint(Paint.ANTI_ALIAS_FLAG);
        PipePaintObj.setColor(Color.argb(90, 0, 100, 0));  // A semi-transparent green
    }

    /**
     * Generate a random pipe
     * @return A random pipe
     */
    private Pipe getRandomPipe() {
        // Calculate total relative probs in order to scale the random integer generation
        Random r = new Random();
        int temp_num = r.nextInt(4);
        switch (temp_num){
            case 0:
                return new Pipe(context, "straight");
            case 1:
                return new Pipe(context, "curved");
            case 2:
                return new Pipe(context, "cap");
            case 3:
                return new Pipe(context, "tee");
        }
        return null;
    }

    public Pipe getCurrentPipe() {
        return dragging;
    }

    public void setCurrentPipe(Pipe current) {
        if(current == null) {
            for(int i = 0; i < pipes.length; i++) {
                if(pipes[i] == dragging) {
                    pipes[i] = null;
                }
            }
        }
        dragging = current;
    }



    public Pipe HitTest(float xpos, float ypos) {

        dragging = null;

        float pos = xpos;
        if(PipeHor==false) {
            pos = ypos;
        }
        //Calculate the part of the list we hit.
        float temp1 = PipeSpace + PipeSize;
        temp1 = pos/temp1;
        int PartOfTheList = (int)temp1;

        //Calculate the part of the list we hit.
        float temp = PipeSpace + PipeSize;
        temp = pos % temp;
        if((PartOfTheList < ListSize) && (temp > PipeSpace))
        {
            return pipes[PartOfTheList];
        }

        return null;
    }

    public void draw(Canvas canvas, float width, float height, float blockSize) {
        /*
         * Draw the bottom part which displays the lists.
         */

        for(int i=0; i<ListSize; i++)
        {
            if(pipes[i]==null)
            {
                pipes[i] = getRandomPipe();
            }
        }
        canvas.drawRect(0f, 0f, width, height, PipePaintObj);

        float PipeSpaceHor, PipeSpaceVert, scale;

        if(width >= height) {
            PipeHor = true;
            PipeSize = width / (ListSize + 2);
            PipeSpace = PipeSpaceHor = 2 * PipeSize / (ListSize + 1);
            PipeSpaceHor += (PipeSize / 2);
            scale = PipeSize < height ? PipeSize / blockSize : height / blockSize;
            PipeSpaceVert = height / 2;

            for(int i = 0; i < ListSize; i++) {
                canvas.save();
                canvas.translate((PipeSize/2)*i + PipeSpaceHor*(i+1) , PipeSpaceVert);
                canvas.scale(scale, scale);

                if(pipes[i] != dragging) {
                    pipes[i].resetPipe();
                    pipes[i].draw(canvas);
                }

                canvas.restore();
            }
        } else {

            PipeHor = false;
            PipeSize = height / (ListSize + 2);
            PipeSpace = PipeSpaceVert = 2 * PipeSize / (ListSize + 1);
            PipeSpaceVert += (PipeSize / 2);
            scale = PipeSize < width ? PipeSize / blockSize : height / blockSize;
            PipeSpaceHor = width / 2;


            for(int i = 0; i < ListSize; i++) {
                canvas.save();
                canvas.translate(PipeSpaceHor, (PipeSize/2)*i + PipeSpaceVert*(i+1));
                canvas.scale(scale, scale);

                if(pipes[i] != dragging) {
                    pipes[i].setLocation(0, 0);
                    pipes[i].draw(canvas);
                }

                canvas.restore();
            }
        }
    }
}
