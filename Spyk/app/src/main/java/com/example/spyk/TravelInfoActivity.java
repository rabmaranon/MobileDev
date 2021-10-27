package com.example.spyk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TravelInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_info);
        Bundle extras = getIntent().getExtras();
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        if (extras != null) {
            String country = extras.getString("countryName");

            RequestQueue queue = Volley.newRequestQueue(TravelInfoActivity.this);
            String url = "https://travelbriefing.org/" + country + "?format=json";
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("------------> Success");
//                            toastMsg(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                TextView countryNameTextView = findViewById(R.id.countryNameSolution);
                                try {
                                    countryNameTextView.setText(jsonObject.getJSONObject("names").getString("name"));
                                } catch (Exception e) {
                                    countryNameTextView.setText("Error getting country name");
                                }

                                TextView currencyTextView = findViewById(R.id.currencyLabelSolution);
                                try {
                                    currencyTextView.setText(jsonObject.getJSONObject("currency").getString("name"));
                                } catch (Exception e) {
                                    currencyTextView.setText("Error getting currency");
                                }

                                TextView timeZone = findViewById(R.id.TimeZoneSolution);
                                try {
                                    timeZone.setText(jsonObject.getJSONObject("timezone").getString("name"));
                                } catch (Exception e) {
                                    timeZone.setText("Error getting time zone");
                                }

                                TextView telephoneCode = findViewById(R.id.telephoneCodeSolution);
                                try {
                                    telephoneCode.setText(jsonObject.getJSONObject("telephone").getString("calling_code"));
                                } catch (Exception e) {
                                    telephoneCode.setText("Error getting telephone code");
                                }

                                TableLayout tableLayout = findViewById(R.id.weatherTable);
                                tableLayout.addView(addToTable("Month", "High temp", "Low temp"));

                                for (String month : months) {
                                    try {
                                        JSONObject weatherData = jsonObject.getJSONObject("weather").getJSONObject(month);
                                        String tMax = weatherData.getString("tMax");
                                        String tMin = weatherData.getString("tMin");
                                        System.out.println("--------------------> tMax =  " + tMax);
                                        double tMaxDouble = Double.parseDouble(tMax);
                                        double tMinDouble = Double.parseDouble(tMin);
                                        if (tMaxDouble < 0 || tMinDouble < 0) {
                                            continue;
                                        }
                                        tableLayout.addView(addToTable(month, tMax, tMin));
                                    } catch (Exception e) {
                                        System.out.println(e);
                                        tableLayout.addView(addToTable(month, "Error", "Error"));
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("----------> Fail");
                    toastMsg("Error " + error);
                }
            });
            toastMsg("Fetching data from the internet");
            queue.add(request);
        }
    }

    public TableRow addToTable(String monthName,
                           String highTemp,
                           String lowTemp) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(monthName.equals("Month") ? 0xFF00FF00 : 0x00000000);
        gd.setCornerRadius(2);
        gd.setStroke(1, 0xFF000000);

        TableRow tr = new TableRow(TravelInfoActivity.this);
        tr.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView monthNameLabel = new TextView(TravelInfoActivity.this);
        monthNameLabel.setBackground(gd);
        monthNameLabel.setText(monthName);
        monthNameLabel.setPadding(10, 5, 5, 5);
        monthNameLabel.setGravity(Gravity.CENTER);

        tr.addView(monthNameLabel);

        TextView highLabel = new TextView(TravelInfoActivity.this);
        int highLabelId = View.generateViewId();
        highLabel.setId(highLabelId);
        highLabel.setText(highTemp);
        highLabel.setPadding(10, 5, 5, 5);
        highLabel.setGravity(Gravity.CENTER);
        highLabel.setBackground(gd);

        tr.addView(highLabel);

        TextView lowLabel = new TextView(TravelInfoActivity.this);
        lowLabel.setText(lowTemp);
        lowLabel.setPadding(10, 5, 5, 5);
        lowLabel.setGravity(Gravity.CENTER);
        lowLabel.setBackground(gd);
        if (monthName.equals("Month")) {
            monthNameLabel.setTypeface(null, Typeface.BOLD);
            lowLabel.setTypeface(null, Typeface.BOLD);
            highLabel.setTypeface(null, Typeface.BOLD);
        }

        tr.addView(lowLabel);
        return tr;
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}