package com.example.ecole;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ListClasses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_classe);
        ListView listView = (ListView)findViewById(R.id.listView);
        remplirListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String classe = item.toString();
                Intent intent = new Intent(ListClasses.this, ListEtudiantsNote.class);
                intent.putExtra("classename",classe);
                startActivity(intent);
            }
        });


    }

    private void remplirListView(ListView lv){
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

                    ListClasses.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // This code will always run on the UI thread, therefore is safe to modify UI elements.
                            lv.setAdapter(arr);
                        }
                    });
                }
            }
        });
    }



}