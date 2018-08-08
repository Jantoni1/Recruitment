package com.awin.recruitment.library.consumer;

import com.awin.recruitment.library.ProducerConsumerFactory;
import com.awin.recruitment.library.messages.Transaction;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerExecuteRunner {
    private ProducerConsumerFactory producerConsumerFactory;

    private ExecutorService executorService;

    private LinkedBlockingQueue<ConsumerImpl> consumers;
    private AtomicLong numberOfConsumersRunning;
    private AtomicBoolean isClosed;

    public ConsumerExecuteRunner(ProducerConsumerFactory producerConsumerFactory) {
        this.producerConsumerFactory = producerConsumerFactory;
        this.executorService = Executors.newCachedThreadPool();
        this.isClosed = new AtomicBoolean(false);
        this.numberOfConsumersRunning = new AtomicLong(0);
        consumers = new LinkedBlockingQueue<>();
    }

    public void consume(Iterable<Transaction> messages) {
        ConsumerImpl consumer = producerConsumerFactory.getConsumer();
        consumer.setInputTransactionQueue(messages);
        consumers.add(consumer);
        numberOfConsumersRunning.getAndIncrement();
        executorService.execute(() -> {
            consumer.run();
            consumers.remove(consumer);
            if(numberOfConsumersRunning.decrementAndGet() == 0 && isClosed.get()) {
                consumer.getOutputTransactionQueue().close();
            }
        });
        Logger.getLogger("ConsumerExecuteRunner").info("Created ConsumerThread " + consumer.getConsumerId());
    }

    public void stop() {
        if(!isClosed.getAndSet(true)) {
            executorService.shutdown();
            if(numberOfConsumersRunning.get() == 0) {
                producerConsumerFactory.getProducerInputQueue().close();
            }

        }
    }

    public void stopNow() {
        if(!isClosed.getAndSet(true)) {
            executorService.shutdown();
            if(numberOfConsumersRunning.get() == 0) {
                producerConsumerFactory.getProducerInputQueue().close();
            }
            consumers.forEach(consumer -> consumer.getInputTransactionQueue().close());

        }

    }

}
