package com.paris.IOU;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/16/12
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewOwedScreen extends Activity {

    //GUI elements
    private Button confirmOwed;
    private Button cancelOwed;
    private EditText newNameEdit;
    private EditText newAmountEdit;

    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.new_owed_screen);

        //find GUI views by id
        confirmOwed = (Button) findViewById(R.id.confirm_owed_button);
        cancelOwed = (Button) findViewById(R.id.cancel_owed_button);
        newNameEdit = (EditText) findViewById(R.id.new_owed_name);
        newAmountEdit = (EditText) findViewById(R.id.new_owed_amount);

        //set onClickListeners
        confirmOwed.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOwed();
            }
        });

        cancelOwed.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOwedScreen.this, OwedScreen.class);
                startActivity(intent);
            }
        });

    }

    private void addNewOwed() {
        if(newNameEdit.getText().length() != 0 &&
                newAmountEdit.getText().length() != 0) {
            Intent intent = new Intent(NewOwedScreen.this, OwedScreen.class);
            intent.putExtra("name", newNameEdit.getText().toString());
            intent.putExtra("amount", Double.parseDouble(newAmountEdit.getText().toString()));
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Enter a name and amount", Toast.LENGTH_SHORT).show();
        }
    }
}
