package com.theboringman.behonest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }
    public void onClickSettings(View view){
       /* ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent= new Intent(LaunchActivity.this,Options.class);
        intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        ActivityCompat.startActivity(this, intent, options.toBundle());*/
    }
    public void onClickPlay(View view){
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent= new Intent(LaunchActivity.this,TruthDareSettingsActivity.class);
        intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
