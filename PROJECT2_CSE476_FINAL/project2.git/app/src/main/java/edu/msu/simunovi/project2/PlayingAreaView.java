package edu.msu.simunovi.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by boson_000 on 10/10/2016.
 */
public class PlayingAreaView extends View {
    /**
     * Normalized y location of the top of the pipe bank
     */
    private final static float bankLocation = 0.8f;

    private String player1Name = "default";
    private String player2Name = "default";

    public static String[][] myGamePipes = new String[400][5];

    private Bitmap startPiece;

    private Bitmap valve;

    public static String currentTurn;

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        makePuzzleSize(gridSize);
        //this.gridSize = gridSize;
    }

    private int gridSize;// = 1;

    //Touches, same as hatter
    private Touch touch1 = new Touch();

    private Touch touch2 = new Touch();

    /**
     * Standard block size in the playing field, used to determine the scale to draw components
     */
    public float blockSize = 0f;

    public PlayingArea getPlayingArea() {
        return playingArea;
    }

    /**
     * Member object that represents the pipe bank
     */
    private PipeList bank = null;

    public void setPlayingArea(PlayingArea playingArea) {
        this.playingArea = playingArea;
    }

    private PlayingArea playingArea;

    private Parameters params = null;

    public PlayingAreaView(Context context) {
        super(context);
        init(null, 0);
    }

    public PlayingAreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlayingAreaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        //playingArea = new PlayingArea(gridSize, gridSize);

        //playingArea.setPlayingAreaView(this);
        bank = new PipeList(getContext());
        params = new Parameters();
        //makePuzzleSize(gridSize);
    }

    private void makePuzzleSize(int puzzleSize){
        if (puzzleSize == 0){
            playingArea = new PlayingArea(5, 5);
            placeStartingPipes(5);
        }else if (puzzleSize == 1){
            playingArea = new PlayingArea(10,10);
            placeStartingPipes(10);
        }else{
            
            playingArea = new PlayingArea(20, 20);
            placeStartingPipes(20);
        }

        params.playingAreaWidth = params.gridSize * playingArea.getWidth();
        params.playingAreaHeight = params.gridSize * playingArea.getHeight();

    }

    //place the initial pipes
    private void placeStartingPipes(int gridSize){
        params.p1StartPipe = new Pipe(getContext(), "start");
        params.p1StartPipe.setPlayerPipe(1);
        params.p2StartPipe = new Pipe(getContext(), "start");
        params.p2StartPipe.setPlayerPipe(1);
        params.p1EndPipe = new Pipe(getContext(), "end");
        params.p1EndPipe.setPlayerPipe(1);
        params.p2EndPipe = new Pipe(getContext(), "end");
        params.p2EndPipe.setPlayerPipe(1);
        if (gridSize == 5){
            playingArea.add(params.p1StartPipe, 0, 0);
            playingArea.add(params.p2StartPipe, 0, 2);
            playingArea.add(params.p1EndPipe, 4, 1);
            playingArea.add(params.p2EndPipe, 4, 3);
        }else  if(gridSize == 10){
            playingArea.add(params.p1StartPipe, 0, 2);
            playingArea.add(params.p2StartPipe, 0, 5);
            playingArea.add(params.p1EndPipe, 9, 3);
            playingArea.add(params.p2EndPipe, 9, 6);
        }else if(gridSize == 20){
            playingArea.add(params.p1StartPipe, 0, 5);
            playingArea.add(params.p2StartPipe, 0, 12);
            playingArea.add(params.p1EndPipe, 19, 7);
            playingArea.add(params.p2EndPipe, 19, 14);
        }

        //we'll use the height of the bitmap to compare when we scale
        params.gridSize = params.p1StartPipe.getBitmap().getHeight();




    }
    /**
     * Handle a touch event
     * @param event The touch event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //Set the id of the touches
                touch1.id = id;
                touch2.id = -1;

                getPositions(event);
                touch1.copyToLast();

                //JAIWANT: Somewhere youll have to check if youve grabbed a peice in the pipe list

                // Convert the raw touch data to location in the pipe bank
                // to check if it's a valid location
                float bankx = (event.getX() - params.bankXOffset);
                float banky = (event.getY() - params.bankYOffset);

                // Handle the case where the first touch is over the Pipe Bank
                if(bankx >= 0 && banky >= 0) {
                    params.currentPipe = bank.HitTest(bankx, banky);
                    bank.setCurrentPipe(params.currentPipe);

                    if(params.currentPipe != null) {
                        // This location is relative to the playing area
                        params.currentPipe.setLocation(touch1.x,touch1.y);
                    }

                }


                params.draggingPipe = false;
                if (params.currentPipe != null && params.currentPipe.isTouched(touch1.x, touch1.y)) {
                    params.draggingPipe = true;
                }


                // If we're not dragging the pipe, then touch1 should store
                // raw locations (not relative to the playing area)
                if (!params.draggingPipe) {
                    getPositions(event, false);
                    touch1.copyToLast();
                }
                touch1.copyToLast();

                invalidate();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    if(params.draggingPipe) {
                        getPositions(event);
                    } else {
                        getPositions(event, false);
                    }
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                invalidate();
                return true;

            //More hatter code
            case MotionEvent.ACTION_POINTER_UP:
                if(id == touch2.id) {
                    touch2.id = -1;
                } else if(id == touch1.id) {
                    // Make what was touch2 now be touch1 by
                    // swapping the objects.
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                //Start with moving the playing area, will deal with grabbing pipes later
                //getPositions(event);
                /*if(!params.draggingPipe && params.currentPipe == null) {
                    playingAreaScroll();
                    getPositions(event);
                }else{
                    getPositions(event);
                    moveCurrentPipe();
                }*/
                if(params.draggingPipe && params.currentPipe!=null){
                    getPositions(event);
                    moveCurrentPipe();
                }else{
                    getPositions(event);
                    playingAreaScroll();
                }

                return true;

        }
        return super.onTouchEvent(event);
    }

    public void rotatePipe(float x1, float y1, float angle) {
        params.currentPipe.setBitmapRotation(params.currentPipe.getBitmapRotation() + angle);
    }

    /**
     * Move the currentPipe based on the computed deltas in touch1
     */
    private void moveCurrentPipe() {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if(touch1.id < 0) {
            return;
        }

        if(touch1.id >= 0) {
            // At least one touch
            // We are moving
            touch1.computeDeltas();

            params.currentPipe.setLocation(params.currentPipe.getX() + touch1.dX, params.currentPipe.getY() + touch1.dY);
        }
        if(touch2.id >= 0) {
            float angle1 = (float) Math.toDegrees(Math.atan2(touch2.lastY- touch1.lastY, touch2.lastX-touch1.lastX));
            float angle2 = (float) Math.toDegrees(Math.atan2(touch2.y- touch1.y, touch2.x-touch1.x));
            //float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            //float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float diff = angle2 - angle1;
            rotatePipe(touch1.x, touch1.y, diff);
        }
    }

    /**
     * Store touch locations in touch objects, translate raw points to playing area points if desired
     * @param event The touch event to handle
     * @param makeRelativeToPlayArea Should the raw locations be converted to playing area locations?
     */
    private void getPositions(MotionEvent event, boolean makeRelativeToPlayArea) {
        for(int i=0;  i<event.getPointerCount();  i++) {
            // Get the pointer id
            int id = event.getPointerId(i);

            // Convert to image coordinates only if we don't want the raw positions
            float x = event.getX(i);
            float y = event.getY(i);
            if(makeRelativeToPlayArea) {
                x = (x - params.marginLeft) / params.playingAreaScaleFactor;
                y = (y - params.marginTop) / params.playingAreaScaleFactor;
            }

            if(id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch2.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }

        invalidate();
    }

    private void playingAreaScroll(){
        if(touch1.id >= 0) {
            // At least one touch
            // We are moving
            touch1.computeDeltas();

            params.marginLeft += touch1.dX;
            params.marginTop += touch1.dY;
        }

        if(touch2.id >= 0) {

            float distance1 = (float)Math.hypot(touch2.lastX - touch1.lastX, touch2.lastY - touch1.lastY);
            float distance2 = (float)Math.hypot(touch2.x - touch1.x, touch2.y - touch1.y);
            float scaleFactor = distance2 / distance1;
            scalePlayingArea(scaleFactor);
        }
    }

    private void scalePlayingArea(float scaleFactor){
        float startWidth = params.playingAreaWidth * params.playingAreaScaleFactor;
        float endWidth = params.playingAreaWidth * params.playingAreaScaleFactor;

        //now update the scaling factor
        params.playingAreaScaleFactor *= scaleFactor;

        float width2 = params.playingAreaWidth * params.playingAreaScaleFactor;
        float height2 = params.playingAreaWidth * params.playingAreaScaleFactor;

        params.marginLeft -= (width2 - startWidth) / 2;
        params.marginTop -= (height2 - endWidth) / 2;
    }

    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(MotionEvent event) {
        for(int i=0; i <event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            /// Convert to image coordinates (from mad hatter)
            float x = (event.getX(i) - params.marginLeft) / params.playingAreaScaleFactor;
            float y = (event.getY(i) - params.marginTop) / params.playingAreaScaleFactor;


            if(id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch1.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {





        //temp.snapThisPipe();
        //temp.set(playingArea,4,4);
        //temp.snapThisPipe();
        //temp.draw(canvas);

        for(int i=0; i<400; i++){
            if( myGamePipes != null) {
                if (myGamePipes[i][4].equals("TRUE")) {
                    Pipe temp = new Pipe(getContext(), myGamePipes[i][0]);
                    temp.setBitmapRotation(Float.valueOf(myGamePipes[i][3]));
                    temp.snapThisPipe();
                    playingArea.add(temp, Integer.valueOf(myGamePipes[i][1]), Integer.valueOf(myGamePipes[i][2]));
                } else if (myGamePipes[i][4].equals("FALSE")) {
                    break;
                } else {
                    break;
                }
            }
        }


        // Portrait layout
        int fieldWidth = canvas.getWidth();
        int fieldHeight = (int)(canvas.getHeight() * bankLocation);
        int bankWidth = canvas.getWidth();
        int bankHeight = (int)(canvas.getHeight() * (1 - bankLocation));
        params.bankXOffset = 0f;
        params.bankYOffset = canvas.getHeight() * bankLocation;

        canvas.save();
        canvas.translate(params.bankXOffset, params.bankYOffset);
        bank.draw(canvas, bankWidth, bankHeight, params.gridSize);



        canvas.restore();

        /*
         * Draw the active pipe if there is one
         */
        if(params.currentPipe != null){
            canvas.save();
            canvas.translate(params.marginLeft, params.marginTop);
            canvas.scale(params.playingAreaScaleFactor, params.playingAreaScaleFactor);
            params.currentPipe.draw(canvas);
            canvas.restore();
        }

        startPiece = BitmapFactory.decodeResource(getResources(), R.drawable.straight);
        valve = BitmapFactory.decodeResource(getResources(), R.drawable.handle);

        if(params.playingAreaScaleFactor == 0f){
            params.playingAreaScaleFactor = canvas.getWidth() / params.playingAreaWidth;
        }

        if(params.marginTop == -1 || params.marginLeft == -1){
            params.marginLeft = (int)((canvas.getWidth() - params.playingAreaWidth * params.playingAreaScaleFactor) / 2);
            params.marginTop = (int)((canvas.getHeight() - params.playingAreaHeight * params.playingAreaScaleFactor) / 2);
        }

        if(canvas.getWidth() > canvas.getHeight()) {
            if(params.playingAreaHeight * params.playingAreaScaleFactor < canvas.getHeight()) {
                params.playingAreaScaleFactor = canvas.getHeight() / params.playingAreaHeight;
            }

        } else {
            if(params.playingAreaWidth * params.playingAreaScaleFactor < canvas.getWidth()) {
                params.playingAreaScaleFactor = canvas.getWidth() / params.playingAreaWidth;
            }
        }

        if(params.marginLeft > 0) {
            params.marginLeft = 0;
        }
        if(params.marginTop > 0) {
            params.marginTop = 0;
        }
        if(canvas.getWidth() - params.marginLeft > params.playingAreaWidth * params.playingAreaScaleFactor) {
            params.marginLeft = canvas.getWidth() - (int)(params.playingAreaWidth * params.playingAreaScaleFactor);
        }
        if(canvas.getHeight() - params.marginTop > params.playingAreaHeight * params.playingAreaScaleFactor) {
            params.marginTop = canvas.getHeight() - (int)(params.playingAreaHeight * params.playingAreaScaleFactor);
        }

        canvas.save();
        canvas.translate(params.marginLeft, params.marginTop);
        canvas.scale(params.playingAreaScaleFactor, params.playingAreaScaleFactor);
        playingArea.draw(canvas, params.gridSize);

        // Draw tha player's names
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);

        getPlayerNames();

        if (PlayingArea.drawText == 5) {
            canvas.drawText(player1Name, 0, 350, paint);
            canvas.drawText(player2Name, 0, 950, paint);
        } else if (PlayingArea.drawText == 10) {
            canvas.drawText(player1Name, 0, 950, paint);
            canvas.drawText(player2Name, 0, 1850, paint);
        } else if (PlayingArea.drawText == 20) {
            canvas.drawText(player1Name, 0, 1850, paint);
            canvas.drawText(player2Name, 0, 3950, paint);
        }

        canvas.restore();


    }




    public void saveNewPipe(final String TYPE, final String XLOC, final String YLOC, final String ROTATION)
    {
        new Thread(new Runnable() {

            @Override
            public void run() {


                try {

                    boolean tempW = true;

                    do {

                        // Create a cloud object and get the XML

                        Cloud cloud = new Cloud();
                        InputStream stream = cloud.setPipe(TYPE, XLOC, YLOC, ROTATION);

                        // Test for an error
                        boolean fail = stream == null;

                        XmlPullParser xml = Xml.newPullParser();
                        Log.i("XML", String.valueOf(xml));
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "proj2");
                        String status = xml.getAttributeValue(null, "status");


                        if (status.equals("test")) {
                            Log.i("SETPIPE_STATUS: ", "IT WAS EQUAL TO TEST");
                        }

                        if (status.equals("yes")) {

                            Log.i("SETPIPE_STATUS: ", status);

                        } else {
                            Log.i("SETPIPE_STATUS: ", status);

                        }
                        tempW = !(status.equals("yes"));
                        SystemClock.sleep(1000);

                    }while(tempW);

                } catch (IOException ex) {
                    Log.i("exception: ", "IO Except");
                } catch (XmlPullParserException ex) {
                    Log.i("exception: ", "XML PULL EXCEP");
                } catch (Exception e){
                    Log.i("exception ee: ", String.valueOf(e));
                }
                finally {
                    Log.i("SETPIPE_STATUS: ", "FINISHED THREAD");
                }

            }
        }).start();

        //echo '<proj2 status="yes"/>';
    }


    public void installThisPipe(){
        if(params.currentPipe!=null){

            String [] temp1 = new String[4];



            // Snap pipe to right coordinates and rotation
            int x = (int)(params.currentPipe.getX()/params.gridSize);
            int y = (int)(params.currentPipe.getY()/params.gridSize);

            params.currentPipe.snapThisPipe();

            params.currentPipe.set(playingArea, x,y);
            if(params.currentPipe.PipeConnectionBool()){
                if(playingArea.getPipe(x,y)==null){

                    temp1[0] = params.currentPipe.pipeType;
                    temp1[1] = String.valueOf(x);
                    temp1[2] = String.valueOf(y);
                    temp1[3] = String.valueOf((int)params.currentPipe.getBitmapRotation());

                    saveNewPipe(temp1[0],temp1[1],temp1[2],temp1[3]);

                    playingArea.add(params.currentPipe, x,y);
                    bank.setCurrentPipe(null);
                    params.currentPipe=null;
                }
            }


            invalidate();
        }
        Log.i("You hit the install","button");
        /**
        Cloud cloud = new Cloud();
        String playerPipe = String.valueOf(params.currentPipe.getPlayerPipe());
        cloud.setGameState(params.currentPipe.pipeType, (int)params.currentPipe.getX(),(int)params.currentPipe.getY(),params.currentPipe.pipeType,playerPipe);
        **/
        invalidate();
    }

    public void discardThisPipe(){
        bank.setCurrentPipe(null);
        params.currentPipe=null;
        Log.i("You hit the discard","button");
        invalidate();
    }

    public boolean checkForSteamBitmap(){

        if(params.currentPipe == null){
            return true;
        }
        else
        {
            boolean temp = params.currentPipe.isCheckForSteam();
            return temp;
        }

    }

    private static class Parameters implements Serializable{

        //The scale for each grid square based on the grid size (10, 20, 5 etc.)
        public float gridSize;

        public float playingAreaWidth = 0f;
        public float playingAreaHeight = 0f;

        //cant declare to null so set to 0 for now
        public float playingAreaScaleFactor = 0f;

        //Move the margins into params so we can serialze them
        public int marginLeft = -1;
        public int marginTop = -1;

        public Pipe p1StartPipe;
        public Pipe p1EndPipe;

        public Pipe p2StartPipe;
        public Pipe p2EndPipe;

        /**
                * X and Y offset of the pipe bank
        */
        public float bankXOffset = 0;
        public float bankYOffset = 0;

        /**
         * Reference to the currently selected pipe
         */
        public Pipe currentPipe = null;

        /**
         * Are we dragging a pipe?
         */
        public boolean draggingPipe = false;



    }
    private class Touch {
        //Changes in touch location
        public float dX = 0;

        public float dY = 0;
        /**
         * Touch id
         */
        public int id = -1;

        public float x = 0;

        public float y = 0;

        public float lastX = 0;

        public float lastY = 0;

        public void copyToLast() {
            lastX = x;
            lastY = y;
        }
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }

    void getPlayerNames()
    {
        new Thread(new Runnable() {

            @Override
            public void run() {


                try {

                    boolean tempW = true;

                    do {

                        // Create a cloud object and get the XML

                        Cloud cloud = new Cloud();
                        InputStream stream = cloud.getPlayers();

                        // Test for an error
                        boolean fail = stream == null;

                        XmlPullParser xml = Xml.newPullParser();
                        Log.i("XML", String.valueOf(xml));
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "proj2");
                        String status = xml.getAttributeValue(null, "status");
                        String pla1 = xml.getAttributeValue(null, "player1");
                        String pla2 = xml.getAttributeValue(null, "player2");


                        if (status.equals("test")) {
                            Log.i("CHANGETURN_STATUS: ", "IT WAS EQUAL TO TEST");
                        }

                        if (status.equals("yes")) {

                            Log.i("CHANGETURN_STATUS: ", status);

                        } else {
                            Log.i("CHANGETURN_STATUS: ", status);

                        }
                        tempW = !(status.equals("yes"));
                        SystemClock.sleep(1000);
                        player1Name = pla1;
                        player2Name = pla2;
                    }while(tempW);

                } catch (IOException ex) {
                    Log.i("exception: ", "IO Except");
                } catch (XmlPullParserException ex) {
                    Log.i("exception: ", "XML PULL EXCEP");
                } catch (Exception e){
                    Log.i("exception ee: ", String.valueOf(e));
                }


            }
        }).start();
    }

    public static void ChangeTurn(TextView whoseTurn)
    {
        // Change whose turn it is

/*
        if( currentTurn.equals(MainActivity.p1) ) {
            whoseTurn.setText("It is " + MainActivity.p2 + "'s turn now!");
            currentTurn = MainActivity.p2;

        }
        else if( currentTurn.equals(MainActivity.p2) ) {
            whoseTurn.setText("It is " + MainActivity.p1 + "'s turn now!");
            currentTurn = MainActivity.p1;
        }
        */
    }

}
