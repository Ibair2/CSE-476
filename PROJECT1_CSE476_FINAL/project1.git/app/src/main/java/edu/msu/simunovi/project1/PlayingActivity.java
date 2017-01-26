package edu.msu.simunovi.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PlayingActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        Intent intent = getIntent();
        int gridSize = intent.getIntExtra("gridsize", 0);
        getPlayingArea().setGridSize(gridSize);


        // Set the turn as Player 1's turn
        TextView whoseTurn = (TextView) findViewById(R.id.Player1);

        whoseTurn.setText("It is " + MainActivity.p1 + "'s turn now!");
        PlayingAreaView.currentTurn = MainActivity.p1;

        //getPlayingArea().setPlayingArea(new PlayingArea(gridSize, gridSize));
    }



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
        TextView whoseTurn = (TextView) findViewById(R.id.Player1);
        PlayingAreaView.ChangeTurn(whoseTurn);

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
            TextView whoseTurn = (TextView) findViewById(R.id.Player1);
            PlayingAreaView.ChangeTurn(whoseTurn);

            Intent intent = new Intent(this, FinishedActivity.class);
            startActivity(intent);
        }
    }

    public void onInstallPressed(View view) {
        getPlayingArea().installThisPipe();

        TextView whoseTurn = (TextView) findViewById(R.id.Player1);
        PlayingAreaView.ChangeTurn(whoseTurn);
    }

    //private PlayingAreaView getPlayingAreaView() {
      //  return (PlayingAreaView)this.findViewById(R.id.playingAreaView);
    //}
    private PlayingAreaView getPlayingArea() {
        return (PlayingAreaView) this.findViewById(R.id.playingAreaView);
    }






}
