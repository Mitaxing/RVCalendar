package com.xinger.mita.rvcalendar.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinger.mita.rvcalendar.R;
import com.xinger.mita.rvcalendar.entity.DateEntity;

import java.util.List;

/**
 * @author MiTa
 * @date 2017/11/20.
 */
public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private Context context;
    private List<DateEntity> list;

    public DateAdapter(Context context, List<DateEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        DateEntity de = list.get(position);
        holder.itemView.setTag(R.id.tag_parent_pos, de.getParentPos());
        holder.itemView.setTag(R.id.tag_pos, position);

        int date = de.getDate();
        int type = de.getType();

        if (type == 1) {//留白
            holder.mTvDate.setText("");
            holder.mTvDesc.setText("");
            holder.itemView.setClickable(false);
        } else if (type == 0) {//日常
            holder.mTvDate.setText(date == 77 ? "今天" : String.valueOf(de.getDate()));
            holder.mTvDate.setTextColor(date == 77 ? ContextCompat.getColor(context, R.color.blue_85) : ContextCompat.getColor(context, R.color.black_2c));
            holder.mTvDesc.setText(de.getDesc());

            int mod = position % 7;
            if (mod == 5 || mod == 6) {
                holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.color_red));
            }
        } else if (type == 3) {//日常选中
            holder.mTvDate.setText(date == 77 ? "今天" : String.valueOf(de.getDate()));
            holder.mTvDesc.setText(de.getDesc());
            holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mLlDate.setBackgroundResource(R.drawable.state_selected);
        } else if (type == 4) {//今天之前的日期
            holder.itemView.setClickable(false);
            holder.mTvDate.setText(String.valueOf(de.getDate()));
            holder.mTvDesc.setText(de.getDesc());
            holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.black_cc));
            holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.black_cc));
        } else if (type == 5) {//中间
            holder.mTvDate.setText(String.valueOf(de.getDate()));
            holder.mTvDesc.setText(de.getDesc());
            holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mLlDate.setBackgroundResource(R.drawable.state_middle_range);
        } else if (type == 6) {//终点
            holder.mTvDate.setText(String.valueOf(de.getDate()));
            holder.mTvDesc.setText("离店");
            holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mLlDate.setBackgroundResource(R.drawable.state_end_range);
        } else if (type == 7) {//起点
            holder.mTvDate.setText(date == 77 ? "今天" : String.valueOf(de.getDate()));
            holder.mTvDesc.setText("入住");
            holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mLlDate.setBackgroundResource(R.drawable.state_first_range);
        } else if (type == 8) {//单选
            holder.mTvDate.setText(date == 77 ? "今天" : String.valueOf(de.getDate()));
            holder.mTvDesc.setText(de.getDesc());
            holder.mTvDate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mTvDesc.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.mLlDate.setBackgroundResource(R.drawable.state_selected);
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        DateViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
            mTvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            mLlDate = (LinearLayout) itemView.findViewById(R.id.ll_date);
        }

        TextView mTvDate, mTvDesc;
        LinearLayout mLlDate;

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                if (view != null && view.getTag(R.id.tag_parent_pos) != null && view.getTag(R.id.tag_pos) != null) {
                    clickListener.onDateClick((Integer) view.getTag(R.id.tag_parent_pos), (Integer) view.getTag(R.id.tag_pos));
                }
            }
        }
    }

    private static OnDateClickListener clickListener;

    public interface OnDateClickListener {
        void onDateClick(int parentPos, int pos);
    }

    public void setClickListener(OnDateClickListener clickListener) {
        DateAdapter.clickListener = clickListener;
    }
}
