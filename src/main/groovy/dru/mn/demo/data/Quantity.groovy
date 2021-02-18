
package dru.mn.demo.data

import groovy.transform.Immutable
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType

@TypeDef(type = DataType.INTEGER)
@Immutable
class Quantity {
    int amount
}
