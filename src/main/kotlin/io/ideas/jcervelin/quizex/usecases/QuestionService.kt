package io.ideas.jcervelin.quizex.usecases

import io.ideas.jcervelin.quizex.configs.questionsMap
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.stream.appendHTML
import java.io.StringWriter

fun randomQuestion() = questionsMap.keys.random()

fun checkAnswer(question: String, answer: String) = questionsMap[question].equals(answer.uppercase())

fun label(): String {
    val questionText = randomQuestion()
    return StringWriter()
        .append(questionText)
        .appendHTML().input {
            id = "hidden-question"
            type = InputType.hidden
            name = "question"
            value = questionText
            attributes["hx-swap-oob"] = "true"
        }.toString()
}

fun nextQuestion() =
    StringWriter()
        .append(label())
        .appendHTML()
            .input {
                id = "answer-input"
                type = InputType.text
                name = "answer"
                value = ""
                attributes["hx-swap-oob"] = "true"
            }.toString()

fun renderQuestionContent(template: String) =
    StringWriter()
        .append(template)
        .toString()

fun handleAnswer(question: String?, answer: String?) =
    when {
        question.isNullOrBlank() || answer.isNullOrBlank() -> "Invalid answer"
        checkAnswer(question, answer) -> "Correct!"
        else -> "Incorrect!"
    }
