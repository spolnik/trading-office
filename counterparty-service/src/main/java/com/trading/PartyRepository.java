package com.trading;

@FunctionalInterface
public interface PartyRepository {
    Party getById(String id);
}
