package com.fayaz.guidie.dto;

import java.util.List;

public class YelpSearchResponse {

    private List<YelpBusinessResponse> businesses;
    private long total;
    private YelpRegionResponse region;

    public List<YelpBusinessResponse> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<YelpBusinessResponse> businesses) {
        this.businesses = businesses;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public YelpRegionResponse getRegion() {
        return region;
    }

    public void setRegion(YelpRegionResponse region) {
        this.region = region;
    }
}
