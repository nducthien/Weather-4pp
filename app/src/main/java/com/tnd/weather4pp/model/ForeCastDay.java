package com.tnd.weather4pp.model;

public class ForeCastDay {
    private int code;
    private String day;
    private int high;
    private int low;
    private String text;

    public ForeCastDay(){

    }

    public ForeCastDay(int code, String day, int high, int low, String text) {
        this.code = code;
        this.day = day;
        this.high = high;
        this.low = low;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
