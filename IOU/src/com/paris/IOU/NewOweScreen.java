package com.paris.IOU;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/16/12
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewOweScreen extends Activity {


    //GUI elements
    private Button confirmOwe;
    private Button cancelOwe;
    private EditText newNameEdit;
    private EditText newAmountEdit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_owe_screen);

        //find GUI views by id
        confirmOwe = (Button) findViewById(R.id.confirm_owe_button);
        cancelOwe = (Button) findViewById(R.id.cancel_owe_button);
        newNameEdit = (EditText) findViewById(R.id.new_owe_name);
        newAmountEdit = (EditText) findViewById(R.id.new_owe_amount);

        //set onClickListeners
        confirmOwe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOwe();
            }
        });

        cancelOwe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOweScreen.this, OweScreen.class);
                startActivity(intent);
            }
        });
    }

    private void addNewOwe() {
        if(newNameEdit.getText().length() != 0 &&
                newAmountEdit.getText().length() != 0) {
           Intent intent = new Intent(NewOweScreen.this, OweScreen.class);
           intent.putExtra("name", newNameEdit.getText().toString());
           intent.putExtra("amount", Double.parseDouble(newAmountEdit.getText().toString()));
           startActivity(intent);
        }
        else {
            Toast.makeText(this, "Enter a name and amount", Toast.LENGTH_SHORT).show();
        }
    }

}
