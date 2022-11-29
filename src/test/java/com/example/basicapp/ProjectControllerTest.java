package com.example.basicapp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@MockBean(ProjectManager.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProjectManager manager;

    @Test
    public void shouldFindProject() throws Exception {
        var location = new LocationDAO();
        location.setLocationId(65432L);
        location.setLocationCity("Daly City");
        location.setLocationMarket("West");

        var project = new ProjectDAO();
        project.setLocation(location);
        project.setProjectId(12345L);
        project.setName("Sample Project");

        when(manager.getProjectById(Mockito.anyLong())).thenReturn(project);

        this.mvc.perform(get("/api/project/12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.project_id").value(12345));
    }
}
