package dru.mn.demo

import com.agorapulse.dru.Dru
import com.agorapulse.dru.parser.sql.DataSourceProvider
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Specification

import javax.inject.Inject
import javax.sql.DataSource

@MicronautTest
@Property(name = 'datasources.default.schema-generate', value = 'NONE')
class SqlSpec extends Specification implements DataSourceProvider {

    @AutoCleanup Dru dru = Dru.create {
        from 'sales.sql'
    }

    @Inject GlobalSalesService globalSalesService
    @Inject DataSource dataSource

    void setup() {
        dru.load()
    }

    void 'count global sales'() {
        expect:
            globalSalesService.countTotalSales() == 505
    }

}
