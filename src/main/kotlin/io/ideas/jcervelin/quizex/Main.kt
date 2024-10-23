package io.ideas.jcervelin.quizex

import io.ideas.jcervelin.quizex.configs.loadQuestionsFromCSV
import io.ideas.jcervelin.quizex.usecases.handleAnswer
import io.ideas.jcervelin.quizex.usecases.label
import io.ideas.jcervelin.quizex.usecases.nextQuestion
import io.ideas.jcervelin.quizex.usecases.renderQuestionContent
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.io.File

fun main() {
    loadQuestionsFromCSV("/questions.csv")
    val app = routes(
        "/" bind Method.GET to {
            Response(Status.OK).body(loadStaticFile("index.html"))
        },
        "/question-content" bind Method.GET to {
            val template = loadStaticFile("question-template.html")
            Response(Status.OK).body(renderQuestionContent(template))
        },
        "/next_question" bind Method.GET to {
            Response(Status.OK).body(nextQuestion())
        },
        "/label" bind Method.GET to {
            Response(Status.OK).body(label())
        },
        "/submit_answer" bind Method.GET to { request: Request ->
            val question = request.query("question")
            val answer = request.query("answer")

            Response(Status.OK).body(handleAnswer(question, answer))
        },
        "/static" bind static(ResourceLoader.Classpath("static"))
    )

    app.asServer(SunHttp(8080)).start()
    println("Server started on http://localhost:8080")
}

fun loadStaticFile(fileName: String): String {
    return File({}.javaClass.classLoader.getResource("static/$fileName")!!.toURI()).readText()
}
