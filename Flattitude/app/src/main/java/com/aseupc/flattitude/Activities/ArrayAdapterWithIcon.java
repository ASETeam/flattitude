package com.aseupc.flattitude.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MetzoDell on 10-11-15.
 */
public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

    private List<Integer> images;
    private List<Drawable> imagesD;

    public ArrayAdapterWithIcon(Context context, List<String> items, List<Integer> images) {
        super(context, android.R.layout.select_dialog_item, items);
        this.images = images;
    }

    public ArrayAdapterWithIcon(Context context, String[] items, Integer[] images) {
        super(context, android.R.layout.select_dialog_item, items);
        this.images = Arrays.asList(images);
    }
    public ArrayAdapterWithIcon(Context context, List<String> items, List<Drawable> images, boolean distinguish) {
        super(context, android.R.layout.select_dialog_item, items);
        this.imagesD = images;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, getContext().getResources().getDisplayMetrics()));
        return view;
    }

}