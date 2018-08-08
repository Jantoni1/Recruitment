package com.awin.recruitment.library.producer;

import com.awin.recruitment.library.ProducerConsumerFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerExecuteRunner implements Runnable {

    private ProducerConsumerFactory producerConsumerFactory;

    private ExecutorService executorService;

    private int numberOfThreads;

    public ProducerExecuteRunner(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        producerConsumerFactory = new ProducerConsumerFactory();
        executorService = Executors.newFixedThreadPool(numberOfThreads);

    }

    public ProducerExecuteRunner(ProducerConsumerFactory producerConsumerFactory, int numberOfThreads) {
        this.producerConsumerFactory = producerConsumerFactory;
        this.numberOfThreads = numberOfThreads;
        executorService = Executors.newFixedThreadPool(numberOfThreads);

    }

    @Override
    public void run() {
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for(int i = 0; i < numberOfThreads; ++i) {
            ProducerImpl producer = producerConsumerFactory.getProducer();
            Logger.getLogger("ProducerExecuteRunner")
                    .info("Created ProducerThread " + producer.getProducerId());
            executorService.submit(() -> {
                producer.run();
                latch.countDown();
            });
        }
        executorService.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        producerConsumerFactory.getProducerOutputQueue().close();

    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
