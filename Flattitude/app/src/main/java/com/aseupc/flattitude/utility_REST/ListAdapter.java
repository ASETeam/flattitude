package com.aseupc.flattitude.utility_REST;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.R;

import java.util.List;

/**
 * Created by MetzoDell on 30-11-15.
 */
public class ListAdapter extends ArrayAdapter<PlanningTask> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<PlanningTask> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        PlanningTask p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.type);
            TextView tt2 = (TextView) v.findViewById(R.id.time);
            TextView tt3 = (TextView) v.findViewById(R.id.description);
          //  TextView tt4 = (TextView) v.findViewById(R.id.author);

            if (tt1 != null) {
                tt1.setText(p.getType());
            }

            if (tt2 != null) {
                tt2.setText(PlanningTask.getCleanDate(p.getPlannedTime()));
            }

            if (tt3 != null) {
                tt3.setText(p.getDescription());
            }
        /*    if (tt4 != null) {
                tt3.setText(p.getAuthor());
            }*/
        }

        return v;
    }

}
