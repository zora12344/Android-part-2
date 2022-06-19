package com.example.ecole;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNote extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newnote);
        AutoCompleteTextView ac_classe = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        AutoCompleteTextView ac_etudiant = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewForStudent);
        AutoCompleteTextView ac_matiere = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewForMatiere);
        EditText note = (EditText) findViewById(R.id.note);
        Button savebtn = (Button) findViewById(R.id.savebtn);
        remplirClasse(ac_classe);
        ac_etudiant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!ac_classe.getText().toString().trim().isEmpty() ) {
                    String className = ac_classe.getText().toString();
                    remplirEtudiant(className,ac_etudiant);

                }
            }
        });

        ac_matiere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!ac_etudiant.getText().toString().trim().isEmpty() ) {
                   ;
                    remplirMatiere(ac_matiere);

                }
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = note.getText().toString();
                String classe = ac_classe.getText().toString();
                String etudiant = ac_etudiant.getText().toString();
                String matiere = ac_matiere.getText().toString();
                if(!n.isEmpty() && !etudiant.isEmpty() && !classe.isEmpty() && !matiere.isEmpty()){
                    processSave(matiere,etudiant,n);
                }else{
                    System.out.println("Attention vous devez remplir tout les champs");
                }


            }
        });


    }

    private void processSave(String matiere, String etudiant, String note){


        OkHttpClient client = new OkHttpClient();
        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        System.out.println("http://172.20.10.5:8080/etudiants/addstudentnote/"+etudiant+"/"+matiere+"/"+note);
        Request request = new Request.Builder()
                .url("http://172.20.10.5:8080/etudiants/addstudentnote/"+etudiant+"/"+matiere+"/"+note)
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
                    Intent intent = new Intent(AddNote.this, ProfAccueil.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    private void remplirEtudiant(String classe,AutoCompleteTextView ac){
        Context ctx = this;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://172.20.10.5:8080/etudiants/etudiants/classes/"+classe)
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
                    List<String> res = new ArrayList<>();
                    try{
                        JSONArray jsonArrayRoot = new JSONArray(data);

                        for (int cpt = 0; cpt<jsonArrayRoot.length(); cpt++ ){
                            JSONObject jsonObject = jsonArrayRoot.getJSONObject(cpt);
                            res.add(jsonObject.getString("nom") + " "+jsonObject.getString("prenom")) ;
                        }
                    }catch(JSONException js){
                        js.printStackTrace();
                    }
                    for(int c=0 ; c<res.size(); c++){
                        System.out.println("''''''''''"+res.get(c));
                    }
                    ArrayAdapter<String> arr;
                    arr
                            = new ArrayAdapter<>(ctx
                            ,
                            R.layout.support_simple_spinner_dropdown_item,
                            res);

                    AddNote.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // This code will always run on the UI thread, therefore is safe to modify UI elements.
                            ac.setAdapter(arr);
                        }
                    });


                }
            }
        });


    }

    private void remplirMatiere(AutoCompleteTextView ac){
        Context ctx = this;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://172.20.10.5:8080/etudiants/matieres")
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
                    data =data.replace("[","");
                    data =data.replace("]","");
                    data = data.replace("\"","");
                    String[] res = data.split(",");
                    ArrayAdapter<String> arr;
                    arr
                            = new ArrayAdapter<>(ctx
                            ,
                            R.layout.support_simple_spinner_dropdown_item,
                            res);

                    AddNote.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // This code will always run on the UI thread, therefore is safe to modify UI elements.
                            ac.setAdapter(arr);
                        }
                    });


                }
            }
        });


    }

    private void remplirClasse(AutoCompleteTextView ac){
        Context ctx = this;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://172.20.10.5:8080/etudiants/classes")
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
                    data =data.replace("[","");
                    data =data.replace("]","");
                    data = data.replace("\"","");
                    String[] res = data.split(",");
                    System.out.println("##################"+data);
                    ArrayAdapter<String> arr;
                    arr
                            = new ArrayAdapter<>(ctx
                            ,
                            R.layout.support_simple_spinner_dropdown_item,
                            res);

                    AddNote.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // This code will always run on the UI thread, therefore is safe to modify UI elements.
                            ac.setAdapter(arr);
                        }
                    });
                }
            }
        });

    }



}