package com.fayaz.guidie.dto;

import java.util.List;

public class WeatherResponse {
    private String cod;
    private long message;

    private long cnt;

    private List<DayResponse> list;

    private CityResponse city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public long getMessage() {
        return message;
    }

    public void setMessage(long message) {
        this.message = message;
    }

    public long getCnt() {
        return cnt;
    }

    public void setCnt(long cnt) {
        this.cnt = cnt;
    }

    public List<DayResponse> getList() {
        return list;
    }

    public void setList(List<DayResponse> list) {
        this.list = list;
    }

    public CityResponse getCity() {
        return city;
    }

    public void setCity(CityResponse city) {
        this.city = city;
    }
}
