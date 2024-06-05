package com.fayaz.guidie.dto;

import com.google.gson.annotations.SerializedName;

public class YelpCoordinateResponse {

    @SerializedName("latitude")

    private double latitude;

    @SerializedName("longitude")

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
