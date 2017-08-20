package com.theboringman.behonest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class TruthDareSettingsActivity extends Activity {

    private CheckBox kids;
    private CheckBox teens;
    private CheckBox adults;
    private CheckBox custom;
    private boolean kidsCheck;
    private boolean teensCheck;
    private boolean adultsCheck;
    private boolean customCheck;
    private TextView scoreLimit;
    private boolean checked;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_dare_settings);
        loadStuff();
    }

    private void loadStuff(){
        db = new DatabaseHelper(this);
        kids = (CheckBox)findViewById(R.id.kids);
        teens = (CheckBox)findViewById(R.id.teen);
        adults = (CheckBox)findViewById(R.id.adults);
        custom = (CheckBox)findViewById(R.id.custom);
        scoreLimit = (TextView)findViewById(R.id.scoreLimiterTD);
    }

    private void getChecks(){
        kidsCheck = false;
        teensCheck = false;
        adultsCheck = false;
        customCheck = false;
        checked = false;
        kidsCheck = kids.isChecked();
        teensCheck = teens.isChecked();
        adultsCheck = adults.isChecked();
        customCheck = custom.isChecked();
        if(kidsCheck || teensCheck || adultsCheck || customCheck){
            checked = true;
        }
    }

    public void onClickNext (View view){
        getChecks();
        if(!checked && scoreLimit.getText().toString().isEmpty()){
            Toast.makeText(this,"Select at least one Question Type and specify a Score Limit.",Toast.LENGTH_LONG).show();
        }
        else if(!checked){
            Toast.makeText(this,"Select at least one Question Type.",Toast.LENGTH_SHORT).show();
        }
        else if(scoreLimit.getText().toString().isEmpty()){
            Toast.makeText(this,"Please a specify Score Limit.",Toast.LENGTH_SHORT).show();
        }
        else if(Double.parseDouble(scoreLimit.getText().toString())>100){
            Toast.makeText(this,"Score Limit should be between 1-100!",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(TruthDareSettingsActivity.this, TruthDarePlayersActivity.class);

            /*if(kidsCheck) {
                intent.putExtra("kidsChecked", true);
            } else{
                intent.putExtra("kidsChecked", false);
            }

            if(teensCheck) {
                intent.putExtra("teensChecked", true);
            } else{
                intent.putExtra("teensChecked", false);
            }

            if(adultsCheck) {
                intent.putExtra("adultsChecked", true);
            } else{
                intent.putExtra("adultsChecked", false);
            }

            if(customCheck) {
                intent.putExtra("customChecked", true);
            } else{
                intent.putExtra("customChecked", false);
            }*/
            //intent.putExtra("scoreLimit",scoreLimit.getText().toString());
            db.deleteDataChecks("1");
            db.insertDataChecks( 1, (kidsCheck)?1:0, (teensCheck)?1:0, (adultsCheck)?1:0, (customCheck)?1:0, Integer.parseInt(scoreLimit.getText().toString()) );
            startActivity(intent);
        }
    }
}
