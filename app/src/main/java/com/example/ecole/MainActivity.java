package com.example.ecole;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean isStudent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button savebtn = (Button) findViewById(R.id.connexion);
        EditText login = (EditText) findViewById(R.id.login);
        EditText password = (EditText) findViewById(R.id.password);
        Spinner sp = (Spinner)  findViewById(R.id.planets_spinner);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<>(this
                ,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.planets_array));
        sp.setAdapter(arr);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l = login.getText().toString();
                String p = password.getText().toString();
                String role = sp.getSelectedItem().toString();
                if(!l.isEmpty() && !p.isEmpty() && !role.isEmpty()){
                    processConnexion(l,p, role);
                }


            }
        });
    }

    private void processConnexion(String l, String p, String role) {
        String req = "";

        if(role.equals("Etudiant")){
            isStudent =  true;
            req = "http://172.20.10.5:8080/etudiants/authentification/student/"+l+"/"+p;
        }else{
            req = "http://172.20.10.5:8080/etudiants/authentification/prof/"+l+"/"+p;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(req)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String data = response.body().string();
                    if(data.isEmpty()){
                       System.out.println("Erreur de connexion");
                    }else{
                        if(isStudent){
                            Intent intent = new Intent(MainActivity.this, StudAccueil.class);
                            intent.putExtra("email", l);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, ProfAccueil.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }


}