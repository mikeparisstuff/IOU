package com.paris.IOUv2;

import android.*;
import android.app.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.TabHost;
import com.paris.IOU.*;
import com.paris.IOU.R;

import java.io.Serializable;
import java.util.prefs.Preferences;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/3/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BalanceScreen extends Activity implements OweFragment.OnOweSelectedListener,
                OwedFragment.OnOwedSelectedListener, IouInfoFragment.OnIouEditListener {

    private final String TAG = "BALANCE SCREEN";
    private final String OWETAG = "owe";
    private final String OWEDTAG = "owed";
    private final String ABOUTTAG = "about";
    private final String POSITION = "position";

    public void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_screen);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.owe)
                .setTabListener(new TabListener<OweFragment>(
                        this, OWETAG, OweFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.owed)
                .setTabListener(new TabListener<OwedFragment> (
                        this, OWEDTAG, OwedFragment.class));
        actionBar.addTab(tab);

        //If returning from a previous state return or else have overlapping fragments
        if( savedInstanceState != null ) {
            Log.w(TAG, "savedInstanceState has Int " + savedInstanceState.get(POSITION));
            ActionBar.Tab activeTab = actionBar.getTabAt(savedInstanceState.getInt(POSITION));
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(POSITION));

        }

        //Add the Fragment to the screen
        if( findViewById(R.id.content) != null ) {



//            OweFragment firstFrag = new OweFragment();
//            getFragmentManager().beginTransaction().add(R.id.content, firstFrag, OWETAG).commit();
        }
    }


    //LIFECYCLE METHODS
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(POSITION, getActionBar().getSelectedNavigationIndex());
        Log.w(TAG, "onSaveInstanceState" + " with Bundle: " + savedInstanceState.get(POSITION));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.w(TAG,"onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        int position = savedInstanceState.getInt(POSITION);
        getActionBar().setSelectedNavigationItem(position);
    }

    @Override
    protected void onPause() {
        Log.w(TAG, "onPause");
        super.onPause();

        SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
        pref.edit().putInt(POSITION, getActionBar().getSelectedNavigationIndex()).commit();
        Log.w(TAG, "Prefs in Pause: " + String.valueOf(getPreferences(MODE_PRIVATE).getInt(POSITION, 0)));
    }

    @Override
    protected void onResume() {
        Log.w(TAG, "onResume");
        super.onResume();

        Log.w(TAG, String.valueOf(getPreferences(MODE_PRIVATE).getInt(POSITION, 0)));
        getActionBar().setSelectedNavigationItem(getPreferences(MODE_PRIVATE).getInt(POSITION, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new:
                Log.w(TAG, "new menu button clicked");
                int pos = getActionBar().getSelectedTab().getPosition();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("newIOU");
                //remove any present dialogs
                if (prev != null) {
                    ft.remove(prev);
                }

                //Create and show dialog
                DialogFragment newFrag = NewIouFragment.newInstance(pos);
                newFrag.show(ft, "newIOU");
                break;

            case R.id.delete_all:
                Log.w(TAG, "delete all clicked");
                if( getActionBar().getSelectedTab().getPosition() == 0) {
                    OweFragment oweFrag = (OweFragment)getFragmentManager().findFragmentByTag(OWETAG);
                    //findFragmentById(R.id.iou_frag);
                    if( oweFrag != null ) {
                        oweFrag.deleteAllOwes();
                    }
                    else {
                        OweFragment frag = new OweFragment();
                        Bundle args = new Bundle();
                        args.putString(frag.DELETEALL, null);
                        frag.setArguments(args);

                        FragmentTransaction oweTrans = getFragmentManager().beginTransaction();

                        //replace what is in the container with the new one
                        oweTrans.replace(R.id.content, frag, OWETAG);
                        oweTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        //uncomment to add to backstack
//              ft.addToBackStack(null);

                        //commit transaction
                        oweTrans.commit();
                    }
                }
                else {
                    OwedFragment owedFrag = (OwedFragment)getFragmentManager().findFragmentByTag(OWEDTAG);
                    //findFragmentById(R.id.iou_frag);
                    if( owedFrag != null ) {
                        owedFrag.deleteAllOwed();
                    }
                    else {
                        OwedFragment frag = new OwedFragment();
                        Bundle args = new Bundle();
                        args.putString(frag.DELETEALL, null);
                        frag.setArguments(args);

                        FragmentTransaction owedTrans = getFragmentManager().beginTransaction();

                        //replace what is in the container with the new one
                        owedTrans.replace(R.id.content, frag, OWEDTAG);
                        owedTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        //uncomment to add to backstack
//              ft.addToBackStack(null);

                        //commit transaction
                        owedTrans.commit();
                    }
                }

                break;
            case R.id.about:
                Log.w(TAG, "about button clicked");

//                WindowManager.LayoutParams params = getWindow().getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                getWindow().setAttributes((WindowManager.LayoutParams) params);

                Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.about);
                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(params);
                dialog.show();
                break;

            default:
                super.onOptionsItemSelected(item);


            }
                //NEED TO PASS TO NEW OWE/OWED SCREEN DEPENDING ON THE TAB POSITION
                //THEN PASS BACK AND PASS A MESSAGE TO THE FRAGMENT TO UPDATE THE DATASOURCE
                //AND LISTVIEW



                Log.w("TAB POSITION", String.valueOf(getActionBar().getSelectedTab().getPosition()));




        return true;
    }

    public void addOwe(String name, double amount, String desc) {
        OweFragment oweFrag = (OweFragment)getFragmentManager().findFragmentByTag(OWETAG);//findFragmentById(R.id.iou_frag);

        if( oweFrag != null ) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
            oweFrag.addOwe(name, amount, desc);

        }
        else {
            OweFragment newFrag = new OweFragment();
            Bundle args = new Bundle();
            args.putString(newFrag.NAME, name);
            args.putDouble(newFrag.AMOUNT, amount);
            args.putString(newFrag.DESC, desc);
            newFrag.setArguments(args);

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            //replace what is in the container with the new one
            ft.replace(R.id.content, newFrag, OWETAG);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //uncomment to add to backstack
//              ft.addToBackStack(null);

            //commit transaction
            ft.commit();
        }
    }

    public void addOwed(String name, double amount, String desc) {
        OwedFragment owedFrag = (OwedFragment)getFragmentManager().findFragmentByTag(OWEDTAG);//findFragmentById(R.id.iou_frag);

        if( owedFrag != null ) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
            owedFrag.addOwed(name, amount, desc);

        }
        else {
            OwedFragment newFrag = new OwedFragment();
            Bundle args = new Bundle();
            args.putString(newFrag.NAME, name);
            args.putDouble(newFrag.AMOUNT, amount);
            args.putString(newFrag.DESC, desc);
            newFrag.setArguments(args);

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            //replace what is in the container with the new one
            ft.replace(R.id.content, newFrag, OWEDTAG);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //uncomment to add to backstack
//              ft.addToBackStack(null);

            //commit transaction
            ft.commit();
        }
    }

    public void cancelNew() {
        Log.w(TAG, "cancel button clicked");
    }

    public void onOweSelected(int position, AdapterView<?> parent) {
        Owe owe = (Owe) parent.getItemAtPosition(position);
        int pos = getActionBar().getSelectedTab().getPosition();
        IouInfoFragment newFrag = IouInfoFragment.newInstance(pos,
                owe);

        //Remove any Dialogs that may be there
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("infoFrag");

        if(prev != null) {
            ft.remove(prev);
        }
        newFrag.show(ft, "infoFrag");
    }

    public void onOwedSelected(int position, AdapterView<?> parent) {
        Owed owed = (Owed) parent.getItemAtPosition(position);

        int pos = getActionBar().getSelectedTab().getPosition();
        IouInfoFragment newFrag = IouInfoFragment.newInstance(pos,
                owed);

        //Remove any Dialogs that may be there
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("infoFrag");

        if(prev != null) {
            ft.remove(prev);
        }

        newFrag.show(ft, "infoFrag");

    }

    public void editAmount(int pos, boolean add, double amount, Serializable iou) {
        if( pos == 0 ) {   //OWE POSITION
            OweFragment oweFrag = (OweFragment)getFragmentManager().findFragmentByTag(OWETAG);

            if( oweFrag != null ) {
                oweFrag.editAmount(add, amount, iou);
            }
            else {
                OweFragment newFrag = new OweFragment();
                Bundle args = new Bundle();
                args.putSerializable("iou", iou);
                args.putDouble("amount", amount);
                args.putBoolean("add", add);
                newFrag.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                //replace what is in the container with the new one
                ft.replace(R.id.content, newFrag, OWETAG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //uncomment to add to backstack
//              ft.addToBackStack(null);

                //commit transaction
                ft.commit();
            }


        }
        else {
            OwedFragment owedFrag = (OwedFragment)getFragmentManager().findFragmentByTag(OWEDTAG);

            if( owedFrag != null ) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
                owedFrag.editAmount(add, amount, iou);
            }
            else {
                OwedFragment newFrag = new OwedFragment();
                Bundle args = new Bundle();
                args.putSerializable("iou", iou);
                args.putDouble("amount", amount);
                args.putBoolean("add", add);
                newFrag.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                //replace what is in the container with the new one
                ft.replace(R.id.content, newFrag, OWEDTAG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //uncomment to add to backstack
//              ft.addToBackStack(null);

                //commit transaction
                ft.commit();
            }

        }

    }
    public void deleteIou(int pos, Serializable iou) {
        if( pos == 0 ) {
            OweFragment oweFrag = (OweFragment)getFragmentManager().findFragmentByTag(OWETAG);
            Owe owe = (Owe)iou;

            if( oweFrag != null ) {
                oweFrag.deleteOwe(owe);
            }
            else {
                OweFragment newFrag = new OweFragment();
                Bundle args = new Bundle();
                args.putSerializable(newFrag.DELETE, iou);
                newFrag.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                //replace what is in the container with the new one
                ft.replace(R.id.content, newFrag, OWETAG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //uncomment to add to backstack
//              ft.addToBackStack(null);

                //commit transaction
                ft.commit();
            }
        }
        else {
            OwedFragment owedFrag = (OwedFragment)getFragmentManager().findFragmentByTag(OWEDTAG);
            Owed owed = (Owed)iou;

            if( owedFrag != null ) {
                owedFrag.deleteOwed(owed);
            }
            else {
                OwedFragment newFrag = new OwedFragment();
                Bundle args = new Bundle();
                args.putSerializable(newFrag.DELETE, iou);
                newFrag.setArguments(args);

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                //replace what is in the container with the new one
                ft.replace(R.id.content, newFrag, OWEDTAG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //uncomment to add to backstack
//              ft.addToBackStack(null);

                //commit transaction
                ft.commit();
            }
        }


    }

//    public void onOweUpdate() {
//        int pos = getActionBar().getSelectedTab().getPosition();
//        if (pos == 0) {
//            OweFragment oweFrag = (OweFragment)getFragmentManager().findFragmentById(R.id.iou_frag);
//
//        }
//    }
}