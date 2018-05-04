package com.xinger.mita.rvcalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
 * @date 2017/11/20.
 */
public class CalendarActivity extends AppCompatActivity implements MonthAdapter.OnMonthChildClickListener {

    private RecyclerView mRvCalendar;

    private MonthAdapter adapter;

    private List<MonthEntity> monthList = new ArrayList<>();

    private int year, month, day;
    private int nowDay;
    private int lastDateSelect = -1, lastMonthSelect = -1;
    private List<Integer> selectMonth = new ArrayList<>();
    private List<Integer> selectDate = new ArrayList<>();
    private boolean selectComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getViews();
        initData();
        initRv();
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
                    de.setDate(77);
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
            monthEntity.setList(deList);
            monthList.add(monthEntity);

            calendar.add(Calendar.MONTH, 1);
        }

    }

    private void initRv() {
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
        if (parentPos != lastMonthSelect || pos != lastDateSelect) {

            //1、第二次选择；2、选择的月份相等日期比之前选择的大或者选择的月份比之前的大；3、选择未完成
            boolean haveMiddle = lastMonthSelect != -1 && ((lastMonthSelect == parentPos && pos > lastDateSelect) || (parentPos > lastMonthSelect))
                    && !selectComplete;
            if (haveMiddle) {
                monthList.get(parentPos).getList().get(pos).setType(6);
                selectDate.add(1);
                monthList.get(lastMonthSelect).getList().get(lastDateSelect).setType(7);
                selectDate.add(1);

                int monthLen = parentPos - lastMonthSelect;
                List<DateEntity> list;
                int dateLen;
                if (monthLen == 0) {
                    dateLen = pos - lastDateSelect;
                    for (int i = 1; i < dateLen; i++) {
                        monthList.get(parentPos).getList().get(i + lastDateSelect).setType(5);
                        selectDate.add(1);
                    }
                    adapter.notifyItemChanged(lastMonthSelect);
                    //选择了这个月
                    selectMonth.add(parentPos);
                } else {
                    //第一个月
                    int lastMonthSize = monthList.get(lastMonthSelect).getList().size();
                    dateLen = lastMonthSize - lastDateSelect;
                    for (int i = 1; i < dateLen; i++) {
                        monthList.get(lastMonthSelect).getList().get(i + lastDateSelect).setType(5);
                        selectDate.add(1);
                    }
                    adapter.notifyItemChanged(lastMonthSelect);
                    //选择了这个月
                    selectMonth.add(lastMonthSelect);

                    //中间月份
                    int month;
                    int middleMonthLen = parentPos - lastMonthSelect;
                    for (int i = 1; i < middleMonthLen; i++) {
                        month = lastMonthSelect + i;
                        list = monthList.get(month).getList();
                        dateLen = list.size();
                        for (int j = 0; j < dateLen; j++) {
                            if (list.get(j).getType() != 1) {
                                list.get(j).setType(5);
                                selectDate.add(1);
                            }
                        }
                        adapter.notifyItemChanged(month);
                        //选择了这个月
                        selectMonth.add(month);
                    }

                    //最后那个月
                    dateLen = pos;
                    for (int i = 0; i < dateLen; i++) {
                        DateEntity de = monthList.get(parentPos).getList().get(i);
                        if (de.getType() != 1) {
                            de.setType(5);
                            selectDate.add(1);
                        }
                    }
                    adapter.notifyItemChanged(parentPos);
                    //选择了这个月
                    selectMonth.add(parentPos);
                }
                Log.d("mita", "选择的天数：" + selectDate.size());
                selectComplete = true;
                lastMonthSelect = -1;
                lastDateSelect = -1;
            } else {
                selectDate.clear();

                //清除已选
                if (selectComplete) {
                    List<DateEntity> list;
                    DateEntity de;
                    int len = selectMonth.size();
                    for (int i = 0; i < len; i++) {
                        list = monthList.get(selectMonth.get(i)).getList();
                        int size = list.size();
                        for (int j = 0; j < size; j++) {
                            de = list.get(j);
                            if (de.getType() == 5 || de.getType() == 6 || de.getType() == 7) {
                                de.setType(0);
                            }
                        }
                        adapter.notifyItemChanged(selectMonth.get(i));
                    }
                    selectMonth.clear();
                }

                monthList.get(parentPos).getList().get(pos).setType(3);
                adapter.notifyItemChanged(parentPos);
                if (lastDateSelect != -1) {
                    monthList.get(lastMonthSelect).getList().get(lastDateSelect).setType(0);
                    adapter.notifyItemChanged(lastMonthSelect);
                }
                lastMonthSelect = parentPos;
                lastDateSelect = pos;
                selectComplete = false;
            }
        }
    }
}
