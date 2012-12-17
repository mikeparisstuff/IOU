package com.paris.IOU;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/6/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OwedScreen extends ListActivity {

    private OwedsDataSource datasource;

    private ArrayList<HashMap<String, String>> owedList;
    private Button newButton;
    private SimpleAdapter adapter;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owed_screen);

        datasource = new OwedsDataSource(this);

        try {
            datasource.open();
        }
        catch ( SQLException e) {
            e.printStackTrace();
        }


        List<Owed> oweds = datasource.getAllOwed();

        owedList = new ArrayList<HashMap<String, String>>();

        //get UI elements by ID
        newButton = (Button) findViewById(R.id.new_owed_button);

        newButton.setOnClickListener( new View.OnClickListener() {
            Owed owed = null;
            @Override
            public void onClick(View v) {
                //addOwedToList();
                Intent i = new Intent(OwedScreen.this, NewOwedScreen.class);
                startActivity(i);
            }
        });

        //make list adapter to tie data from DB to listView
        adapter = new SimpleAdapter(
                this, owedList,  R.layout.owed_row,
                new String[] {"name", "owedAmount"},
                new int[] { R.id.owed_name, R.id.owed_amount}
                );

        populateList(oweds);

        setListAdapter(adapter);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            Owed owed = datasource.createOwed(extras.getString("name"),
                    extras.getDouble("amount"));
            addOwed(owed.getName(), owed.getOwedAmount());
            adapter.notifyDataSetChanged();
        }
    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            Owed owed = datasource.createOwed(extras.getString("name"),
                    extras.getDouble("amount"));
            addOwed(owed.getName(), owed.getOwedAmount());
            adapter.notifyDataSetChanged();
        }
    }

    private void addOwedToList() {
        Owed owed = null;
        //Still need to implement a way to update the information
        //personally

        owed = datasource.createOwed("Test Owed", 7.60);
        addOwed(owed.getName(), owed.getOwedAmount());

        adapter.notifyDataSetChanged();

    }

    private List<HashMap<String, String>> addOwed(String name, double value) {
        HashMap<String, String> owedMap = new HashMap<String, String>();
        owedMap.put("name", name);
        owedMap.put("owedAmount", String.valueOf(value));
        owedList.add(owedMap);
        return owedList;
    }

    private void populateList(List<Owed> oweds) {
        for( Owed owed: oweds) {
            addOwed(owed.getName(), owed.getOwedAmount());
        }
    }
}