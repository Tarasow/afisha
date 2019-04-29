package afisha.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Performances : IntIdTable() {
    val name = varchar("name", 100)
    val director = varchar("director", 100)
    val time = datetime("time")
    val theatre = reference("theatre_id", Theatres)
}

class Performance(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Performance>(Performances)

    var name by Performances.name
    var director by Performances.director
    var time by Performances.time
    var theatre by Theatre referencedOn Performances.theatre

    var actors by Actor via actors_performance
}
