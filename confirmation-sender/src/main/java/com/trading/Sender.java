package com.trading;

public interface Sender<T> {
    void send(T item);
}
