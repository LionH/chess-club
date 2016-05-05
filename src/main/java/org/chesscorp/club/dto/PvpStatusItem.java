package org.chesscorp.club.dto;

import org.chesscorp.club.model.people.Player;

/**
 * Wraps a single PVP opponent information.
 */
public class PvpStatusItem implements Comparable<PvpStatusItem> {
    private Player opponent;
    private PvpStatus pvpStatus;

    public PvpStatusItem(Player opponent, PvpStatus pvpStatus) {
        this.opponent = opponent;
        this.pvpStatus = pvpStatus;
    }

    public Player getOpponent() {
        return opponent;
    }

    public PvpStatus getPvpStatus() {
        return pvpStatus;
    }

    @Override
    public int compareTo(PvpStatusItem o) {
        int result = getPvpStatus().getWins() - o.getPvpStatus().getWins();

        if (result == 0) {
            result = getOpponent().compareTo(o.getOpponent());
        }

        return result;
    }
}
