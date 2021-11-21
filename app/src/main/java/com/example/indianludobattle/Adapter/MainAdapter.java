package com.example.indianludobattle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.bumptech.glide.Glide;
import com.example.indianludobattle.Fragment.TabFragment;
import com.example.indianludobattle.Model.MainModel;
import com.example.indianludobattle.R;

import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {
    private Context context;
    private int noOfTabs;
    private List<MainModel> mainModelList;
    public MainAdapter(@NonNull FragmentManager fm, Context context, int noOfTabs, List<MainModel> mainModelList) {
        super(fm);
        this.context = context;
        this.noOfTabs = noOfTabs;
        this.mainModelList = mainModelList;
    }

    @NonNull
    @Override
    public TabFragment getItem(int position) {
        MainModel mainModel = mainModelList.get(position);
        return  TabFragment.newInstance(mainModel.getTab_name());
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        MainModel mainModel = mainModelList.get(position);
        TextView tv = (TextView) v.findViewById(R.id.item_name);
        TextView textView = v.findViewById(R.id.item_count);
        if(mainModel.getCount() != 0){
            textView.setVisibility(View.GONE);
            textView.setText(String.valueOf(mainModel.getCount()));
        }
        else{
            textView.setVisibility(View.GONE);
        }
        tv.setText(mainModel.getLabel());
        ImageView img = (ImageView) v.findViewById(R.id.item_icon);
//        Glide.with(context).load(mainModel.getImage()).placeholder(R.drawable.placeholdre).into(img);

        return v;
    }
}