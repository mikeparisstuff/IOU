package com.paris.IOUv2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.paris.IOU.Owed;
import com.paris.IOU.OwedAdapter;
import com.paris.IOU.OwedsDataSource;
import com.paris.IOU.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/3/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OwedFragment extends Fragment {

    OnOwedSelectedListener mCallback;

    public final String TAG = "OwedFragment";

    public final String NAME = "name";
    public final String AMOUNT = "amount";
    public final String DESC = "desc";
    public final String DELETEALL = "deleteall";
    public final String DELETE = "delete";

    private OwedsDataSource datasource;

    //ListAdapter
    private OwedAdapter adapter;

    Context context;

    //Custom ListView
    private ListView owedListView;

    //List of all oweds in the database
    private List<Owed> oweds;

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        context = getActivity();

        datasource = new OwedsDataSource(context);

        try {
            datasource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        oweds = datasource.getAllOwed();

        adapter = new OwedAdapter(context,
                R.layout.owe_row, oweds);

        //Find ListView and set adapter
        owedListView = (ListView)getView().findViewById(R.id.owed_listview);

        owedListView.setAdapter(adapter);

        owedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onOwedSelected(position, parent);
            }
        });

        Bundle args = getArguments();
        if(args != null) {
            Log.w(TAG, "Args passed: " + args.toString());
            if(args.containsKey(NAME) && args.containsKey(AMOUNT) && args.containsKey(DESC)) {
                String name = args.getString(NAME);
                double amount = args.getDouble(AMOUNT);
                String desc = args.getString(DESC);
                addOwed(name, amount, desc);
            }
            if( args.containsKey(DELETEALL)) {
                deleteAllOwed();
            }
            if ( args.containsKey("iou") && args.containsKey("amount") &&
                    args.containsKey("add")) {
                editAmount(args.getBoolean("add"), args.getDouble("amount"),
                        args.getSerializable("iou"));
            }
            if ( args.containsKey(DELETE)) {
                deleteOwed((Owed)args.getSerializable(DELETE));
            }
        }
    }

    //pass back info to container
    public interface OnOwedSelectedListener {
        public void onOwedSelected(int position, AdapterView<?> parent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Make sure the container has implemented calls
        try {
            mCallback = (OnOwedSelectedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnIouSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout for the fragment
        return inflater.inflate(R.layout.owed_frag, container, false);
    }

    public void addOwed(String name, double amount, String desc) {
        Owed owed = datasource.createOwed(name, amount, desc);
        if(oweds.contains(owed)) {
            datasource.deleteOwed(owed);
            Toast.makeText(context, "An owed for " + owed.getName() + " already exists",
                    Toast.LENGTH_LONG).show();
        }
        else {
            oweds.add(owed);
            adapter.notifyDataSetChanged();
        }
    }

    public void deleteAllOwed() {
        Log.w(TAG, "Delete all oweds called");
        datasource.deleteAllOweds();
        oweds.clear();
        adapter.notifyDataSetChanged();
    }

    public void editAmount(boolean add, double amount, Serializable iou) {
        Owed owed = (Owed)iou;
        double amt = owed.getOwedAmount();
        if( add ) {
            amt += amount;
        }
        else {
            amt -= amount;
        }
        datasource.updateOwed(owed, amt);
        int index = oweds.indexOf(owed);
        oweds.remove(owed);
        owed.setOwedAmount(amt);
        oweds.add(index, owed);
        adapter.notifyDataSetChanged();
    }

    public void deleteOwed(Owed owed) {
        datasource.deleteOwed(owed);
        oweds.remove(owed);
        adapter.notifyDataSetChanged();

    }

    public void updateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if( !datasource.isOpen() ) {
            try {
                Log.w(TAG, "onResume opening database");
                datasource.open();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if( datasource.isOpen() ) {
            Log.w(TAG, "onPause closing database");
            datasource.close();
        }
    }
}