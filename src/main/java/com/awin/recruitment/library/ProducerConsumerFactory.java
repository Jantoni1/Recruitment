package com.awin.recruitment.library;

import com.oath.cyclops.async.adapters.Queue;

public class ProducerConsumerFactory {

    private Queue<Transaction> producerInputQueue;

    private Queue<EnrichedTransaction> producerOutputQueue;

    public ProducerConsumerFactory() {
        producerInputQueue = new Queue<>();
        producerOutputQueue = new Queue<>();
    }


    public ProducerImpl getProducer() {
        return new ProducerImpl(producerInputQueue, producerOutputQueue);
    }

    public ConsumerImpl getConsumer() {
        return new ConsumerImpl(producerInputQueue);
    }


    public Queue<Transaction> getProducerInputQueue() {
        return producerInputQueue;
    }

    public void setProducerInputQueue(Queue<Transaction> producerInputQueue) {
        this.producerInputQueue = producerInputQueue;
    }

    public Queue<EnrichedTransaction> getProducerOutputQueue() {
        return producerOutputQueue;
    }

    public void setProducerOutputQueue(Queue<EnrichedTransaction> producerOutputQueue) {
        this.producerOutputQueue = producerOutputQueue;
    }


}
