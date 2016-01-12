package org.chesscorp.club.dto;

import java.util.Set;

/**
 * An opening model.
 */
public class ChessOpeningDescription {

    private String name;
    private Set<String> variants;

    public ChessOpeningDescription(String name, Set<String> variants) {
        this.name = name;
        this.variants = variants;
    }

    public String getName() {
        return name;
    }

    public Set<String> getVariants() {
        return variants;
    }

    @Override
    public String toString() {
        return "ChessOpeningDescription{" +
                "name='" + name + '\'' +
                ", variants=" + variants +
                '}';
    }
}
