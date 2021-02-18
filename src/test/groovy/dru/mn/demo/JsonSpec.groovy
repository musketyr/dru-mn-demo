package dru.mn.demo

import com.agorapulse.dru.Dru
import dru.mn.demo.data.Sale
import io.micronaut.context.ApplicationContext
import io.micronaut.context.ApplicationContextProvider
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class JsonSpec extends Specification implements ApplicationContextProvider {

    @AutoCleanup Dru dru = Dru.create {
        include TestData.sales
    }

    @Inject ApplicationContext applicationContext
    @Inject GlobalSalesService globalSalesService

    void setup() {
        dru.load()
    }

    void 'count global sales'() {
        expect:
            globalSalesService.countTotalSales() == 505
    }

}
