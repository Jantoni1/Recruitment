package com.awin.recruitment.library;

import com.oath.cyclops.async.adapters.Queue;
import org.apache.log4j.Logger;


public class ConsumerImpl implements Consumer<Transaction> {

    private Queue<Transaction> inputTransactionQueue;

    public ConsumerImpl() {}

    public ConsumerImpl(Queue<Transaction> inputTransactionQueue) {
        this.inputTransactionQueue = inputTransactionQueue;
    }

    @Override
    public void consume(Iterable<Transaction> messages) {
        Logger.getLogger("Consumer's thread").info("Consuming messages.");
        messages.forEach(this.inputTransactionQueue::add);
        Logger.getLogger("Consumer's thread").info("Closing input queue");
        inputTransactionQueue.close();
    }

    public Queue<Transaction> getInputTransactionQueue() {
        return inputTransactionQueue;
    }

    public void setInputTransactionQueue(Queue<Transaction> inputTransactionQueue) {
        this.inputTransactionQueue = inputTransactionQueue;
    }

}
