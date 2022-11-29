package com.example.basicapp;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class ProjectManagerTest {

    private final Logger log = LoggerFactory.getLogger(ProjectManagerTest.class);

    protected ProjectManager manager;

    @Autowired
    public ProjectManagerTest(ProjectManager manager) {
        this.manager = manager;
    }


    @Test
    public void testGetProjectById() {
        ProjectDAO project = this.manager.getProjectById(1L);
        assertThat(project).isNotNull().hasFieldOrPropertyWithValue("projectId", 1L)
                .hasFieldOrPropertyWithValue("name", "Facebook");
    }

    @Test
    public void testCreateLocation() {
        var location = new LocationDAO();
        location.setLocationMarket("Central");
        location.setLocationCity("Colorado");
        Long locationId = this.manager.saveLocation(location);
    }

    @Test
    public void testGetLocationById() {
        var location = this.manager.getLocationById(1L);
        assertThat(location)
                .isNotNull()
                .hasFieldOrPropertyWithValue("locationMarket", "West")
                .hasFieldOrPropertyWithValue("locationCity", "San Francisco");
    }

    @Test
    public void testCreateProject() {
        var location = this.manager.getLocationById(1L);

        var project = new ProjectDAO();
        project.setName("Yodel");
        project.setLocation(location);
        Long projectId = this.manager.saveProject(project);

        assertThat(projectId).isGreaterThan(0);
    }

    @Test
    public void testFindAllPaginated() {
        List<ProjectDAO> results = this.manager.findAllPaginated(0, 3);
        log.info(results.toString());
        assertThat(results.size()).isEqualTo(3);
    }
}
