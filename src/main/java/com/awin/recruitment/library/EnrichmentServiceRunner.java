package com.awin.recruitment.library;

import com.awin.recruitment.library.consumer.ConsumerExecuteRunner;
import com.awin.recruitment.library.messages.EnrichedTransaction;
import com.awin.recruitment.library.messages.Transaction;
import com.awin.recruitment.library.producer.ProducerExecuteRunner;

import java.util.concurrent.atomic.AtomicBoolean;

public class EnrichmentServiceRunner {

    private ProducerConsumerFactory producerConsumerFactory;

    private ProducerExecuteRunner producerExecuteRunner;

    private ConsumerExecuteRunner consumerExecuteRunner;

    private AtomicBoolean isRunning;
    private AtomicBoolean isClosed;

    private Thread producerExecuteRunnerThread;

    public EnrichmentServiceRunner(int numberOfProducers) {
        producerConsumerFactory = new ProducerConsumerFactory();
        producerExecuteRunner = new ProducerExecuteRunner(producerConsumerFactory, numberOfProducers);
        consumerExecuteRunner = new ConsumerExecuteRunner(producerConsumerFactory);
        isRunning = new AtomicBoolean(false);
        isClosed = new AtomicBoolean(false);
    }

    public void consume(Iterable<Transaction> messages) {
        if(isRunning.get() && !isClosed.get()) {
            consumerExecuteRunner.consume(messages);
        }
    }

    public void start() {
        if(isClosed.get()) throw new IllegalStateException("Service has been closed.");
        if(!isRunning.get()) {
            producerExecuteRunnerThread = new Thread(producerExecuteRunner);
            producerExecuteRunnerThread.start();
            isRunning.set(true);
        }
    }

    public void shutdownNow() {
        if(!isClosed.getAndSet(true) && isRunning.getAndSet(false)) {
            consumerExecuteRunner.stopNow();
            producerExecuteRunnerThread.interrupt();
        }
    }

    public void shutdown() {
        if(!isClosed.getAndSet(true) && isRunning.getAndSet(false)) {
            consumerExecuteRunner.stop();
        }
    }

    public Iterable<EnrichedTransaction> getEnrichedMessages() {
        return producerConsumerFactory.getProducerOutputQueue().stream();
    }

    public ProducerConsumerFactory getProducerConsumerFactory() {
        return producerConsumerFactory;
    }

    public void setProducerConsumerFactory(ProducerConsumerFactory producerConsumerFactory) {
        this.producerConsumerFactory = producerConsumerFactory;
    }

    public ProducerExecuteRunner getProducerExecuteRunner() {
        return producerExecuteRunner;
    }

    public void setProducerExecuteRunner(ProducerExecuteRunner producerExecuteRunner) {
        this.producerExecuteRunner = producerExecuteRunner;
    }
}
