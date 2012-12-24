package com.paris.IOU;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/18/12
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class OwedInfo extends Activity {

    //DATA fields
    String name;
    double amount;
    Owed owed;

    //GUI ELEMENTS
    Button deleteButton;
    Button subtractButton;
    Button addButton;
    ImageButton doneButton;

    //Context
    Context context;

    TextView owedName;
    TextView owedAmount;
    TextView owedTitle;

    //Datasource for modifying database
    private OwedsDataSource datasource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owe_info_dialog);

        context = this;

        //Get datasource from OwedScreen
        datasource = new OwedsDataSource(this);
        try {
            datasource.open();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);

        //Find GUI ELEMENTS
        deleteButton = (Button)findViewById(R.id.owe_delete);
        subtractButton = (Button)findViewById(R.id.owe_subtract);
        addButton = (Button)findViewById(R.id.owe_add);
        doneButton = (ImageButton)findViewById(R.id.owe_done_button);
        owedName = (TextView)findViewById(R.id.owe_dialog_name);
        owedAmount = (TextView)findViewById(R.id.owe_dialog_amount);
        owedTitle = (TextView)findViewById(R.id.owe_dialog_title);

        //Set title to "Owed"
        owedTitle.setText("Owed");


        Bundle extras = getIntent().getExtras();
        if( extras != null) {
            owed = (Owed)extras.getSerializable("owed");
            name = owed.getName();
            amount = owed.getOwedAmount();
            owedName.setText(name);
            owedAmount.setText(String.valueOf(amount));
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DELETE THE OWED ENTRY AND SEND BACK TO OWED SCREEN
                datasource.deleteOwed(owed);
                Intent i = new Intent(OwedInfo.this, OwedScreen.class);
                i.putExtra("owed", owed);
                startActivity(i);
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SUBTRACT THE VALUE FROM THE DATABASE TABLE, RESTORE NEW VAL,
                //AND UPDATE OWEDSCREEN

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Subtract Amount");
                alert.setMessage("How much less are you owed?");

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
                //ADD THE VAL TO THE DATABASE, RE-STORE THE NEW VAL,
                //AND UPDATE OWEDSCREEN
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Add Amount");
                alert.setMessage("How much more are you owed?");

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
                startActivity(new Intent(OwedInfo.this, OwedScreen.class));
            }
        });
    }

    public void addAmount(double amt) {
        this.amount += amt;
        datasource.updateOwed(owed, amount);
        startActivity(new Intent(OwedInfo.this, OwedScreen.class));


//        Log.w("oweAddAmount","New Amount: " + amount);
    }
}