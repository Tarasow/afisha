package afisha.server

import afisha.database.Actor
import afisha.database.Performance
import afisha.database.Theatre
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.Template
import io.ktor.html.TemplatePlaceholder
import io.ktor.html.insert
import io.ktor.html.respondHtml
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File


class AppServer {
    fun createAndStart(host: String = "127.0.0.1", port: Int = 8081) {
        embeddedServer(Netty, port, host) {
            val currentDir = File(".").absoluteFile
            environment.log.info("Current directory: $currentDir")

            val webDir = listOf(
                "resources",
                "../src/main/resources",
                "src/main/resources"
            ).map {
                File(currentDir, it)
            }.firstOrNull { it.isDirectory }?.absoluteFile ?: error("Can't find 'web' folder for this sample")

            environment.log.info("Web directory: $webDir")

            routing {
                get("/") {
                    createMainPage(call)
                }

                get("/theatres") {
                    createTheatresPage(call)
                }

                get("/performances") {
                    createPerformancesPage(call)
                }

                get("/actors") {
                    createActorsPage(call)
                }
                // TODO create pages for tickets and booking

                static("/static") {
                    files(webDir)
                }
            }
        }.start(wait = true)
    }


    private suspend fun createMainPage(call: ApplicationCall) {
        call.respondHtml {
            head {
                title("АФИША")
                link(rel = "stylesheet", href = "/static/css/styles.css", type = "text/css")
                link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            }
            body {
                insert(Header(), TemplatePlaceholder())

                script(src = "/static/require.min.js") {}
                script {
                    +"require.config({baseUrl: '/static'});\n"
                }

                div {
                    classes = setOf("main-block")
                    id = "js-response"
                    p {
                        classes = setOf("random-header")
                        +"Случайный театр"
                    }
                    transaction {
                        val theatre = Theatre.all().toList().random()
                        insert(TheatreElement(theatre), TemplatePlaceholder())
                    }
                }

                div {
                    classes = setOf("main-block")
                    p {
                        classes = setOf("random-header")
                        +"Случайный актер"
                    }
                    transaction {
                        val actor = Actor.all().toList().random()
                        insert(ActorElement(actor), TemplatePlaceholder())
                    }
                }

                div {
                    classes = setOf("main-block")
                    p {
                        classes = setOf("random-header")
                        +"Случайный спектакль"
                    }
                    transaction {
                        val performance = Performance.all().toList().random()
                        insert(PerformanceElement(performance), TemplatePlaceholder())
                    }
                }
            }
        }
    }

    private suspend fun createTheatresPage(call: ApplicationCall) {
        call.respondHtml {
            head {
                title("Театры")
                link(rel = "stylesheet", href = "/static/css/styles.css", type = "text/css")
                link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            }
            body {
                insert(Header(), TemplatePlaceholder())

                div {
                    classes = setOf("page-wrapper")
                    transaction {
                        Theatre.all().forEach {
                            div {
                                classes = setOf("page-item")
                                insert(TheatreElement(it), TemplatePlaceholder())
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun createPerformancesPage(call: ApplicationCall) {
        call.respondHtml {
            head {
                title("Спектакли")
                link(rel = "stylesheet", href = "/static/css/styles.css", type = "text/css")
                link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            }
            body {
                insert(Header(), TemplatePlaceholder())

                div {
                    classes = setOf("page-wrapper")
                    transaction {
                        Performance.all().forEach {
                            div {
                                classes = setOf("page-item")
                                insert(PerformanceElement(it), TemplatePlaceholder())
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun createActorsPage(call: ApplicationCall) {
        call.respondHtml {
            head {
                title("Актеры")
                link(rel = "stylesheet", href = "/static/css/styles.css", type = "text/css")
                link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            }
            body {
                insert(Header(), TemplatePlaceholder())

                div {
                    classes = setOf("page-wrapper")
                    transaction {
                        Actor.all().forEach {
                            div {
                                classes = setOf("page-item")
                                insert(ActorElement(it), TemplatePlaceholder())
                            }
                        }
                    }
                }
            }
        }
    }


    class Header : Template<FlowContent> {
        override fun FlowContent.apply() {
            div {
                id = "header"

                div {
                    a {
                        classes = setOf("action-choice")
                        href = "./"
                        +"На главную"
                    }
                }

                div {
                    a {
                        classes = setOf("action-choice")
                        href = "./theatres"
                        +"Театры"
                    }
                }

                div {
                    a {
                        classes = setOf("action-choice")
                        href = "./performances"
                        +"Спектакли"
                    }
                }

                div {
                    a {
                        classes = setOf("action-choice")
                        href = "./actors"
                        +"Актеры"
                    }
                }
            }
        }
    }

    class TheatreElement(private val theatre: Theatre) : Template<FlowContent> {
        override fun FlowContent.apply() {
            p {
                classes = setOf("item-name")
                +theatre.name
            }
            p {
                classes = setOf("secondary-info")
                +theatre.address
            }
            p {
                classes = setOf("secondary-info")
                +"${theatre.places} мест в зале"
            }
        }
    }

    class ActorElement(private val actor: Actor) : Template<FlowContent> {
        override fun FlowContent.apply() {
            val theatre = Theatre.all().find { it.id._value == actor.theatre.id._value }?.name!!

            p {
                classes = setOf("item-name")
                +actor.name
            }
            p {
                classes = setOf("secondary-info")
                +theatre
            }
        }
    }

    class PerformanceElement(private val performance: Performance) : Template<FlowContent> {
        override fun FlowContent.apply() {
            val theatre = Theatre.all().find { it.id._value == performance.theatre.id._value }?.name!!
            p {
                classes = setOf("item-name")
                +performance.name
            }
            p {
                classes = setOf("secondary-info")
                +theatre
            }
            p {
                classes = setOf("secondary-info")
                +"Режиссер: ${performance.director}"
            }
            p {
                classes = setOf("secondary-info")
                +"Дата и время: ${performance.time.toString("dd/MM/yyyy HH:mm")}"
            }
        }
    }
}