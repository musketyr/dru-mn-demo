
package dru.mn.demo.data

import io.micronaut.core.annotation.Creator

import javax.persistence.*

@Entity
class Manufacturer {
    @Id
    @GeneratedValue
    Long id

    String name
}
