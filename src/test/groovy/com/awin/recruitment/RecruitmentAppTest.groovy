package com.awin.recruitment

import com.awin.recruitment.library.*
import com.oath.cyclops.async.adapters.Queue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

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

    def "Consumer consumes transaction data sending it to producers' queue"() {

        given: "Consumer, output queue and collection containing one transaction"

        ArrayList listContainingOneTransaction =
                (ArrayList<Transaction>)applicationContext.getBean("transactionInputList")
        ConsumerImpl consumer = new ConsumerImpl()
        Queue<Transaction> outputQueue = new Queue<Transaction>()
        consumer.setInputTransactionQueue(outputQueue)

        when: "Consumer consumes collection of transactions"
        consumer.consume(listContainingOneTransaction)

        then: "Output queue contains one enriched transaction"
        consumer.getInputTransactionQueue().get() == listContainingOneTransaction.first()
    }

    def "Producer calculates sum of values of transaction and puts it into a queue"() {

        given: "Producer and a transaction containing two products that cost 10 and 21 respectively"
        Transaction transaction = (Transaction)applicationContext.getBean("sampleTransaction")
        ProducerImpl producer = (ProducerImpl)applicationContext.getBean("producer")

        when: "Produces calculates sum of values of products transaction contains"
        BigDecimal sumOfProductValues = producer.calculateSum(transaction)

        then: "Output value equals 10 + 21 = 31"

        sumOfProductValues == 31

    }
    def "Producer enriches transaction and puts it into queue"() {

        given: "Producer connected to its input and output queues and array of transactions containing one transaction"
        Transaction transaction = (Transaction)applicationContext.getBean("sampleTransaction")
        ProducerImpl producer = (ProducerImpl)applicationContext.getBean("producer")
        Queue<EnrichedTransaction> outputQueue = (Queue<EnrichedTransaction>)applicationContext.getBean("enrichedTransactionQueue")

        ArrayList<Transaction> inputList = new ArrayList<>()
        inputList.add(transaction)

        when: "Producer produces enriched transaction"
        producer.produce(inputList)

        then: "there will be one enriched transaction in output queue with sum value of 31"
        EnrichedTransaction enrichedTransaction = outputQueue.get()
        enrichedTransaction.costSum == new BigDecimal(31)

    }






}
