package dru.mn.demo.data

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

    RepositoryBindings(BookRepository bookRepository, ProductRepository productRepository, SaleRepository saleRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository
        this.productRepository = productRepository
        this.saleRepository = saleRepository
        this.userRepository = userRepository
    }

    @Override
    Map<String, ?> getBinding() {
        return [
                books: bookRepository,
                products: productRepository,
                sales: saleRepository,
                users: userRepository
        ]
    }
}
