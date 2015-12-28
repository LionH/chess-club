package org.chesscorp.club.service;

import org.chesscorp.club.dto.ReleaseInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Return status information based on the runtime environment.
 */
@Component
public class ServerStatusServiceImpl implements ServerStatusService {
    @Value("${application.name}")
    private String applicationName;

    @Value("${application.revision}")
    private String applicationRevision;

    @Override
    public ReleaseInformation getReleaseInformation() {
        return new ReleaseInformation(applicationName, applicationRevision);
    }
}
