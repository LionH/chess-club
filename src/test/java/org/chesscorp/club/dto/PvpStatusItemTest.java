package org.chesscorp.club.dto;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.model.people.ClubPlayer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test ordering and access to this DTO.
 */
public class PvpStatusItemTest {

    @Test
    public void testOrdering() {
        PvpStatusItem item1 = new PvpStatusItem(new ClubPlayer("Player2"), new PvpStatus(0, 0, 2));
        PvpStatusItem item2 = new PvpStatusItem(new ClubPlayer("Player3"), new PvpStatus(0, 0, 1));
        PvpStatusItem item3 = new PvpStatusItem(new ClubPlayer("Player1"), new PvpStatus(1, 0, 0));

        List<PvpStatusItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Collections.sort(items);

        Assertions.assertThat(items.get(0)).isEqualTo(item3);
        Assertions.assertThat(items.get(1)).isEqualTo(item2);
        Assertions.assertThat(items.get(2)).isEqualTo(item1);
    }
}
