package com.example.ecole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Bultin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int textSize = 20;
        int wid = 350;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bultins);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_data_view);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setMinimumWidth(1200);
        tbrow0.setBackground(getResources().getDrawable(R.color.black));
        TextView tv0 = new TextView(this);
        tv0.setText("Matiere");
        tv0.setWidth(wid);
        tv0.setPadding(0,0,20,0);
        tv0.setTextSize(textSize);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);

        TextView tv2 = new TextView(this);
        tv2.setText("Coef");
        tv2.setWidth(wid);
        tv2.setPadding(0,10,20,0);
        tv2.setTextSize(textSize);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("Note");
        tv3.setWidth(wid);
        tv3.setPadding(0,10,20,0);
        tv3.setTextSize(textSize);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3);
        Intent i = getIntent();
        String email = i.getStringExtra("email");

        tableLayout.addView(tbrow0);
        loaddata(tableLayout, email);


    }

    protected void loaddata(TableLayout tableLayout, String email){
        //gethtpdataForOneStudent(tableLayout);
        gethtpdataFormanyStudents(tableLayout, email);

    }



    void gethtpdataFormanyStudents(TableLayout tableLayout, String email) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noCache()
                .build())
                .url("http://172.20.10.5:8080/etudiants/etudiant/"+email)
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
                    System.out.println("req = http://172.20.10.5:8080/etudiants/etudiant/"+email);
                    System.out.println("#################"+data);
                    remplirdataForManyStudents( data,  tableLayout);
                    client.dispatcher().executorService().shutdown();
                    client.connectionPool().evictAll();


                }
            }
        });
    }


    private void remplirdataForManyStudents(String data, TableLayout tableLayout){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray listEtudiantMatiere = jsonObject.getJSONArray("list_matiere");
            double somenoteCoef = 0;
            double somecoef = 0;
            for(int i=0 ; i<listEtudiantMatiere.length() ; i++){
                JSONObject matiereEtudiant = listEtudiantMatiere.getJSONObject(i);
                JSONObject extractinfomatiere = matiereEtudiant.getJSONObject("matiere");
                String note = matiereEtudiant.getString("note");
                String idEtudiantMatiere = matiereEtudiant.getString("id");
                String idEtudiant = jsonObject.getString("id");
                String matierename = extractinfomatiere.getString("nom");
                String coef = extractinfomatiere.getString("coef");
                somecoef += Double.parseDouble(coef);
                somenoteCoef += (Double.parseDouble(note)*Double.parseDouble(coef));
                remplirdata(matierename,note,coef,tableLayout,idEtudiant,matierename,idEtudiantMatiere);
                }
            TextView tv = (TextView) findViewById(R.id.resultat);
            tv.setText("Moyenne general = "+String.format("%.2f", (somenoteCoef/somecoef)));



        }catch(JSONException jaex){
            jaex.printStackTrace();
        }
    }

    private void remplirdata(String data1, String data2, String data3, TableLayout tableLayout, String idEtudiant, String matiere, String idMatiereEtudiant ){
        int textSize = 20;
        int wid = 350;

        TableRow tbrow1 = new TableRow(this);
        tbrow1.setMinimumWidth(1200);
        tbrow1.setBackground(getResources().getDrawable(R.color.white));
        TextView tv3 = new TextView(this);
        tv3.setText(data1);
        tv3.setWidth(wid);
        tv3.setPadding(0,0,20,0);
        tv3.setTextSize(textSize);
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.CENTER);
        tbrow1.addView(tv3);

        TextView tv5 = new TextView(this);
        tv5.setText(data3);
        tv5.setWidth(wid);
        tv5.setPadding(0,10,20,0);
        tv5.setTextSize(textSize);
        tv5.setTextColor(Color.BLACK);
        tv5.setGravity(Gravity.CENTER);
        tbrow1.addView(tv5);

        TextView tv6 = new TextView(this);
        tv6.setText(data2);
        tv6.setWidth(wid);
        tv6.setPadding(0,10,20,0);
        tv6.setTextSize(textSize);
        tv6.setTextColor(Color.BLACK);
        tv6.setGravity(Gravity.CENTER);
        tbrow1.addView(tv6);


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                tableLayout.addView(tbrow1);
            }
        });

    }

}