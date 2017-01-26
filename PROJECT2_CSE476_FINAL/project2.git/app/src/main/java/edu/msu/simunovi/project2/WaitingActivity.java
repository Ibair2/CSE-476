package edu.msu.simunovi.project2;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class WaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML



                                    try {
                                        if(MainActivity.firstTime) {
                                            boolean tempW = true;
                                            do {
                                                boolean temp = false;
                                                Cloud cloud = new Cloud();
                                                InputStream stream = cloud.setPlayer(MainActivity.p1, MainActivity.gridSize);

                                                // Test for an error
                                                boolean fail = stream == null;


                                                XmlPullParser xml = Xml.newPullParser();
                                                xml.setInput(stream, "UTF-8");

                                                xml.nextTag();      // Advance to first tag
                                                xml.require(XmlPullParser.START_TAG, null, "proj2");
                                                String status = xml.getAttributeValue(null, "status");

                                                if (status.equals("yes")) {

                                                    Log.i("SET_PLAYER: ", "yes");
                                                    //temp = true;
                                                    temp = true;
                                                    break;


                                                } else {
                                                    Log.i("SET_PLAYER: ", "no");
                                                    //return false;
                                                    //fail = true;
                                                    temp = false;


                                                }
                                                tempW = !(status.equals("yes"));
                                                SystemClock.sleep(1000);
                                            }while(tempW);
                                            MainActivity.firstTime = false;
                                            //return;

                                        }

                } catch (IOException ex) {

                } catch (XmlPullParserException ex) {

                } finally {







                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            String temp = "";
                            try {

                                boolean tempW = true;

                                do {

                                    // Create a cloud object and get the XML

                                    Cloud cloud = new Cloud();
                                    InputStream stream = cloud.getPlayerTurn();

                                    // Test for an error
                                    boolean fail = stream == null;

                                    XmlPullParser xml = Xml.newPullParser();
                                    Log.i("XML", String.valueOf(xml));
                                    xml.setInput(stream, "UTF-8");

                                    xml.nextTag();      // Advance to first tag
                                    xml.require(XmlPullParser.START_TAG, null, "proj2");
                                    String status = xml.getAttributeValue(null, "status");
                                    String user = xml.getAttributeValue(null, "user");
                                    MainActivity.gameSessionID = xml.getAttributeValue(null, "session_id");

                                    if (status.equals("test")) {
                                        Log.i("GETPLAYERTURN_STATUS: ", "IT WAS EQUAL TO TEST");
                                    }

                                    if (status.equals("yes")) {

                                        Log.i("GETPLAYERTURN_STATUS: ", status);
                                        Log.i("GETPLAYERTURN_USER: ", user);
                                        Log.i("GETPLAYERTURN_ID: ", MainActivity.gameSessionID);
                                        MainActivity.playerTurn = user;
                                        temp = user;





                                    } else {
                                        Log.i("GETPLAYERTURN_STATUS: ", status);
                                        Log.i("GETPLAYERTURN_USER: ", user);
                                        Log.i("GETPLAYERTURN_ID: ", MainActivity.gameSessionID);
                                        temp = "";
                                        MainActivity.playerTurn = "";




                                    }
                                    tempW = !(temp.equals(MainActivity.p1));
                                    SystemClock.sleep(1000);

                                    if(temp.equals("NULL"))
                                    {
                                        // Get a handler that can be used to post to the main thread
                                        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

                                        Runnable myRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                TextView displayText = (TextView) findViewById(R.id.textDisplay);

                                                displayText.setText("Waiting on Player 2 to join the game");
                                            } // This is your code
                                        };
                                        mainHandler.post(myRunnable);

                                    }
                                    else if (!(temp.equals(MainActivity.p1)))
                                    {
                                        final String temp3 = temp;
                                        // Get a handler that can be used to post to the main thread
                                        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

                                        Runnable myRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                TextView displayText = (TextView) findViewById(R.id.textDisplay);

                                                displayText.setText("Waiting on " + temp3 + " to finish their turn");//
                                            } // This is your code
                                        };
                                        mainHandler.post(myRunnable);


                                    }

                                }while(tempW);

                            } catch (IOException ex) {
                                Log.i("exception: ", "IO Except");
                            } catch (XmlPullParserException ex) {
                                Log.i("exception: ", "XML PULL EXCEP");
                            } catch (Exception e){
                                Log.i("exception ee: ", String.valueOf(e));
                            }
                            finally {


                                // NEED TO PUT THIS INSIDE OF A 2nd THREAD THAT WILL RUN AFTER THE 1ST
                                // THREAD IS DONE, THIS WILL CALL THE GETPLAYERTURN() AND CHECK WHAT THE
                                // RETURN VALUE IS AND CHANGE THE TEXT DISPLAYED IN THE ACTIVITY BASED ON THAT

                                if(temp.equals(MainActivity.p1))
                                {
                                    Intent intent = new Intent(getApplicationContext(), PlayingActivity.class);
                                    startActivity(intent);
                                // getGameStat() will then be called inside of PlayingActivity
                                   finish();
                                }


                            }

                        }
                    }).start();













                    // NEED TO PUT THIS INSIDE OF A 2nd THREAD THAT WILL RUN AFTER THE 1ST
                    // THREAD IS DONE, THIS WILL CALL THE GETPLAYERTURN() AND CHECK WHAT THE
                    // RETURN VALUE IS AND CHANGE THE TEXT DISPLAYED IN THE ACTIVITY BASED ON THAT

                    /*if(cloud.getPlayerTurn().equals(MainActivity.p1))
                    {
                    Intent intent = new Intent(this, PlayingActivity.class);
                    // getGameStat() will then be called inside of PlayingActivity
                    // finish();
                    }else if(cloud.getPlayerTurn().equals("NULL"))
                    {
                    TextView displayText = (TextView) findViewById(R.id.textDisplay);

                    displayText.setText("Waiting on Player 2 to join the game");
                    }
                    else
                    {
                    TextView displayText = (TextView) findViewById(R.id.textDisplay);

                    displayText.setText("Waiting on " + cloud.getPlayerTurn() + "to finish their turn");
                    }*/
                }

            }
        }).start();


    }

}
