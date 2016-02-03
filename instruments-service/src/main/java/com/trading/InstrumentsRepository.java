package com.trading;

public interface InstrumentsRepository {
    InstrumentDetails queryBySedol(String sedol);
}
