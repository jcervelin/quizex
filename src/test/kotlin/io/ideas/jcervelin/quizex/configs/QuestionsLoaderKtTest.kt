package io.ideas.jcervelin.quizex.configs

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class QuestionsLoaderKtTest {

    @Test
    fun `should load map with 2 questions`() {
        val mapLoaded = loadQuestionsFromCSV("/questions.csv")
        assertEquals(mapLoaded.size, 2)
    }

    @Test
    fun `should throw error if map is empty`() {
        val exception = assertThrows<RuntimeException>
        { loadQuestionsFromCSV("/emptyQuestions.csv") }
        assertEquals(expected = "Questions not loaded correctly", actual = exception.message)
    }

}