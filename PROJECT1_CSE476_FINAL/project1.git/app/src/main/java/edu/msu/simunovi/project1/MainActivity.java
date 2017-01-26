package edu.msu.simunovi.project1;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private int gridSize = 0;
    public static String p1;
    public static String p2;


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
        Intent intent = new Intent(this, PlayingActivity.class);
        intent.putExtra("gridsize", gridSize);


        EditText p1text = (EditText)findViewById(R.id.editPlayer1);
        p1 =  p1text.getText().toString();

        EditText p2text = (EditText)findViewById(R.id.editPlayer2);
        p2 =  p2text.getText().toString();

        if (p1.matches("")) {
            Toast.makeText(this, "Player 1 needs a name", Toast.LENGTH_SHORT).show();
            return;
        }else if (p2.matches("")) {
            Toast.makeText(this, "Player 2 needs a name", Toast.LENGTH_SHORT).show();
            return;
        } else startActivity(intent);
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
}



