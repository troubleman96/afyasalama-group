package com.afyasalama.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.R;
import com.afyasalama.models.DrugLabel;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {
    private List<DrugLabel> drugList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DrugLabel drug);
    }

    public DrugAdapter(List<DrugLabel> drugList, OnItemClickListener listener) {
        this.drugList = drugList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrugLabel drug = drugList.get(position);
        holder.tvName.setText(drug.getBrandName());
        holder.tvGeneric.setText(drug.getGenericName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(drug));
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGeneric;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_drug_name);
            tvGeneric = itemView.findViewById(R.id.tv_generic_name);
        }
    }
}
