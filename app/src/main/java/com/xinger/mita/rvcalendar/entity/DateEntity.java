package com.xinger.mita.rvcalendar.entity;

/**
 * @author MiTa
 * @date 2017/11/20.
 */
public class DateEntity {

    /**
     * 0：常规状态
     * 1:1号前面的空余
     * 2:周末
     * 3:点击选中
     * 4:不可选
     * 5:中间选中
     * 6：终点
     * 7:起点
     */
    private int type;
    private int date;
    private int parentPos;
    private String desc;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }
}
