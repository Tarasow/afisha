package afisha.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Tickets : IntIdTable() {
    val price = integer("price")
    val place = integer("place")
    val performanceId = reference("performance_id", Performances.id)
    val ownerId = reference("owner_id", Users.id)
}

class Ticket(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Ticket>(Tickets)

    var price by Tickets.price
    var place by Tickets.place
    var performanceId by Performance referencedOn Tickets.performanceId
    var ownerId by User referencedOn Tickets.ownerId
}
