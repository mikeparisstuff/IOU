package com.paris.IOUv2;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.paris.IOU.R;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/4/13
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewIouFragment extends DialogFragment {

    private final String TAG = "NewIouFragment";

    int position;
    Button add;
    Button cancel;
    EditText name;
    EditText amount;
    EditText desc;

    static NewIouFragment newInstance(int position) {
        NewIouFragment f = new NewIouFragment();

        //Supply position from activity to determine Owe/Owed
        Bundle args = new Bundle();
        args.putInt("pos", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        //FIND A WAY TO MAKE IT FIT FULL SCREEN
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceBundle) {

        position = getArguments().getInt("pos");
        Log.w(TAG, "OnCreateView with position: " + position);
        View v;
        final InputMethodManager imm = (InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if( position == 0) {

            v = inflater.inflate(R.layout.new_owe_frag, container, false);
            add = (Button)v.findViewById(R.id.confirm_owe_button);
            cancel = (Button)v.findViewById(R.id.cancel_owe_button);
            name = (EditText)v.findViewById(R.id.new_owe_name);
            amount = (EditText)v.findViewById(R.id.new_owe_amount);
            desc = (EditText)v.findViewById(R.id.new_owe_desc);

            //Call back up to the container holding the dialog
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w(TAG, "Attempting to add Owe");
                    if( name.getText().length() != 0 && amount.getText().length() != 0) {
                        String nameText = name.getText().toString();
                        double amountVal = Double.parseDouble(amount.getText().toString());
                        String descText = desc.getText().toString();
                        ((BalanceScreen)getActivity()).addOwe(nameText, amountVal, descText);
                        dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Enter a name and amount", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
//                    ((BalanceScreen)getActivity()).cancelNew();
                }
            });
        }
        else {
            v = inflater.inflate(R.layout.new_owed_frag, container, false);
            add = (Button)v.findViewById(R.id.confirm_owed_button);
            cancel = (Button)v.findViewById(R.id.cancel_owed_button);
            name = (EditText)v.findViewById(R.id.new_owed_name);
            amount = (EditText)v.findViewById(R.id.new_owed_amount);
            desc = (EditText)v.findViewById(R.id.new_owed_desc);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w(TAG, "Attempting to add Owed");
                    if( name.getText().length() != 0 && amount.getText().length() != 0) {
                        String nameText = name.getText().toString();
                        double amountVal = Double.parseDouble(amount.getText().toString());
                        String descText = desc.getText().toString();
                        ((BalanceScreen)getActivity()).addOwed(nameText, amountVal, descText);
                        dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Enter a name and amount", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
//                    ((BalanceScreen)getActivity()).cancelNew();
                }
            });
        }
        name.requestFocus();
        return v;
    }
}