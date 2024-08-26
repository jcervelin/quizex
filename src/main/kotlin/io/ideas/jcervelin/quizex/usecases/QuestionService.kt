package io.ideas.jcervelin.quizex.usecases

import io.ideas.jcervelin.quizex.configs.questionsMap

fun randomQuestion(): String {
    return questionsMap.keys.random()
}

fun checkAnswer(question: String, answer: String): Boolean {
    return questionsMap[question].equals(answer.uppercase())
}