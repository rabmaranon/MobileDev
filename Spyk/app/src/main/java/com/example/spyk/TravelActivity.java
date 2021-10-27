package com.example.spyk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TravelActivity extends AppCompatActivity {

    Spinner travelSpinner = null;
    List<String> selectedCountry = new LinkedList<>();
    final List<String> countriesList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        countriesList.add("Select country");
        travelSpinner = findViewById(R.id.countriesSpinner);
        travelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedCountry.clear();
                    selectedCountry.add(countriesList.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // https://www.youtube.com/watch?v=xPi-z3nOcn8
        String url = "https://travelbriefing.org/countries.json";

        RequestQueue queue = Volley.newRequestQueue(TravelActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("------------> Success");
//                        toastMsg(response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                System.out.println("----> Adding to countries list");
                                countriesList.add(jsonObject.getString("name"));
                            }
                            updateSpinnerAdapter();
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
        updateSpinnerAdapter();
        Button travelInfoButton = findViewById(R.id.travelDetailedInfoButton);
        travelInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCountry.size() == 0 || selectedCountry.get(0).equalsIgnoreCase("Select country")) {
                    toastMsg("Please select a valid country");
                    return;
                }
                Intent intent = new Intent(TravelActivity.this, TravelInfoActivity.class);
                intent.putExtra("countryName", selectedCountry.get(0));
                startActivity(intent);
            }
        });
    }

    public void updateSpinnerAdapter() {
        String[] countriesArray = countriesList.stream().toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,
                    countriesArray);
        System.out.println("Countries array size = " + countriesArray.length);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (travelSpinner != null) {
            // https://www.tutlane.com/tutorial/android/android-spinner-dropdown-list-with-examples
            travelSpinner.setAdapter(adapter);
        } else {
            System.out.println("---------> Travel spinner is null");
        }
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

//    public boolean isPermissionGranted() {
//        System.out.println("-----------> returning " + (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)  == PackageManager.PERMISSION_GRANTED));
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
//                == PackageManager.PERMISSION_GRANTED;
//    }
}