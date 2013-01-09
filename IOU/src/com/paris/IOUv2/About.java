package com.paris.IOUv2;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.paris.IOU.R;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 1/8/13
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class About extends DialogFragment {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceBundle) {
        View v = inflater.inflate(R.layout.about, container, false);
        return v;
    }
}