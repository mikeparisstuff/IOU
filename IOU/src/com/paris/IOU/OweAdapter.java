package com.paris.IOU;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/17/12
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OweAdapter extends ArrayAdapter<Owe> {

    Context context;
    int layoutResourceId;
    List<Owe> data;

    public OweAdapter(Context context, int layoutResourceId,
                       List<Owe> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        OweHolder holder = null;
        DecimalFormat df = new DecimalFormat("#.##");

        if( row == null ) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new OweHolder();
            holder.name = (TextView)row.findViewById(R.id.owe_name);
            holder.amount = (TextView)row.findViewById(R.id.owe_amount);
            holder.date = (TextView)row.findViewById(R.id.owe_date_created);

            row.setTag(holder);
        }
        else {
            holder = (OweHolder)row.getTag();
        }

        Owe owe = data.get(position);
        holder.name.setText(owe.getName());
        holder.amount.setText(String.valueOf(df.format(owe.getOweAmount())));
        holder.date.setText(owe.getDateTime());

        return row;
    }

    static class OweHolder {
        TextView name;
        TextView amount;
        TextView date;
    }
}
