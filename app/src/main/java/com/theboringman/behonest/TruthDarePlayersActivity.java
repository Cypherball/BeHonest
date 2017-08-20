package com.theboringman.behonest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
            Intent intent = new Intent(TruthDarePlayersActivity.this, TruthDareSelectActivity.class);
            startActivity(intent);
        }
    }
}
