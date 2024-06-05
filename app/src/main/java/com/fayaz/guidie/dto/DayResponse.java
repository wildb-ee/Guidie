package com.fayaz.guidie.dto;

import java.util.List;

public class DayResponse {
    private long dt;
    private MainResponse main;
    private List<WeatherIconResponse> weather;
    private CloudResponse clouds;
    private WindResponse wind;

    private long visibility;

    private double pop;
    private SysResponse sys;

    private String dt_txt;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public MainResponse getMain() {
        return main;
    }

    public void setMain(MainResponse main) {
        this.main = main;
    }

    public List<WeatherIconResponse> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherIconResponse> weather) {
        this.weather = weather;
    }

    public CloudResponse getClouds() {
        return clouds;
    }

    public void setClouds(CloudResponse clouds) {
        this.clouds = clouds;
    }

    public WindResponse getWind() {
        return wind;
    }

    public void setWind(WindResponse wind) {
        this.wind = wind;
    }

    public long getVisibility() {
        return visibility;
    }

    public void setVisibility(long visibility) {
        this.visibility = visibility;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public SysResponse getSys() {
        return sys;
    }

    public void setSys(SysResponse sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
