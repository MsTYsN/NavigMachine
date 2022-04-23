package com.mestaoui.navigmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mestaoui.navigmachine.R;
import com.mestaoui.navigmachine.beans.Machine;

import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> implements Filterable {
    private List<Machine> machines;
    private List<Machine> machinesFilter;
    private LayoutInflater inflater;
    private Context context;
    private NewFilter mfilter;

    public MachineAdapter(Context context, List<Machine> machines) {
        this.context = context;
        this.machines = machines;
        this.machinesFilter = new ArrayList<>();
        this.machinesFilter.addAll(machines);
        this.inflater = LayoutInflater.from(context);
        mfilter = new NewFilter(this);
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.machine_item, parent, false);
        return new MachineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        holder.reference.setText("Référence : " + machinesFilter.get(position).getReference());
        holder.marque.setText("Marque : " + machinesFilter.get(position).getMarque().getCode());
        holder.prix.setText("Prix : " + machinesFilter.get(position).getPrix());
        holder.date.setText("Date d'achat : " + machinesFilter.get(position).getDateAchat());
    }

    @Override
    public int getItemCount() {
        return machinesFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public class MachineViewHolder extends RecyclerView.ViewHolder {
        TextView reference, marque, prix, date;
        public MachineViewHolder(@NonNull View itemView) {
            super(itemView);
            reference = itemView.findViewById(R.id.reference);
            marque = itemView.findViewById(R.id.marque);
            prix = itemView.findViewById(R.id.prix);
            date = itemView.findViewById(R.id.date);
        }
    }

    public class NewFilter extends Filter {
        public RecyclerView.Adapter mAdapter;
        public NewFilter(RecyclerView.Adapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            machinesFilter.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                machinesFilter.addAll(machines);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Machine m : machines) {
                    if (m.getMarque().getCode().toLowerCase().startsWith(filterPattern)) {
                        machinesFilter.add(m);
                    }else if(m.getReference().toLowerCase().startsWith(filterPattern)) {
                        machinesFilter.add(m);
                    }else if(String.valueOf(m.getPrix()).startsWith(filterPattern)) {
                        machinesFilter.add(m);
                    }
                }
            }
            results.values = machinesFilter;
            results.count = machinesFilter.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            machinesFilter = (List<Machine>) filterResults.values;
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
