package io.ideas.jcervelin.quizex.configs

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlin.io.path.Path

val questionsMap = HashMap<String, String>()

fun loadQuestionsFromCSV(fileName: String): HashMap<String, String> {

    csvReader().open(Path(fileName).toFile()) {
        readAllWithHeaderAsSequence().forEach { row ->
            row["question"]?.let { question ->
                row["answer"]?.let { answer ->
                    questionsMap[question] = answer
                }
            }
        }
    }
    return questionsMap
}

fun main() {
    val questionsMap = loadQuestionsFromCSV("questions.csv")
    questionsMap.forEach { (question, answer) ->
        println("$question -> $answer")
    }
}