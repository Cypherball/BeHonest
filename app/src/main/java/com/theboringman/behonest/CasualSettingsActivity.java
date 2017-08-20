package com.theboringman.behonest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CasualSettingsActivity extends Activity {
    private TextView player1;
    private TextView player2;
    private TextView scoreLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casual_settings);
        player1 = (TextView)findViewById(R.id.name1);
        player2 = (TextView)findViewById(R.id.name2);
        scoreLimit = (TextView)findViewById(R.id.scoreLimiter);
    }
    public void onClickPlay(View view){
        if(player1.getText().toString().isEmpty()){
            Toast.makeText(this,"Enter Player 1's Name!",Toast.LENGTH_SHORT).show();
        }
        else if(player2.getText().toString().isEmpty()){
            Toast.makeText(this,"Enter Player 2's Name!",Toast.LENGTH_SHORT).show();
        }
        else if(scoreLimit.getText().toString().isEmpty()){
            Toast.makeText(this,"Enter Score Limit!",Toast.LENGTH_SHORT).show();
        }
        else if(Double.parseDouble(scoreLimit.getText().toString()) < 10){
            Toast.makeText(this,"Score Limit should be between 10 and 99!",Toast.LENGTH_SHORT).show();
        }
        else if(player1.getText().toString().equals(player2.getText().toString())){
            Toast.makeText(this,"Players must have different names!",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(CasualSettingsActivity.this, CasualActivity.class);
            intent.putExtra("player1Name",player1.getText().toString());
            intent.putExtra("player2Name",player2.getText().toString());
            intent.putExtra("scoreLimit",scoreLimit.getText().toString());
            startActivity(intent);
        }
    }
}
