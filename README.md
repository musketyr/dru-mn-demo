# Micronaut Data + Micronaut Console + Dru Demo

Micronaut Console and Dru for Micronaut Data Demo 

https://github.com/musketyr/dru-mn-demo

## Micronaut Console

Documentation: https://agorapulse.github.io/micronaut-console

### 1. Add Micronaut Console Dependency

```groovy
dependencies {
    implementation 'com.agorapulse:micronaut-console:1.0.4'
}
```

### 2. Create a HTTP Client file for console friendly execution (returning JSON)

Once you start the application, you can reach the console endpoint.

Create a following file inside `src/test/resources` folder.

#### console.http
```
POST http://localhost:8080/console/execute/result
Content-Type: text/groovy
Accept: text/plain

// language=groovy                                                                      
import dru.mn.demo.data.Book
import dru.mn.demo.data.BookRepository

BookRepository bookRepository = ctx.getBean(BookRepository)

bookRepository.deleteAll()

bookRepository.save(new Book("It", 1_000_000))
bookRepository.save(new Book("The Old Man And The Sea", 10))

bookRepository.findAll()
```

### 3. Create a HTTP Client file for web friendly execution (returning JSON)

#### console-json.http
```
POST http://localhost:8080/console/execute
Content-Type: text/groovy
Accept: application/json

// language=Groovy
import dru.mn.demo.data.Book
import dru.mn.demo.data.BookRepository

BookRepository bookRepository = ctx.getBean(BookRepository)

println "You can find books in 'result'"

bookRepository.findAll()
```

### 4. Add additional bindings

#### RepositoryBindings.groovy
```groovy
import com.agorapulse.micronaut.console.BindingProvider
import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class RepositoryBindings implements BindingProvider {

    private final BookRepository bookRepository
    private final ProductRepository productRepository
    private final SaleRepository saleRepository
    private final UserRepository userRepository
    private final ManufacturerRepository manufacturerRepository

    RepositoryBindings(
            BookRepository bookRepository,
            ProductRepository productRepository,
            SaleRepository saleRepository,
            UserRepository userRepository,
            ManufacturerRepository manufacturerRepository
    ) {
        this.bookRepository = bookRepository
        this.productRepository = productRepository
        this.saleRepository = saleRepository
        this.userRepository = userRepository
        this.manufacturerRepository = manufacturerRepository
    }

    @Override
    Map<String, ?> getBinding() {
        return [
                books: bookRepository,
                products: productRepository,
                sales: saleRepository,
                users: userRepository,
                manufacturers: manufacturerRepository,
        ]
    }
}
```

### 5. Download the GDSL file

```shell
 curl http://localhost:8080/console/dsl/gdsl > src/test/resources/console.gdsl

```

Now you have a working context assist for the additional bindings.

### 6. You can use any JSR223 compatible language

#### console-json-js.http
```
POST http://localhost:8080/console/execute
Content-Type: script/javascript
Accept: application/json

// language=Javascript
books.findAll()
```

### 7. Explore console's security features

Built-in security features: https://agorapulse.github.io/micronaut-console/#_security

 * IP filtering
 * User filtering
 * Cloud disabled by default
 * Until window  
 * Advisors
 * Auditing

Integration with Micronaut Security: https://agorapulse.github.io/micronaut-console/#_micronaut_security_integration

```groovy
dependencies {
    implementation "io.micronaut.security:micronaut-security"
    implementation "io.micronaut.security:micronaut-security-jwt"
}
```

Once Micronaut Security is on a place, you can no longer reach the console anonymously.

## Dru

Documentation: https://agorapulse.github.io/dru

### 1. Add Dru dependencies

```groovy
dependencies {
    implementation "com.agorapulse:dru:0.8.1"
    implementation "com.agorapulse:dru-client-micronaut-data:0.8.1"
    implementation "com.agorapulse:dru-parser-json:0.8.1"
    implementation "com.agorapulse:dru-parser-sql:0.8.1"
}
```

### 2. Create a simple service working with data

#### GlobalSalesService.groovy
```groovy
import dru.mn.demo.data.SaleRepository
import groovy.transform.CompileStatic

import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
@CompileStatic
class GlobalSalesService {

    private final SaleRepository sales
    private final DataSource dataSource

    GlobalSalesService(SaleRepository sales, DataSource dataSource) {
        this.sales = sales
        this.dataSource = dataSource
    }

    int countTotalSales() {
        return (int) sales.findAll()*.quantity*.amount.sum()
    }

}
```


### 3. Prepare JSON data using the console

#### console-json-data.http

```
POST http://localhost:8080/console/execute
Content-Type: text/groovy
Accept: application/json

// language=Groovy
import dru.mn.demo.data.Manufacturer
import dru.mn.demo.data.Product
import dru.mn.demo.data.Quantity
import dru.mn.demo.data.Sale

Manufacturer apple = manufacturers.save(new Manufacturer('Apple'))

Product macbookAir = products.save(new Product('MacBook Air', apple))
Product macbookPro = products.save(new Product('MacBook Pro', apple))

Manufacturer ms = manufacturers.save(new Manufacturer('Microsoft'))

Product surfacePro = products.save(new Product('Surface Pro', ms))
Product surfaceGo = products.save(new Product('Surface Go', ms))

Sale mbaSale = sales.save(new Sale(macbookAir, new Quantity(100)))
Sale mbpSale = sales.save(new Sale(macbookPro, new Quantity(35)))
Sale surfaceProSale = sales.save(new Sale(surfacePro, new Quantity(70)))
Sale surfaceGoSale = sales.save(new Sale(surfaceGo, new Quantity(300)))

sales.findAll()
```

Save the output as in `src/test/resources/dru/mn/demo/JsonSpec/sales.json`

#### sales.json
```json
{
  "result": [
    {
      "product": {
        "id": 45,
        "name": "MacBook Air",
        "manufacturer": {
          "id": 25,
          "name": "Apple"
        }
      },
      "quantity": {
        "amount": 100
      },
      "id": 19
    },
    {
      "product": {
        "id": 46,
        "name": "MacBook Pro",
        "manufacturer": {
          "id": 25,
          "name": "Apple"
        }
      },
      "quantity": {
        "amount": 35
      },
      "id": 20
    },
    {
      "product": {
        "id": 47,
        "name": "Surface Pro",
        "manufacturer": {
          "id": 26,
          "name": "Microsoft"
        }
      },
      "quantity": {
        "amount": 70
      },
      "id": 21
    },
    {
      "product": {
        "id": 48,
        "name": "Surface Go",
        "manufacturer": {
          "id": 26,
          "name": "Microsoft"
        }
      },
      "quantity": {
        "amount": 300
      },
      "id": 22
    }
  ]
}
```


### 4. Create a test for the service using JSON fixtures

Create a test file `src/test/groovy/dru/mn/demo/JsonSpec.groovy`

#### JsonSpec.groovy

```groovy
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
        from 'sales.sql', {
            map 'result', {
                to Sale
            }
        }
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
```

### 5. Prepare SQL data using the console

As an alternative, you can specify the data using a SQL script (useful for migrating the framework).

#### console-sql-data.http

```
POST http://localhost:8080/console/execute/result
Content-Type: text/groovy
Accept: text/plain

// language=groovy                                                                      
import dru.mn.demo.data.Manufacturer
import dru.mn.demo.data.Product
import dru.mn.demo.data.Quantity
import dru.mn.demo.data.Sale

Manufacturer apple = manufacturers.save(new Manufacturer('Apple'))

Product macbookAir = products.save(new Product('MacBook Air', apple))
Product macbookPro = products.save(new Product('MacBook Pro', apple))

Manufacturer ms = manufacturers.save(new Manufacturer('Microsoft'))

Product surfacePro = products.save(new Product('Surface Pro', ms))
Product surfaceGo = products.save(new Product('Surface Go', ms))

sales.save(new Sale(macbookAir, new Quantity(100)))
sales.save(new Sale(macbookPro, new Quantity(35)))
sales.save(new Sale(surfacePro, new Quantity(70)))
sales.save(new Sale(surfaceGo, new Quantity(300)))

String fileName = "${System.getProperty('user.dir')}/src/test/resources/dru/mn/demo/SqlSpec/sales.sql"
ctx.getBean(io.micronaut.transaction.TransactionOperations).executeWrite {
    ctx.getBean(javax.sql.DataSource).connection.prepareStatement("SCRIPT TO '$fileName'").execute()
}
```

### 6. Create a test for the service using SQL fixtures

Create a test file `src/test/groovy/dru/mn/demo/SqlSpec.groovy`

#### SqlSpec.groovy

```groovy

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
```

### 7. Create a JSON fixture files for each entity

Split the `sales.json` file by unique entity and keep only the id references. The new files must be created inside `src/test/resources/dru/mn/demo/TestData` directory.

#### manufacturers.json
```json
[
  {
    "id": 25,
    "name": "Apple"
  },
  {
    "id": 26,
    "name": "Microsoft"
  }
]
```
 
#### products.json
```json
[
  {
    "id": 45,
    "name": "MacBook Air",
    "manufacturer": {
      "id": 25
    }
  },
  {
    "id": 46,
    "name": "MacBook Pro",
    "manufacturer": {
      "id": 25
    }
  },
  {
    "id": 47,
    "name": "Surface Pro",
    "manufacturer": {
      "id": 26
    }
  },
  {
    "id": 48,
    "name": "Surface Go",
    "manufacturer": {
      "id": 26
    }
  }
]
```

#### sales.json
```json
[
  {
    "product": {
      "id": 45
    },
    "quantity": {
      "amount": 100
    },
    "id": 19
  },
  {
    "product": {
      "id": 46
    },
    "quantity": {
      "amount": 35
    },
    "id": 20
  },
  {
    "product": {
      "id": 47
    },
    "quantity": {
      "amount": 70
    },
    "id": 21
  },
  {
    "product": {
      "id": 48
    },
    "quantity": {
      "amount": 300
    },
    "id": 22
  }
]

```

### 8. Create a test data provider class

Create a new class in `src/test/groovy/dru/mn/demo/TestData.groovy`

#### TestData.groovy
```groovy
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
        include manufacturers
        from 'products.json', {
            map {
                to Product
            }
        }
    }

    public static final PreparedDataSet sales = Dru.prepare {
        include products
        from 'sales.json', {
            map {
                to Sale
            }
        }
    }

}
```

### 9. Rewrite the service test

```groovy
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
```

### 10. Explore advanced Dru features

 * Advanced Property and Type Mapping - https://agorapulse.github.io/dru/#_property_and_type_mapping
 * Available Parsers - https://agorapulse.github.io/dru/#_parsers
 * Supported Frameworks - https://agorapulse.github.io/dru/#_clients
