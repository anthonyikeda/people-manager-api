package com.example.basicapp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationResultSetExtractor implements ResultSetExtractor<LocationDAO> {
    @Override
    public LocationDAO extractData(ResultSet rs) throws SQLException, DataAccessException {
        if(rs.next()) {
            var location = new LocationDAO();
            location.setLocationId(rs.getLong("location_id"));
            location.setLocationCity(rs.getString("location_city"));
            location.setLocationMarket(rs.getString("location_market"));
            return location;
        } else {
            return null;
        }
    }
}
