package com.afyasalama.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.R;
import com.afyasalama.models.Medication;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    private List<Medication> medicationList;
    private OnMedicationClickListener listener;

    public interface OnMedicationClickListener {
        void onEdit(Medication med);
        void onDelete(Medication med);
    }

    public MedicationAdapter(List<Medication> medicationList, OnMedicationClickListener listener) {
        this.medicationList = medicationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = medicationList.get(position);
        holder.tvName.setText(med.getName());
        holder.tvDosage.setText(med.getDosage());
        holder.tvTime.setText(med.getTime());

        holder.btnOverflow.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.medication_item_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_edit) {
                    listener.onEdit(med);
                    return true;
                } else if (id == R.id.action_delete) {
                    listener.onDelete(med);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDosage, tvTime;
        ImageButton btnOverflow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_med_name);
            tvDosage = itemView.findViewById(R.id.tv_med_dosage);
            tvTime = itemView.findViewById(R.id.tv_med_time);
            btnOverflow = itemView.findViewById(R.id.btn_overflow);
        }
    }
}
