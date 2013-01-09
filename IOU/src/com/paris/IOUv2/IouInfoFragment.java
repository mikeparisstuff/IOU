package com.paris.IOUv2;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.paris.IOU.Owe;
import com.paris.IOU.Owed;
import com.paris.IOU.R;

import java.io.Serializable;
import java.lang.Override;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/4/13
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class IouInfoFragment extends DialogFragment {

    OnIouEditListener mCallback;

    int position;
    private ImageButton close;
    private Button delete_cancel;
    private Button edit_confirm;
    private TextView title;
    private TextView date;
    private TextView name;
    private TextView amount;
    private TextView desc;
    private Spinner spinner;
    private LinearLayout editLayout;
    private EditText editAmount;

    //Info Fields
    private String nameVal;
    private double amountVal;
    private String descVal;
    private String dateVal;
    private Owe owe;
    private Owed owed;
    private Context context;

    //Determine if in edit mode or not
    private boolean editMode;
    private boolean add; //true if spinner set to add

    private final String TAG = "IouInfoFragment";



    static IouInfoFragment newInstance(int position,
                            Serializable iou) {

        IouInfoFragment f = new IouInfoFragment();

        Bundle args = new Bundle();
        if( position == 0) {
            //Supply position from activity to determine Owe/Owed
            args.putSerializable("owe", iou);
            args.putInt("pos", position);
        }
        else {
            args.putSerializable("owed", iou);
            args.putInt("pos", position);
        }
        f.setArguments(args);
        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

        //Make sure the container activity has implemented the calls
        try {
            mCallback = (OnIouEditListener)activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + "must implement OnIouSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceBundle) {
        Bundle args = getArguments();
        position = args.getInt("pos");
        editMode = false;
        DecimalFormat df = new DecimalFormat("#.##");

        if( position == 0) {
            //Info Fields
            owe = (Owe)args.getSerializable("owe");
            nameVal = owe.getName();
            amountVal = owe.getOweAmount();
            descVal = owe.getDescription();
            dateVal = owe.getDateTime();
        }
        else if( position == 1) {
            //Info Fields
            owed = (Owed)args.getSerializable("owed");
            nameVal = owed.getName();
            amountVal = owed.getOwedAmount();
            descVal = owed.getDescription();
            dateVal = owed.getDateTime();
        }

//        View v = inflater.inflate(R.layout.iou_info_frag, container, false);
        View v = inflater.inflate(R.layout.iou_info_edit_frag, container, false);
        delete_cancel = (Button)v.findViewById(R.id.iou_deletecancel);
        edit_confirm = (Button)v.findViewById(R.id.iou_editconfirm);
        close = (ImageButton)v.findViewById(R.id.owe_done_button);
        date = (TextView)v.findViewById(R.id.owe_info_date);
        name = (TextView)v.findViewById(R.id.owe_dialog_name);
        amount = (TextView)v.findViewById(R.id.owe_dialog_amount);
        desc = (TextView)v.findViewById(R.id.owe_description);
        spinner = (Spinner)v.findViewById(R.id.add_subt_spinner);
        editLayout = (LinearLayout)v.findViewById(R.id.edit_layout);
        editAmount = (EditText)v.findViewById(R.id.edit_amount);

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
                    Log.w(TAG, "Item: " + spinner.getSelectedItem().toString() + " Add=" + add);
                }
                else {
                    add = false;
                    Log.w(TAG, "Item: " + spinner.getSelectedItem().toString() + " Add=" + add);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        name.setText(nameVal);
        amount.setText(String.valueOf(df.format(amountVal)));
        if(descVal.trim().length() != 0) {
            desc.setText(descVal);
        }
        else { desc.setText("None"); }

        date.setText(dateVal);

        if(position == 1 ) {
            title = (TextView)v.findViewById(R.id.owe_dialog_title);
            title.setText(R.string.owed_title);
        }


            //Call Back Methods to container holding the dialog
            edit_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!editMode) {
                        editLayout.setVisibility(View.VISIBLE);
                        editMode = true;
                        edit_confirm.setText(R.string.confirm);
                        delete_cancel.setText(R.string.cancel);
                    }
                    else {
                        //ADD VALUE TO CURRENT VALUE
                        //MAY BE ABLE TO INTERFACE BACK TO ACTIVITY TO SAVE TIME
                        String amt = editAmount.getText().toString().trim();
                        if( amt.length() != 0) {
                            if( position == 0) {
                                mCallback.editAmount(position, add, Double.parseDouble(amt), owe);
                            }
                            else {
                                mCallback.editAmount(position, add, Double.parseDouble(amt), owed);
                            }
                            dismiss();
                        }
                        else {
                            Toast.makeText(context, "Enter an amount", Toast.LENGTH_SHORT).show();
                        }
                    }


//                    InfoEditFragment newFrag = InfoEditFragment.newInstance(position, nameVal,
//                            amountVal, descVal, dateVal);
//
//                    //Remove any Dialogs present
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.addToBackStack(null);
//                    Fragment prev = getFragmentManager().findFragmentByTag("editFrag");
//
//                    if( prev != null ) {
//                        ft.remove(prev);
//                    }
//                    newFrag.show(ft, "editFrag");
////                    dismiss();
                }
            });

            delete_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!editMode) {
                        //DELETE OWE
                        if(position == 0) {
                            mCallback.deleteIou(position, owe);
                        }
                        else {
                            mCallback.deleteIou(position, owed);
                        }
                        dismiss();
                    }
                    else {
                        editLayout.setVisibility(View.GONE);
                        editMode = false;
                        edit_confirm.setText(R.string.edit_iou);
                        delete_cancel.setText(R.string.owe_delete);
                    }

                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        return v;
    }

    public interface OnIouEditListener {
        public void editAmount(int pos, boolean add, double amount, Serializable iou);
        public void deleteIou(int pos, Serializable iou);
    }

}