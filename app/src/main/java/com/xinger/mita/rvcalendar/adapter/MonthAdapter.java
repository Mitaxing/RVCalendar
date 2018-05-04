package com.xinger.mita.rvcalendar.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinger.mita.rvcalendar.R;
import com.xinger.mita.rvcalendar.entity.MonthEntity;

import java.util.List;

/**
 * @author MiTa
 * @date 2017/11/20.
 */
public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.CalendarViewHolder> {

    private Context context;
    private List<MonthEntity> list;

    public MonthAdapter(Context context, List<MonthEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
        return new CalendarViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        holder.mTvTitle.setText(list.get(position).getTitle());
        GridLayoutManager glm = new GridLayoutManager(context, 7) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        glm.setAutoMeasureEnabled(true);
        final DateAdapter adapter = new DateAdapter(context, list.get(position).getList());
        adapter.setClickListener(new DateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(int parentPos, int pos) {
                if (childClickListener != null) {
                    childClickListener.onMonthClick(parentPos, pos);
                }
            }
        });
        holder.mRvCal.setAdapter(adapter);
        holder.mRvCal.setLayoutManager(glm);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {

        CalendarViewHolder(View itemView) {
            super(itemView);
            mRvCal = (RecyclerView) itemView.findViewById(R.id.rv_cal);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_cal_title);
        }

        TextView mTvTitle;
        RecyclerView mRvCal;
    }

    private static OnMonthChildClickListener childClickListener;

    public interface OnMonthChildClickListener {
        void onMonthClick(int parentPos, int pos);
    }

    public void setChildClickListener(OnMonthChildClickListener childClickListener) {
        MonthAdapter.childClickListener = childClickListener;
    }
}
