package org.chesscorp.club.service;

import org.chesscorp.club.dto.ReleaseInformation;

/**
 * Access status information.
 */
public interface ServerStatusService {
    /**
     * Get current release information.
     *
     * @return a release information data structure
     */
    ReleaseInformation getReleaseInformation();
}
