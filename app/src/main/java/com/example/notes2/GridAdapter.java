package com.example.notes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class GridAdapter extends BaseAdapter {
    Context applicationcontext;
    List<String> names;
    List<Integer> images;
    LayoutInflater inflater;

    public GridAdapter(Context applicationcontext,List<String> names,List<Integer> image){
        this.applicationcontext=applicationcontext;
        this.names=names;
        this.images=image;
        inflater = LayoutInflater.from(applicationcontext);
    }
    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.syntexs, null);
        ImageView image = v.findViewById(R.id.icon);
        TextView fname = v.findViewById(R.id.filename);
        fname.setText(names.get(i));
//        image.setImageResource(images.get(i));
        return v;
    }

}
