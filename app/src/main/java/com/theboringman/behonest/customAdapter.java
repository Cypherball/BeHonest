package com.theboringman.behonest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class customAdapter extends ArrayAdapter<String>{

    String[] names;
    Context context;

    public customAdapter(Context context, String[] names) {
        super(context, R.layout.listview_layout);
        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView==null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_layout, parent, false);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.names);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        mViewHolder.name.setText(names[position]);
        return convertView;
    }

    static class ViewHolder{
        TextView name;
    }
}
