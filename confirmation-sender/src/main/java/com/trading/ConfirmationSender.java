package com.trading;

@FunctionalInterface
interface ConfirmationSender {
    void send(Confirmation confirmation);
}
