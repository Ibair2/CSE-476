package edu.msu.simunovi.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class FinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        TextView whoWon = (TextView) findViewById(R.id.endGameResults);
        if(PlayingAreaView.currentTurn.equals(MainActivity.p1))
            whoWon.setText(MainActivity.p1 + " won and " + MainActivity.p2 + " lost!");
        else
            whoWon.setText(MainActivity.p2 + " won and " + MainActivity.p1 + " lost!");
    }

    /*
    * Project 1 Checkpoint Requirement:
    * Navigate to the opening screen
    */
    public void onMainPress_FinishedActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
    * Project 1 Checkpoint Requirement:
    * Navigate to the game playing screen
    */
    public void onStartGamePress_FinishedActivity(View view) {
        Intent intent = new Intent(this, PlayingActivity.class);
        startActivity(intent);
    }



}
