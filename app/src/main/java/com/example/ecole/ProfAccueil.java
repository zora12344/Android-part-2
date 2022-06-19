package com.example.ecole;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProfAccueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_accueil);
        Button ajoutbtn = (Button) findViewById(R.id.ajoutbtn);
        Button listNote = (Button) findViewById(R.id.listNote);
        Button deconnextion = (Button) findViewById(R.id.deconnexion);

        ajoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfAccueil.this, AddNote.class);
                startActivity(intent);
            }
        });

        listNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfAccueil.this, ListClasses.class);
                startActivity(intent);
            }
        });

        deconnextion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfAccueil.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }



}