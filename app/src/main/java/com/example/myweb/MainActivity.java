package com.example.myweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button searchButton;
    TextView result;
    DatabaseHelper db;

    public void logout(View view) {
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();

        Intent LoginPage = new Intent(MainActivity.this,LoginActivity.class);
        LoginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(LoginPage);
    }


    class  Weather extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... address) {

            try {
                URL url = new URL(address[0]);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char)data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search(View view){

        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);


        String cName = cityName.getText().toString().trim();

        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+ cName +"&appid=b6907d289e10d714a6e88b30761fae22").get();

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
            JSONArray array = new JSONArray(weatherData);

            String main= "";
            String description = "";
            String temperature = "";

            for(int i=0;i<array.length();i++){
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");
            Log.i("Temperatute : ",temperature);
//            Log.i("main",main);
//            Log.i("description",description);

            result.setText("Main : "+main+"\nDescription : "+description+"\nTemp : "+temperature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        int userId = sessionManagement.getSession();

        TextView user;
        user = findViewById(R.id.uname);
        db = new DatabaseHelper(this);
        String uName = db.getUserName(userId);
        user.setText("Hii  "+uName);
    }
}
