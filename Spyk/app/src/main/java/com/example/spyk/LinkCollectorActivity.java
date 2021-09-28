package com.example.spyk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkCollectorActivity extends AppCompatActivity {

    private FloatingActionButton button;
    private ArrayList<String> list = null;
    private EditText editTextName;
    private EditText editTextUrl;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Button buttonAdd;
    private ConstraintLayout layout;
    private final String DATA = "DATA";
    private Map<String, String> map = new HashMap<>();

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList(DATA, list);
        for (String key : map.keySet()) {
            savedInstanceState.putString(key, map.get(key));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);
        editTextName = findViewById(R.id.editTextName);
        editTextUrl = findViewById(R.id.editTextUrl);
        button = findViewById(R.id.floatingButton);
        listView = findViewById(R.id.listView);
        buttonAdd = findViewById(R.id.buttonAdd);
        layout = findViewById(R.id.rootLayout);
        if (savedInstanceState != null && savedInstanceState.containsKey(DATA)) {
            list = savedInstanceState.getStringArrayList(DATA);
            for (String element : list) {
                map.put(element, savedInstanceState.getString(element));
            }
        } else {
            list = new ArrayList<>();
            map = new HashMap<>();
        }

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setVisibility(View.VISIBLE);
                editTextUrl.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.VISIBLE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                String url = map.get(item);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText() == null || editTextName.getText().length() == 0) {
                    displaySnackBar("Invalid URL name");
                    return;
                }
                if (editTextUrl.getText() == null || editTextUrl.getText().length() == 0) {
                    displaySnackBar("Invalid URL");
                    return;
                }
                try {
                    URL url = new URL(editTextUrl.getText().toString());
                } catch (Exception e) {
                    displaySnackBar("Invalid URL");
                    return;
                }
                list.add(editTextName.getText().toString());
                map.put(editTextName.getText().toString(), editTextUrl.getText().toString());
                adapter.notifyDataSetChanged();
                editTextName.setVisibility(View.INVISIBLE);
                editTextUrl.setVisibility(View.INVISIBLE);
                buttonAdd.setVisibility(View.INVISIBLE);
                editTextName.setText("");
                editTextUrl.setText("http://");
                displaySnackBar("URL Added successfully");
            }
        });
    }

    private void displaySnackBar(String msg) {
        Snackbar snackbar = Snackbar
                .make(layout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}