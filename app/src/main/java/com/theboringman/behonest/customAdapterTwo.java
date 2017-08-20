package com.theboringman.behonest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class customAdapterTwo extends ArrayAdapter<String>{

    String[] names;
    String[] scores;
    Context context;

    public customAdapterTwo(Context context, String[] names, String[] scores) {
        super(context, R.layout.score_board_listview);
        this.names = names;
        this.scores = scores;
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
            convertView = layoutInflater.inflate(R.layout.score_board_listview, parent, false);
            mViewHolder.name = (TextView) convertView.findViewById(R.id._names);
            mViewHolder.score = (TextView) convertView.findViewById(R.id._scores);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        mViewHolder.name.setText(names[position]);
        mViewHolder.score.setText(scores[position]);
        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView score;
    }
}
