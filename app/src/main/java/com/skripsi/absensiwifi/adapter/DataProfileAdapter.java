package com.skripsi.absensiwifi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skripsi.absensiwifi.model.DataProfile;

import java.util.ArrayList;
import java.util.List;

public class DataProfileAdapter extends RecyclerView.Adapter<DataProfileAdapter.ViewHolder> {
    Context context;
    List<DataProfile> data;

    public DataProfileAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void add(DataProfile item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<DataProfile> items) {
        for (DataProfile item : items) {
            add(item);
        }
    }

    public DataProfile getData(int position) {
        return data.get(position);
    }

    public void remove(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNIK;
        TextView tvNama;
        TextView tvTanggalLahir;
        TextView tvAlamat;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile, parent, false));
            initViews();
        }

        public void bind(DataProfile item) {
            int nomer = getAdapterPosition() + 1;
            tvNIK.setText(item.getNik());
            tvNama.setText(item.getNama());
            tvTanggalLahir.setText(item.getTanggallahir());
            tvAlamat.setText(item.getAlamat());
        }

        public void initViews() {
            tvNIK = (TextView) itemView.findViewById(R.id.tv_nik);
            tvNama = (TextView) itemView.findViewById(R.id.tv_nama);
            tvTanggalLahir = (TextView) itemView.findViewById(R.id.tv_tanggal_lahir);
            tvAlamat = (TextView) itemView.findViewById(R.id.tv_alamat);
        }
    }
}

