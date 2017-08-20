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
    public void onClickCasual(View view){
        Intent intent = new Intent (LaunchActivity.this, CasualSettingsActivity.class);
        startActivity(intent);
    }
    public void onCickTruthDare(View view){
        Intent intent= new Intent(LaunchActivity.this,TruthDareSettingsActivity.class);
        startActivity(intent);
    }
}
