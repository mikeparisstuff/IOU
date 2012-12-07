package com.paris.IOU;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/6/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */




public class OweScreen extends ListActivity {

    private ArrayList<HashMap<String, String>> oweList;
    Button newButton;
    SimpleAdapter adapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owe_screen);

        oweList = new ArrayList<HashMap<String, String>>();

        findViewsById();

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOwe("Test New", 5);
                adapter.notifyDataSetChanged();
            }
        });

        //make list adapter to hold tie data to listView
        adapter = new SimpleAdapter(
                this, oweList, R.layout.owe_row,
                new String[] {"name", "oweAmount"},
                new int[] { R.id.owe_name, R.id.owe_amount}
                );

        populateList();

        setListAdapter(adapter);

    }

    private void findViewsById() {
        newButton = (Button) findViewById(R.id.new_owe_button);
    }

    private void populateList() {
        addOwe("Alex", 7);
        addOwe("Kyle", 7000);
        addOwe("Will", 16);
    }

    private List<HashMap<String, String>> addOwe(String name, double value) {
        HashMap<String, String> oweMap = new HashMap<String, String>();
        oweMap.put("name", name);
        oweMap.put("oweAmount", String.valueOf(value));
        oweList.add(oweMap);
        return oweList;
    }
}