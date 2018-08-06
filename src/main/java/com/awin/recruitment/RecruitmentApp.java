package com.awin.recruitment;

import com.awin.recruitment.infrastructure.spring.ClassPathXmlApplicationContextFactory;
import com.awin.recruitment.library.ConsumerImpl;
import com.awin.recruitment.library.ProducerConsumerFactory;
import com.awin.recruitment.library.ProducerExecuteRunner;
import com.awin.recruitment.library.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;


public final class RecruitmentApp {

    private RecruitmentApp() { }

    public static void main(
        String[] args
    )  {

        ClassPathXmlApplicationContext applicationContext = ClassPathXmlApplicationContextFactory.create();

        ProducerConsumerFactory factory = new ProducerConsumerFactory();

        new Thread(new ProducerExecuteRunner(factory, 4)).start();

        ConsumerImpl consumer = factory.getConsumer();

        consumer.consume((ArrayList<Transaction>)applicationContext.getBean("transactionInputList"));

    }
}
