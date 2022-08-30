package com.example.asaan4kissan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import com.example.asaan4kissan.databinding.ActivityMain4Binding;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity4 extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    Button get;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "92b84a906538b0f088cc36b6bc7aafba";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        get = findViewById(R.id.btnGet);
        AdView adView = (AdView)findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempUrl = "";
                String city = etCity.getText().toString().trim();
                String country = etCountry.getText().toString().trim();
                if(city.equals("")){
                    tvResult.setText("City field can not be empty!");
                }else{
                    if(!country.equals("")){
                        tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
                    }else{
                        tempUrl = url + "?q=" + city + "&appid=" + appid;
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String output = "";
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                String description = jsonObjectWeather.getString("description");
                                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                                float pressure = jsonObjectMain.getInt("pressure");
                                int humidity = jsonObjectMain.getInt("humidity");
                                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");
                                JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                                String clouds = jsonObjectClouds.getString("all");
                                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                                String countryName = jsonObjectSys.getString("country");
                                String cityName = jsonResponse.getString("name");
                               // tvResult.setTextColor(Color.rgb(68,134,199));
                                output += "Current weather of " + cityName + " (" + countryName + ")" + "\n"
                                        + "\n Temp: " + df.format(temp) + " °C" +"\n"
                                        + "\n Feels Like: " + df.format(feelsLike) + " °C"+"\n"
                                        + "\n Humidity: " + humidity + "%"+"\n"
                                        + "\n Description: " + description+"\n"
                                        + "\n Wind Speed: " + wind + "m/s (meters per second)"+"\n"
                                        + "\n Cloudiness: " + clouds + "%"+"\n"
                                        + "\n Pressure: " + pressure + " hPa"+"\n";
                                tvResult.setText(output);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

    }

}