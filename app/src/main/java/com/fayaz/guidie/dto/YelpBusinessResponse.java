package com.fayaz.guidie.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpBusinessResponse {
    @SerializedName("id")

    private String id;
    @SerializedName("alias")

    private String alias;
    @SerializedName("name")

    private String name;

    @SerializedName("image_url")
    private String image_url;

    @SerializedName("is_closed")

    private boolean is_closed;

    @SerializedName("url")

    private String url;

    @SerializedName("review_count")

    private long review_count;

    @SerializedName("categories")

    private List<YelpCategoryResponse> categories;

    @SerializedName("rating")

    private double rating;
    @SerializedName("coordinates")

    private YelpCoordinateResponse coordinates;

    @SerializedName("transactions")

    private List<String> transactions;

    @SerializedName("price")

    private String price;
    @SerializedName("location")

    private YelpLocationResponse location;
    @SerializedName("phone")

    private String phone;
    @SerializedName("display_phone")

    private String display_phone;

    @SerializedName("distance")

    private double distance;
    @SerializedName("attributes")

    private YelpAttributeResponse attributes;

    public YelpBusinessResponse(String id ,String name, String image_url, List<YelpCategoryResponse> categories, double rating) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.categories = categories;
        this.rating = rating;
    }

    public YelpBusinessResponse(String id, String name, String image_url, List<YelpCategoryResponse> categories, double rating, YelpLocationResponse location) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.categories = categories;
        this.rating = rating;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getReviev_count() {
        return review_count;
    }

    public void setReviev_count(long review_count) {
        this.review_count = review_count;
    }

    public List<YelpCategoryResponse> getCategories() {
        return categories;
    }

    public void setCategories(List<YelpCategoryResponse> categories) {
        this.categories = categories;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public YelpCoordinateResponse getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(YelpCoordinateResponse coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public YelpLocationResponse getLocation() {
        return location;
    }

    public void setLocation(YelpLocationResponse location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public void setDisplay_phone(String display_phone) {
        this.display_phone = display_phone;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public YelpAttributeResponse getAttributes() {
        return attributes;
    }

    public void setAttributes(YelpAttributeResponse attributes) {
        this.attributes = attributes;
    }
}
