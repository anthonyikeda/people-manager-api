package com.example.basicapp;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class ProjectManager {
    private final JdbcTemplate template;

    private MeterRegistry registry;


    public ProjectManager(JdbcTemplate jdbcTemplate, MeterRegistry meterRegistry) {
        this.template = jdbcTemplate;
        this.registry = meterRegistry;
    }

    public LocationDAO getLocationById(Long locationId) {
        String query = "select location_id, location_city, location_market from location where location_id = ? and location_deleted = false";

        return this.template.query(psc -> {
            PreparedStatement pstmt = psc.prepareStatement(query);
            pstmt.setLong(1, locationId);
            return pstmt;
        }, new LocationResultSetExtractor());
    }

    public Long saveLocation(LocationDAO location) {
        String insert = "insert into location(location_city, location_market) values(?, ?) returning location_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.template.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(insert, new String[]{"location_id"});
            pstmt.setString(1, location.getLocationCity());
            pstmt.setString(2, location.getLocationMarket());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    public Long saveProject(ProjectDAO project) {
        String insert = "insert into project(project_name, project_location_id) values(?, ?) returning project_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.template.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(insert, new String[]{"project_id"});
            pstmt.setString(1, project.getName());
            pstmt.setLong(2, project.getLocation().getLocationId());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }
    public ProjectDAO getProjectById(Long projectId) {
        String query = "select project_id, project_name, location_id, location_city, location_market from project join location " +
                "on project_location_id = location_id where project_id = ? and project_deleted = false and location_deleted = false";

        return this.template.query(
                psc -> {
                    PreparedStatement pstmt = psc.prepareStatement(query);
                    pstmt.setLong(1, projectId);
                    return pstmt;
                },
                new ProjectRowsSetExtractor()
        );
    }

    public Long countAllProjects() {
        final String query = "SELECT reltuples AS estimate FROM pg_class where relname = 'project';";
        return this.template.query(query, rs -> {
            long value = 0L;
            if(rs.next()) {
                value = rs.getLong("estimate");
            }
            return value;
        });
    }

    public List<ProjectDAO> findAllPaginated(Integer start, Integer size) {
        final String findAllPaginated = "select project_id, project_name, location_id, location_city, location_market from project join location " +
                "on project_location_id = location_id where project_deleted = false and location_deleted = false limit ? offset ?";
        Timer timer = Timer.builder("manager-get-projects-paginated")
                .description("Measures how long to call the jdbc template for paginated list of projects")
                .tags("manager", "projectManager", "paginated", "true")
                .register(registry);

        return timer.record( () -> this.template.query( con -> {
            PreparedStatement pstmt = con.prepareStatement(findAllPaginated);
            pstmt.setInt(1, size);
            pstmt.setInt(2, start);
            return pstmt;
        }, new ProjectRowMapper()));
    }
}
