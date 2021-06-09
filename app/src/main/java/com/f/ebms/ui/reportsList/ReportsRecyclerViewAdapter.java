package com.f.ebms.ui.reportsList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.f.ebms.R;

import java.util.List;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.ViewHolder>{

    private final List<String> reportsList;
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    ReportsRecyclerViewAdapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.reportsList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.partsrecyclerview_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String reportsWithID = reportsList.get(position);
        String report = reportsWithID.split(";")[1];
        holder.partTextView.setText(report);
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
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
        return reportsList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}