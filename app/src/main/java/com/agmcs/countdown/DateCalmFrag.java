package com.agmcs.countdown;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateCalmFrag extends android.support.v4.app.Fragment implements View.OnClickListener {
    private ButtonFlat from_date,to_date,add_sub_date;
    private TextView calm_result,days2_result,days1_result;
    private EditText days1,days2;
    private Calendar now,from,to,add_sub;
    private SimpleDateFormat sdf;


    public DateCalmFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_calm, container, false);
        from_date = (ButtonFlat)view.findViewById(R.id.from_date);
        to_date = (ButtonFlat)view.findViewById(R.id.to_date);
        add_sub_date = (ButtonFlat)view.findViewById(R.id.add_sub_date);
        calm_result = (TextView)view.findViewById(R.id.calm_result);
        days2_result = (TextView)view.findViewById(R.id.days2_result);
        days1_result = (TextView)view.findViewById(R.id.days1_result);
        days1 = (EditText)view.findViewById(R.id.days1);
        days2 = (EditText)view.findViewById(R.id.days2);
        now = Calendar.getInstance();
        from = Calendar.getInstance();
        to = Calendar.getInstance();
        add_sub = Calendar.getInstance();

        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);
        add_sub_date.setOnClickListener(this);
        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String now_date = sdf.format(new Date(from.getTimeInMillis()));
        from_date.setText(now_date);
        to_date.setText(now_date);
        add_sub_date.setText(now_date);


        days1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    int offset = Integer.parseInt(s.toString());
                    Calendar days1 = Calendar.getInstance();
                    days1.setTimeInMillis(add_sub.getTimeInMillis());
                    days1.add(Calendar.DAY_OF_MONTH,offset);
                    days1_result.setText(sdf.format(new Date(days1.getTimeInMillis())));
                }else{
                    days1_result.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//
            }
        });
        days2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    int offset = Integer.parseInt(s.toString());
                    Calendar days2 = Calendar.getInstance();
                    days2.setTimeInMillis(add_sub.getTimeInMillis());
                    days2.add(Calendar.DAY_OF_MONTH,-1 * offset);
                    days2_result.setText(sdf.format(new Date(days2.getTimeInMillis())));
                }else{
                    days2_result.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.from_date:{
                new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        from.set(year,monthOfYear,dayOfMonth);
                        from_date.setText(
                                sdf.format(new Date(from.getTimeInMillis()))
                        );
                        if(to != null){
                            double days_d = (to.getTimeInMillis() - from.getTimeInMillis())/(1000 * 60 * 60 * 24.0);
                            int days = (int)Math.ceil(days_d);
                            if(days>0){
                                calm_result.setText(String.valueOf(days));
                            }else{
                                calm_result.setText(String.valueOf(-1 * days));
                            }                        }
                    }
                },from.get(Calendar.YEAR)
                        ,from.get(Calendar.MONTH)
                        ,from.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            }
            case R.id.to_date:{
                new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        to.set(year,monthOfYear,dayOfMonth);
                        to_date.setText(
                                sdf.format(new Date(to.getTimeInMillis()))
                        );
                        if(from != null){
                            double days_d = (to.getTimeInMillis() - from.getTimeInMillis())/(1000 * 60 * 60 * 24.0);
                            int days = (int)Math.ceil(days_d);
                            if(days>0){
                                calm_result.setText(String.valueOf(days));
                            }else{
                                calm_result.setText(String.valueOf(-1 * days));
                            }
                        }
                    }
                },to.get(Calendar.YEAR)
                        ,to.get(Calendar.MONTH)
                        ,to.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            }
            case R.id.add_sub_date:{
                new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        add_sub.set(year,monthOfYear,dayOfMonth);
                        add_sub_date.setText(
                                new SimpleDateFormat("yyyy年MM月dd日").format(new Date(add_sub.getTimeInMillis()))
                        );
                        String s1 = days1.getText().toString();
                        if(!TextUtils.isEmpty(s1)){
                            int offset = Integer.parseInt(s1.toString());
                            Calendar days1 = Calendar.getInstance();
                            days1.setTimeInMillis(add_sub.getTimeInMillis());
                            days1.add(Calendar.DAY_OF_MONTH,offset);
                            days1_result.setText(sdf.format(new Date(days1.getTimeInMillis())));
                        }
                        String s2 = days2.getText().toString();
                        if(!TextUtils.isEmpty(s2)){
                            int offset = Integer.parseInt(s2.toString());
                            Calendar days2 = Calendar.getInstance();
                            days2.setTimeInMillis(add_sub.getTimeInMillis());
                            days2.add(Calendar.DAY_OF_MONTH,-1 * offset);
                            days2_result.setText(sdf.format(new Date(days2.getTimeInMillis())));
                        }

                    }
                },add_sub.get(Calendar.YEAR)
                        ,add_sub.get(Calendar.MONTH)
                        ,add_sub.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            }
        }
    }
}
