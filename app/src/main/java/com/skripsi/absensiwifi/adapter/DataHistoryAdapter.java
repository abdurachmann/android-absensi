package com.skripsi.absensiwifi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skripsi.absensiwifi.R;
import com.skripsi.absensiwifi.model.DataHistory;

import java.util.ArrayList;
import java.util.List;

public class DataHistoryAdapter extends RecyclerView.Adapter<DataHistoryAdapter.ViewHolder> {
    Context context;
    List<DataHistory> data;

    public DataHistoryAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void add(DataHistory item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<DataHistory> items) {
        for (DataHistory item : items) {
            add(item);
        }
    }

    public DataHistory getData(int position) {
        return data.get(position);
    }

    public void remove(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public DataHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataHistoryAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(DataHistoryAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTanggal;
        TextView tvAbsenMasuk;
        TextView tvAbsenKeluar;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_history, parent, false));
            initViews();
        }

        public void bind(DataHistory item) {
            //int nomer = getAdapterPosition() + 1;
            tvTanggal.setText(item.getTanggal());
            tvAbsenMasuk.setText(item.getAbsenmasuk());
            tvAbsenKeluar.setText(item.getAbsenkeluar());
        }

        public void initViews() {
            tvTanggal = (TextView) itemView.findViewById(R.id.tv_tanggal);
            tvAbsenMasuk = (TextView) itemView.findViewById(R.id.tv_absen_masuk);
            tvAbsenKeluar = (TextView) itemView.findViewById(R.id.tv_absen_keluar);
        }
    }
}