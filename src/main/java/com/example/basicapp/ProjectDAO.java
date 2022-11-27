package com.example.basicapp;

import javax.persistence.*;

@Entity
@Table(name = "project")
public class ProjectDAO {

    @Id
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_name")
    private String name;

    @ManyToOne(targetEntity = LocationDAO.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_location_id")
    private LocationDAO location;

    @Column(name = "project_deleted")
    private boolean projectDeleted;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDAO getLocation() {
        return location;
    }

    public void setLocation(LocationDAO location) {
        this.location = location;
    }

    public boolean isProjectDeleted() {
        return projectDeleted;
    }

    public void setProjectDeleted(boolean projectDeleted) {
        this.projectDeleted = projectDeleted;
    }
}
