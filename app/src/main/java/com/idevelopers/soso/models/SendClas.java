package com.idevelopers.soso.models;

/**
 * Created by Gio on 11/15/2016.
 */

public class SendClas {
    public double latitude;
    public double longitude;
    private String number;

    public SendClas(double latitude, double longitude, String number) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.number = number;
    }
}
