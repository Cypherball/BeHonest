package com.theboringman.behonest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
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
    private int revealX;
    private int revealY;
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_dare_settings);
        final Intent intent = getIntent();
        rootLayout = findViewById(R.id.truth_dare_settings);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)){
            rootLayout.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
                ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        @TargetApi(21)
                        public void onGlobalLayout() {
                            revealActivity(revealX, revealY);
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                } else{
                rootLayout.setVisibility(View.VISIBLE);
             }
        }else {
            rootLayout.setVisibility(View.VISIBLE);
        }
        loadStuff();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        unRevealActivity();
    }

    protected void revealActivity( int x, int y){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(600);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(300);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });
            circularReveal.start();
        }
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
            db.deleteDataChecks("1");
            db.insertDataChecks( 1, (kidsCheck)?1:0, (teensCheck)?1:0, (adultsCheck)?1:0, (customCheck)?1:0, Integer.parseInt(scoreLimit.getText().toString()) );
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
            int revealX = (int) (view.getX() + view.getWidth() / 2);
            int revealY = (int) (view.getY() + view.getHeight() / 2);
            Intent intent = new Intent(TruthDareSettingsActivity.this, TruthDarePlayersActivity.class);
            intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
            ActivityCompat.startActivity(this, intent, options.toBundle());
        }
    }
}
