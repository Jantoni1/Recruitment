package com.awin.recruitment.library.consumer;

public interface Consumer<T> {

    void consume(
        Iterable<T> messages
    );
}