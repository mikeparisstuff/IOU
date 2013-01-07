package com.paris.IOUv2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.paris.IOU.*;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/3/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OweFragment extends Fragment {

    OnOweSelectedListener mCallback;

    private final String TAG = "OweFragment";

    public final String NAME = "name";
    public final String AMOUNT = "amount";
    public final String DESC = "desc";
    public final String DELETEALL = "deleteall";
    public final String DELETE = "delete";

    private OwesDataSource datasource;

    //ListAdapter
    private OweAdapter adapter;

    Context context;

    //Custom ListView
    private ListView oweListView;

    //List of all owes in the database
    private List<Owe> owes;

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        context = getActivity();

        datasource = new OwesDataSource(context);

        try {
            datasource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        owes = datasource.getAllOwes();

        adapter = new OweAdapter(context,
                R.layout.owe_row, owes);

        //Find ListView and set adapter
        oweListView = (ListView)getView().findViewById(R.id.owe_listview);
        oweListView.setAdapter(adapter);
        oweListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onOweSelected(position, parent);
            }
        });

        Bundle args = getArguments();
        if(args != null) {
            Log.w(TAG, "args passed: " + args.toString());
            if(args.containsKey(NAME) && args.containsKey(AMOUNT) && args.containsKey(DESC)) {
                String name = args.getString(NAME);
                double amount = args.getDouble(AMOUNT);
                String desc = args.getString(DESC);
                addOwe(name, amount, desc);
            }
            if ( args.containsKey(DELETEALL)) {
                deleteAllOwes();
            }
            if ( args.containsKey("iou") && args.containsKey("amount") &&
                    args.containsKey("add")) {
                editAmount(args.getBoolean("add"), args.getDouble("amount"),
                        args.getSerializable("iou"));
            }
            if ( args.containsKey(DELETE)) {
                deleteOwe((Owe) args.getSerializable(DELETE));
            }
        }
    }

    //container activity implements this to get item clicked
    public interface OnOweSelectedListener {
        public void onOweSelected(int position, AdapterView<?> parent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Make sure the container activity has implemented the calls
        try {
            mCallback = (OnOweSelectedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + "must implement OnIouSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //inflate layout for the fragment                     //TRIAL
        return inflater.inflate(R.layout.owe_frag, container, false);

    }

    public void addOwe(String name, double amount, String desc) {
        Owe owe = datasource.createOwe(name, amount, desc);
        if(owes.contains(owe)) {
            datasource.deleteOwe(owe);
            Toast.makeText(context, "An owe for " + owe.getName() + " already exists",
                    Toast.LENGTH_LONG).show();
        }
        else {
            owes.add(owe);
            adapter.notifyDataSetChanged();
        }
    }

    public void editAmount(boolean add, double amount, Serializable iou) {
        Owe owe = (Owe)iou;
        double amt = owe.getOweAmount();
        if( add ) {
            amt += amount;
        }
        else {
            amt -= amount;
        }
        datasource.updateOwe(owe, amt);
        int index = owes.indexOf(owe);
        owes.remove(owe);
        owe.setOweAmount(amt);
        owes.add(index, owe);
        adapter.notifyDataSetChanged();
    }

    public void deleteAllOwes() {
        Log.w(TAG, "Delete all owes called");
        datasource.deleteAllOwes();
        owes.clear();
        adapter.notifyDataSetChanged();
    }

    public void deleteOwe(Owe owe) {
        datasource.deleteOwe(owe);
        owes.remove(owe);
        adapter.notifyDataSetChanged();
    }

    public void updateIou(String name, double amount, String desc) {
        Owe owe = datasource.createOwe(name,
                amount, desc);
        if(owes.contains(owe)) {
            datasource.deleteOwe(owe);
            Toast.makeText(context, "An owe for " + owe.getName() + " already exists",
                    Toast.LENGTH_LONG).show();
        }
        else {
            owes.add(owe);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateView() {
        adapter.notifyDataSetChanged();
    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();
        Log.w(TAG, "onResume called");

//        try {
//            datasource.open();
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    public void onPause(Bundle savedInstanceState) {
        datasource.close();
        super.onPause();

        Log.w(TAG, "onPause");

    }
}