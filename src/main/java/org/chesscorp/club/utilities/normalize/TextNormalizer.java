package org.chesscorp.club.utilities.normalize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Convert international strings to alphabetical lowercase.
 */
@Component
public class TextNormalizer {

    public String normalize(String text) {
        String transformed = text;
        transformed = StringUtils.lowerCase(transformed);
        transformed = StringUtils.stripAccents(transformed);
        transformed = transformed.replaceAll("[^a-z]+", "");
        return transformed;
    }

}
