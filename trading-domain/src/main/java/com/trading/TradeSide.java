package com.trading;

import java.util.Arrays;
import java.util.Optional;

public enum TradeSide {
    BUY("1"),
    SELL("2");

    private final String fixTradeSide;

    TradeSide(String fixTradeSide) {
        this.fixTradeSide = fixTradeSide;
    }

    public static TradeSide getTradeSide(String fixTradeSide) {
        Optional<TradeSide> tradeSide = Arrays.asList(values())
                .stream()
                .filter(value -> value.fixTradeSide.equals(fixTradeSide))
                .findFirst();

        return tradeSide.orElseThrow(
            () -> new UnsupportedOperationException( "Trade Side is unsupported: " + fixTradeSide)
        );
    }
}
