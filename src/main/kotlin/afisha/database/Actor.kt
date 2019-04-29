package afisha.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Actors : IntIdTable() {
    val name = varchar("name", 150)
    val theatre = reference("theatre_id", Theatres)
}

class Actor(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Actor>(Actors)

    var name by Actors.name
    var theatre by Theatre referencedOn Actors.theatre
}
