package com.example.finora;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class IncomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private RecyclerView recyclerView;

    private TextView incomeTotalSum;

    ///Update edit text.

    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    //button for update and delete

    private Button btnUpdate;
    private Button btnDelete;

    //Data item value
    private String type;
    private String note;
    private int amount;

    private  String post_key;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_income, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mAuth!=null) {
            String uid = mUser.getUid();

            mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        }

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);
        recyclerView=myview.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {

            int totalvalue=0;

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot mysnapshot:dataSnapshot.getChildren()){
                    Data data=mysnapshot.getValue(Data.class);
                    totalvalue=totalvalue+data.getAmount();
                    String stTotalvalue=String.valueOf(totalvalue);
                    incomeTotalSum.setText(stTotalvalue+".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        View fab = myview.findViewById(R.id.fab_add_income);
        if (fab != null) {
            fab.setOnClickListener(v -> showAddIncomeDialog());
        }

        return  myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter= new FirebaseRecyclerAdapter<Data, MyViewHolder>(

                Data.class,
                R.layout.income_recycler_data,
                MyViewHolder.class,
                mIncomeDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder,final Data model,final int position) {

                myViewHolder.setType(model.getType());
                myViewHolder.setNote(model.getNote());
                myViewHolder.setDate(model.getDate());
                myViewHolder.setAmount(model.getAmount());

                myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(position).getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void showAddIncomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.update_data_item, null);
        builder.setView(dialogView);

        EditText edtAmount = dialogView.findViewById(R.id.amount_edt);
        EditText edtType = dialogView.findViewById(R.id.type_edt);
        Spinner typeSpinner = dialogView.findViewById(R.id.type_spinner);
        EditText edtNote = dialogView.findViewById(R.id.note_edt);
        Button btnUpdate = dialogView.findViewById(R.id.btn_upd_Update);
        Button btnDelete = dialogView.findViewById(R.id.btnuPD_Delete);
        btnDelete.setVisibility(View.GONE);
        btnUpdate.setText("Add");

        typeSpinner.setVisibility(View.VISIBLE);
        String[] categories = new String[]{"Salary", "Business", "Investment", "Gift", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        typeSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        btnUpdate.setOnClickListener(v -> {
            String amountStr = edtAmount.getText().toString().trim();
            String typeStr = edtType.getText().toString().trim();
            String noteStr = edtNote.getText().toString().trim();
            if (amountStr.isEmpty()) {
                edtAmount.setError("Required");
                return;
            }
            if (typeStr.isEmpty()) {
                typeStr = typeSpinner.getSelectedItem() != null ? typeSpinner.getSelectedItem().toString() : "Other";
            }
            int amount = Integer.parseInt(amountStr);
            String id = mIncomeDatabase.push().getKey();
            String date = java.text.DateFormat.getDateInstance().format(new java.util.Date());
            Data data = new Data(amount, typeStr, noteStr, id, date);
            mIncomeDatabase.child(id).setValue(data);
            dialog.dismiss();
        });
        dialog.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void setAmount(int amount){
            TextView mAmount=mView.findViewById(R.id.amount_txt_income);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }

    private void updateDataItem()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmount=myview.findViewById(R.id.amount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);

        //Set data to edit text..
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        btnUpdate=myview.findViewById(R.id.btn_upd_Update);
        btnDelete=myview.findViewById(R.id.btnuPD_Delete);

        AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();

                String mdAmount=String.valueOf(amount);
                mdAmount=edtAmount.getText().toString().trim();

                int myAmount=Integer.parseInt(mdAmount);

                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(myAmount,type,note,post_key,mDate);

                mIncomeDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}