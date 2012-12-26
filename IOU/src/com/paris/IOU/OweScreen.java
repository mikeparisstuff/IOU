package com.paris.IOU;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/6/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */




public class OweScreen extends Activity { //ListActivity {

    private OwesDataSource datasource;

//    private ArrayList<HashMap<String, String>> oweList;
    private Button newButton;
    private Button backbutton;
//    SimpleAdapter adapter;
    private OweAdapter adapter;

    //Custom ListView
    private ListView oweListView;

    //List of all owes in the database
    private List<Owe> owes;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owe_screen);

        datasource = new OwesDataSource(this);

        try {
            datasource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        owes = datasource.getAllOwes();

        adapter = new OweAdapter(this,
                R.layout.owe_row, owes);

        //Find ListView and set adapter
        oweListView = (ListView)findViewById(R.id.owe_listview);

        oweListView.setAdapter(adapter);

        oweListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Owe owe = (Owe) parent.getItemAtPosition(position);
                Intent i = new Intent(OweScreen.this, OweInfo.class);
                i.putExtra("owe", owe);
                startActivity(i);

            }
        });


        //Find Button and set Listener
        newButton = (Button) findViewById(R.id.new_owe_button);
        backbutton = (Button) findViewById(R.id.owe_back_button);

        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Send to New Home Screen to custom add a new owe
                Intent intent = new Intent(OweScreen.this, NewOweScreen.class);
                startActivity(intent);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OweScreen.this, HomeScreen.class));
            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            if(extras.containsKey("name") && extras.containsKey("amount")) {
                Owe owe = datasource.createOwe(extras.getString("name"),
                        extras.getDouble("amount"), extras.getString("desc"));
                owes.add(owe);
                adapter.notifyDataSetChanged();
            }
            if(extras.containsKey("owe")) {
                Owe owe = (Owe)extras.getSerializable("owe");
                owes.remove(owe);
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