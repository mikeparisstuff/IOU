package com.paris.IOU;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/6/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */




public class OweScreen extends ListActivity {

    private OwesDataSource datasource;

    private ArrayList<HashMap<String, String>> oweList;
    Button newButton;
    SimpleAdapter adapter;






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

        List<Owe> owes = datasource.getAllOwes();


        oweList = new ArrayList<HashMap<String, String>>();

        findViewsById();

        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Send to New Home Screen to custom add a new owe
                Intent intent = new Intent(OweScreen.this, NewOweScreen.class);
                startActivity(intent);
            }
        });

        //make list adapter to hold tie data to listView
        adapter = new SimpleAdapter(
                this, oweList, R.layout.owe_row,
                new String[] {"name", "oweAmount"},
                new int[] { R.id.owe_name, R.id.owe_amount}
                );

        populateList(owes);

        setListAdapter(adapter);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            Owe owe = datasource.createOwe(extras.getString("name"),
                    extras.getDouble("amount"));
            addOwe(owe.getName(), owe.getOweAmount());
            adapter.notifyDataSetChanged();
        }

    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if(extras.getString("name") != null && extras.containsKey("amount")) {
             Owe owe = datasource.createOwe(extras.getString("name"),
                     extras.getDouble("amount"));
             addOwe(owe.getName(), owe.getOweAmount());
             adapter.notifyDataSetChanged();
        }

    }



    private void addOweToList() {
        Owe owe = datasource.createOwe("Test Owe", 5.90);
        addOwe(owe.getName(), owe.getOweAmount());

//                addOwe("Test New", 5);
        adapter.notifyDataSetChanged();
    }

    private void findViewsById() {
        newButton = (Button) findViewById(R.id.new_owe_button);
    }

    private void populateList(List<Owe> owes) {
        for( Owe owe: owes) {
              addOwe(owe.getName(), owe.getOweAmount());
        }

//        addOwe("Alex", 7);
//        addOwe("Kyle", 7000);
//        addOwe("Will", 16);
    }

    private List<HashMap<String, String>> addOwe(String name, double value) {
        HashMap<String, String> oweMap = new HashMap<String, String>();
        oweMap.put("name", name);
        oweMap.put("oweAmount", String.valueOf(value));
        oweList.add(oweMap);
        return oweList;
    }
}