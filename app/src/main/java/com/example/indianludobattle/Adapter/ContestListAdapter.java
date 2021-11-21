package com.example.indianludobattle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.indianludobattle.Activity.CountDown.CountActivity;
import com.example.indianludobattle.Model.ContestModel;
import com.example.indianludobattle.R;

import java.util.List;

public class ContestListAdapter extends RecyclerView.Adapter<ContestListAdapter.RVViewHolder> {

	private Context mCtx;
	private List<ContestModel> arrayList;
	private final OnItemClickListener listener;
	public interface OnItemClickListener {
		void onItemClick(ContestModel item);
	}

	public ContestListAdapter(Context mCtx, List<ContestModel> arrayList, OnItemClickListener listener) {
		this.mCtx = mCtx;
		this.arrayList = arrayList;
		this.listener = listener;
	}

	@NonNull
	@Override
	public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		View view = inflater.inflate(R.layout.contest_item, null,false);
		return new RVViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RVViewHolder holder, final int position) {
		ContestModel bidModel = arrayList.get(position);
			holder.tvName.setText(bidModel.getContestTitle());
		holder.tvPricePool.setText("\u20B9 "+bidModel.getGamePricePool());
		if(bidModel.getGameEntry().equalsIgnoreCase("0")) {
			holder.tvEntry.setText("FREE");
		}else {
			holder.tvEntry.setText("\u20B9 "+bidModel.getGameEntry());
		}


		if(bidModel.getTotalJoined().equalsIgnoreCase(""))
			holder.tvJoined.setText("0 Joinded Now");
		else
			holder.tvJoined.setText(bidModel.getTotalJoined()+" Joinded Now");

		if(bidModel.getUseBonus().equalsIgnoreCase("")) {
			holder.tvBonusOuter.setVisibility(View.GONE);
		}else {
			holder.tvBonusOuter.setVisibility(View.VISIBLE);
			holder.tvBonus.setText("Use Upto \u20B9 "+bidModel.getUseBonus()+" Bonus");
		}

		if(bidModel.isJoinedOrNot()) {
			holder.tvEntry.setVisibility(View.GONE);
			holder.tvMyJoined.setVisibility(View.VISIBLE);
		}else {
			holder.tvEntry.setVisibility(View.VISIBLE);
			holder.tvMyJoined.setVisibility(View.GONE);
		}

		holder.bind(arrayList.get(position),listener, position);
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

		TextView tvName,tvPricePool,tvEntry,tvJoined,tvBonus,tvMyJoined;
		LinearLayout tvBonusOuter;

		public RVViewHolder(@NonNull View itemView) {
			super(itemView);
			tvName=(TextView) itemView.findViewById(R.id.tvName);
			tvPricePool=(TextView) itemView.findViewById(R.id.tvPricePool);
			tvEntry=(TextView) itemView.findViewById(R.id.tvEntry);
			tvMyJoined=(TextView) itemView.findViewById(R.id.tvMyJoined);
			tvJoined=(TextView) itemView.findViewById(R.id.tvJoined);
			tvBonus=(TextView) itemView.findViewById(R.id.tvBonus);
			tvBonusOuter=(LinearLayout) itemView.findViewById(R.id.tvBonusOuter);
		}
		public void bind(final ContestModel item, final OnItemClickListener listener, final int position) {

			tvEntry.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					listener.onItemClick(item);
					notifyDataSetChanged();
				}
			});

		}
	}
}
