package com.theboringman.behonest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class TruthDarePlayersActivity extends Activity {

    private ArrayList<String> playerNames;
    private EditText nameField;
    private ListView listView;
    private Button remove;
    private DatabaseHelper db;
    private Cursor res;
    private int id;
    private customAdapter _customAdapter;
    private int revealX;
    private int revealY;
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_dare_players);
        loadStuff();
        while(res.moveToNext()){
            playerNames.add(res.getString(1));
        }
        id = playerNames.size();
        setListView();
        final Intent intent = getIntent();
        rootLayout = findViewById(R.id.truth_dare_players);
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

    private void setListView(){
        String[] names = playerNames.toArray(new String[playerNames.size()]);
        _customAdapter = new customAdapter(this, names);
        listView.setAdapter(_customAdapter);
        if(playerNames.size() >= 1){
            remove.setVisibility(View.VISIBLE);
        }
    }

    private void loadStuff(){
        db = new DatabaseHelper(this);
        res = db.getAllDataPlayers();
        playerNames = new ArrayList<>();
        nameField = (EditText)findViewById(R.id.enterName);
        listView = (ListView)findViewById(R.id.players);
        remove = (Button)findViewById(R.id.remove);
    }

    public void onClickAdd(View view){
        if(nameField.getText().toString().isEmpty()){
            Toast.makeText(this,"Please enter a Name first.",Toast.LENGTH_SHORT).show();
        }
        else if(playerNames.size() > 15){
            Toast.makeText(this,"Maximum of 15 players reached.",Toast.LENGTH_SHORT).show();
        }
        else if(playerNames.contains(nameField.getText().toString())){
            Toast.makeText(this,"Entry with same name exists. Please enter a different name.",Toast.LENGTH_LONG).show();
        }
        else{
            playerNames.add(nameField.getText().toString());
            nameField.setText("");
            id++;
            db.insertDataPlayers(id,playerNames.get(id-1),0);
            if(playerNames.size() >= 1){
                remove.setVisibility(View.VISIBLE);
            }
            setListView();
        }
    }

    public void onClickRemove(View view){
        playerNames.remove(playerNames.size()-1);
        db.deleteDataPlayers(Integer.toString(id));
        id--;
        if(playerNames.isEmpty()){
            remove.setVisibility(View.INVISIBLE);
        }
        setListView();
    }

    public void onClickPlay (View view){
        if(playerNames.size()<2){
            Toast.makeText(this,"Add at least two players!",Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
            int revealX = (int) (view.getX() + view.getWidth() / 2);
            int revealY = (int) (view.getY() + view.getHeight() / 2);

            Intent intent = new Intent(TruthDarePlayersActivity.this, TruthDareSelectActivity.class);
            intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(TruthDareSettingsActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
            ActivityCompat.startActivity(this, intent, options.toBundle());
        }
    }
}
