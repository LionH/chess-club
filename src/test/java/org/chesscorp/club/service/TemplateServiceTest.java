package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test message sending.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;

    /**
     * Build a simple template.
     */
    @Test
    public void testAccountValidation() {
        String html = templateService.buildEmailAccountValidation("John Doe", "###Token###");
        Assertions.assertThat(html).contains("John Doe");
        Assertions.assertThat(html).contains("###Token###");
    }
}
