package com.awin.recruitment.library;

import com.awin.recruitment.library.consumer.ConsumerImpl;
import com.awin.recruitment.library.messages.EnrichedTransaction;
import com.awin.recruitment.library.messages.Transaction;
import com.awin.recruitment.library.producer.ProducerImpl;
import com.oath.cyclops.async.adapters.Queue;

public class ProducerConsumerFactory {

    private Queue<Transaction> producerInputQueue;

    private Queue<EnrichedTransaction> producerOutputQueue;

    public ProducerConsumerFactory() {
        producerInputQueue = new Queue<>();
        producerOutputQueue = new Queue<>();
    }

    public ProducerConsumerFactory(Queue<EnrichedTransaction> producerOutputQueue) {
        producerInputQueue = new Queue<>();
        this.producerOutputQueue = producerOutputQueue;
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
