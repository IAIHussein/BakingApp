package com.iaihussein.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MainAdapter extends BaseAdapter {


    List<Recipe> mList;
    private Context mContext;

    public MainAdapter(Context c, List<Recipe> list) {
        mContext = c;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.cellview, null);
        } else {
            view = convertView;
        }
        TextView mTextView = (TextView) view.findViewById(R.id.cellview_name_txt);
        mTextView.setText(mList.get(position).getName());
//        ImageView imageView = (ImageView) view.findViewById(R.id.imgViewId);
//        Picasso.with(mContext).load(Var.URL_IMAGE + mList.get(position).getBackdropPath()).into(imageView);
        return view;
    }

}
