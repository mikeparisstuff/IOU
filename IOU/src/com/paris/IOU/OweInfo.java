package com.paris.IOU;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/18/12
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OweInfo extends Activity {

    //DATA FIELDS
    String name;
    double amount;
    String date;
    String desc;
    Owe owe;

    //GUI ELEMENTS
    Button deleteButton;
    Button subtractButton;
    Button addButton;
    ImageButton doneButton;

    //Context
    private Context context;

    TextView oweName;
    TextView oweAmount;
    TextView oweDate;
    TextView oweDesc;

    //DATASOURCE FOR MODIFYING DATABASE
    private OwesDataSource datasource;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owe_info_dialog);

        context = this;

        //Get datasource from OwesScreen
        datasource = new OwesDataSource(this);
        try {
            datasource.open();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }



        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);

        //Find GUI elements by ID
        deleteButton = (Button)findViewById(R.id.owe_delete);
        subtractButton = (Button)findViewById(R.id.owe_subtract);
        addButton = (Button)findViewById(R.id.owe_add);
        doneButton = (ImageButton)findViewById(R.id.owe_done_button);
        oweName = (TextView)findViewById(R.id.owe_dialog_name);
        oweAmount = (TextView)findViewById(R.id.owe_dialog_amount);
        oweDate = (TextView)findViewById(R.id.owe_info_date);
        oweDesc = (TextView)findViewById(R.id.owe_description);

        //THIS ACTIVITY SHOULD BE CALLED WITH AN INTENT WITH AN EXTRA
        //THAT SPECIFIES WHICH ELEMENT IN THE LIST WAS CLICKED AND THEN WE CAN LOAD
        //THE DATA FROM THE DATABASE DIRECTLY TO THIS ACTIVITY AND MODIFY IT
        Bundle extras = getIntent().getExtras();
        if( extras != null) {
            owe = (Owe)extras.getSerializable("owe");
            name = owe.getName();
            amount = owe.getOweAmount();
            date = owe.getDateTime();
            desc = owe.getDescription();
            oweName.setText(name);
            oweAmount.setText(String.valueOf(amount));
            oweDate.setText(date);
            if(desc.length() != 0) {
                oweDesc.setText(desc);
            }
            else {
                oweDesc.setText(R.string.empty_desc);
            }
            //COME BACK TO... YOU HAVE UPDATED THE FIELDS OF THE DIALOG
            //TO SHOW THE CORRECT VALUES FROM THE TABLE

        }


        //Set OnClickListeners
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DELETE THE OWE ENTRY AND SEND BACK TO OWE SCREEN
                datasource.deleteOwe(owe);
                Intent i = new Intent(OweInfo.this, OweScreen.class);
                i.putExtra("owe", owe);
                startActivity(i);
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SUBTRACT THE VALUE FROM THE DATABASE TABLE, RESTORE NEW VAL,
                //AND UPDATE OWESCREEN
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Subtract Amount");
                alert.setMessage("How much less do you owe?");

                //Set an EditText view to get input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                alert.setView(input);

                alert.setPositiveButton("Subtract Amount", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().length() != 0) {
                            double amount = Double.parseDouble(input.getText().toString());
                            addAmount(-amount);
                        }
                        else {
                            Toast.makeText(context, "Enter an amount", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }
                        })
                        .show();


            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ADD THE VALUE TO THAT OF THE DATABASE, RE-STORE NEW VAL,
                //AND UPDATE OWESCREEN

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Add Amount");
                alert.setMessage("How much more do you owe?");

                //Set an EditText view to get input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                alert.setView(input);

                alert.setPositiveButton("Add Amount", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().length() != 0) {
                            double amount = Double.parseDouble(input.getText().toString());
                            addAmount(amount);
                        }
                        else {
                            Toast.makeText(context, "Enter an amount", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                })
                .show();


            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OweInfo.this, OweScreen.class));
            }
        });



    }

    public void addAmount(double amt) {
        this.amount += amt;
        datasource.updateOwe(owe, amount);
        startActivity(new Intent(OweInfo.this, OweScreen.class));


//        Log.w("oweAddAmount","New Amount: " + amount);
    }
}