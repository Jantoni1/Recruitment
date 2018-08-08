package com.awin.recruitment.library.producer;

public interface Producer<T> {

    void produce(
        Iterable<T> messages
    );
}
