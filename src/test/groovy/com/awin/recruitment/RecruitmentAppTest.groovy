package com.awin.recruitment

import com.awin.recruitment.library.Product
import com.awin.recruitment.library.Transaction
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class RecruitmentAppTest extends Specification {

    def "Test stub"() {

        setup:

        when:
        1 == 1

        then:
        1 == 1

        expect:
        1 == 1
    }

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
        transaction.getSaleDate().equals(saleDate)
        transaction.getProducts().equals(products)

    }
}
