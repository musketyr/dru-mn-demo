package dru.mn.demo

import com.agorapulse.dru.Dru
import com.agorapulse.dru.PreparedDataSet
import dru.mn.demo.data.Manufacturer
import dru.mn.demo.data.Product
import dru.mn.demo.data.Sale
import groovy.transform.CompileStatic

@CompileStatic
class TestData {

    public static final PreparedDataSet manufacturers = Dru.prepare {
        from 'manufacturers.json', {
            map {
                to Manufacturer
            }
        }
    }

    public static final PreparedDataSet products = Dru.prepare {
        include TestData.manufacturers
        from 'products.json', {
            map {
                to Product
            }
        }
    }

    public static final PreparedDataSet sales = Dru.prepare {
        include TestData.products
        from 'sales.json', {
            map {
                to Sale
            }
        }
    }

}
