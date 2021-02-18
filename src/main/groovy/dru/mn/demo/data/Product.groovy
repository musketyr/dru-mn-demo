
package dru.mn.demo.data

import javax.persistence.*

@Entity
class Product {

    @Id
    @GeneratedValue
    Long id
    String name

    @ManyToOne
    Manufacturer manufacturer
}
