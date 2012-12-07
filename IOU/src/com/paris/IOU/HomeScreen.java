package com.paris.IOU;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends Activity {
    Button homeOweButton;
    Button homeOwedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //find GUI views by id
        findViewsById();

        //set OnClickListeners
        homeOweButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, OweScreen.class));
            }
        });

        homeOwedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, OwedScreen.class));
            }
        });
    }

    private void findViewsById() {
        homeOweButton = (Button)findViewById(R.id.home_owe_button);
        homeOwedButton = (Button)findViewById(R.id.home_owed_button);
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_home_screen, menu);
//        return true;
//    }
}
