
package dru.mn.demo.data

import javax.persistence.Id
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.GeneratedValue

@Entity
class Sale {

    @ManyToOne
    Product product

    Quantity quantity

    @Id
    @GeneratedValue
    Long id

}
