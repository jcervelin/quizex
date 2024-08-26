package io.ideas.jcervelin.quizex

import io.ideas.jcervelin.quizex.configs.loadQuestionsFromCSV
import io.ideas.jcervelin.quizex.usecases.checkAnswer
import io.ideas.jcervelin.quizex.usecases.randomQuestion
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.http4k.core.*
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    loadQuestionsFromCSV("src/main/resources/questions.csv")
    val app = routes(
        "/" bind Method.GET to { _: Request ->
            Response(Status.OK).body(loadStaticFile("index.html"))
        },
        "/question-content" bind Method.GET to { _: Request ->
            Response(Status.OK).body(renderQuestionContent())
        },
        "/next_question" bind Method.GET to { _: Request ->
            Response(Status.OK).body(nextQuestion())
        },
        "/label" bind Method.GET to { _: Request ->
            Response(Status.OK).body(label())
        },
        "/submit_answer" bind Method.GET to { request: Request ->
            Response(Status.OK).body(handleAnswer(request))
        },
        "/static" bind static(ResourceLoader.Classpath("static"))
    )

    app.asServer(SunHttp(8080)).start()
    println("Server started on http://localhost:8080")
}

fun loadStaticFile(fileName: String): String {
    val path = Paths.get("src/main/resources/static/$fileName")
    return Files.readAllBytes(path).toString(Charsets.UTF_8)
}

fun label(): String {
    val questionText = randomQuestion()
    val writer = StringWriter()
    writer.append(questionText)

    writer.appendHTML().input {
        id = "hidden-question"
        type = InputType.hidden
        name = "question"
        value = questionText
        attributes["hx-swap-oob"] = "true"
    }
    return writer.toString()
}

fun nextQuestion(): String {
    val questionText = label()
    val writer = StringWriter()

    writer.append(questionText)

    writer.appendHTML().input {
        id = "answer-input"
        type = InputType.text
        name = "answer"
        value = ""
        attributes["hx-swap-oob"] = "true"
    }

    return writer.toString()
}

fun renderQuestionContent(): String {
    val template = loadStaticFile("question-template.html")
    val writer = StringWriter()
    writer.append(template)
    return writer.toString()
}

fun handleAnswer(request: Request): String {
    val question = request.query("question")
    val answer = request.query("answer")

    if (question.isNullOrBlank() || answer.isNullOrBlank()) {
        return "Invalid answer"
    }

    val result = if (checkAnswer(question, answer)) "Correct!" else "Incorrect!"
    return result
}





