package com.example.ecole;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;

import java.io.IOException;

public class UpdateNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_update_note);
        Intent i = getIntent();
        TextView nom = (TextView) findViewById(R.id.nom);
        TextView prenom = (TextView) findViewById(R.id.prenom);
        TextView note = (TextView) findViewById(R.id.note);
        filldata(i, nom, prenom, note);
        Button updatenotebtn = (Button) findViewById(R.id.updatebtn);
        Button deletenotebtn = (Button) findViewById(R.id.deletebtn);

        updatenotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processupdate(i,note.getText().toString());
            }
        });

        deletenotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processdelete(i);
            }
        });

    }




    private void filldata(Intent i, TextView t1, TextView t2, TextView t3){

        String nom = i.getStringExtra("nom");
        String prenom = i.getStringExtra("prenom");
        String note = i.getStringExtra("note");
        t1.setText(nom);
        t1.setEnabled(false);
        t2.setText(prenom);
        t2.setEnabled(false);
        t3.setText(note);
    }

    private void processdelete(Intent i){
        String idEtudiantMatiere = i.getStringExtra("idMatiereEtudiant");
        OkHttpClient client = new OkHttpClient();
        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        String req = "http://172.20.10.5:8080/etudiants/deletestudentnote/"+idEtudiantMatiere;
        System.out.println(req);
        Request request = new Request.Builder()
                .url(req)
                .method("DELETE",reqbody).header("Content-Length", "0")
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
                    client.dispatcher().executorService().shutdown();
                    client.connectionPool().evictAll();

                    Intent intent = new Intent(UpdateNote.this, ProfAccueil.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });

    }


    private void processupdate(Intent i, String note){
        String idEtudiant = i.getStringExtra("idEtudiant");
        String matiere = i.getStringExtra("matiere");
        OkHttpClient client = new OkHttpClient();
        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        System.out.println("http://172.20.10.5:8080/etudiants/updatestudentnote/"+idEtudiant+"/"+matiere+"/"+note);
        Request request = new Request.Builder()
                .url("http://172.20.10.5:8080/etudiants/updatestudentnote/"+idEtudiant+"/"+matiere+"/"+note)
                .method("PUT",reqbody).header("Content-Length", "0")
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

                    client.dispatcher().executorService().shutdown();
                    client.connectionPool().evictAll();
                    Intent intent = new Intent(UpdateNote.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
    }




}