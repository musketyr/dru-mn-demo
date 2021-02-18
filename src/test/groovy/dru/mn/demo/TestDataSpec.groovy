package dru.mn.demo

import com.agorapulse.dru.Dru
import dru.mn.demo.data.ManufacturerRepository
import dru.mn.demo.data.ProductRepository
import dru.mn.demo.data.SaleRepository
import io.micronaut.context.ApplicationContext
import io.micronaut.context.ApplicationContextProvider
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class TestDataSpec extends Specification implements ApplicationContextProvider {

    @Inject ApplicationContext applicationContext
    @Inject ManufacturerRepository manufacturerRepository
    @Inject ProductRepository productRepository
    @Inject SaleRepository saleRepository

    void 'load manufacturers'() {
        when:
            Dru.create(this).load(TestData.manufacturers)
        then:
            manufacturerRepository.findAll().size() == 2
    }

    void 'load products'() {
        when:
            Dru.create(this).load(TestData.products)
        then:
            manufacturerRepository.findAll().size() == 2
            productRepository.findAll().size() == 4
    }

    void 'load sales'() {
        when:
            Dru.create(this).load(TestData.sales)
        then:
            manufacturerRepository.findAll().size() == 2
            productRepository.findAll().size() == 4
            saleRepository.findAll().size() == 4
    }

}
