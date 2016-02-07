package com.trading;

@FunctionalInterface
interface Sender<T> {
    void send(T item);
}
