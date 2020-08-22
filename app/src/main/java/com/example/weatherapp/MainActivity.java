package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    // Variable
    TextView InputCityName;
    TextView CityName;
    TextView Clouds;
    TextView temp;
    ImageView tempImage;
    String description;
    String Main;
    String Temp;
    //private static final String myAPIKey = "2fd0e6c8f6f7b73b173534e925d4ab67";


    // For View The Web Content
    @SuppressLint("StaticFieldLeak")
    public class DownloadWebContent extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        //End

        // Use The JSON For Record The Weather
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                String weatherData = object.getString("weather");
                String mainTemp = object.getString("main");
                Log.i("Weather = ", weatherData);

                // weatherData define Array.
                JSONArray array = new JSONArray(weatherData);
                for (int i=0; i<array.length(); i++){
                    JSONObject weatherPart = array.getJSONObject(i);
                    description = weatherPart.getString("id");
                    Main = weatherPart.getString("main");
                }
                // mainTemp define Object.
                JSONObject mainPart = new JSONObject(mainTemp);
                Temp = mainPart.getString("temp");


                CityName.setText(InputCityName.getText().toString());
                Clouds.setText(Main);
                temp.setText(Temp+"*C");
                tempImage.setImageResource(R.drawable.rain_cloud);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    // End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputCityName = (TextView) findViewById(R.id.InputCityName);
        CityName =(TextView) findViewById(R.id.CityName);
        Clouds = (TextView) findViewById(R.id.clouds);
        temp = (TextView) findViewById(R.id.temp);
        tempImage = (ImageView) findViewById(R.id.tempImage);
    }


    // When user Click the Search Button
    public void showWeather(View view) {

        // When Button Click KeyBoard Hide
        InputMethodManager mrg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mrg.hideSoftInputFromWindow(CityName.getWindowToken(), 0);
        //End

        try {
            String encodedCityName = URLEncoder.encode(InputCityName.getText().toString(), "UTF-8");

            DownloadWebContent downloadWebContent = new DownloadWebContent();
            downloadWebContent.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}