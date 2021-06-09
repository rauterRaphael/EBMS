package com.f.ebms.ui.partsList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.f.ebms.R;

import java.util.List;

public class PartsRecyclerViewAdapter extends RecyclerView.Adapter<PartsRecyclerViewAdapter.ViewHolder>{

    private final List<String> partsList;
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    PartsRecyclerViewAdapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.partsList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.partsrecyclerview_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String bikePartWithID = partsList.get(position);
        String bikePart = bikePartWithID.split(";")[1];
        holder.partTextView.setText(bikePart);
    }

    @Override
    public int getItemCount() {
        return partsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partTextView;

        ViewHolder(View itemView) {
            super(itemView);
            partTextView = itemView.findViewById(R.id.bikePartTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return partsList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}