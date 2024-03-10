package agenda;

import java.time.LocalDate;

/**
 * Enumeratie voor frequenties.
 *
 * @author OU
 */

public enum Frequentie {
    WEKELIJKS;

    /**
     * Retourneert de volgende datum voor over een week
     *
     * @return volgende datum
     */
    public LocalDate volgendeDatum(LocalDate datum) {
        return datum.plusDays(7);
    }
}


