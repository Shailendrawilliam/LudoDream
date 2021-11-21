package com.example.indianludobattle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.indianludobattle.Model.ContestModel;
import com.example.indianludobattle.Model.JoinPlayerModel;
import com.example.indianludobattle.R;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.RVViewHolder> {

	private Context mCtx;
	private List<JoinPlayerModel> arrayList;
	private final OnItemClickListener listener;
	public interface OnItemClickListener {
		void onItemClick(JoinPlayerModel item);
	}

	public PlayerListAdapter(Context mCtx, List<JoinPlayerModel> arrayList, OnItemClickListener listener) {
		this.mCtx = mCtx;
		this.arrayList = arrayList;
		this.listener = listener;
	}

	@NonNull
	@Override
	public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		View view = inflater.inflate(R.layout.player_item, null,false);
		return new RVViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RVViewHolder holder, final int position) {
		JoinPlayerModel bidModel = arrayList.get(position);
		holder.tvName.setText(bidModel.getContestTitle());
		holder.tvPlayerId.setText(bidModel.getPlayerId());

		holder.tvSelect.setBackgroundResource(bidModel.isSelected() ? R.drawable.button_small_yellow : R.drawable.button_player_joined);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bidModel.setSelected(!bidModel.isSelected());
				holder.tvSelect.setBackgroundResource(bidModel.isSelected() ? R.drawable.button_small_yellow : R.drawable.button_player_joined);
			}
		});

	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public class RVViewHolder extends RecyclerView.ViewHolder{

		TextView tvName,tvPlayerId,tvSelect,tvJoined,tvBonus,tvMyJoined;
		LinearLayout tvBonusOuter;

		public RVViewHolder(@NonNull View itemView) {
			super(itemView);
			tvName=(TextView) itemView.findViewById(R.id.tvName);
			tvPlayerId=(TextView) itemView.findViewById(R.id.tvPlayerId);
			tvSelect=(TextView) itemView.findViewById(R.id.tvSelect);
			tvMyJoined=(TextView) itemView.findViewById(R.id.tvMyJoined);
			tvJoined=(TextView) itemView.findViewById(R.id.tvJoined);
			tvBonus=(TextView) itemView.findViewById(R.id.tvBonus);
			tvBonusOuter=(LinearLayout) itemView.findViewById(R.id.tvBonusOuter);
		}

	}
}
