package afisha

import afisha.connector.Connector
import afisha.server.AppServer

fun main() {
    Connector.connectToDatabase(dbName = "afisha-db")
    AppServer().createAndStart()
}