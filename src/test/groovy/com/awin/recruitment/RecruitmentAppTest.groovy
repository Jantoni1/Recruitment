package com.awin.recruitment

import com.awin.recruitment.library.EnrichmentServiceRunner
import com.awin.recruitment.library.ProducerConsumerFactory
import com.awin.recruitment.library.consumer.ConsumerExecuteRunner
import com.awin.recruitment.library.consumer.ConsumerImpl
import com.awin.recruitment.library.messages.EnrichedTransaction
import com.awin.recruitment.library.messages.Product
import com.awin.recruitment.library.messages.Transaction
import com.awin.recruitment.library.producer.ProducerExecuteRunner
import com.awin.recruitment.library.producer.ProducerImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month
import java.util.concurrent.CountDownLatch

@ContextConfiguration(locations = "classpath:/di.test.xml")
@ActiveProfiles("test")
class RecruitmentAppTest extends Specification {

    @Autowired
    ApplicationContext applicationContext

    def "Create a new product"() {

        given: "Product's name and amount paid for it"
        def name = "Milk"
        def totalCost = new BigDecimal(14.0)

        when: "New product object is created"
        Product product = new Product(name, totalCost)

        then: "Its fields have same values as those passed to the constructor"
        product.getName() == name
        product.getTotalCost() == totalCost
    }

    def "Create a new transaction"() {

        given: "Transaction data including list of products"
        def idNumber = 178
        def saleDate = LocalDate.of(2018, Month.AUGUST, 2)

        def product = new Product("Milk", new BigDecimal(14.0))
        def products = new ArrayList<Product>()
        products.add(product)

        when: "New Transaction object is created"
        def transaction = new Transaction(idNumber, saleDate, products)

        then: "Its fields have values same as those passed to the constructor"
        transaction.getIdNumber() == idNumber
        transaction.getSaleDate() == saleDate
        transaction.getProducts() == products

    }

    def "Create new ProducerConsumerFactory that contains two non-null queues"() {
        setup:
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        expect:
        factory.getProducerInputQueue() != null
        factory.getProducerOutputQueue() != null
    }


    def "ProducerConsumerFactory returns new ConsumerImpl containing same queue as factory"() {
        given: "ProducerConsumerFactory object"
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        when: "Factory produces a ConsumerImpl object"
        ConsumerImpl consumer = factory.getConsumer()

        then: "consumer's output queue is the same as queue in factory"
        factory.getProducerInputQueue() == consumer.getOutputTransactionQueue()
    }

    def "ProducerConsumerFactory returns new ProducerImpl containing input and input queues same as those inside factory"() {
        given: "ProducerConsumerFactory object"
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        when: "Factory produces a ProducerImpl object"
        ProducerImpl producer = factory.getProducer()

        then: "consumer's output queue is the same as queue in factory"
        factory.getProducerInputQueue() == producer.getInputTransactionQueue()
        factory.getProducerOutputQueue() == producer.getEnrichedTransactionQueue()
    }

    def "Consumer consumes transaction data sending it to producers' queue"() {
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        given: "Consumer, output queue and collection containing one transaction"
        ArrayList listContainingOneTransaction =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")
        ConsumerImpl consumer = factory.getConsumer()

        when: "Consumer consumes collection of transactions"
        consumer.consume(listContainingOneTransaction)

        then: "Output queue contains one enriched transaction"
        consumer.getInputTransactionQueue().get() == listContainingOneTransaction.first()
    }

    def "Producer calculates sum of values of transaction and puts it into a queue"() {
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        given: "Producer and a transaction containing two products that cost 10 and 21 respectively"
        Transaction transaction = (Transaction)applicationContext.getBean("sampleTransaction")
        ProducerImpl producer = factory.getProducer()

        when: "Produces calculates sum of values of products transaction contains"
        BigDecimal sumOfProductValues = producer.calculateSum(transaction)

        then: "Output value equals 10 + 21 = 31"

        sumOfProductValues == 31

    }
    def "Producer enriches transaction and puts it into queue"() {
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        given: "Producer connected to its input and output queues and array of transactions containing one transaction"
        Transaction transaction = (Transaction)applicationContext.getBean("sampleTransaction")
        ProducerImpl producer = factory.getProducer()

        ArrayList<Transaction> inputList = new ArrayList<>()
        inputList.add(transaction)

        when: "Producer produces enriched transaction"
        producer.produce(inputList)

        then: "there will be one enriched transaction in output queue with sum value of 31"
        producer.getEnrichedTransactionQueue().get().getCostSum() == new BigDecimal(31)

    }

    def "Consumer consumes transaction, sends it to producer, then producer produces EnrichedTransaction object"() {
        ProducerConsumerFactory factory = new ProducerConsumerFactory()

        given: "An input array and consumer and producer connected with each other by a queue"
        ConsumerImpl consumer = factory.getConsumer()
        ProducerImpl producer = factory.getProducer()
        ArrayList<Transaction> messageInput =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")
        Thread producerThread = new Thread(producer)
        producerThread.start()

        when:"consumer consumes message"
        consumer.consume(messageInput)

        then: "producer has created EnrichedTransaction object and put it into OutputQueue"
        producer.getEnrichedTransactionQueue().get().costSum == 31

        cleanup:
        consumer.getOutputTransactionQueue().close()
        producerThread.join()
    }

    def "50 producer threads are run, after closing input queue all of them will be shut down"() {
        setup:
        ProducerConsumerFactory factory = new ProducerConsumerFactory()
        int numberOfThreads = 50
        CountDownLatch latch = new CountDownLatch(numberOfThreads)
        for(int i = 0; i < numberOfThreads; ++i) {
            ProducerImpl producer = factory.getProducer()
            new Thread({
                producer.run()
                latch.countDown()
            }).start()
        }

        expect:
        latch.count == numberOfThreads

        when:
        factory.getProducerInputQueue().close()
        latch.await()

        then:
        latch.count == 0

    }

    def "ConsumerExecuteRunner creates a consumer thread that pushes all messages from its input queue to producer's input"() {
        given: "ConsumerExecuteRunner"
        ProducerConsumerFactory factory = new ProducerConsumerFactory()
        ConsumerExecuteRunner runner = new ConsumerExecuteRunner(factory)
        ArrayList<Transaction> messageInput =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")

        when: "Runner runs new consumer thread consuming one transaction"
        runner.consume(messageInput)
        runner.stop()

        then: "Producer's input queue contains one transaction of id equal to 1"
        factory.getProducerInputQueue().get().getIdNumber() == 1

    }

    def "ProducerExecuteRunner creates a producer thread that processes one transaction"() {
        given: "ConsumerExecuteRunner"
        ProducerConsumerFactory factory = new ProducerConsumerFactory()
        ProducerExecuteRunner runner = new ProducerExecuteRunner(factory, 10)
        Transaction transaction = (Transaction)applicationContext.getBean("sampleTransaction")

        when: "Runner runs new consumer thread consuming one transaction"
        Thread producerRunnerThread = new Thread(runner)
        producerRunnerThread.start()
        factory.getProducerInputQueue().add(transaction)
        factory.getProducerInputQueue().close()
        producerRunnerThread.join()

        then: "Producer's input queue contains one transaction of id equal to 1"
        EnrichedTransaction enrichedTransaction = factory.getProducerOutputQueue().get()
        enrichedTransaction.getIdNumber() == 1
        enrichedTransaction.getCostSum() == 31

    }

    def "ConsumerExecuteRunner creates a consumer thread that consume messages"() {
        given: "ConsumerExecuteRunner, a producer and an array containing input data"
        ProducerConsumerFactory factory = new ProducerConsumerFactory()
        ConsumerExecuteRunner runner = new ConsumerExecuteRunner(factory)
        Thread producerThread = new Thread(factory.getProducer())
        producerThread.start()
        ArrayList<Transaction> messageInput =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")

        when: "ConsumerExecuteRunner's consume() method is called"

        runner.consume(messageInput)
        runner.stop()
        producerThread.join()

        then: "Producer processes one transaction and pushes an enriched transaction of sum value = 31 to queue"
        factory.getProducerOutputQueue().get().costSum == 31

    }

    def "ConsumerExecuteRunner sends given message to producer's queue and ProducerExecuteRunner processes it"() {
        setup:
        ProducerConsumerFactory factory = new ProducerConsumerFactory()
        ConsumerExecuteRunner consumerExecuteRunner = new ConsumerExecuteRunner(factory)
        ProducerExecuteRunner producerExecuteRunner = new ProducerExecuteRunner(factory, 5)

        ArrayList<Transaction> messageInput =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")

        consumerExecuteRunner.consume(messageInput)
        consumerExecuteRunner.stop()
        producerExecuteRunner.run()

        expect:
        EnrichedTransaction enrichedTransaction = factory.getProducerOutputQueue().get()
        enrichedTransaction.getIdNumber() == 1
        enrichedTransaction.getCostSum() == 31
    }

    def "EnrichmentServiceRunner throws an exception when restarted"() {
        setup:
        EnrichmentServiceRunner enrichmentServiceRunner = new EnrichmentServiceRunner(10)
        enrichmentServiceRunner.start()
        enrichmentServiceRunner.shutdown()

        when:
        enrichmentServiceRunner.start()

        then:
        def illegalStateException = thrown(IllegalStateException)
        illegalStateException.message == "Service has been closed."

    }

    def "EnrichmentServiceRunner processes input messages and produces enriched transactions"() {
        setup:
        EnrichmentServiceRunner enrichmentServiceRunner = new EnrichmentServiceRunner(10)
        enrichmentServiceRunner.start()
        ArrayList<Transaction> messageInput =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")
        enrichmentServiceRunner.consume(messageInput)
        enrichmentServiceRunner.shutdown()

        EnrichedTransaction transaction = enrichmentServiceRunner.getEnrichedMessages().first()

        expect:
        transaction.idNumber == 1
        transaction.costSum == 31

    }







}
