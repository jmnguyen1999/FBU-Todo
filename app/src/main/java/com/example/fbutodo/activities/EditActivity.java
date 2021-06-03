package com.example.fbutodo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fbutodo.R;

public class EditActivity extends AppCompatActivity {
    private static final String KEY_ITEM = "itemToEdit";
    private static final String KEY_ITEM_PLACE = "itemPlace";

    EditText etNewItem;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //1.) Connect views:
        etNewItem = findViewById(R.id.etNewItem);
        btnDone = findViewById(R.id.btnDone);
        String itemToEdit = getIntent().getStringExtra(KEY_ITEM);
        int itemPosition = getIntent().getIntExtra(KEY_ITEM_PLACE, -1);

        etNewItem.setText(itemToEdit);      //auto-populate the item into there
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send the edited item back to the MainActivity:
                Intent sendBack = new Intent();
                sendBack.putExtra(KEY_ITEM, etNewItem.getText().toString());
                sendBack.putExtra(KEY_ITEM_PLACE, itemPosition);
                setResult(RESULT_OK, sendBack);
                finish();       //exit out of the activity
            }
        });
    }
}