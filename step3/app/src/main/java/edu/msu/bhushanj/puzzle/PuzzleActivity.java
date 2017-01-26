package edu.msu.bhushanj.puzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PuzzleActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_shuffle:
                getPuzzleView().getPuzzle().shuffle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_puzzle, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        getPuzzleView().saveInstanceState(bundle);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_puzzle);

        if(bundle != null) {
            // We have saved state
            getPuzzleView().loadInstanceState(bundle);
        }
    }

    /**
     * Get the puzzle view
     * @return PuzzleView reference
     */
    private PuzzleView getPuzzleView() {
        return (PuzzleView)this.findViewById(R.id.puzzleView);
    }

}
