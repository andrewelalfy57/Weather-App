package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

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

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
TextView cityName ;
Button searchButton;
TextView result;


    class Weather extends AsyncTask<String,Void,String> {

        protected String doInBackground(String... address) {

    try {
        URL url= new URL(address[0]);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.connect();

        InputStream is =connection.getInputStream();
        InputStreamReader isr=new InputStreamReader(is);

        int data=isr.read();
        String content="";

        char ch;
        while (data!=-1){
            ch=(char) data;
            content=content+ch;
            data =isr.read();
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
        cityName=findViewById(R.id.cityName);
        searchButton=findViewById(R.id.searchButton);
        result=findViewById(R.id.result);
        String cName = cityName.getText().toString();


         String content;
         Weather weather = new Weather();
         try {
             content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+cName+"&appid=b6907d289e10d714a6e88b30761fae22").get();

             Log.i ("contentData",content);

             JSONObject jsonObject = new JSONObject(content);
             String weatherData= jsonObject.getString("weather");
             String maintemperature=jsonObject.getString("main");
             double visibility;

//             Log.i("weatherData",weatherData);
             JSONArray array= new JSONArray(weatherData);
             String main="";
             String description="";
             String temperature="";
             for (int i=0;i<array.length();i++){
                 JSONObject weatherPart= array.getJSONObject(i);
                 main=weatherPart.getString("main");
                 description=weatherPart.getString("description");
             }
             JSONObject mainPart = new JSONObject(maintemperature);
            temperature=mainPart.getString("temp");
  visibility=Double.parseDouble(jsonObject.getString("visibility"));
//             Log.i("main",main);

            int visibilityInKilometer =(int) visibility/1000;
             Log.i("Temperature",temperature);
//             Log.i("description",description);

            String resultText="Main : "+ main+"\nDescription : "+ description+"\nTemperature : "+temperature+"\nVisibility : "+visibilityInKilometer +"k";
             result.setText(resultText);
         }
         catch(Exception e){
             e.printStackTrace();
         }

     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
