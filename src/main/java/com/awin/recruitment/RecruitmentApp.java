package com.awin.recruitment;

import com.awin.recruitment.infrastructure.spring.ClassPathXmlApplicationContextFactory;
import com.awin.recruitment.library.ConsumerImpl;
import com.awin.recruitment.library.ProducerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public final class RecruitmentApp {

    private RecruitmentApp() { }


    public static void main(
        String[] args
    )  {

        ClassPathXmlApplicationContext applicationContext = ClassPathXmlApplicationContextFactory.create();

        ConsumerImpl consumer = (ConsumerImpl)applicationContext.getBean("consumer");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Thread producerThreads = new Thread((ProducerImpl)applicationContext.getBean("producer"));
        producerThreads.start();
    }
}
