package com.awin.recruitment.library;

import com.oath.cyclops.async.adapters.Queue;

public class ConsumerImpl implements Consumer<Transaction>, Runnable {

    private Queue<Transaction> inputTransactionQueue;
    private Queue<EnrichedTransaction> enrichedTransactionQueue;

    public ConsumerImpl() {}

    @Override
    public void consume(Iterable<Transaction> messages) {
        messages.forEach(this.inputTransactionQueue::add);
    }

    @Override
    public void run() {
        enrichedTransactionQueue.stream().forEach(System.out::println);
    }

    public Queue<EnrichedTransaction> getEnrichedTransactionQueue() {
        return enrichedTransactionQueue;
    }

    public Queue<Transaction> getInputTransactionQueue() {
        return inputTransactionQueue;
    }

    public void setInputTransactionQueue(Queue<Transaction> inputTransactionQueue) {
        this.inputTransactionQueue = inputTransactionQueue;
    }

    public void setEnrichedTransactionQueue(Queue<EnrichedTransaction> enrichedTransactionQueue) {
        this.enrichedTransactionQueue = enrichedTransactionQueue;
    }
}
