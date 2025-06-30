package com.example.finora;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.AdapterView;
import android.app.DatePickerDialog;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.myapplication.Model.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnalyticsFragment extends Fragment {
    private BarChart expenseBarChart;
    private BarChart incomeBarChart;
    private Spinner spinnerFilter;
    private Button btnStartDate, btnEndDate;
    private String filterType = "All Time";
    private long customStart = 0, customEnd = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference expenseRef, incomeRef;
    private String uid;

    private final SimpleDateFormat firebaseFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private final SimpleDateFormat chartFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    // Only keep BarChart logic for expense and income

    private void setupRealtimeListeners() {
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot expenseSnap) {
                Map<String, Float> expenseDateTotals = new HashMap<>();
                long startMillis = 0, endMillis = Long.MAX_VALUE;
                if (filterType.equals("This Month")) {
                    Date now = new Date();
                    String monthYear = monthYearFormat.format(now);
                    try {
                        startMillis = monthYearFormat.parse(monthYear).getTime();
                        endMillis = startMillis + 31L * 24 * 60 * 60 * 1000;
                    } catch (Exception ignored) {}
                } else if (filterType.equals("This Year")) {
                    Date now = new Date();
                    String year = yearFormat.format(now);
                    try {
                        startMillis = yearFormat.parse(year).getTime();
                        endMillis = startMillis + 366L * 24 * 60 * 60 * 1000;
                    } catch (Exception ignored) {}
                } else if (filterType.equals("Custom Range")) {
                    startMillis = customStart;
                    endMillis = customEnd;
                }
                for (DataSnapshot ds : expenseSnap.getChildren()) {
                    Data data = ds.getValue(Data.class);
                    if (data == null) continue;
                    try {
                        Log.d("AnalyticsDebug", "Expense Data: " + data.getAmount() + ", " + data.getDate());
                        Date d = firebaseFormat.parse(data.getDate());
                        if (d == null) {
                            Log.e("AnalyticsDebug", "Date parse failed for: " + data.getDate());
                            continue;
                        }
                        long millis = d.getTime();
                        if (millis < startMillis || millis > endMillis) continue;
                        float amount = (float) data.getAmount();
                        String dayKey = chartFormat.format(d);
                        expenseDateTotals.put(dayKey, expenseDateTotals.getOrDefault(dayKey, 0f) + amount);
                    } catch (Exception e) {
                        Log.e("AnalyticsDebug", "Expense parse error: " + e.getMessage());
                    }
                }
                updateExpenseBarChart(expenseDateTotals);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot incomeSnap) {
                Map<String, Float> incomeDateTotals = new HashMap<>();
                long startMillis = 0, endMillis = Long.MAX_VALUE;
                if (filterType.equals("This Month")) {
                    Date now = new Date();
                    String monthYear = monthYearFormat.format(now);
                    try {
                        startMillis = monthYearFormat.parse(monthYear).getTime();
                        endMillis = startMillis + 31L * 24 * 60 * 60 * 1000;
                    } catch (Exception ignored) {}
                } else if (filterType.equals("This Year")) {
                    Date now = new Date();
                    String year = yearFormat.format(now);
                    try {
                        startMillis = yearFormat.parse(year).getTime();
                        endMillis = startMillis + 366L * 24 * 60 * 60 * 1000;
                    } catch (Exception ignored) {}
                } else if (filterType.equals("Custom Range")) {
                    startMillis = customStart;
                    endMillis = customEnd;
                }
                for (DataSnapshot ds : incomeSnap.getChildren()) {
                    Data data = ds.getValue(Data.class);
                    if (data == null) continue;
                    try {
                        Date d = firebaseFormat.parse(data.getDate());
                        if (d == null) continue;
                        long millis = d.getTime();
                        if (millis < startMillis || millis > endMillis) continue;
                        float amount = (float) data.getAmount();
                        String dayKey = chartFormat.format(d);
                        incomeDateTotals.put(dayKey, incomeDateTotals.getOrDefault(dayKey, 0f) + amount);
                    } catch (Exception ignored) {}
                }
                updateIncomeBarChart(incomeDateTotals);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        
        
        expenseBarChart = view.findViewById(R.id.expense_bar_chart);
        incomeBarChart = view.findViewById(R.id.income_bar_chart);
        spinnerFilter = view.findViewById(R.id.spinner_filter);
        btnStartDate = view.findViewById(R.id.btn_start_date);
        btnEndDate = view.findViewById(R.id.btn_end_date);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) return view;
        uid = mUser.getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        incomeRef = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        setupFilterControls();
        setupRealtimeListeners();
        return view;
    }

    private void setupFilterControls() {
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = parent.getItemAtPosition(position).toString();
                boolean custom = filterType.equals("Custom Range");
                btnStartDate.setVisibility(custom ? View.VISIBLE : View.GONE);
                btnEndDate.setVisibility(custom ? View.VISIBLE : View.GONE);
                if (!custom) setupRealtimeListeners();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isStart) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(getContext(), (view, year, month, day) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, month, day, 0, 0, 0);
            if (isStart) {
                customStart = picked.getTimeInMillis();
                btnStartDate.setText("Start: " + chartFormat.format(picked.getTime()));
            } else {
                customEnd = picked.getTimeInMillis();
                btnEndDate.setText("End: " + chartFormat.format(picked.getTime()));
            }
            if (customStart > 0 && customEnd > 0) setupRealtimeListeners();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dlg.show();
    }

    private void updateExpenseBarChart(Map<String, Float> expenseDateTotals) {
        // Sort dates
        List<String> sortedDates = new ArrayList<>(expenseDateTotals.keySet());
        sortedDates.sort((d1, d2) -> {
            try {
                return chartFormat.parse(d1).compareTo(chartFormat.parse(d2));
            } catch (Exception e) { return d1.compareTo(d2); }
        });
        ArrayList<BarEntry> entries = new ArrayList<>();
        int idx = 0;
        for (String date : sortedDates) {
            entries.add(new BarEntry(idx++, expenseDateTotals.get(date)));
        }
        // If no data, show a placeholder bar
        if (entries.isEmpty()) {
            entries.add(new BarEntry(0, 0f));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setValueTextSize(14f);
        BarData data = new BarData(dataSet);
        expenseBarChart.setData(data);
        expenseBarChart.getDescription().setEnabled(false);
        expenseBarChart.getXAxis().setDrawGridLines(false);
        expenseBarChart.getAxisRight().setEnabled(false);
        expenseBarChart.animateY(900);
        expenseBarChart.invalidate();
    }

    private void updateIncomeBarChart(Map<String, Float> incomeDateTotals) {
        List<String> sortedDates = new ArrayList<>(incomeDateTotals.keySet());
        sortedDates.sort((d1, d2) -> {
            try {
                return chartFormat.parse(d1).compareTo(chartFormat.parse(d2));
            } catch (Exception e) { return d1.compareTo(d2); }
        });
        ArrayList<BarEntry> entries = new ArrayList<>();
        int idx = 0;
        for (String date : sortedDates) {
            entries.add(new BarEntry(idx++, incomeDateTotals.get(date)));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Income");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setValueTextSize(14f);
        BarData data = new BarData(dataSet);
        incomeBarChart.setData(data);
        incomeBarChart.getDescription().setEnabled(false);
        incomeBarChart.getXAxis().setDrawGridLines(false);
        incomeBarChart.getAxisRight().setEnabled(false);
        incomeBarChart.animateY(900);
        incomeBarChart.invalidate();
    }
}
