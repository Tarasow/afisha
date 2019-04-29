package afisha.database

import org.jetbrains.exposed.sql.Table

object actors_performance : Table() {
    val performance = reference("performance_id", Performances).primaryKey(0)
    val actor = reference("actor_id", Actors).primaryKey(1)
}