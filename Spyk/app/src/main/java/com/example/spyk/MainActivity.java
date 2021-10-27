package com.example.spyk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutMeButton = findViewById(R.id.aboutMeButton);
        final ImageView initialImage = findViewById(R.id.initialImage);
        aboutMeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toastMsg("Rachelle Angeli Marañon\nmaranon.r@northeastern.edu");
                initialImage.setVisibility(View.VISIBLE);
            }
        });

        Button clickyClickButton = findViewById(R.id.clickyClickButton);
        clickyClickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClickClickActivity.class);
                startActivity(intent);
            }
        });

        Button linkCollectorButton = findViewById(R.id.linkCollector);
        linkCollectorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, LinkCollectorActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button locationCollectorButton = findViewById(R.id.locationCollectorButton);
        locationCollectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocatorActivity.class);
                startActivity(intent);
            }
        });

        Button travelButton = findViewById(R.id.travelButton);
        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TravelActivity.class);
                startActivity(intent);
            }
        });
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}