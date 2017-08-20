package com.theboringman.behonest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CasualActivity extends Activity {
    private TextView playerName;
    private TextView q1;
    private TextView q2;
    private TextView q3;
    private TextView q4;
    private TextView q5;
    private RadioGroup r1;
    private RadioGroup r2;
    private RadioGroup r3;
    private RadioGroup r4;
    private RadioGroup r5;
    private RadioButton r1_1;
    private RadioButton r1_2;
    private RadioButton r1_3;
    private RadioButton r2_1;
    private RadioButton r2_2;
    private RadioButton r2_3;
    private RadioButton r3_1;
    private RadioButton r3_2;
    private RadioButton r3_3;
    private RadioButton r4_1;
    private RadioButton r4_2;
    private RadioButton r4_3;
    private RadioButton r5_1;
    private RadioButton r5_2;
    private RadioButton r5_3;
    private Button done;
    private ScrollView scrollView;
    private int scoreLimit;
    private String player1Name;
    private String player2Name;
    private String player1NameSet;
    private String player2NameSet;
    private int checkCount;
    private boolean isCheckMin;
    private boolean isCheckCountFive;
    private int checkMin;
    private int score;
    private int score1;
    private int score2;
    private List<String> questions;
    private  List<String> tempQuestions;
    //private  List<String> tempQuestions_2;                *****Consider for this******


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casual);
        Intent intent = getIntent();
        player1Name = intent.getStringExtra("player1Name");
        player2Name = intent.getStringExtra("player2Name");
        scoreLimit = Integer.parseInt(intent.getStringExtra("scoreLimit"));
        loadStuff();
        player1NameSet = player1Name+"'s Turn";
        player2NameSet = player2Name+"'s Turn";
        playerName.setText(player1NameSet);
        tempQuestions = questions;
        //tempQuestions_2 = questions;
        setText();
    }
    private void loadStuff(){
        questions = new ArrayList<>();
        tempQuestions = new ArrayList<>();
        // tempQuestions_2 = new ArrayList<>();
        playerName = (TextView)findViewById(R.id.Name);
        q1 = (TextView)findViewById(R.id.q1);
        q2 = (TextView)findViewById(R.id.q2);
        q3 = (TextView)findViewById(R.id.q3);
        q4 = (TextView)findViewById(R.id.q4);
        q5 = (TextView)findViewById(R.id.q5);
        r1 = (RadioGroup)findViewById(R.id.r1);
        r2 = (RadioGroup)findViewById(R.id.r2);
        r3 = (RadioGroup)findViewById(R.id.r3);
        r4 = (RadioGroup)findViewById(R.id.r4);
        r5 = (RadioGroup)findViewById(R.id.r5);
        r1_1 = (RadioButton)findViewById(R.id.r1_1);
        r1_2 = (RadioButton)findViewById(R.id.r1_2);
        r1_3 = (RadioButton)findViewById(R.id.r1_3);
        r2_1 = (RadioButton)findViewById(R.id.r2_1);
        r2_2 = (RadioButton)findViewById(R.id.r2_2);
        r2_3 = (RadioButton)findViewById(R.id.r2_3);
        r3_1 = (RadioButton)findViewById(R.id.r3_1);
        r3_2 = (RadioButton)findViewById(R.id.r3_2);
        r3_3 = (RadioButton)findViewById(R.id.r3_3);
        r4_1 = (RadioButton)findViewById(R.id.r4_1);
        r4_2 = (RadioButton)findViewById(R.id.r4_2);
        r4_3 = (RadioButton)findViewById(R.id.r4_3);
        r5_1 = (RadioButton)findViewById(R.id.r5_1);
        r5_2 = (RadioButton)findViewById(R.id.r5_2);
        r5_3 = (RadioButton)findViewById(R.id.r5_3);
        done = (Button)findViewById(R.id.done);
        scrollView = (ScrollView)findViewById(R.id.main);
        score = 0;
        score1 = 0;
        score2 = 0;
        checkCount = 0;
        checkMin = 0;
        isCheckCountFive = false;
        isCheckMin = false;
        loadText();
    }

    private void setText(){
        Random random = new Random();

        int ini1 = random.nextInt(tempQuestions.size());
        String t1 = "1. " + tempQuestions.get(ini1);
        q1.setText(t1);
        tempQuestions.remove(ini1);

        int ini2 = random.nextInt(tempQuestions.size());
        String t2 = "2. " + tempQuestions.get(ini2);
        q2.setText(t2);
        tempQuestions.remove(ini2);

        int ini3 = random.nextInt(tempQuestions.size());
        String t3 = "3. " + tempQuestions.get(ini3);
        q3.setText(t3);
        tempQuestions.remove(ini3);

        int ini4 = random.nextInt(tempQuestions.size());
        String t4 = "4. " + tempQuestions.get(ini4);
        q4.setText(t4);
        tempQuestions.remove(ini4);

        int ini5 = random.nextInt(tempQuestions.size());
        String t5 = "5. " + tempQuestions.get(ini5);
        q5.setText(t5);
        tempQuestions.remove(ini5);
    }

    public void onClickRadioButton_1(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.r1_1:
                if(checked){
                    score += 1;
                    r1_1.setEnabled(false);
                    r1_2.setEnabled(false);
                    r1_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r1_2:
                if(checked){
                    score += 1;
                    r1_1.setEnabled(false);
                    r1_2.setEnabled(false);
                    r1_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r1_3:
                if(checked){
                    r1_1.setEnabled(false);
                    r1_2.setEnabled(false);
                    r1_3.setEnabled(false);
                    checkCount += 1;
                }
                break;
        }
        if(checkCount == 5){
            done.setTypeface(null, Typeface.BOLD);
        }
    }

    public void onClickRadioButton_2(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.r2_1:
                if(checked){
                    score += 1;
                    r2_1.setEnabled(false);
                    r2_2.setEnabled(false);
                    r2_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r2_2:
                if(checked){
                    score += 1;
                    r2_1.setEnabled(false);
                    r2_2.setEnabled(false);
                    r2_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r2_3:
                if(checked){
                    r2_1.setEnabled(false);
                    r2_2.setEnabled(false);
                    r2_3.setEnabled(false);
                    checkCount += 1;
                }
                break;
        }
        if(checkCount == 5){
            done.setTypeface(null, Typeface.BOLD);
        }
    }

    public void onClickRadioButton_3(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.r3_1:
                if(checked){
                    score += 1;
                    r3_1.setEnabled(false);
                    r3_2.setEnabled(false);
                    r3_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r3_2:
                if(checked){
                    score += 1;
                    r3_1.setEnabled(false);
                    r3_2.setEnabled(false);
                    r3_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r3_3:
                if(checked){
                    r3_1.setEnabled(false);
                    r3_2.setEnabled(false);
                    r3_3.setEnabled(false);
                    checkCount += 1;
                }
                break;
        }
        if(checkCount == 5){
            done.setTypeface(null, Typeface.BOLD);
        }
    }

    public void onClickRadioButton_4(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.r4_1:
                if(checked){
                    score += 1;
                    r4_1.setEnabled(false);
                    r4_2.setEnabled(false);
                    r4_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r4_2:
                if(checked){
                    score += 1;
                    r4_1.setEnabled(false);
                    r4_2.setEnabled(false);
                    r4_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r4_3:
                if(checked){
                    r4_1.setEnabled(false);
                    r4_2.setEnabled(false);
                    r4_3.setEnabled(false);
                    checkCount += 1;
                }
                break;
        }
        if(checkCount == 5){
            done.setTypeface(null, Typeface.BOLD);
        }
    }

    public void onClickRadioButton_5(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.r5_1:
                if(checked){
                    score += 1;
                    r5_1.setEnabled(false);
                    r5_2.setEnabled(false);
                    r5_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r5_2:
                if(checked){
                    score += 1;
                    r5_1.setEnabled(false);
                    r5_2.setEnabled(false);
                    r5_3.setEnabled(false);
                    checkMin += 1;
                    checkCount += 1;
                }
                break;
            case R.id.r5_3:
                if(checked){
                    r5_1.setEnabled(false);
                    r5_2.setEnabled(false);
                    r5_3.setEnabled(false);
                    checkCount += 1;
                }
                break;
        }
        if(checkCount == 5){
            done.setTypeface(null, Typeface.BOLD);
        }
    }

    private void checkForPass(){
        if(checkMin >= 3){
            isCheckMin = true;
        }
    }
    private void checkForPass2(){
        if(checkCount == 5){
            isCheckCountFive = true;
        }
    }

    public void onClickDone(View view){
        checkForPass();
        checkForPass2();
        if(!isCheckCountFive){
            Toast.makeText(this,"Please check for all questions.",Toast.LENGTH_LONG).show();
        }
        else if(!isCheckMin){
            Toast.makeText(this,"You have answer with only Yes/No for at least 3 questions.",Toast.LENGTH_LONG).show();
        }
        else{
            if(playerName.getText().toString().equals(player1NameSet)){
                score1 += score;
                playerName.setText(player2NameSet);
            }
            else{
                score2 += score;
                //Check if Score Limit is reached.
                if(score1 >= scoreLimit || score2 >= scoreLimit){
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                    mBuilder.setTitle(R.string.limitReachedDialog);
                    if(score1>score2) {
                        mBuilder.setMessage("Congratulations " + player1Name.toUpperCase() + "!" + " You WON!!\n\n"+player1Name+"'s Score: "+score1+"\n"+player2Name+"'s Score: "+score2+"\n\nClick OK to exit to Main Menu.\nClick PLAY AGAIN to restart.");
                    }
                    else if(score1 == score2){
                        mBuilder.setMessage("Congratulations You Both!" + " It's a TIE!!\n\n"+player1Name+"'s Score: "+score1+"\n"+player2Name+"'s Score: "+score2+"\n\nClick OK to exit to Main Menu.\nClick PLAY AGAIN to restart.");
                    }
                    else{
                        mBuilder.setMessage("Congratulations " + player2Name.toUpperCase() + "!" + " You WON!!\n\n"+player2Name+"'s Score: "+score2+"\n"+player1Name+"'s Score: "+score1+"\n\nClick OK to exit to Main Menu.\nClick PLAY AGAIN to restart.");
                    }
                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CasualActivity.this,LaunchActivity.class);
                            startActivity(intent);
                        }
                    });
                    mBuilder.setNeutralButton("PLAY AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempQuestions = questions;
                            playerName.setText(player1NameSet);
                            score1 = 0;
                            score2 = 0;
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = mBuilder.create();
                    alertDialog.show();
                }
                //If Score Limit is not reached.....
                else {
                    playerName.setText(player1NameSet);
                }
            }
            setText();
            Reset();
        }
    }

    public void onClickReset(View view){
        Reset();
    }

    public void Reset(){
        score = 0;
        checkCount = 0;
        isCheckCountFive = false;
        checkMin = 0;
        isCheckMin = false;
        done.setTypeface(null, Typeface.NORMAL);

        r1_1.setEnabled(true);
        r1_2.setEnabled(true);
        r1_3.setEnabled(true);
        r1.clearCheck();

        r2_1.setEnabled(true);
        r2_2.setEnabled(true);
        r2_3.setEnabled(true);
        r2.clearCheck();

        r3_1.setEnabled(true);
        r3_2.setEnabled(true);
        r3_3.setEnabled(true);
        r3.clearCheck();

        r4_1.setEnabled(true);
        r4_2.setEnabled(true);
        r4_3.setEnabled(true);
        r4.clearCheck();

        r5_1.setEnabled(true);
        r5_2.setEnabled(true);
        r5_3.setEnabled(true);
        r5.clearCheck();

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0,View.FOCUS_UP);
            }
        });
    }

    public void onClickExit(View view){
        //Alert Dialog creation on Exit.
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Are you sure you want to exit?");
        mBuilder.setMessage("Click YES to exit.\nClick NO to resume game.\nClick Exit With Score to view score and then exit.");
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CasualActivity.this,LaunchActivity.class);
                startActivity(intent);
            }
        });

        mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setNeutralButton("Exit With Score", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Alert Dialog to show Scores after clicking Exit With Score.
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(CasualActivity.this);
                mBuilder2.setTitle("SCORES");
                mBuilder2.setMessage(player1Name+"'s Score: "+score1+"\n\n"+player2Name+"'s Score: "+score2);
                mBuilder2.setCancelable(false);
                mBuilder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CasualActivity.this,LaunchActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = mBuilder2.create();
                alertDialog.show();
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    private void loadText(){
        questions.add("Do you currently have a crush on someone?");
        questions.add("Have you ever masturbated in a public place?");
        questions.add("Have you made out with anyone?");
        questions.add("Are you gay?");
        questions.add("Are you suicidal?");
        questions.add("Have you ever seriously thought about murdering someone?");
        questions.add("Do you watch porn?");
        questions.add("Are you a virgin?");
        questions.add("Are you satisfied with your current life?");
        questions.add("Have you ever stolen something?");
        questions.add("Do you sing?");
        questions.add("Did you ever cheat on your partner / former partner?");
        questions.add("Did any of your friends get in trouble because of you?");
        questions.add("Have you ever been arrested?");
        questions.add("Have you ever been drunk?");
        questions.add("Have you ever done IT on a public place?");
        questions.add("Have you ever flashed someone?");
        questions.add("Have you ever slept naked as a teenager/adult?");
        questions.add("Have you made out with something not human?");
        questions.add("Did you ever do drugs?");
        questions.add("Have you ever done something that you can go to jail for?");
        questions.add("Have you ever forgotten a friend's name?");
        questions.add("Have you ever beaten up any of your friends?");
        questions.add("Have you ever been in a fist fight?");
        questions.add("Have you ever cried over someone?");
        questions.add("Have you ever laughed until something you were drinking came out your nose");
        questions.add("Do you believe in Love?");
        questions.add("Are you into kinky stuff?");
        questions.add("Are you insecure?");
        questions.add("Have you ever had a crush on your best friend's date?");
        questions.add("Did you ever loose your cool in public?");
        questions.add("Do you like kids?");
        questions.add("Do you regularly tell lies?");
        questions.add("Have you ever betrayed a friend?");
        questions.add("Are you living up to your own expectations?");
        questions.add("Are you living up to your parents expectations?");
        questions.add("Do you truly love your siblings?");
        questions.add("Do you truly love your parents?");
        questions.add("Do you think I'm ugly?");
        questions.add("Did you pee on your bed while sleeping recently?");
        questions.add("Have you ever cheated on an important test?");
        questions.add("Have you ever ran a red light?");
        questions.add("Have you ever shaved any part of your body?");
        questions.add("Do you think the gender opposite to yours is better?");
        questions.add("Do you fantasize about random people you meet?");
        questions.add("Have you had a near-death experience?");
        questions.add("Have you ever kissed someone you didn't like?");
        questions.add("Did you ever cause an accident that harmed other people?");
        questions.add("Have you ever been suspended from school?");
        questions.add("Have you ever wished death upon yourself?");
        questions.add("Have you ever dumped someone over text message?");
        questions.add("Have you farted during an intimate moment?");
        questions.add("Have you ever been stalked by an ex?");
        questions.add("Have you ever gone on a date with a friend?");
        questions.add("Have you ever had a friend with benefits?");
        questions.add("Have you ever hooked up with a complete stranger?");
        questions.add("Have you ever fell in love at first sight?");
        questions.add("Have you ever forgotten an anniversary?");
        questions.add("Have you ever written someone a love poem?");
        questions.add("Have you ever watched porn with someone else?");
        questions.add("Would you like to go on a date with me?");
        questions.add("Are you an introvert?");
        questions.add("Have you ever fallen asleep at school/work?");
        questions.add("Have you ever sang in the shower?");
        questions.add("Have you ever been in a band?");
        questions.add("Have you ever shot a gun?");
        questions.add("Have you ever picked up money fallen on the ground and used it for yourself?");
        questions.add("Do you think you're funny?");
        questions.add("Do you think I am funny?");
    }
}
