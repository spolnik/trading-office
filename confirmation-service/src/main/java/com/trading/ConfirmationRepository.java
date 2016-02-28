package com.trading;

import java.io.IOException;

interface ConfirmationRepository {
    Confirmation queryById(String id);
    void save(Confirmation confirmation) throws IOException;
}
