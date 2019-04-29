package afisha.connector

import org.jetbrains.exposed.sql.Database

object Connector {
    fun connectToDatabase(dbName: String): Database {
        return Database.connect(
            url = "jdbc:postgresql://localhost:5432/$dbName",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "psgspswd"
        )
    }
}