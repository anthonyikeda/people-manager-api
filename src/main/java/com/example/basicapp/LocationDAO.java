package com.example.basicapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class LocationDAO {

    @Id
    @Column(name = "location_id")
    @JsonProperty("location_id")
    private Long locationId;

    @Column(name = "location_city")
    @JsonProperty("location_city")
    private String locationCity;

    @Column(name = "location_market")
    @JsonProperty("location_market")
    private String locationMarket;

    @Column(name = "location_deleted")
    @JsonIgnore
    private boolean deleted;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationMarket() {
        return locationMarket;
    }

    public void setLocationMarket(String locationMarket) {
        this.locationMarket = locationMarket;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
