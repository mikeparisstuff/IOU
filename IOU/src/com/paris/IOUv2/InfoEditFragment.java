package com.paris.IOUv2;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.paris.IOU.R;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/5/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfoEditFragment extends DialogFragment  {

    int position;
    private ImageButton close;
    private Button delete;
    private Button confirm;
    private Button cancel;
    private TextView title;
    private TextView date;
    private TextView name;
    private TextView amount;
    private TextView desc;
    private Spinner spinner;
    private LinearLayout editBox;
    private boolean add = true;

    private final String TAG = "InfoEditFragment";

    static InfoEditFragment newInstance(int position, String name, double amount,
                                        String desc, String date) {

        InfoEditFragment frag = new InfoEditFragment();

        //Supply position for Owe/Owed as well as Strings to fill in
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putString("name", name);
        args.putDouble("amount", amount);
        args.putString("desc", desc);
        args.putString("date", date);
        frag.setArguments(args);
        return frag;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceBundle) {
        Bundle args = getArguments();

        //If position == 0 then Owe, if == 1 then Owed
        position = args.getInt("pos");

        View v;
        v = inflater.inflate(R.layout.iou_info_edit_frag, container, false);

        //Find Views by id
        confirm = (Button)v.findViewById(R.id.iou_editconfirm);
        cancel = (Button)v.findViewById(R.id.iou_deletecancel);
        close = (ImageButton)v.findViewById(R.id.owe_done_button);
        date = (TextView)v.findViewById(R.id.owe_info_date);
        name = (TextView)v.findViewById(R.id.owe_dialog_name);
        amount = (TextView)v.findViewById(R.id.owe_dialog_amount);
        desc = (TextView)v.findViewById(R.id.owe_description);
        spinner = (Spinner)v.findViewById(R.id.add_subt_spinner);
        editBox = (LinearLayout)v.findViewById(R.id.edit_layout);


        //Set up Spinner Values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.add_subt_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if(position==0) {
                     add = true;
                 }
                 else {
                     add = false;
                 }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        //Set the TextViews
        name.setText(args.getString("name"));
        amount.setText(String.valueOf(args.getDouble("amount")));
        if(args.getString("desc").length() != 0) {
            desc.setText(args.getString("desc"));
        }
        else { desc.setText("None"); }
        date.setText(args.getString("date"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBox.setVisibility(View.VISIBLE);
            }
        });



        return v;
    }

}