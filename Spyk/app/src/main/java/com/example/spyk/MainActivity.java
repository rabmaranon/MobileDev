package com.example.spyk;

import androidx.appcompat.app.AppCompatActivity;

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
                toastMsg("Hello! It's me!");
                initialImage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}