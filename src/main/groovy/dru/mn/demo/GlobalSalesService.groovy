package dru.mn.demo

import dru.mn.demo.data.SaleRepository
import groovy.transform.CompileStatic

import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
@CompileStatic
class GlobalSalesService {

    private final SaleRepository sales;
    private final DataSource dataSource;

    GlobalSalesService(SaleRepository sales, DataSource dataSource) {
        this.sales = sales
        this.dataSource = dataSource
    }

    int countTotalSales() {
        return (int) sales.findAll()*.quantity*.amount.sum()
    }

}
