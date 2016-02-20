package com.trading;

@FunctionalInterface
interface InstrumentsRepository {
    OpenFigiResponse queryBySedol(String sedol);
}
