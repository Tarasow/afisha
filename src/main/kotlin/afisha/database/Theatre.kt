package afisha.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Theatres : IntIdTable() {
    val name = varchar("name", 250)
    val address = varchar("address", 250)
    val places = (integer("places"))
}

class Theatre(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Theatre>(Theatres)

    var name by Theatres.name
    var address by Theatres.address
    var places by Theatres.places
}
