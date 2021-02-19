# Micronaut Data + Micronaut Console + Dru Demo

Micronaut Console and Dru for Micronaut Data Demo 

## Micronaut Console

Documentation: https://agorapulse.github.io/micronaut-console

### 1. Add Micronaut Console Dependency

```groovy
dependencies {
    implementation 'com.agorapulse:micronaut-console:0.1.4'
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


### 3. Prepare the test data using the console

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

[
        mbaSale,
        mbpSale,
        surfaceProSale,
        surfaceGoSale,
]
```
