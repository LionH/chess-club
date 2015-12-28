package org.chesscorp.club.controllers;

import org.chesscorp.club.Application;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class StatusControllerTest {
    @Autowired
    private StatusController statusController;

    @Test
    @Transactional
    public void testRelease() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(statusController).build();

        mockMvc.perform(
                get("/api/status/release")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$.name", Matchers.is("Chess Club Test"))
        ).andExpect(
                jsonPath("$.revision", Matchers.is("Test-6.6.6-SNAPSHOT"))
        );

    }

}