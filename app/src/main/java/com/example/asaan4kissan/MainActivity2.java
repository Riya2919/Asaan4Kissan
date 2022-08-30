package com.example.asaan4kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    Button aboutus;
    ImageView weather,cropcam,sos,cropview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        aboutus = findViewById(R.id.button4);
        weather = findViewById(R.id.imageView7);
        cropcam = findViewById(R.id.imageView8);
        sos = findViewById(R.id.imageView9);
        cropview = findViewById(R.id.imageView6);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(i);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity4.class);
                startActivity(i);
            }
        });
        cropcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity5.class);
                startActivity(i);
            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(Intent.ACTION_DIAL);
              i.setData(Uri.parse("tel:+91100")); //this number can be rendered according to the area
              startActivity(i);
            }
        });
        cropview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://demo.digifarm.io/punjab_india#15.5/30.931175/76.658004");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}