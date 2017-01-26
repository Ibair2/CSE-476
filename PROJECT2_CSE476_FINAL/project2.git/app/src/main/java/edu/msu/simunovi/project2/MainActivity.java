package edu.msu.simunovi.project2;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static boolean firstTime = true;
    public static String gameSessionID = "";
    public static String playerTurn = "";
    public static int gridSize = 0;
    public static String p1;
    public static String p2;
    private static volatile int signedIn = 1;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);



                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.sizeSpinner, android.R.layout.simple_spinner_item);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                getSpinner().setAdapter(adapter);

                getSpinner().setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
                {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view,int pos, long id) {
                       gridSize = pos;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Project 1 Checkpoint Requirement:
    * Navigate to the game playing screen
    */
    public void onStartGamePress_MainActivity(View view) {
        //Intent intent = new Intent(this, PlayingActivity.class);
        //intent.putExtra("gridsize", gridSize);




        EditText p1text = (EditText)findViewById(R.id.editPlayer1);
        p1 =  p1text.getText().toString();

        EditText p2text = (EditText)findViewById(R.id.editPlayer2);
        p2 =  p2text.getText().toString();

        if (p1.matches("")) {
            Toast.makeText(this, "Please input a username", Toast.LENGTH_SHORT).show();
            return;
        }else if (p2.matches("")) {
            Toast.makeText(this, "Please input a password", Toast.LENGTH_SHORT).show();
            return;
        } else {

            saveInUser(p1, p2);


        }
        }

    public void onCreateAccountPress_MainActivity(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private Spinner getSpinner() {
        return (Spinner) findViewById(R.id.sizeSelect);
    }

    public void onHowToPressed(View view){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext());

        builder.setTitle(R.string.instructionsTitle);
        builder.setMessage(R.string.instructionsExplained);
        builder.setPositiveButton(android.R.string.ok, null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void saveInUser(final String user, final String password) {
        //

         /*
         * Create a thread to load the hatting from the cloud
         */
        //final boolean temp;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                int temp = 1;
                Cloud cloud = new Cloud();
                InputStream stream = cloud.signIn(user, password);

                // Test for an error
                boolean fail = stream == null;

                try {

                    XmlPullParser xml = Xml.newPullParser();
                    xml.setInput(stream, "UTF-8");

                    xml.nextTag();      // Advance to first tag
                    xml.require(XmlPullParser.START_TAG, null, "proj2");
                    String status = xml.getAttributeValue(null, "status");
                    String message = xml.getAttributeValue(null, "msg");

                    if (status.equals("yes")) {

                        Log.i("SIGNED IN", "yes");
                        //temp = true;
                        temp = 0;
                        return;


                    } else {
                        Log.i("SIGNED IN", "no");
                        //return false;
                        //fail = true;
                        if(message.equals("no user"))
                        {
                            temp = 1;
                        }
                        else
                        {
                            temp = 2;
                        }

                        return;

                    }

                } catch (IOException ex) {
                    fail = true;
                    setSignedIn(temp);
                } catch (XmlPullParserException ex) {
                    fail = true;
                    setSignedIn(temp);
                } finally {
                    try {
                        stream.close();
                        setSignedIn(temp);

                        Intent intent = new Intent(getApplicationContext(), WaitingActivity.class);

                        if(getSignedIn()== 0)
                        {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                                }
                            });


                            startActivity(intent);
                            setSignedIn(1);
                        }
                        else
                        {
                            if(getSignedIn() ==  1) {

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Sign in failed, no user. Create an account.", Toast.LENGTH_LONG).show();
                                    }
                                });

                                //Toast.makeText(getApplicationContext(), "Sign in failed, no user. Create an account. Please try one more time", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Sign in failed, wrong password.", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    } catch (IOException ex) {
                        setSignedIn(temp);
                    }
                }

            }
        }).start();
        return;
    }

    void setSignedIn(int b)
    {
        signedIn = b;

    }

    int getSignedIn()
    {
        return signedIn;
    }


}



