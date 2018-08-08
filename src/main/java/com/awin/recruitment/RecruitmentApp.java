package com.awin.recruitment;

import com.awin.recruitment.infrastructure.spring.ClassPathXmlApplicationContextFactory;
import com.awin.recruitment.library.EnrichmentServiceRunner;
import com.awin.recruitment.library.messages.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;


public final class RecruitmentApp {

    private RecruitmentApp() { }

    public static void main(
        String[] args
    )  {

        ClassPathXmlApplicationContext applicationContext = ClassPathXmlApplicationContextFactory.create();

        EnrichmentServiceRunner runner = new EnrichmentServiceRunner(3);
        runner.start();
        runner.consume((ArrayList<Transaction>)applicationContext.getBean("transactionInputList"));
        runner.shutdown();
    }
}
