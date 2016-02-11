package com.trading;

interface ConfirmationRepository {
    Confirmation queryById(String id);
    void save(Confirmation confirmation);
}
