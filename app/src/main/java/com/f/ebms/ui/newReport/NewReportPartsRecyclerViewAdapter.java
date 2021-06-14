package com.f.ebms.ui.newReport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.f.ebms.R;

import java.util.ArrayList;
import java.util.List;

public class NewReportPartsRecyclerViewAdapter extends RecyclerView.Adapter<NewReportPartsRecyclerViewAdapter.ViewHolder>{

    private final List<String> partsList;
    private final List<String> partsListCopy;
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    NewReportPartsRecyclerViewAdapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.partsList = data;
        this.partsListCopy = new ArrayList<String>(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.newreportrecyclerview_entry, parent, false);
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

    void setClickListener(NewReportPartsRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

   public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String searchText) {
        partsList.clear();
        if(searchText.isEmpty()){
            partsList.addAll(partsListCopy);
        } else{
            searchText = searchText.toLowerCase();
            for(String item: partsListCopy){
                if(item.toLowerCase().contains(searchText)){
                    partsList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
