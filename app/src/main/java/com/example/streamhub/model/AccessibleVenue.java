package com.example.streamhub.model;

import java.io.Serializable;

public class AccessibleVenue implements Serializable {
    private final String id;
    private final String name;
    private final String distance;
    private final String description;
    private final double latitude;
    private final double longitude;
    private final String category;

    public AccessibleVenue(String id, String name, String distance, String description, double latitude, double longitude, String category) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDistance() { return distance; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCategory() { return category; }
}
