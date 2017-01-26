package edu.msu.simunovi.project2;

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

public class AccountActivity extends AppCompatActivity {

    private static volatile boolean accountCreated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onSubmit(View view) {
        String username = (((EditText) findViewById(R.id.editText)).getText().toString());
        String password = (((EditText) findViewById(R.id.editText2)).getText().toString());
        String passwordConfirm = (((EditText) findViewById(R.id.editText3)).getText().toString());

        if (username.length() == 0 || password.length() == 0 || passwordConfirm.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
        } else if (username.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
        } else if (passwordConfirm.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter the password again", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match, type in same password twice", Toast.LENGTH_LONG).show();
        } else if (password.equals(passwordConfirm)) {
            saveUser(username, password);


                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                finish();

        }
    }

    public void saveUser(final String user, final String password) {
        //

         /*
         * Create a thread to load the hatting from the cloud
         */
        //final boolean temp;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML
                boolean temp = false;
                Cloud cloud = new Cloud();
                InputStream stream = cloud.createAccount(user, password);

                // Test for an error
                boolean fail = stream == null;

                try {

                    XmlPullParser xml = Xml.newPullParser();
                    xml.setInput(stream, "UTF-8");

                    xml.nextTag();      // Advance to first tag
                    xml.require(XmlPullParser.START_TAG, null, "proj2");
                    String status = xml.getAttributeValue(null, "status");

                    if (status.equals("yes")) {

                        Log.i("CREATE ACCOUNT", "yes");
                        //temp = true;
                        temp = true;
                        return;


                    } else {
                        Log.i("CREATE ACCOUNT", "no");
                        //return false;
                        //fail = true;
                        temp = false;
                        return;

                    }

                } catch (IOException ex) {
                    fail = true;
                    setAccountCreated(temp);
                } catch (XmlPullParserException ex) {
                    fail = true;
                    setAccountCreated(temp);
                } finally {
                    try {
                        stream.close();
                        setAccountCreated(temp);
                    } catch (IOException ex) {
                        setAccountCreated(temp);
                    }
                }

            }
        }).start();
        return;
    }

    void setAccountCreated(boolean b)
    {
        accountCreated = b;
    }

    boolean getAccountCreated()
    {
        return accountCreated;
    }

}
