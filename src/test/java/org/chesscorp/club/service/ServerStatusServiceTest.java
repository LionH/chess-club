package org.chesscorp.club.service;


import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.ReleaseInformation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ServerStatusServiceTest {

    @Autowired
    private ServerStatusService serverStatusService;

    @Test
    public void testReleaseInformation() {
        ReleaseInformation releaseInformation = serverStatusService.getReleaseInformation();
        Assertions.assertThat(releaseInformation.getName()).isNotEmpty();
        Assertions.assertThat(releaseInformation.getRevision()).isNotEmpty();
    }
}
