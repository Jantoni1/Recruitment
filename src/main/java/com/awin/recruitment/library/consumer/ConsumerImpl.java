package com.awin.recruitment.library.consumer;

import com.awin.recruitment.library.messages.Transaction;
import com.oath.cyclops.async.adapters.Queue;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;


public class ConsumerImpl implements Consumer<Transaction>, Runnable {

    private Iterable<Transaction> inputTransactionQueue;
    private Queue<Transaction> outputTransactionQueue;

    private static AtomicLong consumerIdGenerator = new AtomicLong(0);

    private final long consumerId;

    public ConsumerImpl() {
        consumerId = consumerIdGenerator.incrementAndGet();
    }

    public ConsumerImpl(Queue<Transaction> outputTransactionQueue) {
        consumerId = consumerIdGenerator.incrementAndGet();
        this.outputTransactionQueue = outputTransactionQueue;
    }

    @Override
    public void consume(Iterable<Transaction> messages) {
        Logger.getLogger("ConsumerThread " + consumerId).info("Consuming messages.");
        Iterator<Transaction> iterator = messages.iterator();
        while(iterator.hasNext() && !Thread.interrupted()) {
            outputTransactionQueue.add(iterator.next());
        }
        Logger.getLogger("ConsumerThread " + consumerId).info("Closed input queue");

    }

    public Queue<Transaction> getInputTransactionQueue() {
        return outputTransactionQueue;
    }

    public void setInputTransactionQueue(Iterable<Transaction> inputTransactionQueue) {
        this.inputTransactionQueue = inputTransactionQueue;
    }

    public Queue<Transaction> getOutputTransactionQueue() {
        return outputTransactionQueue;
    }

    public void setOutputTransactionQueue(Queue<Transaction> outputTransactionQueue) {
        this.outputTransactionQueue = outputTransactionQueue;
    }

    public long getConsumerId() {
        return consumerId;
    }

    @Override
    public void run() {
        consume(inputTransactionQueue);
    }
}
