package com.paris.IOU;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import static android.view.WindowManager.LayoutParams;

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
    private EditText newOwedDesc;
    private TextView newOwedTitle;

    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.new_owe_screen);

        LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((LayoutParams) params);

        //find GUI views by id
        confirmOwed = (Button) findViewById(R.id.confirm_owe_button);
        cancelOwed = (Button) findViewById(R.id.cancel_owe_button);
        newNameEdit = (EditText) findViewById(R.id.new_owe_name);
        newAmountEdit = (EditText) findViewById(R.id.new_owe_amount);
        newOwedTitle = (TextView)findViewById(R.id.new_owe_title);
        newOwedDesc = (EditText)findViewById(R.id.new_owe_desc);

        //SET TITLE TO BE FOR OWED
        newOwedTitle.setText(R.string.new_owed);

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
            intent.putExtra("desc", newOwedDesc.getText().toString());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Enter a name and amount", Toast.LENGTH_SHORT).show();
        }
    }
}
