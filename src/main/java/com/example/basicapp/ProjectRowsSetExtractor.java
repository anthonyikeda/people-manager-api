package com.example.basicapp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectRowsSetExtractor implements ResultSetExtractor<ProjectDAO> {
    @Override
    public ProjectDAO extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.next()) {
            var location = new LocationDAO();
            location.setLocationId(rs.getLong("location_id"));
            location.setLocationCity(rs.getString("location_city"));
            location.setLocationMarket(rs.getString("location_market"));

            var project = new ProjectDAO();
            project.setProjectId(rs.getLong("project_id"));
            project.setName(rs.getString("project_name"));
            project.setLocation(location);

            return project;
        } else {
            return null;
        }
    }
}
