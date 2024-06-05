package com.fayaz.guidie.dto;

import com.google.gson.annotations.SerializedName;

public class YelpAttributeResponse {
    @SerializedName("business_temp_closed")

    private boolean business_temp_closed;
    @SerializedName("menu_url")

    private String menu_url;
    @SerializedName("open24_hours")

    private boolean open24_hours;
    @SerializedName("waitlist_reservation")

    private boolean waitlist_reservation;
}
