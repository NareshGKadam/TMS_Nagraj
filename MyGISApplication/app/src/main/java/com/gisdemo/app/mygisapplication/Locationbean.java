package com.gisdemo.app.mygisapplication;

import com.google.android.gms.maps.model.Marker;

public class Locationbean {

    String status;
    String amt;
    double lat;
    double lon;
    double totDue;
    String type;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    Marker marker;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public double gettotDue() {
        return totDue;
    }

    public void settotDue(double totDue) {
        this.totDue = totDue;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }


}
