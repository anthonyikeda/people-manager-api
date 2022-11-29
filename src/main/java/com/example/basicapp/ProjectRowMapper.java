package com.example.basicapp;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectRowMapper implements RowMapper<ProjectDAO> {
    @Override
    public ProjectDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
        var location = new LocationDAO();
        location.setLocationId(rs.getLong("location_id"));
        location.setLocationCity(rs.getString("location_city"));
        location.setLocationMarket(rs.getString("location_market"));

        var project = new ProjectDAO();
        project.setProjectId(rs.getLong("project_id"));
        project.setName(rs.getString("project_name"));
        project.setLocation(location);

        return project;
    }
}
