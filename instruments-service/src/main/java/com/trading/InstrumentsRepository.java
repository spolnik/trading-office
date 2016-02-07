package com.trading;

@FunctionalInterface
interface InstrumentsRepository {
    InstrumentDetails queryBySedol(String sedol);
}
