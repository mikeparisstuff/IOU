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
    private ListView owedListView;

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


        //NOW USING OWE_ROW FOR BETTER CODE REUSE
        adapter = new OwedAdapter(this,
                R.layout.owe_row, oweds);

        owedListView = (ListView)findViewById(R.id.owed_listview);

        owedListView.setAdapter(adapter);

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
        owedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Owed owed = (Owed) parent.getItemAtPosition(position);
                Intent i = new Intent(OwedScreen.this, OwedInfo.class);
                i.putExtra("owed", owed);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            if(extras.containsKey("name") && extras.containsKey("amount")) {
                Owed owed = datasource.createOwed(extras.getString("name"),
                        extras.getDouble("amount"), extras.getString("desc"));
                oweds.add(owed);
                adapter.notifyDataSetChanged();
            }
            if(extras.containsKey("owed")) {
                Owed owed = (Owed)extras.getSerializable("owed");
                oweds.remove(owed);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();

        try {
            datasource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onPause(Bundle savedInstanceState) {
        super.onPause();
        datasource.close();
    }

}