package com.awin.recruitment.library;

import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerExecuteRunner implements Runnable {

    private ProducerConsumerFactory producerConsumerFactory;

    private int numberOfThreads;

    public void setProducerConsumerFactory(ProducerConsumerFactory producerConsumerFactory) {
        this.producerConsumerFactory = producerConsumerFactory;
    }

    public ProducerExecuteRunner() {
        producerConsumerFactory = new ProducerConsumerFactory();
    }

    public ProducerExecuteRunner(ProducerConsumerFactory producerConsumerFactory, int numberOfThreads) {
        this.producerConsumerFactory = producerConsumerFactory;
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for(int i = 0; i < numberOfThreads; ++i) {
            ProducerImpl producer = producerConsumerFactory.getProducer();
            Logger.getLogger("ProducerExecuteRunner")
                    .info("Created producer No." + producer.getProducerId());
            executorService.submit(() -> {
                producer.run();
                latch.countDown();
            });
        }
        try {
            executorService.shutdown();
            latch.await();

        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        producerConsumerFactory.getProducerOutputQueue().close();
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }
}
