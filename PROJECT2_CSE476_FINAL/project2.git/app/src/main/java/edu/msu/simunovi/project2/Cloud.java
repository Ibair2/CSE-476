package edu.msu.simunovi.project2;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.Xml;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


@SuppressWarnings("ALL")
public class Cloud {

    private static final String LOGIN_URL = "https://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-login.php";
    private static final String CREATE_URL = "https://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-newuser.php";
    private static final String SET_PLAYER_URL = "https://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-setplayer.php";
    private static final String GET_PLAYER_TURN_URL = "https://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-getplayerturn.php";
    private static final String CHANGE_PLAYER_TURN_URL = "https://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-changeplayerturn.php";
    private static final String GET_PIPES_URL = "http://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-getpipes.php";
    private static final String SET_PIPE_URL = "http://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-setpipe.php";
    private static final String GET_PLAYERS_LIST_URL ="http://webdev.cse.msu.edu/~kinseyky/cse476/project2/project2-getplayernames.php";
    private static final String UTF8 = "UTF-8";


    public InputStream createAccount(final String username, final String password) {
        String query = CREATE_URL + "?user=" + username + "&pw=" + password;
        // Create a get query


        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            // InputStream stream = conn.getInputStream();
           return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }

    }


    public InputStream signIn(final String username, final String password) {
        String query = LOGIN_URL + "?user=" + username + "&pw=" + password;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            //InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }


    }

/*
    public InputStream getPipes(){
        String query = GET_PIPES_URL + "?session_id=" + MainActivity.gameSessionID;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            //stream = conn.getInputStream();
            //logStream(stream);
            //InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }*/

    public InputStream setPipe(String TYPE, String XLOC, String YLOC, String ROTATION){
        String query = SET_PIPE_URL + "?session_id=" + MainActivity.gameSessionID + "&type=" + TYPE + "&xloc=" + XLOC + "&yloc=" + YLOC + "&rotation=" + ROTATION;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            //stream = conn.getInputStream();
            //logStream(stream);
            //InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public InputStream getPlayerTurn(){
        //return "~!CloudPlayer!~";
        String query = GET_PLAYER_TURN_URL;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            //stream = conn.getInputStream();
            //logStream(stream);
            //InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public InputStream changePlayerTurn(){

        String query = CHANGE_PLAYER_TURN_URL + "?session_id=" + MainActivity.gameSessionID;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            //stream = conn.getInputStream();
            //logStream(stream);
            //InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    // Grid size is 5, 10 or 20
    public InputStream setPlayer(final String yourOwnUsername, final int gridSize){
        String query = SET_PLAYER_URL + "?user=" + yourOwnUsername + "&gridsize=" + gridSize;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
             //InputStream stream = conn.getInputStream();
           return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }


    }

    public void getVictoryStatus(){
        return;
    }

    public void setVictoryStatus(final String vicotryStatusOfNextPlayer){
        return;
    }

    public InputStream getPlayers() {
        String query = GET_PLAYERS_LIST_URL + "?session_id=" + MainActivity.gameSessionID;
        // Returns the first player and the 2nd player so that the name of the P1 and P2 pipes
        // can be drawn;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream =  new InputStreamIntercept(conn.getInputStream());//conn.getInputStream();
            //InputStream stream = conn.getInputStream();
            
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }

    }


    public static void logStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));

        Log.e("476", "logStream: If you leave this in, code after will not work!");
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e("476", line);
            }
        } catch (IOException ex) {
            return;
        }
    }

    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }

    /**
     * Get the catalog items from the server
     * @return Array of items or null if failed
     */
    public String[][] getAllPipes() {
        String[][] currentPipes = new String[400][5];
        int i = 0;


        // Create a GET query
        String query = GET_PIPES_URL + "?session_id=" + MainActivity.gameSessionID;

        /**
         * Open the connection
         */
        InputStream stream = null;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();


            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }



            stream = conn.getInputStream();
            //stream = conn.getInputStream();
            //logStream(stream);


            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml = Xml.newPullParser();
                xml.setInput(stream, "UTF-8");

                xml.nextTag();      // Advance to first tag
                xml.require(XmlPullParser.START_TAG, null, "proj2");

                String status = xml.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return null;
                }

                while(xml.nextTag() == XmlPullParser.START_TAG) {
                    if(xml.getName().equals("pipe")) {

                        currentPipes[i][0] = xml.getAttributeValue(null, "pipetype");
                        currentPipes[i][1] = xml.getAttributeValue(null, "xloc");
                        currentPipes[i][2] = xml.getAttributeValue(null, "yloc");
                        currentPipes[i][3] = xml.getAttributeValue(null, "rotation");
                        currentPipes[i][4] = "TRUE";

                        i++;

                    }

                    skipToEndTag(xml);
                }

                currentPipes[i][4] = "FALSE";

                // We are done
            } catch(XmlPullParserException ex) {
                return null;
            } catch(IOException ex) {
                return null;
            } finally {
                try {
                    stream.close();
                } catch(IOException ex) {

                }
            }

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }

        return currentPipes;
    }


}


