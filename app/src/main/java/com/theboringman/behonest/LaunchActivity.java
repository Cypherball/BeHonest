package com.theboringman.behonest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }
    public void onClickSettings(View view){
        //Intent intent = new Intent (LaunchActivity.this, Settings.class);
        //startActivity(intent);
    }
    public void onCickPlay(View view){
        Intent intent= new Intent(LaunchActivity.this,TruthDareSettingsActivity.class);
        startActivity(intent);
    }
}
