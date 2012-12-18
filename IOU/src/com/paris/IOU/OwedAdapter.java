package com.paris.IOU;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/17/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OwedAdapter extends ArrayAdapter<Owed> {

    Context context;
    int layoutResourceId;
    List<Owed> data;
//    Owed data[] = null;

    public OwedAdapter(Context context, int layoutResourceId,
                       List<Owed> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        OwedHolder holder = null;

        if( row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new OwedHolder();
            holder.name = (TextView)row.findViewById(R.id.owed_name);
            holder.amount = (TextView)row.findViewById(R.id.owed_amount);

            row.setTag(holder);
        }
        else {
            holder = (OwedHolder)row.getTag();
        }

//        Owed owed = data[position];
        Owed owed = data.get(position);
        holder.name.setText(owed.getName());
        holder.amount.setText(String.valueOf(owed.getOwedAmount()));

        return row;
    }

    static class OwedHolder {
        TextView name;
        TextView amount;
    }
}
