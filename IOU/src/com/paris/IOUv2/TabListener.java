package com.paris.IOUv2;

import android.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import com.paris.IOU.OweAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/3/13
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    public TabListener(Activity activity, String tag, Class<T> clz) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //Check if the fragment is already initialized
        if (mFragment == null) {
            //If not, instantiate and add to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            Log.w( "TAB LISTENER", "mClass name: " + mClass.getName());
            Log.w( "TAB LISTENER", "mTag: " + mTag);
            ft.add(R.id.content, mFragment, mTag);
        }
        else {
            //If it exists, simply attach it in order to show it
            ft.attach(mFragment);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment != null) {
            //Detach the fragment, because another one is being attached
            ft.detach(mFragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //User selected the already selected tab. Do nothing
    }
}
