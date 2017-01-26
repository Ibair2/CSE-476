package edu.msu.simunovi.project2;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class PlayingActivity extends AppCompatActivity {



    Cloud mCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        Intent intent = getIntent();
        int gridSize = intent.getIntExtra("gridsize", 0);
        getPlayingArea().setGridSize(gridSize);

        mCloud = new Cloud();
        // Set the turn as Player 1's turn
        TextView whoseTurn = (TextView) findViewById(R.id.Player1);

        whoseTurn.setText("It is " + MainActivity.p1 + "'s turn now!");
        PlayingAreaView.currentTurn = MainActivity.p1;

        getCurrentPipes();

    }

        void getCurrentPipes() {
    /*
     * Create a thread to load the hatting from the cloud
     */
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // Create a cloud object and get the XML
                    Cloud cloud = new Cloud();
                    String[][] currentPipes = new String[400][5];
                    currentPipes = cloud.getAllPipes();


                    final String[][] temp = currentPipes;

                    getPlayingArea().myGamePipes = temp;
                    return;
                }


            }).start();



        }
        //getPlayingArea().setPlayingArea(new PlayingArea(gridSize, gridSize));




    /*
    * Project 1 Checkpoint Requirement:
    * Navigate to the opening screen
    */
    public void onMainPress_PlayingActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
     * Project 1 Checkpoint Requirement:
     * Navigate to the game finished screen
     */
    public void onTestEndPress_PlayingActivity(View view) {
        Intent intent = new Intent(this, FinishedActivity.class);
        startActivity(intent);
    }


    public void onSurrenderPressed(View view) {


        Intent intent = new Intent(this, FinishedActivity.class);
        startActivity(intent);
    }

    public void onDiscardPressed(View view) {
        getPlayingArea().discardThisPipe();
    }

    public void onOpenValvePressed(View view) {

        if(getPlayingArea().checkForSteamBitmap()==true){
            // Current player won
            Intent intent = new Intent(this, FinishedActivity.class);
            startActivity(intent);
        }
        else{
            // Current player lost


            Intent intent = new Intent(this, FinishedActivity.class);
            startActivity(intent);
        }
    }

    public void onInstallPressed(View view) {
        getPlayingArea().installThisPipe();

        changeTurn();


    }

    //private PlayingAreaView getPlayingAreaView() {
      //  return (PlayingAreaView)this.findViewById(R.id.playingAreaView);
    //}
    private PlayingAreaView getPlayingArea() {
        return (PlayingAreaView) this.findViewById(R.id.playingAreaView);
    }


void changeTurn()
{
    new Thread(new Runnable() {

        @Override
        public void run() {


            try {

                boolean tempW = true;

                do {

                    // Create a cloud object and get the XML

                    Cloud cloud = new Cloud();
                    InputStream stream = cloud.changePlayerTurn();

                    // Test for an error
                    boolean fail = stream == null;

                    XmlPullParser xml = Xml.newPullParser();
                    Log.i("XML", String.valueOf(xml));
                    xml.setInput(stream, "UTF-8");

                    xml.nextTag();      // Advance to first tag
                    xml.require(XmlPullParser.START_TAG, null, "proj2");
                    String status = xml.getAttributeValue(null, "status");


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

                }while(tempW);

            } catch (IOException ex) {
                Log.i("exception: ", "IO Except");
            } catch (XmlPullParserException ex) {
                Log.i("exception: ", "XML PULL EXCEP");
            } catch (Exception e){
                Log.i("exception ee: ", String.valueOf(e));
            }
            finally {

                Intent intent = new Intent(getApplicationContext(), WaitingActivity.class);
                startActivity(intent);
                finish();

            }

        }
    }).start();

    //echo '<proj2 status="yes" user="currentPlayer"/>';
}


}
