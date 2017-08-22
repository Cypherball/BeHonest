package com.theboringman.behonest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class TruthDareSelectActivity extends Activity {

    private TextView currPlayerName;
    private int i;                                                //Mostly used as ID
    private ArrayList<String> playerNames;                        //List of Players
    private ArrayList<String> scores;                             //Players' Scores
    private ArrayList<String> kidsTruthQ;                         //Truth Questions for kids
    private ArrayList<String> teensTruthQ;                        //Truth Questions for teens
    private ArrayList<String> adultsTruthQ;                       //Truth Questions for adults
    private ArrayList<String> kidsDares;                          //Dares for kids
    private ArrayList<String> teensDares;                         //Dares for teens
    private ArrayList<String> adultsDares;                        //Dare for adults
    private ArrayList<String> finalTruthQuestions;                //Setting the Final Truth Questions to be displayed according to checks
    private ArrayList<String> finalDareQuestions;                 //Setting the Final Dares to be displayed according to checks
    private boolean isTruth;
    private boolean kidsCheck;
    private boolean teensCheck;
    private boolean adultsCheck;
    private boolean customCheck;
    private int scoreLimit;
    private int ini1;
    private int ini2;
    private DatabaseHelper db;
    private AlertDialog alertDialog;
    private Random random;
    private customAdapterTwo _customAdapter;                       //For ScoreBoard ListView
    private int revealX;
    private int revealY;
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_dare_select);
        loadStuff();
       setCurrPlayerName();
        final Intent intent = getIntent();
        rootLayout = findViewById(R.id.truth_dare_select);
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

    private void loadStuff() {
        db = new DatabaseHelper(this);
        playerNames = new ArrayList<>();
        scores = new ArrayList<>();
        kidsTruthQ = new ArrayList<>();
        teensTruthQ = new ArrayList<>();
        adultsTruthQ = new ArrayList<>();
        kidsDares = new ArrayList<>();
        teensDares = new ArrayList<>();
        adultsDares = new ArrayList<>();
        finalTruthQuestions = new ArrayList<>();
        finalDareQuestions = new ArrayList<>();
        Cursor res = db.getAllDataPlayers();
        Cursor resC = db.getAllDataChecks();
        currPlayerName = (TextView) findViewById(R.id.playerName);
        i=0;
        while (res.moveToNext()) {
            playerNames.add(res.getString(1));
        }
        while (resC.moveToNext()) {
            kidsCheck = convertToBool(resC.getInt(1));
            teensCheck = convertToBool(resC.getInt(2));
            adultsCheck = convertToBool(resC.getInt(3));
            customCheck = convertToBool(resC.getInt(4));
            scoreLimit = resC.getInt(5);
        }
        makeScoresZero();
        isTruth = false;
        setKidsTruthQ();
        setTeensTruthQ();
        setAdultsTruthQ();
        setKidsDares();
        setTeensDares();
        setAdultsDares();
        setFinalTruthQuestions();
        setFinalDareQuestions();
    }

    private void makeScoresZero(){
        for(int j = 1; j <= playerNames.size(); j++){
            db.updateScorePlayers(Integer.toString(j),"0");
        }
    }

    //Set The Text View to Current Player's Name
    private void setCurrPlayerName(){
        String currPlayerSetText = playerNames.get(i) + getText(R.string.turn);
        currPlayerName.setText(currPlayerSetText);
    }

    //Update the scores to the Scores ArrayList
    private void getScores(){
        Cursor res = db.getAllDataPlayers();
        scores.clear();
        while (res.moveToNext()) {
            scores.add(res.getString(2));
        }
    }

    //Increment the ID / reset if condition
    private void checkID(){
        i++;
        if (i >= (playerNames.size())) {
            i = 0;
        }
    }

    //Converting the Integer checks to Boolean values.
    private boolean convertToBool(int i) {
        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void onClickTruth(View view) {
        isTruth = true;
        alertDialog();
    }

    public void onClickDare(View view) {
        isTruth = false;
        alertDialog();
    }

    public void onClickViewScores(View view) {
        ViewScores(false);
    }

    public void onClickExit(View view) {
        //Alert Dialog creation on Exit.
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Are you sure you want to exit?");
        mBuilder.setMessage("Click YES to exit.\nClick NO to resume game.\nClick Exit With Score to view score and then exit.");
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TruthDareSelectActivity.this, LaunchActivity.class);
                startActivity(intent);
                finish();
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
                ViewScores(true);
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    private void ViewScores(boolean isExit){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.view_scores, null);
        ListView scoresView = (ListView) mView.findViewById(R.id.Scores);
        Button back = (Button) mView.findViewById(R.id.back);
        if(isExit){
            back.setText(R.string.exit);
        }
        //Set ListView
        getScores();
        String[] names = playerNames.toArray(new String[playerNames.size()]);
        String[] score = scores.toArray(new String[scores.size()]);
        _customAdapter = new customAdapterTwo(this, names, score);
        scoresView.setAdapter(_customAdapter);
        //Button Listener
        if(isExit){
            back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TruthDareSelectActivity.this, LaunchActivity.class);
                startActivity(intent);
                finish();
            }
        });
        }else{
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
        mBuilder.setView(mView);
        alertDialog = mBuilder.create();
        alertDialog.show();
    }


    //Alert Dialog for the Questions to pop up------------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void alertDialog() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.questions_layout, null);
        TextView truthOrDare = (TextView) mView.findViewById(R.id.truthOrDare);
        TextView textDisp = (TextView) mView.findViewById(R.id.textDisp);
        Button completed = (Button) mView.findViewById(R.id.completed);
        Button forfeit = (Button) mView.findViewById(R.id.forfeit);
        //Set Title
        if (isTruth) {
            String nameTruth = playerNames.get(i) + (": Truth");
            truthOrDare.setText(nameTruth);
        } else {
            String nameDare = playerNames.get(i) + (": Dare");
            truthOrDare.setText(nameDare);
        }
        //Set Questions
        if (isTruth) {
            if(finalTruthQuestions.isEmpty()){
                Toast.makeText(this,"Out of truth questions! End Game or try with Dares.",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            } else {
                String question = truthRandomizer();             //Get Random Truth
                textDisp.setText(question);
                finalTruthQuestions.remove(question);            //Removes the current Truth from the List
            }
        } else {
            if(finalDareQuestions.isEmpty()){
                Toast.makeText(this,"Out of dares! End Game or try with Truths.",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            } else {
                String question = dareRandomizer();              //Get Random Dare
                textDisp.setText(question);
                finalDareQuestions.remove(question);             //Removes the current Dare from the List
            }
        }
        //Button Listeners
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScores();
                db.updateScorePlayers( Integer.toString(i+1), Integer.toString( (Integer.parseInt(scores.get(i))) + 1) );
                checkID();
                setCurrPlayerName();
                alertDialog.dismiss();
            }
        });
        forfeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkID();
                setCurrPlayerName();
                alertDialog.dismiss();
            }
        });
        //Create & Show Dialog
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        alertDialog = mBuilder.create();
        alertDialog.show();
    }
    //------------------------------------------------------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //Set Final Questions---------------------------------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //Final Truth Questions
    private void setFinalTruthQuestions() {
        if (kidsCheck && teensCheck && adultsCheck) {
            finalTruthQuestions.addAll(kidsTruthQ);
            for(int j = 0; j < teensTruthQ.size()-1; j++) {
                if(!finalTruthQuestions.contains(teensTruthQ.get(j))) {
                    finalTruthQuestions.add(teensTruthQ.get(j));
                }
            }
            for(int j = 0; j < adultsTruthQ.size()-1; j++) {
                if(!finalTruthQuestions.contains(adultsTruthQ.get(j))) {
                    finalTruthQuestions.add(adultsTruthQ.get(j));
                }
            }
        } else if (kidsCheck && teensCheck) {
            finalTruthQuestions.addAll(kidsTruthQ);
            for(int j = 0; j < teensTruthQ.size()-1; j++) {
                if(!finalTruthQuestions.contains(teensTruthQ.get(j))) {
                    finalTruthQuestions.add(teensTruthQ.get(j));
                }
            }
        } else if (kidsCheck && adultsCheck) {
            finalTruthQuestions.addAll(kidsTruthQ);
            for(int j = 0; j < adultsTruthQ.size()-1; j++) {
                if(!finalTruthQuestions.contains(adultsTruthQ.get(j))) {
                    finalTruthQuestions.add(adultsTruthQ.get(j));
                }
            }
        } else if (teensCheck && adultsCheck) {
            finalTruthQuestions.addAll(teensTruthQ);
            for(int j = 0; j < adultsTruthQ.size()-1; j++) {
                if(!finalTruthQuestions.contains(adultsTruthQ.get(j))) {
                    finalTruthQuestions.add(adultsTruthQ.get(j));
                }
            }
        } else if (kidsCheck) {
            finalTruthQuestions.addAll(kidsTruthQ);
        } else if (teensCheck) {
            finalTruthQuestions.addAll(teensTruthQ);
        } else if (adultsCheck) {
            finalTruthQuestions.addAll(adultsTruthQ);
        }
    }
    //Final Dare Questions
    private void setFinalDareQuestions() {
        if (kidsCheck && teensCheck && adultsCheck) {
            finalDareQuestions.addAll(kidsDares);
            for(int j = 0; j < teensDares.size()-1; j++) {
                if(!finalDareQuestions.contains(teensDares.get(j))) {
                    finalDareQuestions.add(teensDares.get(j));
                }
            }
            for(int j = 0; j < adultsDares.size()-1; j++) {
                if(!finalDareQuestions.contains(adultsDares.get(j))) {
                    finalDareQuestions.add(adultsDares.get(j));
                }
            }
        } else if (kidsCheck && teensCheck) {
            finalDareQuestions.addAll(kidsDares);
            for(int j = 0; j < teensDares.size()-1; j++) {
                if(!finalDareQuestions.contains(teensDares.get(j))) {
                    finalDareQuestions.add(teensDares.get(j));
                }
            }
        } else if (kidsCheck && adultsCheck) {
            finalDareQuestions.addAll(kidsDares);
            for(int j = 0; j < adultsDares.size()-1; j++) {
                if(!finalDareQuestions.contains(adultsDares.get(j))) {
                    finalDareQuestions.add(adultsDares.get(j));
                }
            }
        } else if (teensCheck && adultsCheck) {
            finalDareQuestions.addAll(teensDares);
            for(int j = 0; j < adultsDares.size()-1; j++) {
                if(!finalDareQuestions.contains(adultsDares.get(j))) {
                    finalDareQuestions.add(adultsDares.get(j));
                }
            }
        } else if (kidsCheck) {
            finalDareQuestions.addAll(kidsDares);
        } else if (teensCheck) {
            finalDareQuestions.addAll(teensDares);
        } else if (adultsCheck) {
            finalDareQuestions.addAll(adultsDares);
        }
    }
    //----------------------------------------------------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //Randomise the Truth Questions
    private String truthRandomizer() {
        random = new Random();
        int ini1 = random.nextInt(finalTruthQuestions.size());
        return finalTruthQuestions.get(ini1);
    }

    //Randomise the Dare Questions
    private String dareRandomizer(){
        random = new Random();
        int ini2 = random.nextInt(finalDareQuestions.size());
        return finalDareQuestions.get(ini2);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----------------QUESTIONS------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //Truth Questions
    private void setKidsTruthQ() {
        kidsTruthQ.add("Have you ever lied in a Truth or Dare game?");
        kidsTruthQ.add("Who is your crush?");
        kidsTruthQ.add("Why do you like your crush?");
        kidsTruthQ.add("What would you do if you win a lottery of a million dollars?");
        kidsTruthQ.add("If you had one day to live, what would you do?");
        kidsTruthQ.add("What's the strangest dream you've ever had?");
        kidsTruthQ.add("Have you ever kissed a boy/girl?");
        kidsTruthQ.add("Which is your favourite song and what is the reason you like it?");
        kidsTruthQ.add("What superpower would you like to have and why?");
        kidsTruthQ.add("What is the worst thing you have ever done?");
        kidsTruthQ.add("What was your most embarrassing moment?");
        kidsTruthQ.add("Have you ever stolen anything? If yes, then what did you steal and why?");
        kidsTruthQ.add("What is the most annoying habit of each person playing according to you?");
        kidsTruthQ.add("If you were ever stranded on an island, what two people would you have be with you and why?");
        kidsTruthQ.add("Are you afraid of the dark?");
        kidsTruthQ.add("If you were an animal, which one would you like to be and why?");
        kidsTruthQ.add("What is the funniest thing that has happened to you?");
        kidsTruthQ.add("If you were forced to beat someone, who would it be and why?");
        kidsTruthQ.add("Which is your favourite TV show?");
        kidsTruthQ.add("What is your favourite movie?");
        kidsTruthQ.add("Who is your favourite actor and actress?");
        kidsTruthQ.add("What is your favourite color and why?");
        kidsTruthQ.add("When was the last time you wet the bed?");
        kidsTruthQ.add("When was the last time you showered?");
        kidsTruthQ.add("Have you ever started a rumor about someone? What was it?");
        kidsTruthQ.add("What is the last dream you remember?");
        kidsTruthQ.add("What is the strangest dream you ever had?");
        kidsTruthQ.add("Who is your favourite singer?");
        kidsTruthQ.add("Which is your favourite band?");
        kidsTruthQ.add("What is the lowest you have ever scored on a test?");
        kidsTruthQ.add("What would be the perfect date with your crush?");
        kidsTruthQ.add("Did you ever take your friend's property and accidentally broke it and never told him/her about it?");
        kidsTruthQ.add("What was the worst birthday gift you ever got?");
        kidsTruthQ.add("Who is the ugliest person you have dated?");
        kidsTruthQ.add("Who is your best friend? (Name only one)");
        kidsTruthQ.add("What do you like the most about everyone playing the game?");
        kidsTruthQ.add("What are you going to name your kids after you get married?");
        kidsTruthQ.add("If you could change one thing about yourself, what would it be?");
        kidsTruthQ.add("Have you ever said something that you wish you could take back? What was it?");
        kidsTruthQ.add("What is your secret wish?");
        kidsTruthQ.add("What was the worst day of your life?");
        kidsTruthQ.add("What was the best day of your life?");
        kidsTruthQ.add("Do you sing in the shower?");
        kidsTruthQ.add("What is the one question that you don't want anyone to ask you?");
        kidsTruthQ.add("If you could switch places with someone for a day who would it be?");
    }

    private void setTeensTruthQ() {
        teensTruthQ.add("Would you kill someone for money? Who?");
        teensTruthQ.add("Who is your crush?");
        teensTruthQ.add("What parts of your body do you shave?");
        teensTruthQ.add("What is your secret wish?");
        teensTruthQ.add("Explain in detail your dirtiest dream.");
        teensTruthQ.add("Have you ever drank alcohol? Where and when?");
        teensTruthQ.add("Do you sing in the shower?");
        teensTruthQ.add("What was the best day of your life?");
        teensTruthQ.add("What was the worst day of your life?");
        teensTruthQ.add("Have you ever said something that you wish you could take back? What was it?");
        teensTruthQ.add("What is the strangest dream you ever had?");
        teensTruthQ.add("How old were you when you had your first kiss?");
        teensTruthQ.add("Have you ever cheated on your boyfriend/girlfriend? Do you regret it?");
        teensTruthQ.add("Have you ever been cheated on by you boyfriend/girlfriend?");
        teensTruthQ.add("What would be the perfect date with your crush?");
        teensTruthQ.add("If you could go on a date with anyone in the room, who would it be?");
        teensTruthQ.add("What personality traits would cause you to a friendship?");
        teensTruthQ.add("Would you go behind a friend's back with a crush?");
        teensTruthQ.add("What is the one thing about yourself that not even your best friend knows?");
        teensTruthQ.add("Tell something about yourself that you are ashamed to tell anyone.");
        teensTruthQ.add("How long have you gone without showering?");
        teensTruthQ.add("What is the one question that you don't want anyone to ask you?");
        teensTruthQ.add("What do you think your biggest physical flaw is?");
        teensTruthQ.add("What color is your underwear?");
        teensTruthQ.add("Where is the strangest place you've used the bathroom?");
        teensTruthQ.add("Have you ever peed in a swimming pool?");
        teensTruthQ.add("If you couldn't go to the college or get the job of your dreams, what would you do?");
        teensTruthQ.add("What kind of person do you want to marry someday?");
        teensTruthQ.add("Do you want to have kids? How many?");
        teensTruthQ.add("If you could switch places with someone for a day who would it be?");
        teensTruthQ.add("If you could invent anything, what would it be?");
        teensTruthQ.add("If you knew the world was about to end, what would you do?");
        teensTruthQ.add("If you could be born again, who would you come back as?");
        teensTruthQ.add("Are you scared of dying? Why?");
        teensTruthQ.add("What is your biggest fear?");
        teensTruthQ.add("What do you collect that no one knows about?");
        teensTruthQ.add("What is your baby nickname?");
        teensTruthQ.add("What kids' movie do you still secretly watch over and over?");
        teensTruthQ.add("Have you ever danced in front of the mirror naked?");
        teensTruthQ.add("Have you ever pretended to be older or younger than you are to be able to do something? What was it?");
        teensTruthQ.add("Have you ever been in trouble with the police?");
        teensTruthQ.add("What would make you lie for a friend?");
        teensTruthQ.add("What do you think about while sitting on the toilet?");
        teensTruthQ.add("If you could go out on a date with a celebrity, who would it be?");
        teensTruthQ.add("Tell us about your most embarrassing date?");
        teensTruthQ.add("Would you use a dating website?");
        teensTruthQ.add("Are you jealous of someone because of who they date? If yes, who?");
        teensTruthQ.add("How would you want the love of your life to propose to you?");
        teensTruthQ.add("Who is your best friend? (Name only one)");
        teensTruthQ.add("Who was the worst friend you have had and why?");
        teensTruthQ.add("What is the cruelest thing you have done to a friend?");
        teensTruthQ.add("What’s your career ambition?");
        teensTruthQ.add("How confident are you about achieving your dreams?");
        teensTruthQ.add("Did you ever skip school by telling a lie and lie about it to your parents?");
        teensTruthQ.add("Who is the creepiest person you know and why?");
        teensTruthQ.add("Did you pretend to be sick so you can skip class or avoid a test?");
        teensTruthQ.add("If you had the money and resources to start a business, what would it be?");
        teensTruthQ.add("Who is your role model and why?");
        teensTruthQ.add("Imagine this: you wake up one day and realize that you’ve become invisible. What will you do?");
        teensTruthQ.add("What is the one thing you would change in your life if you had the option?");
        teensTruthQ.add("If your parents went out of town for a week and you had the house to yourself, what would you do?");
        teensTruthQ.add("List five disgusting/annoying habits you have?");
        teensTruthQ.add("What is your weight?");
        teensTruthQ.add("Did you let someone else take the fall for something you did? What was it that you did?");
        teensTruthQ.add("What’s the weirdest thing you did when you were alone, in front of a mirror?");
        teensTruthQ.add("What is the silliest thing that you are attached to and still posses (if it were a thing)?");
        teensTruthQ.add("What was the biggest lie you told without getting caught?");
        teensTruthQ.add("Who do you think looks the sexiest in the room?");
        teensTruthQ.add("Could you go a month without junk food?");
        teensTruthQ.add("What do you daydream about the most?");
        teensTruthQ.add("Are you a virgin? If No, who did you loose your virginity to?");
        teensTruthQ.add("Have you ever told one of your best friend’s secrets, even if you said you wouldn’t?");
        teensTruthQ.add("Out of the people playing this game, who would you have sex with?");
        teensTruthQ.add("What is the most expensive thing you own?");
        teensTruthQ.add("Do you have a job? If so, what is your favourite thing about it?");
        teensTruthQ.add("Do you watch porn? If yes, how often?");
        teensTruthQ.add("What is your porn site?");
        teensTruthQ.add("Do you have any membership for a porn site?");
    }

    private void setAdultsTruthQ() {
        adultsTruthQ.add("Are you a virgin? If No, who did you loose your virginity to?");
        adultsTruthQ.add("What is the dirtiest thing you have ever done with someone else?");
        adultsTruthQ.add("Would you kill someone for money? Who?");
        adultsTruthQ.add("Who is your crush?");
        adultsTruthQ.add("Out of the people playing this game, who would you have sex with?");
        adultsTruthQ.add("Do you occasionally sleep naked?");
        adultsTruthQ.add("What parts of your body do you shave?");
        adultsTruthQ.add("What is your secret wish?");
        adultsTruthQ.add("Explain in detail your dirtiest dream.");
        adultsTruthQ.add("Are you an alcoholic? How often do you drink?");
        adultsTruthQ.add("Do you do drugs? Which?");
        adultsTruthQ.add("Have you ever said something that you wish you could take back? What was it?");
        adultsTruthQ.add("What are you going to name your kids after you get married?");
        adultsTruthQ.add("If you could go on a date with anyone in the room, who would it be?");
        adultsTruthQ.add("What is the one thing about yourself that not even your best friend knows?");
        adultsTruthQ.add("Tell something about yourself that you are ashamed to tell anyone.");
        adultsTruthQ.add("What is the one question that you don't want anyone to ask you?");
        adultsTruthQ.add("What is the strangest place you had sex at?");
        adultsTruthQ.add("Do you want to have kids? How many?");
        adultsTruthQ.add("If you could switch places with someone for a day who would it be?");
        adultsTruthQ.add("What is your biggest fear?");
        adultsTruthQ.add("Have you ever been in trouble with the police?");
        adultsTruthQ.add("Have you ever hooked up with someone online?");
        adultsTruthQ.add("Have you ever had a one night stand?");
        adultsTruthQ.add("Do you regret dating someone? If yes, who and why?");
        adultsTruthQ.add("If you had the money and resources to start a business, what would it be?");
        adultsTruthQ.add("Would you ever do a 3-some?");
        adultsTruthQ.add("How do you feel about sex in groups?");
        adultsTruthQ.add("Would you ever watch your partner have sex with someone else?");
        adultsTruthQ.add("Describe how you orgasms feels?");
        adultsTruthQ.add("Have you ever taken it in the butt or would you?");
        adultsTruthQ.add("Do you enjoy rough or slow sex?");
        adultsTruthQ.add("What is your favourite sex position?");
        adultsTruthQ.add("What is your least favourite sex position?");
        adultsTruthQ.add("What is the worst sexual experience you’ve ever had?");
        adultsTruthQ.add("Have you ever paid for sex?");
        adultsTruthQ.add("Have you ever made a video of yourself masturbating or having sex?");
        adultsTruthQ.add("Who is your favourite porn star and why?");
        adultsTruthQ.add("What is your favourite type of porn?");
        adultsTruthQ.add("Have you ever thought about being a stripper?");
        adultsTruthQ.add("Does size matter?");
        adultsTruthQ.add("Do you prefer for your partner to be silent, or do you like moans?");
        adultsTruthQ.add("Do you like a lot of foreplay?");
        adultsTruthQ.add("What is your favourite type of foreplay?");
        adultsTruthQ.add("Have you done role-playing (for sexual purposes)?");
        adultsTruthQ.add("What is your strangest sexual fantasy?");
        adultsTruthQ.add("Would you ever have sex for money?");
        adultsTruthQ.add("How many orgasms have you had in one sexual encounter?");
        adultsTruthQ.add("What is the longest you’ve had sex in one session?");
        adultsTruthQ.add("What is most amount of shots that you have taken in one night?");
        adultsTruthQ.add("Have you ever gotten an STD?");
        adultsTruthQ.add("Have you ever woken up to a stranger in your bed?");
        adultsTruthQ.add("What is the earliest you have ever started drinking in the day?");
        adultsTruthQ.add("Have you had sex at a public place? Where?");
        adultsTruthQ.add("Do you like rough sex or soft?");
        adultsTruthQ.add("How many times can you masturbate in a day?");
        adultsTruthQ.add("What is the most times you have had masturbated in a day?");
        adultsTruthQ.add("Have you ever faked an orgasm? Why?");
        adultsTruthQ.add("Have you ever used a glory hole?");
        adultsTruthQ.add("What is your porn site?");
        adultsTruthQ.add("Do you have any membership for a porn site?");
        adultsTruthQ.add("How many people have you slept with?");
    }

    private void setCustomTruthQ() {

    }

    //Dares
    private void setKidsDares() {
        kidsDares.add("Dance for 30 seconds.");
        kidsDares.add("Sing any song.");
        kidsDares.add("You have 60 seconds, say the alphabets in backward order.");
        kidsDares.add("Do not blink your eyes for a minute.");
        kidsDares.add("Look into girl’s eyes for 30 seconds.");
        kidsDares.add("Hop like a kangaroo for 2 minutes.");
        kidsDares.add("Quack and walk like a duck.");
        kidsDares.add("Stand like a flamingo for 60 seconds.");
        kidsDares.add("Hold your breath for 20 seconds.");
        kidsDares.add("Do anything that will make everyone laugh.");
        kidsDares.add("Smell the feet of someone in the room.");
        kidsDares.add("Act like one of you teacher.");
        kidsDares.add("Try your best at belly dancing.");
        kidsDares.add("Shout out your national anthem, be as loud and clear as you can.");
        kidsDares.add("Pretend that you are an airplane for 60 seconds.");
        kidsDares.add("Get in the shower fully clothed.");
        kidsDares.add("Lick a bar of soap.");
        kidsDares.add("Enact a monkey and a donkey at the same time.");
        kidsDares.add("Go and ring the doorbell of your neighbor’s house, but don’t show up. Do it 5 times!");
        kidsDares.add("Laugh like an evil for 30 seconds.");
        kidsDares.add("Truthfully answer every question asked by each player playing the game (Each player gets to ask two questions).");
        kidsDares.add("Use your teeth to take someone’s sock off.");
        kidsDares.add("Ask your crush out on a date.");
        kidsDares.add("Give person to your left a kiss on the forehead.");
        kidsDares.add("Do a handstand for 30 seconds.");
        kidsDares.add("Sit on the lap of the person on your right for the rest of the game!");
        kidsDares.add("Pour a cup of ice cold or hot water on yourself.");
        kidsDares.add("Have the group do your hair any way they want and you have to keep it like that for the rest of the game.");
        kidsDares.add("Moonwalk across the room.");
        kidsDares.add("Crack an egg on your head.");
        kidsDares.add("Shout your first crush name loudly.");
        kidsDares.add("Tell a bad joke in a really enthusiastic way.");
        kidsDares.add("Go to the neighbour’s house and ask for a banana.");
        kidsDares.add("You must do anything the group says for the next 60 seconds.");
        kidsDares.add("Do 10 jumping jacks, and then do 10 push-ups.");
        kidsDares.add("Act like your favourite Disney person.");
        kidsDares.add("Sit upside down in a chair until your next turn.");
        kidsDares.add("Act like an old lady or an old man.");
        kidsDares.add("Play the game lying on your stomach until your next turn.");
    }

    private void setTeensDares() {
        teensDares.add("You have 60 seconds, say the alphabets in backward order.");
        teensDares.add("Delete your facebook account.");
        teensDares.add("Delete your snapchat account.");
        teensDares.add("Get on your knees and propose to any player as if he/she was the love of your life.");
        teensDares.add("Make a freestyle rap song about each person in the group.");
        teensDares.add("Do a pole dance.");
        teensDares.add("Take off any two items of your clothing for the rest of the game.");
        teensDares.add("Kiss any person in the room, of the opposite sex on the lips. ");
        teensDares.add("Get in the shower fully clothed.");
        teensDares.add("Exchange a clothing item with the player on your right.");
        teensDares.add("Prank call your grandparents and pretend you want to order a pizza.");
        teensDares.add("Act out a any commercial ad.");
        teensDares.add("Kiss the first person that comes in through the door.");
        teensDares.add("Truthfully answer every question asked by each player playing the game (Each player gets to ask two questions).");
        teensDares.add("Use your teeth to take someone’s sock off.");
        teensDares.add("Ask your crush out on a date.");
        teensDares.add("Post a YouTube video of you singing a currently popular song.");
        teensDares.add("Sing instead of speaking for the next two rounds of the game.");
        teensDares.add("Do a handstand for 30 seconds.");
        teensDares.add("Sit on the lap of the person on your right for the rest of the game!");
        teensDares.add("Smell the feet of everyone in the room and rank them from best to worst.");
        teensDares.add("Have the group paint your fingernails any way they want to.");
        teensDares.add("Prank call your Mom.");
        teensDares.add("Go to the kitchen and eat something that is not meant to be eaten alone (like chili powder, pepper, etc).");
        teensDares.add("Have the group do your hair any way they want and you have to keep it like that for the rest of the game.");
        teensDares.add("Go outside and tell the first person you see that you love/hate them.");
        teensDares.add("Moonwalk across the room.");
        teensDares.add("Crack an egg on your head.");
        teensDares.add("Tell a bad joke in a really enthusiastic way.");
        teensDares.add("Do an impression of your favorite celebrity.");
        teensDares.add("Close your eyes and send a blind text to a random person.");
        teensDares.add("Give a 3 minute stand-up comedy routine.");
        teensDares.add("Make up a story about the item to your right.");
        teensDares.add("Go to the neighbour’s house and ask for a banana.");
        teensDares.add("Go up to someone random and ask for a hug.");
        teensDares.add("Make up a short rap about another player.");
        teensDares.add("Do push-ups until it’s your turn again.");
        teensDares.add("Take a selfie with the toilet and post it online.");
        teensDares.add("Twerk for 30 seconds");
        teensDares.add("Pull up your favourite porn site your phone right now.");
        teensDares.add("You must do anything the group says for the next 60 seconds.");
        teensDares.add("Go commando for the rest of the game.");
        teensDares.add("Snort on a line of salt.");
        teensDares.add("Close your eyes, go to the refrigerator and eat whatever you grab.");
        teensDares.add("Do 150 jumping jacks.");
        teensDares.add("Text your crush and tell them how much you like them.");
        teensDares.add("Send an email to one of your teachers, telling them about how your day is going.");
        teensDares.add("Put the socks of the person to your right in your mouth.");
        teensDares.add("Let the group tickle you for 30 seconds.");
        teensDares.add("Prank call a local restaurant and order one of everything on the menu.");
        teensDares.add("Take a selfie with your finger up your nose and post it to all your social media.");
        teensDares.add("Make up a three minute song about the first thing that comes to your mind.");
        teensDares.add("Call and break up with your girlfriend/boyfriend.");
        teensDares.add("French kiss a pillow until it is your turn again.");
        teensDares.add("Take a shower fully clothed.");
        teensDares.add("Play the game lying on your stomach until your next turn.");
    }

    private void setAdultsDares() {
        adultsDares.add("Delete any one of your social accounts.");
        adultsDares.add("Take off all your clothes.");
        adultsDares.add("Act intimate with one of the players for 60 seconds.");
        adultsDares.add("Truthfully answer every question asked by each player playing the game (Each player gets to ask two questions).");
        adultsDares.add("Post a picture of yourself partially naked on all of your social media accounts.");
        adultsDares.add("Drink a shot at the start of every round for the rest of the game.");
        adultsDares.add("Smack the ass of any person in the room and moan.");
        adultsDares.add("Slap your own ass and say something sexual.");
        adultsDares.add("Rank everyone in the room regarding who has the best ass.");
        adultsDares.add("Rank every woman in the room regarding who has the best boobs.");
        adultsDares.add("Prank call someone you know and Propose him/her.");
        adultsDares.add("Strip dance for the player to the right of you.");
        adultsDares.add("Twerk for a minute with only your underwear.");
        adultsDares.add("Act out your sexual fantasy with a pillow.");
        adultsDares.add("Suck on the belly button of any person in the room for 30 seconds.");
        adultsDares.add("Put your hand down on any player's (of the opposite sex) pants until the next round.");
        adultsDares.add("Bite/Kiss the booty of the person to your right/left.");
        adultsDares.add("Blindfold yourself, Spin around for ten seconds, who ever you are pointing to at the end, go in the next room and enjoy 9 minutes of heaven.");
        adultsDares.add("Send a nude to your ex.");
        adultsDares.add("Rub the inner thighs of the any person in the room for the rest of the game.");
        adultsDares.add("You must do anything the group says for the next 60 seconds.");
        adultsDares.add("Give a lap dance to every player.");
        adultsDares.add("Take off the shirt of any player using only yor teeth.");
        adultsDares.add("Switch your underwear with anyone in the room.");
        adultsDares.add("Do an impression of your favourite porn star or act your favourite foreplay scene.");
        adultsDares.add("Give some head with their underwear still on.");
        adultsDares.add("Raise your shirt and play with your nipples.");
        adultsDares.add("Take 2 shots and do a cartwheel.");
        adultsDares.add("Ride the player to your left/right with only your underwear.");
        adultsDares.add("Grab a handful of ice from the refrigerator and put it in your underwear.");
        adultsDares.add("Moan as loud as you can for 60 seconds.");
        adultsDares.add("Show the group your favourite porn video.");
        adultsDares.add("Be somebody's sex slave for the next 60 seconds.");
        adultsDares.add("Invite one of your friends over for threesome.");
        adultsDares.add("Try to do a naked split.");
        adultsDares.add("Take off someone's bra only using your teeth.");
        adultsDares.add("Do an impression of your partner's orgasm (or anyone you've slept with).");
    }

    private void setCustomDares() {

    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

}
