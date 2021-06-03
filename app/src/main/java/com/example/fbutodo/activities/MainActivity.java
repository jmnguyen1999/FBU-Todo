package com.example.fbutodo.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbutodo.R;
import com.example.fbutodo.adapters.ItemsAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_ITEM = "itemToEdit";
    private static final String KEY_ITEM_PLACE = "itemPlace";

    private static final int EDIT_REQUEST = 1234;

    //Declare Views:
    private RecyclerView rvItems;
    private Button btnAdd;
    private EditText etItem;

    private ItemsAdapter adapter;
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSavedItems();

        //1.) Connect listeners:
        rvItems = findViewById(R.id.rvItems);
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItems);
        ItemsAdapter.OnClickListener clickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onClick(String item, int position) {
                //When clicked --> go to EditActivity:
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM, item);
                i.putExtra(KEY_ITEM_PLACE, position);
                startActivityForResult(i, EDIT_REQUEST);
            }

            @Override
            public void onLongClick(int position) {
                //when clicked --> delete it:
                items.remove(position);
                adapter.notifyDataSetChanged();
                saveItems();
            }
        };

        adapter = new ItemsAdapter(items, clickListener);

        //2,) Set up Recycler View:
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //3.) Set up clickListener for add button:
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.) Add the input if there is one
                String input = etItem.getText().toString();
                if(!input.equals("")){
                    items.add(input);
                    adapter.notifyDataSetChanged();

                    //2.) clear the edit text:
                    etItem.setText("");
                    saveItems();
                }
            }
        });

    }

    @Override
    //Purpose:          To handle the data whenever EditActivity is done editing it's item
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //As long as this is the request we are expecting + result was OK --> save it
        if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            int position = data.getIntExtra(KEY_ITEM_PLACE, -1);
            String newItem = data.getStringExtra(KEY_ITEM);

            //Remove whatever was there, add new item back in, notify adapter
            items.remove(position);
            items.add(position, newItem);
            adapter.notifyDataSetChanged();
            saveItems();
        }
    }

    //Purpose:      Get the file in the files directory for this app.
    private File getSavedFile(){
        return new File(getFilesDir(), "data.txt");
    }
    private void loadSavedItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getSavedFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Toast.makeText(this, "Could not load saved items!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getSavedFile():  error=" + e.getLocalizedMessage());
            items = new ArrayList<>();
        }
    }

    //Purpose:          Saves the model, items, by writing all items in it into the saved file.
    private void saveItems(){
        try {
            FileUtils.writeLines(getSavedFile(), items);
        } catch (IOException e) {
            Toast.makeText(this, "Could not load saved items!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "saveItems():  error=" + e.getLocalizedMessage());
        }
    }
}