package com.erstream.horizontalpicker;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.Collections;


public class HorizontalPickerAdapter extends RecyclerView.Adapter<HorizontalPickerAdapter.ViewHolder> {

    private static final long DAY_MILLIS = AlarmManager.INTERVAL_DAY;
    private final int mBackgroundColor;
    private final int mDateSelectedTextColor;
    private final int mDateSelectedColor;
    private final int mTodayDateTextColor;
    private final int mTodayDateBackgroundColor;
    private final int mDayOfWeekTextColor;
    private final int mUnselectedDayTextColor;
    private int itemWidth;
    private final OnItemClickedListener listener;
    private ArrayList<Day> items;
    DateTime now,end,start,test;
    boolean btnAll;
    int today,year,month;


    public HorizontalPickerAdapter(boolean btnAll,int itemWidth, OnItemClickedListener listener, Context context, int mBackgroundColor, int mDateSelectedColor, int mDateSelectedTextColor, int mTodayDateTextColor, int mTodayDateBackgroundColor, int mDayOfWeekTextColor, int mUnselectedDayTextColor,boolean direction,int howManyDays) {
        items=new ArrayList<>();
        this.itemWidth=itemWidth;
        this.listener=listener;
        this.btnAll=btnAll;
        generateDays(false,direction,howManyDays);
        this.mBackgroundColor=mBackgroundColor;
        this.mDateSelectedTextColor=mDateSelectedTextColor;
        this.mDateSelectedColor=mDateSelectedColor;
        this.mTodayDateTextColor=mTodayDateTextColor;
        this.mTodayDateBackgroundColor=mTodayDateBackgroundColor;
        this.mDayOfWeekTextColor=mDayOfWeekTextColor;
        this.mUnselectedDayTextColor=mUnselectedDayTextColor;
    }

    public  void generateDays(boolean cleanArray,boolean direction,int howManyDays) {
        if(cleanArray)
            items.clear();

        now = new DateTime();
        today = now.toLocalDate().getDayOfMonth();
        year = now.getYear();
        month = now.toLocalDate().getMonthOfYear();

        if (howManyDays>0){
            if (btnAll==false){
                start = new DateTime(year, month, today, 0, 0, 0, 0);
                if (today-howManyDays>0){
                    end =  new DateTime(year, month, today-howManyDays, 0, 0, 0, 0);
                }else {
                    int daySize=today-howManyDays;
                    if (month-1==0)
                        end =  new DateTime(year-1, 12, 1, 0, 0, 0, 0);
                    else
                        end =  new DateTime(year, month-1, 1, 0, 0, 0, 0);
                    int gün=end.dayOfMonth().getMaximumValue();
                    if (month-1==0)
                        end =  new DateTime(year-1, 12, gün+daySize, 0, 0, 0, 0);
                    else
                        end =  new DateTime(year, month-1, gün+daySize, 0, 0, 0, 0);
                }

                int days = Days.daysBetween(end,start).getDays();

                for(int j = 1; j <=days; j++) {
                    Day day=new Day(end.plusDays(j));
                    if (String.valueOf(day.getDay())==String.valueOf(today)){
                        day.setSelected(true);
                        if (direction==true){
                            HorizontalPickerRecyclerView.lastPosition=j;
                        }
                    }
                    items.add(day);
                }
                if (direction==false){
                    Collections.reverse(items);
                }
            }else if (btnAll==true){
                test = new DateTime(year, month, today, 0, 0, 0, 0);
                int monthDay=test.dayOfMonth().getMaximumValue();
                if (monthDay==today&&month==12){
                    start = new DateTime(year+1, 1, 1, 0, 0, 0, 0);
                }else if (today<monthDay){
                    start = new DateTime(year, month, today+1, 0, 0, 0, 0);
                }

                if (today-howManyDays>0){
                    end =  new DateTime(year, month, today-howManyDays, 0, 0, 0, 0);
                }else {
                    int daySize=today-howManyDays;
                    if (month-1==0)
                        end =  new DateTime(year-1, 12, 1, 0, 0, 0, 0);
                    else
                        end =  new DateTime(year, month-1, 1, 0, 0, 0, 0);
                    int gün=end.dayOfMonth().getMaximumValue();
                    if (month-1==0)
                        end =  new DateTime(year-1, 12, gün+daySize, 0, 0, 0, 0);
                    else
                        end =  new DateTime(year, month-1, gün+daySize, 0, 0, 0, 0);
                }

                int days = Days.daysBetween(end,start).getDays();

                for(int j = 1; j <=days; j++) {
                    Day day=new Day(end.plusDays(j));
                    if (String.valueOf(day.getDay())==String.valueOf(today+1)){
                        day.setSelected(true);
                        if (direction==true){
                            HorizontalPickerRecyclerView.lastPosition=j;
                        }
                    }
                    items.add(day);
                }
                if (direction==false){
                    Collections.reverse(items);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_day,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day item=getItem(position);
        holder.tvDay.setText(item.getDay());
        if (item.getDay()==String.valueOf(today+1)){
            holder.tvDay.setText("ALL");
            holder.tvWeekDay.setText("");
        }else if (item.getDay()==String.valueOf(today)){
            holder.tvWeekDay.setText("Today");
        }else if(item.getDay()==String.valueOf(today-1)){
            holder.tvWeekDay.setText("Yesterday ");
        }else{
            String day=item.getWeekDay().toLowerCase();
            String output = day.substring(0, 1).toUpperCase() + day.substring(1);
            holder.tvWeekDay.setText(output);
        }
        holder.tvWeekDay.setTextColor(mDayOfWeekTextColor);
        if(item.isSelected())
        {
            holder.layoutSelected.setVisibility(View.VISIBLE);
            holder.tvDay.setTextColor(Color.parseColor("#1389c9"));
            holder.tvWeekDay.setTextColor(Color.parseColor("#1389c9"));
        }else{
            holder.layoutSelected.setVisibility(View.INVISIBLE);
            holder.tvDay.setTextColor(Color.parseColor("#e4e4e4"));
            holder.tvWeekDay.setTextColor(Color.parseColor("#e4e4e4"));
        }
    }

    private Drawable getDaySelectedBackground(View view) {
        Drawable drawable=view.getResources().getDrawable(R.drawable.background_day_selected);
        DrawableCompat.setTint(drawable,mDateSelectedColor);
        return drawable;
    }

    private Drawable getDayTodayBackground(View view) {
        Drawable drawable=view.getResources().getDrawable(R.drawable.background_day_today);
        if(mTodayDateBackgroundColor!=-1)
            DrawableCompat.setTint(drawable,mTodayDateBackgroundColor);
        return drawable;
    }

    public Day getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay,tvWeekDay;
        LinearLayout layoutSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDay= (TextView) itemView.findViewById(R.id.tvDay);
            layoutSelected= (LinearLayout) itemView.findViewById(R.id.layoutSelected);
            tvDay.setWidth(itemWidth);
            tvWeekDay= (TextView) itemView.findViewById(R.id.tvWeekDay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClickView(v,getAdapterPosition());
        }
    }
}