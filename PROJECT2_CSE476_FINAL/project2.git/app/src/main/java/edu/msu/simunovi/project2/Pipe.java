package edu.msu.simunovi.project2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * An example of how a pipe might be represented.
 */
public class Pipe {

    public float getBitmapRotation() {
        return bitmapRotation;
    }

    public void setBitmapRotation(float bitmapRotation) {
        this.bitmapRotation = bitmapRotation;
    }

    public String pipeType;

    public int getPlayerPipe() {
        return PlayerPipe;
    }

    public void setPlayerPipe(int playerPipe) {
        this.PlayerPipe = playerPipe;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    //bitmap for this individual pipe
    private transient Bitmap bitmap;

    //set the rotation ahead of time so we dont have to do it for each case on draw.
    private float rotation = 0f;

    /**
     * Rotation of the bitmap and handle
     */
    private float bitmapRotation = 0f;
    private float handleRotation = 0f;

    //Player this pipe belongs
    public int PlayerPipe;

    private PlayingArea playingArea = null;

    /**
     * Array that indicates which sides of this pipe
     * has flanges. The order is north, east, south, west.
     *
     * As an example, a T that has a horizontal pipe
     * with the T open to the bottom would be:
     *
     * false, true, true, true
     */
    private boolean[] connect = {false, false, false, false};

    /**
     * X location in the playing area (index into array)
     */
    private float x = 0f;

    /**
     * Y location in the playing area (index into array)
     */
    private float y = 0f;

    private int yInt = 0;
    private int xInt = 0;

    /**
     * Depth-first visited visited
     */
    private boolean visited = false;

    private transient Bitmap handleBitmap = null;

    /**
     * Steam bitmap
     */
    private transient Bitmap steamBitmap = null;

    private boolean checkForSteam = false;

    public boolean isCheckForSteam(){ return this.checkForSteam; }


    /**
     * Constructor
     * @param north True if connected north
     * @param east True if connected east
     * @param south True if connected south
     * @param west True if connected west
     */
    public Pipe(boolean north, boolean east, boolean south, boolean west) {
        connect[0] = north;
        connect[1] = east;
        connect[2] = south;
        connect[3] = west;
    }

    public Pipe(Context context, String pipeType) {
        steamBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);

        this.pipeType = pipeType;
        if (pipeType.equals("start")) {
            setConnections(false, true, false, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
            bitmapRotation = 90f;
            handleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.handle);
        }else if(pipeType.equals("end")) {
            setConnections(false, false, false, true);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
            bitmapRotation = -90f;
        }
        else if(pipeType.equals("straight")) {

            setConnections(true, false, true, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        }
        else if(pipeType.equals("curved")) {

            setConnections(false, true, true, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.curved);
        }else if (pipeType.equals("cap")) {
            setConnections(false, false, true, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        }
        else if(pipeType.equals("tee")){
            setConnections(true, true, true, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);
        }
        else{
            setConnections(true, true, true, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);
            Log.i("Pipe type: ", pipeType);
        }

    }

    private void setConnections(boolean northCon, boolean eastCon, boolean southCon, boolean westCon){
        connect[0] = northCon;
        connect[1] = eastCon;
        connect[2] = southCon;
        connect[3] = westCon;
    }

    public boolean search() {
        visited = true;

        for(int d=0; d<4; d++) {
            /*
             * If no connection this direction, ignore
             */
            if(!connect[d]) {
                continue;
            }

            Pipe n = nextPipe(d);
            if(n == null) {
                // We leak
                // We have a connection with nothing on the other side
                return false;
            }

            // What is the matching location on
            // the other pipe. For example, if
            // we are looking in direction 1 (east),
            // the other pipe must have a connection
            // in direction 3 (west)
            int dp = (d + 2) % 4;
            if(!n.connect[dp]) {
                // We have a bad connection, the other side is not
                // a flange to connect to
                return false;
            }

            if(n.visited) {
                // Already visited this one, so no leaks this way
                continue;
            } else {
                // Is there a lead in that direction
                if(!n.search()) {
                    // We found a leak downstream of this pipe
                    return false;
                }
            }
        }

        // Yah, no leaks
        return true;
    }


    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //Did we touch the pipe?
    public boolean isTouched(float xPos, float yPos){
        if((xPos >= this.x - bitmap.getWidth() && xPos <= this.x + bitmap.getWidth()) && (yPos >= this.y - bitmap.getHeight() && yPos <= this.y + bitmap.getHeight())) {
            return true;
        }

        return false;
    }

    public void resetPipe() {
        setLocation(0f, 0f);
        resetConnections();
    }

    public void resetConnections() {
        bitmapRotation = 0f;

        switch(pipeType) {
            case "start":
                setConnections(false, true, false, false);
                bitmapRotation = 90f;
                break;
            case "straight":
                setConnections(true, false, true, false);
                break;
            case "curved":
                setConnections(false, true, true, false);
                break;
            case "cap":
                setConnections(false, false, true, false);
                break;
            case "tee":
                setConnections(true, true, true, false);
                break;
            case "end":
                setConnections(false, false, false, true);
                bitmapRotation = -90f;
                break;
        }
    }

    //This is to find the next pipe.
    private Pipe nextPipe(int d) {
        switch(d) {
            case 0:
                return playingArea.getPipe(xInt, yInt-1);

            case 1:
                return playingArea.getPipe(xInt+1, yInt);

            case 2:
                return playingArea.getPipe(xInt, yInt+1);

            case 3:
                return playingArea.getPipe(xInt-1, yInt);
        }

        return null;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    //Get this playing area.
    public PlayingArea getPlayingArea() {
        return playingArea;
    }

   // Set the playng area to x and y.
    public void set(PlayingArea playingArea, int x, int y) {
        this.playingArea = playingArea;
        this.xInt = x;
        this.yInt = y;
    }

    //Bitmap height and width
    public float getBitmapHeight() {
        return bitmap.getHeight();
    }
    public float getBitmapWidth() {
        return bitmap.getWidth();
    }

    //Return the visited
    public boolean beenVisited() {
        return visited;
    }

    //Set the visited
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void drawPipe(Canvas canvas) {
        if(playingArea != null) {
            // Calculate the x and y locations withing the playing area
            float bitDim = bitmap.getHeight() < bitmap.getWidth() ? bitmap.getHeight() : bitmap.getWidth();
            this.x = (xInt * bitDim) + (bitDim / 2);
            this.y = (yInt * bitDim) + (bitDim / 2);
            draw(canvas);
            if(pipeType != "end") {
                // Draw steam at each opening
                for(int d=0; d<4; d++) {
                /*
                 * If no connection this direction, ignore
                 */
                    if(!connect[d]) {
                        continue;
                    }

                    Pipe n = nextPipe(d);
                    if(n == null) {
                        // We have a connection with nothing on the other side
                        DrawSteamBitmap(canvas,bitDim, d);
                        checkForSteam = true;
                        continue;
                    }
                    else
                        checkForSteam = false;

                    // What is the matching location on
                    // the other pipe. For example, if
                    // we are looking in direction 1 (east),
                    // the other pipe must have a connection
                    // in direction 3 (west)
                    int whichDir = (d + 2) % 4;
                    if(!n.connect[whichDir]) {
                        DrawSteamBitmap(canvas, bitDim, d);
                        checkForSteam = true;
                    }
                    else
                        checkForSteam = false;
                }
            }
        }
    }
    private void DrawSteamBitmap(Canvas canvas, float SizeOfGrid, int thisDirection) {
        int x = this.xInt;
        int y = this.yInt;

        canvas.save();

        float rotation = 0f;

        if(thisDirection==0){
            y=y-1;
        }
        else if(thisDirection==1){
            rotation = 90f;
            x=x+1;
        }
        else if(thisDirection==2){
            rotation = 180f;
            y=y+1;
        }
        else if(thisDirection==3){
            rotation = -90f;
            x=x-1;
        }
        canvas.translate((x*SizeOfGrid + (SizeOfGrid/2) ),(y*SizeOfGrid + (SizeOfGrid/2) ));
        canvas.rotate(rotation);
        canvas.translate(-(SizeOfGrid / 2), -(SizeOfGrid / 2));
        canvas.drawBitmap(steamBitmap, 0, 0, null);
        canvas.restore();
    }

    public void draw(Canvas canvas) {
        float bitDim = bitmap.getHeight() < bitmap.getWidth() ? bitmap.getHeight() : bitmap.getWidth();

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(bitmapRotation);
        canvas.translate(-(bitDim / 2), -(bitDim / 2));

        // Draw pipe bitmap
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();

        // Draw the handle for end pipe
        if (handleBitmap != null) {
            canvas.save();
            canvas.translate(x, y);
            canvas.rotate(rotation);
            canvas.translate(-bitDim / 2, -bitDim / 2);
            canvas.drawBitmap(handleBitmap, 0, 0, null);
            canvas.restore();
        }
    }

    public void snapThisPipe(){
        float closestRotation = (bitmapRotation / 90f);
        closestRotation %= 4;
        int numberOfRotation = Math.round(closestRotation);

        bitmapRotation = 90f * numberOfRotation;

        boolean[] thisConnection = new boolean[4];
        for(int i = 0; i < connect.length; i++) {
            int ind = (i+numberOfRotation) % 4;
            if(ind < 0) ind += 4;
            thisConnection[ind] = connect[i];
        }
        connect = thisConnection;

    }

    public boolean PipeConnectionBool() {
        boolean whichConnection = false;
        for(int d=0; d<4; d++) {
            /*
             * If no connection this direction, ignore
             */
            if(!connect[d]) {
                continue;
            }

            Pipe n = nextPipe(d);
            if(n == null) {
                continue;
            }
            // What is the matching location on
            // the other pipe. For example, if
            // we are looking in direction 1 (east),
            // the other pipe must have a connection
            // in direction 3 (west)
            int thisDirection = (d + 2) % 4;
            if(n.connect[thisDirection]) {
                whichConnection = true;
            }
        }
        if(!whichConnection) {
            return false;
        }

        return true;
    }

}
