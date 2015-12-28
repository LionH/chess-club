package org.chesscorp.club.dto;

/**
 * Server release information used by the status controller.
 */
public class ReleaseInformation {

    private String name;

    private String revision;

    public ReleaseInformation() {
    }

    public ReleaseInformation(String name, String revision) {
        this.name = name;
        this.revision = revision;
    }

    public String getName() {
        return name;
    }

    public String getRevision() {
        return revision;
    }
}
