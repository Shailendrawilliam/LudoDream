package com.example.indianludobattle.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indianludobattle.Activity.Contest.ContestActivity;
import com.example.indianludobattle.Adapter.GameListAdapter;
import com.example.indianludobattle.Adapter.MainAdapter;
import com.example.indianludobattle.Model.MainModel;
import com.example.indianludobattle.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateSessionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    GameListAdapter mainAdapter;
    private TabLayout tabLayout;
    private List<MainModel> mainModels = new ArrayList<>();;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        ContestTag();
    }
    private void ContestTag() {
        reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("TotalJoinPlayer");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mainModels.clear();
                mainModels.add(new MainModel("","All","",2));
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    mainModels.add(new MainModel(snapshot.getKey().toString(),snapshot.getKey().toString(),"",2));

                }

                mainAdapter = new GameListAdapter(getSupportFragmentManager(), CreateSessionActivity.this,mainModels.size(),mainModels);
                viewPager.setAdapter(mainAdapter);
                tabLayout.setupWithViewPager(viewPager);
                for(int i=0;i<tabLayout.getTabCount();i++){
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    tab.setCustomView(mainAdapter.getTabView(i));
                }
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        View view = tab.getCustomView();
                        if(view != null){
                            ImageView imageView = view.findViewById(R.id.item_icon);
                            TextView textView = view.findViewById(R.id.item_name);
                            textView.setTextColor(getResources().getColor(R.color.white));
                            TextView count = view.findViewById(R.id.item_count);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        View view = tab.getCustomView();
                        if(view != null){
                            ImageView imageView = view.findViewById(R.id.item_icon);
                            TextView textView = view.findViewById(R.id.item_name);
                            textView.setTextColor(getResources().getColor(R.color.black));
                            TextView count = view.findViewById(R.id.item_count);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}