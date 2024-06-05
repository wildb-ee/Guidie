package com.fayaz.guidie.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpLocationResponse {

    @SerializedName("address1")

    private String address1;
    @SerializedName("address2")

    private String address2;

    @SerializedName("address3")

    private String address3;

    @SerializedName("city")

    private String city;

    @SerializedName("zip_code")

    private String zip_code;
    @SerializedName("country")

    private String country;
    @SerializedName("state")

    private String state;


    public YelpLocationResponse(String address1, String address2, String address3, String city, String zip_code, String country, String state) {
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.city = city;
        this.zip_code = zip_code;
        this.country = country;
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
