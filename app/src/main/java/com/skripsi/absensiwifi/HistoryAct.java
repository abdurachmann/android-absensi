package com.skripsi.absensiwifi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class HistoryAct extends AppCompatActivity {
    ImageView btn_back;
    EditText date, date_to;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        date = findViewById(R.id.date);
        date_to = findViewById(R.id.date_to);
        btn_back = findViewById(R.id.btn_back);

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
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
    }
}