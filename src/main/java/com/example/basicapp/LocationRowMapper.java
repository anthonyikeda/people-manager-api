package com.example.basicapp;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationRowMapper implements RowMapper<LocationDAO> {

    @Override
    public LocationDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
        var location = new LocationDAO();
        location.setLocationId(rs.getLong("location_id"));
        location.setLocationCity(rs.getString("location_city"));
        location.setLocationMarket(rs.getString("location_market"));
        return location;
    }
}
