package com.agmcs.countdown;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agmcs.countdown.db.EventDB;
import com.agmcs.countdown.model.Event;
import com.gc.materialdesign.views.ButtonFlat;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;


public class DetailActivity extends ActionBarActivity {
    private TextView detail_title,detail_note,detail_days,detail_sol;

    private ButtonFlat detail_edit_delete,detail_edit_ok,edit_date,detail_ok,detail_edit;
    private RadioGroup edit_color_group;
    private EditText edit_title,edit_note;
    private RelativeLayout detail_show_layout;
    private LinearLayout detail_edit_layout;
    private Calendar calendar;
    private Event e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detail_show_layout = (RelativeLayout)findViewById(R.id.detail_show_layout);
        detail_edit_layout = (LinearLayout)findViewById(R.id.detail_edit_layout);
        detail_title = (TextView)findViewById(R.id.detail_title);
        detail_note = (TextView)findViewById(R.id.detail_note);
        detail_days = (TextView)findViewById(R.id.detail_days);
        detail_sol = (TextView)findViewById(R.id.detail_since_or_left);
        detail_ok = (ButtonFlat)findViewById(R.id.detail_ok);
        detail_edit = (ButtonFlat)findViewById(R.id.detail_edit);

        detail_edit_delete = (ButtonFlat)findViewById(R.id.detail_edit_delete);
        detail_edit_ok = (ButtonFlat)findViewById(R.id.detail_edit_ok);
        edit_date = (ButtonFlat)findViewById(R.id.edit_date);
        edit_color_group = (RadioGroup)findViewById(R.id.edit_color_group);
        edit_title = (EditText)findViewById(R.id.edit_title);
        edit_note = (EditText)findViewById(R.id.edit_note);


        Intent i = getIntent();
        e = (Event)i.getSerializableExtra("EVENT");
        Date now = new Date();
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(e.getDate());

        double days_double = (e.getDate() - now.getTime())/(1000 * 60 * 60 * 24.0);
        int days = (int)Math.ceil(days_double);
        if(days>0){
            detail_sol.setText("还有");
            detail_days.setText(String.valueOf(days));
        }else{
            detail_sol.setText("已经过了");
            detail_days.setText(String.valueOf(-1 *days));
        }
        detail_title.setText(e.getTitle());
        detail_note.setText(e.getNote());

        switch(e.getColor()){
            case 1:{
                detail_title.setTextColor(Color.parseColor("#F06292"));
                detail_days.setTextColor(Color.parseColor("#F06292"));
                break;
            }
            case 2:{
                detail_title.setTextColor(Color.parseColor("#26A69A"));
                detail_days.setTextColor(Color.parseColor("#26A69A"));

                break;
            }
            case 3:{
                detail_title.setTextColor(Color.parseColor("#FFCA28"));
                detail_days.setTextColor(Color.parseColor("#FFCA28"));

                break;
            }
            case 4:{
                detail_title.setTextColor(Color.parseColor("#FF7043"));
                detail_days.setTextColor(Color.parseColor("#FF7043"));
                break;
            }
        }

        detail_ok .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detail_show_layout.setVisibility(View.GONE);
                detail_edit_layout.setVisibility(View.VISIBLE);
                edit_title.setText(e.getTitle());
                edit_note.setText(e.getNote());

            }
        });
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year,monthOfYear,dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        detail_edit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_title.getText().toString();
                String note = edit_note.getText().toString();
                if(TextUtils.isEmpty(title)){
                    new AlertDialog.Builder(DetailActivity.this)
                            .setTitle("Wrong")
                            .setMessage("has not set title")
                            .show();
                    return;
                }
                edit_title.setText("");
                edit_note.setText("");
                e.setDate(calendar.getTimeInMillis());
                e.setTitle(title);
                e.setNote(note);
                switch (edit_color_group.getCheckedRadioButtonId()){
                    case R.id.color1:{
                        e.setColor(1);
                        break;
                    }
                    case R.id.color2:{
                        e.setColor(2);
                        break;
                    }
                    case R.id.color3:{
                        e.setColor(3);
                        break;
                    }
                    case R.id.color4:{
                        e.setColor(4);
                        break;
                    }
                }
                EventDB.getInstance(DetailActivity.this).updateEvent(e);
                Intent i = new Intent();
                i.putExtra("EVENT",e);
                setResult(1, i);
                finish();
            }
        });

        detail_edit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                EventDB.getInstance(DetailActivity.this).deleteEvent(e);
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
