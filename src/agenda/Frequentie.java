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
     * @param datum    de datum
     * @return de volgende datum
     */
    public LocalDate volgendeDatum(LocalDate datum) {
        return datum.plusDays(7);
    }
}


