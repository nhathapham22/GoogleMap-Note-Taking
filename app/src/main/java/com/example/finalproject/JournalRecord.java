package com.example.finalproject;

public class JournalRecord {
    // declaration
    private int id;
    private String locationName;

    private String locationAddress;

    private Double latitude;
    private Double longitude;

    private String journalNotes;


    //constructor
    public JournalRecord(int id, String locationName, String locationAddress, Double latitude, Double longitude, String journalNotes) {
        this.id = id;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.journalNotes = journalNotes;
    }

    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Double getLatitude() {
        return this.latitude;
    }
    public Double getLongitude() {
        return this.longitude;
    }

    public void setLatitude(String latLng) {
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getJournalNotes() {
        return journalNotes;
    }

    public void setJournalNotes(String journalNotes) {
        this.journalNotes = journalNotes;
    }
}
