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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    private PieChart pieChart;
    private LineChart lineChart;
    private LineChart incomeLineChart;
    private LineChart combinedLineChart;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart = view.findViewById(R.id.pie_chart);
        lineChart = view.findViewById(R.id.line_chart);
        incomeLineChart = view.findViewById(R.id.income_line_chart);
        combinedLineChart = view.findViewById(R.id.combined_line_chart);
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
        loadAnalyticsData();
        return view;
    }

    private void loadAnalyticsData() {
        // Fetch both expenses and income, aggregate and update all charts
        expenseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot expenseSnap) {
                incomeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot incomeSnap) {
                        // --- Expense Aggregation ---
                        Map<String, Float> categoryTotals = new HashMap<>();
                        Map<String, Float> expenseDateTotals = new HashMap<>();
                        Map<String, Float> expenseMonthTotals = new HashMap<>();
                        Map<String, Float> expenseYearTotals = new HashMap<>();
                        // --- Income Aggregation ---
                        Map<String, Float> incomeDateTotals = new HashMap<>();
                        Map<String, Float> incomeMonthTotals = new HashMap<>();
                        Map<String, Float> incomeYearTotals = new HashMap<>();
                        // --- Combined ---
                        Map<String, Float> combinedDateTotals = new HashMap<>();
                        Map<String, Float> combinedIncomeDateTotals = new HashMap<>();
                        Map<String, Float> combinedExpenseDateTotals = new HashMap<>();

                        // Filter window
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

                        // --- Expenses ---
                        for (DataSnapshot ds : expenseSnap.getChildren()) {
                            Data data = ds.getValue(Data.class);
                            if (data == null) continue;
                            try {
                                Date d = firebaseFormat.parse(data.getDate());
                                if (d == null) continue;
                                long millis = d.getTime();
                                if (millis < startMillis || millis > endMillis) continue;
                                // Category
                                String type = data.getType();
                                float amount = data.getAmount();
                                categoryTotals.put(type, categoryTotals.getOrDefault(type, 0f) + amount);
                                // Date
                                String dayKey = chartFormat.format(d);
                                String monthKey = monthYearFormat.format(d);
                                String yearKey = yearFormat.format(d);
                                expenseDateTotals.put(dayKey, expenseDateTotals.getOrDefault(dayKey, 0f) + amount);
                                expenseMonthTotals.put(monthKey, expenseMonthTotals.getOrDefault(monthKey, 0f) + amount);
                                expenseYearTotals.put(yearKey, expenseYearTotals.getOrDefault(yearKey, 0f) + amount);
                                // Combined
                                combinedDateTotals.put(dayKey, combinedDateTotals.getOrDefault(dayKey, 0f) - amount);
                                combinedExpenseDateTotals.put(dayKey, combinedExpenseDateTotals.getOrDefault(dayKey, 0f) + amount);
                            } catch (Exception ignored) {}
                        }
                        // --- Income ---
                        for (DataSnapshot ds : incomeSnap.getChildren()) {
                            Data data = ds.getValue(Data.class);
                            if (data == null) continue;
                            try {
                                Date d = firebaseFormat.parse(data.getDate());
                                if (d == null) continue;
                                long millis = d.getTime();
                                if (millis < startMillis || millis > endMillis) continue;
                                float amount = data.getAmount();
                                String dayKey = chartFormat.format(d);
                                String monthKey = monthYearFormat.format(d);
                                String yearKey = yearFormat.format(d);
                                incomeDateTotals.put(dayKey, incomeDateTotals.getOrDefault(dayKey, 0f) + amount);
                                incomeMonthTotals.put(monthKey, incomeMonthTotals.getOrDefault(monthKey, 0f) + amount);
                                incomeYearTotals.put(yearKey, incomeYearTotals.getOrDefault(yearKey, 0f) + amount);
                                // Combined
                                combinedDateTotals.put(dayKey, combinedDateTotals.getOrDefault(dayKey, 0f) + amount);
                                combinedIncomeDateTotals.put(dayKey, combinedIncomeDateTotals.getOrDefault(dayKey, 0f) + amount);
                            } catch (Exception ignored) {}
                        }

                        // Update charts
                        updatePieChart(categoryTotals);
                        updateLineChart(expenseDateTotals);
                        updateIncomeLineChart(incomeDateTotals);
                        updateCombinedLineChart(combinedIncomeDateTotals, combinedExpenseDateTotals);
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void setupFilterControls() {
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = parent.getItemAtPosition(position).toString();
                boolean custom = filterType.equals("Custom Range");
                btnStartDate.setVisibility(custom ? View.VISIBLE : View.GONE);
                btnEndDate.setVisibility(custom ? View.VISIBLE : View.GONE);
                if (!custom) loadAnalyticsData();
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
            if (customStart > 0 && customEnd > 0) loadAnalyticsData();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dlg.show();
    }

    private void updatePieChart(Map<String, Float> categoryTotals) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14f);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Expenses by Category");
        pieChart.animateY(900);
        pieChart.invalidate();
    }

    private void updateLineChart(Map<String, Float> dateTotals) {
        updateLineChartInternal(lineChart, dateTotals, "Spending Trend", Color.BLUE);
    }

    private void updateIncomeLineChart(Map<String, Float> dateTotals) {
        updateLineChartInternal(incomeLineChart, dateTotals, "Income Trend", Color.GREEN);
    }

    private void updateCombinedLineChart(Map<String, Float> incomeTotals, Map<String, Float> expenseTotals) {
        // Show both income and expense as two lines
        List<String> allDates = new ArrayList<>();
        allDates.addAll(incomeTotals.keySet());
        for (String d : expenseTotals.keySet()) if (!allDates.contains(d)) allDates.add(d);
        allDates.sort((d1, d2) -> {
            try {
                return chartFormat.parse(d1).compareTo(chartFormat.parse(d2));
            } catch (Exception e) { return d1.compareTo(d2); }
        });
        ArrayList<Entry> incomeEntries = new ArrayList<>();
        ArrayList<Entry> expenseEntries = new ArrayList<>();
        int idx = 0;
        for (String date : allDates) {
            incomeEntries.add(new Entry(idx, incomeTotals.getOrDefault(date, 0f)));
            expenseEntries.add(new Entry(idx, expenseTotals.getOrDefault(date, 0f)));
            idx++;
        }
        LineDataSet incomeSet = new LineDataSet(incomeEntries, "Income");
        incomeSet.setColor(Color.GREEN);
        incomeSet.setValueTextColor(Color.DKGRAY);
        incomeSet.setCircleColors(ColorTemplate.MATERIAL_COLORS);
        incomeSet.setLineWidth(2f);
        LineDataSet expenseSet = new LineDataSet(expenseEntries, "Expense");
        expenseSet.setColor(Color.RED);
        expenseSet.setValueTextColor(Color.DKGRAY);
        expenseSet.setCircleColors(ColorTemplate.MATERIAL_COLORS);
        expenseSet.setLineWidth(2f);
        LineData lineData = new LineData(incomeSet, expenseSet);
        combinedLineChart.setData(lineData);
        combinedLineChart.getDescription().setEnabled(false);
        combinedLineChart.animateY(900);
        combinedLineChart.invalidate();
    }

    private void updateLineChartInternal(LineChart chart, Map<String, Float> dateTotals, String label, int color) {
        // Sort dates
        List<String> sortedDates = new ArrayList<>(dateTotals.keySet());
        sortedDates.sort((d1, d2) -> {
            try {
                return chartFormat.parse(d1).compareTo(chartFormat.parse(d2));
            } catch (Exception e) { return d1.compareTo(d2); }
        });
        ArrayList<Entry> entries = new ArrayList<>();
        int idx = 0;
        for (String date : sortedDates) {
            entries.add(new Entry(idx++, dateTotals.get(date)));
        }
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setCircleColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.animateY(900);
        chart.invalidate();
    }
}
