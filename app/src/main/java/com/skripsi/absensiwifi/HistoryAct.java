package com.skripsi.absensiwifi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skripsi.absensiwifi.adapter.DataHistoryAdapter;
import com.skripsi.absensiwifi.model.DataHistory;
import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAct extends AppCompatActivity {
    ImageView btn_back;
    EditText date, date_to;
    DatePickerDialog datePickerDialog;
    TextView tvNik;
    TextView tvNama;
    Button show_history;

    String tanggaldari;
    String tanggalsampai;

    private static final String TAG = HistoryAct.class.getSimpleName();

    private RecyclerView rvData;
    private DataHistoryAdapter adapter;
    private DataService service;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, HistoryAct.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode
        final String nik = pref.getString("nik", "");
        String nama = pref.getString("nama", "");

        date = findViewById(R.id.date);
        date_to = findViewById(R.id.date_to);
        show_history = findViewById(R.id.show_history);
        btn_back = findViewById(R.id.btn_back);

        tvNik = findViewById(R.id.tv_nik);
        tvNama = findViewById(R.id.tv_nama);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calendar
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // date picker dialog
                datePickerDialog = new DatePickerDialog(HistoryAct.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month, month and year value in the edit text
                        date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        tanggaldari = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender
                final  Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                //date picker dialog
                datePickerDialog = new DatePickerDialog(HistoryAct.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_to.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        tanggalsampai = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        show_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(nik, tanggaldari, tanggalsampai);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent gotohome = new Intent(HistoryAct.this, HomeAct.class);
            startActivity(gotohome);
            finish();
            }
        });


        // Get Data from API
        initViews();

        // Initialization adapter
        adapter = new DataHistoryAdapter(this);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        service = ServiceGenerator.createBaseService(this, DataService.class);

        rvData.setAdapter(adapter);

        tvNik.setText(nik);
        tvNama.setText(nama);

        loadData(nik, tanggaldari, tanggalsampai);
    }

    private void loadData(String nik, String tanggaldari, String tanggalsampai) {
        adapter.clear();

        Call<BaseResponse<List<DataHistory>>> call = service.apiHistory(nik, tanggaldari, tanggalsampai);
        call.enqueue(new Callback<BaseResponse<List<DataHistory>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DataHistory>>> call, Response<BaseResponse<List<DataHistory>>> response) {
                if (response.code() == 200) {
                    adapter.addAll(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DataHistory>>> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        rvData = (RecyclerView) findViewById(R.id.rv_data_history);
    }
}