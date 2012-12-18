package com.paris.IOU;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

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
public class OwedScreen extends Activity { //ListActivity {

    private OwedsDataSource datasource;

//    private ArrayList<HashMap<String, String>> owedList;
    private Button newButton;
    private Button backButton;
    //private SimpleAdapter adapter;
    private OwedAdapter adapter;

    //Custom ListView
    private ListView listView1;

    //List of all oweds in the database
    private List<Owed> oweds;
//    private Owed[] oweds;



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


        oweds = datasource.getAllOwed();
        //Owed owed_data[] = oweds.toArray(new Owed[oweds.size()]);

        adapter = new OwedAdapter(this,
                R.layout.owed_row, oweds);

        listView1 = (ListView)findViewById(R.id.owed_listview);

        listView1.setAdapter(adapter);

        //get UI elements by ID
        newButton = (Button) findViewById(R.id.new_owed_button);
        backButton = (Button) findViewById(R.id.owed_back_button);

        newButton.setOnClickListener( new View.OnClickListener() {
            Owed owed = null;
            @Override
            public void onClick(View v) {
                //addOwedToList();
                Intent i = new Intent(OwedScreen.this, NewOwedScreen.class);
                startActivity(i);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwedScreen.this, HomeScreen.class));
            }
        });

        //Set the OnItemClickListener
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Owed owed = (Owed) parent.getItemAtPosition(position);
                datasource.deleteOwed(owed);
                oweds.remove(owed);
                adapter.notifyDataSetChanged();
            }
        });

        //Used in early version with multiple rows
//        owedList = new ArrayList<HashMap<String, String>>();

        //make list adapter to tie data from DB to listView
//        adapter = new SimpleAdapter(
//                this, owedList,  R.layout.owed_row,
//                new String[] {"name", "owedAmount"},
//                new int[] { R.id.owed_name, R.id.owed_amount}
//                );
//        setListAdapter(adapter);

        //Used when I had to have 2 lists holding the info
        //for the list adapter
//        populateList(oweds);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            Owed owed = datasource.createOwed(extras.getString("name"),
                    extras.getDouble("amount"));

            oweds.add(owed);
            //used on early version with multiple lists
//            addOwed(owed.getName(), owed.getOwedAmount());
            adapter.notifyDataSetChanged();
        }
    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();

//        Bundle extras = getIntent().getExtras();
//
//        if(extras != null) {
//            Owed owed = datasource.createOwed(extras.getString("name"),
//                    extras.getDouble("amount"));
//            addOwed(owed.getName(), owed.getOwedAmount());
//            adapter.notifyDataSetChanged();
//        }
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//           Owed owed = (Owed) getListView().getItemAtPosition(position);
//           datasource.deleteOwed(owed);
//           adapter.notifyDataSetChanged();
//    }

//    private void addOwedToList() {
//        Owed owed = null;
//        //Still need to implement a way to update the information
//        //personally
//
//        owed = datasource.createOwed("Test Owed", 7.60);
//        addOwed(owed.getName(), owed.getOwedAmount());
//
//        adapter.notifyDataSetChanged();
//
//    }

//    private List<HashMap<String, String>> addOwed(String name, double value) {
//        HashMap<String, String> owedMap = new HashMap<String, String>();
//        owedMap.put("name", name);
//        owedMap.put("owedAmount", String.valueOf(value));
//        owedList.add(owedMap);
//        return owedList;
//    }
//
//    private void populateList(List<Owed> oweds) {
//        for( Owed owed: oweds) {
//            addOwed(owed.getName(), owed.getOwedAmount());
//        }
//    }
}