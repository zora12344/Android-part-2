package com.example.ecole;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StudAccueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_accueil);
        Button bltbtn = (Button) findViewById(R.id.bultinbtn);
        Button deconnextion = (Button) findViewById(R.id.deconnexion);
        Intent i = getIntent();
        bltbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudAccueil.this, Bultin.class);
                intent.putExtra("email", i.getStringExtra("email"));
                startActivity(intent);
            }
        });


        deconnextion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudAccueil.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }



}