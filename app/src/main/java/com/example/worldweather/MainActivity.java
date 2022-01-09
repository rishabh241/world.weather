package com.example.worldweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView weatherResult;
    public void getWeather(View view){
        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=ecbdb36d5d352d218a820a00ff114dc8");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String...urls) {
            String result = "";
           try {
               URL url = new URL(urls[0]);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               InputStream in = connection.getInputStream();
               InputStreamReader reader = new InputStreamReader(in);
               int data = reader.read();
               while(data!=-1){
                   char current = (char) data;
                   result+=current;
                   data= reader.read();
               }return result;
           }catch(Exception e){
               e.printStackTrace();

               return null;
           }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String tempInfo = jsonObject.getString("main");
                JSONArray jsonArray = new JSONArray(weatherInfo);
                String message="";
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String main = jsonObject1.getString("main");
                    String description = jsonObject1.getString("description");
                    message+=main+" : "+description+"\r\n";

                }
                JSONObject jsonObject2= new JSONObject(tempInfo);
                String temp1 = jsonObject2.getString("temp");
                String humid = jsonObject2.getString("humidity");
                Double temp= Double.parseDouble(temp1);
                temp-=273.15;
                String string = String.format("%.2f",temp);
                weatherResult.setText(message+"Temp: "+string+"°C"+"\r\n"+"Humidity: "+humid+"%");

            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherResult = findViewById(R.id.textView2);
        editText = findViewById(R.id.editTextTextPersonName);


    }
}