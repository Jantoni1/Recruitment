package com.awin.recruitment.library;

import com.oath.cyclops.async.adapters.Queue;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class ProducerImpl implements Producer<Transaction>, Runnable {

    public ProducerImpl() {
        consumerId = consumerIdGenerator.getAndIncrement();
    }

    private static AtomicLong consumerIdGenerator = new AtomicLong(0);

    private final long consumerId;
    private Queue<Transaction> inputTransactionQueue;
    private Queue<EnrichedTransaction> enrichedTransactionQueue;

    ProducerImpl(Queue<Transaction> inputTransactionQueue, Queue<EnrichedTransaction> enrichedTransactionQueue) {
        this.enrichedTransactionQueue = enrichedTransactionQueue;
        this.inputTransactionQueue = inputTransactionQueue;
        consumerId = consumerIdGenerator.getAndIncrement();
    }

    @Override
    public void run() {
        produce(inputTransactionQueue.stream());
    }

    @Override
    public void produce(Iterable<Transaction> messages) {
        messages.forEach(transaction -> enrichedTransactionQueue.add(
                new EnrichedTransaction(transaction, calculateSum(transaction))));
    }

    public BigDecimal calculateSum(Transaction transaction) {
        BigDecimal sum = new BigDecimal(0);
        for(Product product : transaction.getProducts()) {
            sum = sum.add(product.getTotalCost());
        }
        return sum;
    }

    public Queue<Transaction> getInputTransactionQueue() {
        return inputTransactionQueue;
    }

    public void setInputTransactionQueue(Queue<Transaction> inputTransactionQueue) {
        this.inputTransactionQueue = inputTransactionQueue;
    }

    public Queue<EnrichedTransaction> getEnrichedTransactionQueue() {
        return enrichedTransactionQueue;
    }

    public void setEnrichedTransactionQueue(Queue<EnrichedTransaction> enrichedTransactionQueue) {
        this.enrichedTransactionQueue = enrichedTransactionQueue;
    }
}
