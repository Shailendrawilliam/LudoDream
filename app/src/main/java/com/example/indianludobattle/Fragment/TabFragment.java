package com.example.indianludobattle.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indianludobattle.Activity.Auth.MobileRegisterActivity;
import com.example.indianludobattle.Activity.Contest.ContestActivity;
import com.example.indianludobattle.Activity.CountDown.CountActivity;
import com.example.indianludobattle.Adapter.ContestListAdapter;
import com.example.indianludobattle.Adapter.MainAdapter;
import com.example.indianludobattle.Model.ContestModel;
import com.example.indianludobattle.Model.JoinPlayerModel;
import com.example.indianludobattle.Model.MainModel;
import com.example.indianludobattle.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabFragment extends Fragment {
    private TextView textView;
    public TabFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String name) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString("tabName",name);
        fragment.setArguments(args);
        return fragment;
    }
    AnimatedRecyclerView contestRecycler;
    List<ContestModel> contestModels =new ArrayList<>();
    List<JoinPlayerModel> joinPlayerModels =new ArrayList<>();
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_tab, container, false);
        Log.v("jhsdgfh",""+getArguments().getString("tabName"));
        contestRecycler=root.findViewById(R.id.contestRecycler);
        contestRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        MyJoinList();
        ContestList(""+getArguments().getString("tabName"));
        return root;
    }



    private void ContestList(String ContestId) {
        reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("ContestList");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contestModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    boolean isAvail=false;
                    if(snapshot.child("ContestId").getValue().toString().equalsIgnoreCase(ContestId) || ContestId.equalsIgnoreCase("")) {

                        for (int k = 0; k < joinPlayerModels.size(); k++) {
                            if (snapshot.child("GameId").getValue().toString().equalsIgnoreCase(joinPlayerModels.get(k).getGameId())) {
                                isAvail = true;
                                break;
                            }
                            else
                                isAvail=false;
                        }
                        Log.v("sjdhj",""+isAvail);
                        if(isAvail)
                            contestModels.add(new ContestModel(snapshot.child("GameId").getValue().toString(), snapshot.child("ContestId").getValue().toString(), snapshot.child("ContestTitle").getValue().toString(), snapshot.child("GameEntry").getValue().toString(), snapshot.child("GamePricePool").getValue().toString(), snapshot.child("TotalJoined").getValue().toString(), snapshot.child("UseBonus").getValue().toString(), snapshot.child("showStatus").getValue().toString(), snapshot.child("time").getValue().toString(), true));
                        else
                            contestModels.add(new ContestModel(snapshot.child("GameId").getValue().toString(), snapshot.child("ContestId").getValue().toString(), snapshot.child("ContestTitle").getValue().toString(), snapshot.child("GameEntry").getValue().toString(), snapshot.child("GamePricePool").getValue().toString(), snapshot.child("TotalJoined").getValue().toString(), snapshot.child("UseBonus").getValue().toString(), snapshot.child("showStatus").getValue().toString(), snapshot.child("time").getValue().toString(), false));
                    }

                }
                ContestListAdapter contestListAdapter =new ContestListAdapter(getActivity(), contestModels, new ContestListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ContestModel item) {
                        JoinGame(item.getGameEntry(),item.getGameId(),item.getContestTitle(),item.getGamePricePool(),"Player001","Suraj Verma");
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                startActivity(new Intent(getActivity(), CountActivity.class));
                            }
                        }, 2000);
                    }
                });
                contestRecycler.setAdapter(contestListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void MyJoinList() {
        reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("TotalJoinPlayer");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    for (DataSnapshot subSnapshot : snapshot.getChildren()) {
                        if (subSnapshot.child("PlayerId").getValue().toString().equalsIgnoreCase("Player001")) {
                            joinPlayerModels.add(new JoinPlayerModel(""+subSnapshot.child("ContestTitle").getValue(),
                                    ""+subSnapshot.child("GameEntry").getValue(),
                                    ""+subSnapshot.child("GameId").getValue(),
                                    ""+subSnapshot.child("GamePricePool").getValue(),
                                    ""+subSnapshot.child("JoinTime").getValue(),
                                    ""+subSnapshot.child("PlayerId").getValue(),
                                    ""+subSnapshot.child("PlayerName").getValue(), false));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void JoinGame(String GameEntry, String GameId,String ContestTitle, String GamePricePool, String PlayerId,String PlayerName){
        String JoinTime = String.valueOf(System.currentTimeMillis());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("TotalJoinPlayer");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("GameEntry", GameEntry);
        hashMap.put("ContestTitle", ContestTitle);
        hashMap.put("GameId", GameId);
        hashMap.put("GamePricePool", GamePricePool);
        hashMap.put("PlayerId", PlayerId);
        hashMap.put("PlayerName", PlayerName);
        hashMap.put("JoinTime", JoinTime);
        reference.child(GameId).child(PlayerId).setValue(hashMap);
        MyJoinList();
        ContestList(""+getArguments().getString("tabName"));
    }
}