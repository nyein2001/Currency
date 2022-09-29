package com.neversitup.currency.model;

public class CurrencyRate {

    double dollar,pound,euro;
    String time;

    public void setDollar(double dollar) {
        this.dollar = dollar;
    }

    public double getDollar() {
        return dollar;
    }

    public void setPound(double pound) {
        this.pound = pound;
    }

    public double getPound() {
        return pound;
    }

    public void setEuro(double euro) {
        this.euro = euro;
    }

    public double getEuro() {
        return euro;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
