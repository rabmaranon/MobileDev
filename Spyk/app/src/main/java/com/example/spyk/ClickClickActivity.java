package com.example.spyk;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ClickClickActivity extends AppCompatActivity implements View.OnTouchListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_click);
        Button buttonA = findViewById(R.id.buttonA);
        Button buttonB = findViewById(R.id.buttonB);
        Button buttonC = findViewById(R.id.buttonC);
        Button buttonD = findViewById(R.id.buttonD);
        Button buttonE = findViewById(R.id.buttonE);
        Button buttonF = findViewById(R.id.buttonF);
        buttonA.setOnTouchListener(this);
        buttonB.setOnTouchListener(this);
        buttonC.setOnTouchListener(this);
        buttonD.setOnTouchListener(this);
        buttonE.setOnTouchListener(this);
        buttonF.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        TextView textView = findViewById(R.id.textView);
        if (v instanceof Button) {
            Button button = (Button) v;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    textView.setText("Pressed: " + button.getText().toString());
                    return true;
                case MotionEvent.ACTION_UP:
                    textView.setText("Pressed: -");
                    return true;
            }
        }
        return false;
    }
}