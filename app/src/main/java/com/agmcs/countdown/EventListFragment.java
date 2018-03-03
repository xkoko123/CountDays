package com.agmcs.countdown;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agmcs.countdown.db.EventDB;
import com.agmcs.countdown.model.Event;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import github.chenupt.dragtoplayout.DragTopLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends android.support.v4.app.Fragment {
    private ListView listView;
    private List<Event> list;
    private EditText title_ET;
    private EditText note_ET;
    private RadioGroup color_group;
    private ButtonFlat date_btn,ok_btn;
    private Calendar c;
    private EventDB eventDB;
    private DragTopLayout dtl;
    private Adapter adapter;



    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case 1:{
                Event e = (Event)data.getSerializableExtra("EVENT");
                try {
                    list.set(requestCode,e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                break;
            }
            case 2:{
                try {
                    list.remove(requestCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        dtl = (DragTopLayout)view.findViewById(R.id.dtl);

        listView = (ListView)view.findViewById(R.id.listview);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view != null && view.getChildCount() > 0) {
                    if (view.getChildAt(0).getTop() < 0 || view.getFirstVisiblePosition()!=0) {
                        dtl.setTouchMode(false);
                    }else{
                        dtl.setTouchMode(true);
                    }
                }

            }
        });


        eventDB = EventDB.getInstance(getActivity());
        list = eventDB.loadEvent();

        adapter = new Adapter(getActivity(),R.layout.card_item,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = (Event)(parent.getItemAtPosition(position));
                Intent i = new Intent(getActivity(),DetailActivity.class);
                i.putExtra("EVENT",e);
                startActivityForResult(i, position);
            }
        });



        title_ET = (EditText)view.findViewById(R.id.title_et);
        note_ET = (EditText)view.findViewById(R.id.note_et);
        color_group = (RadioGroup)view.findViewById(R.id.color_group);
        date_btn = (ButtonFlat)view.findViewById(R.id.date_btn);
        ok_btn = (ButtonFlat)view.findViewById(R.id.ok_btn);
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c= Calendar.getInstance();
                        c.set(year,monthOfYear,dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_ET.getText().toString();
                String note = note_ET.getText().toString();
                if (c == null){
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("Wrong")
//                            .setMessage("has not set date")
//                            .show();
                    new Dialog(getActivity(),"Hey","还没设置日期呀").show();
                    return;
                }
                if(TextUtils.isEmpty(title)){
                    new Dialog(getActivity(),"Hey","还没设置标题呀").show();
                    return;
                }
                title_ET.setText("");
                note_ET.setText("");
                Event e = new Event();
                e.setDate(c.getTimeInMillis());
                e.setTitle(title);
                e.setNote(note);
                switch (color_group.getCheckedRadioButtonId()){
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
                eventDB.saveEvent(e);
                list.add(0,e);
                adapter.notifyDataSetChanged();
                dtl.closeTopView(true);
                c = null;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("hihihi", "resume");
    }


    public class Adapter extends ArrayAdapter<Event> {
        private int resId;

        public Adapter(Context context, int resource, List<Event> list) {
            super(context, resource, list);
            resId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resId,null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView)view.findViewById(R.id.card_title);
                viewHolder.date = (TextView)view.findViewById(R.id.card_time);
                viewHolder.note = (TextView)view.findViewById(R.id.card_note);
                viewHolder.days = (TextView)view.findViewById(R.id.card_days);
                viewHolder.since_or_left = (TextView)view.findViewById(R.id.since_or_left);
                viewHolder.color = (RelativeLayout)view.findViewById(R.id.card_color);

                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(list.get(position).getDate())));
            Date now = new Date();
            double days = (list.get(position).getDate() - now.getTime())/(1000 * 60 * 60 * 24.0);
            int i = (int)Math.ceil(days);
            if(i>0){
                viewHolder.since_or_left.setText("Days left");
                viewHolder.days.setText(String.valueOf(i));
            }else{
                viewHolder.since_or_left.setText("Days since");
                viewHolder.days.setText(String.valueOf(-1 *i));
            }
            switch(event.getColor()){
                case 1:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#F06292"));
                    break;
                }
                case 2:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#26A69A"));
                    break;
                }
                case 3:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#FFCA28"));
                    break;
                }
                case 4:{
                    viewHolder.color.setBackgroundColor(Color.parseColor("#FF7043"));
                    break;
                }
            }

            viewHolder.note.setText(list.get(position).getNote());
            return view;
        }

        public class ViewHolder{
            public TextView title;
            public TextView date;
            public TextView note;
            public RelativeLayout color;
            public TextView days;
            public TextView since_or_left;
        }
    }

}
