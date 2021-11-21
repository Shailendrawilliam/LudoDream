package com.example.indianludobattle.Fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.indianludobattle.Activity.CountDown.CountActivity;
import com.example.indianludobattle.Adapter.ContestListAdapter;
import com.example.indianludobattle.Adapter.PlayerListAdapter;
import com.example.indianludobattle.Model.ContestModel;
import com.example.indianludobattle.Model.JoinPlayerModel;
import com.example.indianludobattle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerFragment extends Fragment {

    public PlayerFragment() {
        // Required empty public constructor
    }
    public static PlayerFragment newInstance(String name) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString("tabName",name);
        fragment.setArguments(args);
        return fragment;
    }
    AnimatedRecyclerView playerRecycler;
    List<JoinPlayerModel> joinPlayerModels =new ArrayList<>();
    DatabaseReference reference;
    DownloadManager manager;
    TextView tvCreateRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_player, container, false);

        playerRecycler=root.findViewById(R.id.playerRecycler);
        playerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        MyJoinList(""+getArguments().getString("tabName"));
        root.findViewById(R.id.tvCreateRoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("Rooms");
                HashMap<String, Object> RsubHashMap = new HashMap<>();
                for (int i = 0; i < joinPlayerModels.size(); i++) {
                    HashMap<String, Object> subHashMap = new HashMap<>();
                    if (joinPlayerModels.get(i).isSelected()) {

                        subHashMap.put("GameEntry", joinPlayerModels.get(i).getGameEntry());
                        subHashMap.put("GamePricePool", joinPlayerModels.get(i).getGamePricePool());
                        subHashMap.put("PlayerId", joinPlayerModels.get(i).getPlayerId());
                        subHashMap.put("PlayerName", joinPlayerModels.get(i).getPlayerName());
                        subHashMap.put("PlayerCount", i+1);
                    }
                    RsubHashMap.put(joinPlayerModels.get(i).getPlayerId(),subHashMap);
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("playerId", joinPlayerModels.get(0).getPlayerId());
                hashMap.put("pieceId", 0);
                hashMap.put("diceArrayCount", 0);
                hashMap.put("diceArrayNumber", 0);
                hashMap.put("nestTurn", "RED");
                hashMap.put("nestTurnPlayerId", joinPlayerModels.get(0).getPlayerId());
                hashMap.put("Players", RsubHashMap);
                reference.child("Room001").setValue(hashMap);
            }
        });

        return root;
    }
    private void MyJoinList(String GameId) {
        reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("TotalJoinPlayer");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getValue().toString().equalsIgnoreCase(GameId)) {
                        for (DataSnapshot subSnapshot : snapshot.getChildren()) {
                            joinPlayerModels.add(new JoinPlayerModel(subSnapshot.child("ContestTitle").getValue().toString(), subSnapshot.child("GameEntry").getValue().toString(), subSnapshot.child("GameId").getValue().toString(), subSnapshot.child("GamePricePool").getValue().toString(), subSnapshot.child("JoinTime").getValue().toString(), subSnapshot.child("PlayerId").getValue().toString(), subSnapshot.child("PlayerName").getValue().toString(), false));
                        }
//                    }
                }
                PlayerListAdapter playerListAdapter =new PlayerListAdapter(getActivity(), joinPlayerModels, new PlayerListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(JoinPlayerModel item) {

                    }
                });
                playerRecycler.setAdapter(playerListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void CreateRoomGame(String GameEntry, String PlayerCount,String nestTurnPlayerId, String GamePricePool, String PlayerId,String PlayerName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MultiPlayer").child("Rooms");
        HashMap<String, Object> subHashMap1 = new HashMap<>();


        HashMap<String, Object> subHashMap = new HashMap<>();
        subHashMap.put("GameEntry", GameEntry);
        subHashMap.put("GamePricePool", GamePricePool);
        subHashMap.put("PlayerId", PlayerId);
        subHashMap.put("PlayerName", PlayerName);
        subHashMap.put("PlayerCount", PlayerCount);
        subHashMap1.put("Details",subHashMap);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("playerId", PlayerId);
        hashMap.put("pieceId", 0);
        hashMap.put("diceArrayCount", 0);
        hashMap.put("diceArrayNumber", 0);
        hashMap.put("nestTurn", "RED");
        hashMap.put("nestTurnPlayerId", nestTurnPlayerId);
        hashMap.put("Players", subHashMap1);

        reference.child("Room001").setValue(hashMap);
    }
}