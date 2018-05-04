package com.xinger.mita.rvcalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xinger.mita.rvcalendar.R;
import com.xinger.mita.rvcalendar.adapter.MonthAdapter;
import com.xinger.mita.rvcalendar.entity.DateEntity;
import com.xinger.mita.rvcalendar.entity.MonthEntity;
import com.xinger.mita.rvcalendar.utils.Lunar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author MiTa
 * @date 2017/12/21.
 */
public class CalendarSinglePickActivity extends AppCompatActivity implements MonthAdapter.OnMonthChildClickListener{

    private final int CALENDAR_TODAY = 77;

    private RecyclerView mRvCalendar;

    private MonthAdapter adapter;

    private List<MonthEntity> monthList = new ArrayList<>();

    private int year, month, day;
    private int nowDay;
    private int lastDateSelect = -1, lastMonthSelect = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
        getViews();
        initData();
        initCalendarRv();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        nowDay = day;
        calendar.set(year, month, 1);

        for (int i = 0; i < 6; i++) {
            List<DateEntity> deList = new ArrayList<>();
            MonthEntity monthEntity = new MonthEntity();
            int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int empty = calendar.get(Calendar.DAY_OF_WEEK);
            empty = empty == 1 ? 6 : empty - 2;
            for (int j = 0; j < empty; j++) {
                DateEntity de = new DateEntity();
                de.setType(1);
                deList.add(de);
            }
            for (int j = 1; j <= maxDayOfMonth; j++) {
                DateEntity de = new DateEntity();
                if (i == 0) {
                    de.setType(j < nowDay ? 4 : 0);
                } else {
                    de.setType(0);
                }
                if (i == 0 && nowDay == j) {
                    de.setDate(CALENDAR_TODAY);
                } else {
                    de.setDate(j);
                }
                de.setParentPos(i);
                de.setDesc(Lunar.getLunarDate(year, month + 1, j));
                deList.add(de);
            }

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            monthEntity.setTitle(year + "年" + month + "月");
            monthEntity.setYear(year);
            monthEntity.setList(deList);
            monthList.add(monthEntity);

            calendar.add(Calendar.MONTH, 1);
        }

    }

    private void initCalendarRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRvCalendar.setLayoutManager(llm);
        adapter = new MonthAdapter(this, monthList);
        adapter.setChildClickListener(this);
        mRvCalendar.setAdapter(adapter);
    }

    private void getViews() {
        mRvCalendar = (RecyclerView) findViewById(R.id.rv_calendar);
    }

    @Override
    public void onMonthClick(int parentPos, int pos) {
        if (parentPos == lastMonthSelect && pos == lastDateSelect) {
            return;
        }
        monthList.get(parentPos).getList().get(pos).setType(8);
        adapter.notifyItemChanged(parentPos);
        if (lastDateSelect != -1) {
            monthList.get(lastMonthSelect).getList().get(lastDateSelect).setType(0);
            adapter.notifyItemChanged(lastMonthSelect);
        }
        lastMonthSelect = parentPos;
        lastDateSelect = pos;
    }

}
