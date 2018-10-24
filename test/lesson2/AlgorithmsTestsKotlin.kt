package lesson2

import org.junit.jupiter.api.Tag
import kotlin.test.Test
import kotlin.test.assertEquals

class AlgorithmsTestsKotlin : AbstractAlgorithmsTests() {
    @Test
    @Tag("Easy")
    fun testOptimizeBuyAndSell() {
        optimizeBuyAndSell { optimizeBuyAndSell(it) }
    }

    @Test
    @Tag("Easy")
    fun testJosephTask() {
        josephTask { menNumber, choiceInterval -> josephTask(menNumber, choiceInterval) }
    }

    @Test
    @Tag("Normal")
    fun testLongestCommonSubstring() {
        longestCommonSubstring { first, second -> longestCommonSubstring(first, second) }
    }

    @Test
    @Tag("Easy")
    fun testCalcPrimesNumber() {
        calcPrimesNumber { calcPrimesNumber(it) }
    }

    @Test
    @Tag("Easy")
    fun testCalcPrimesNumber2() {
        assertEquals(788060, calcPrimesNumber(12000000))
        assertEquals(910077, calcPrimesNumber(14000000))
    }

    @Test
    @Tag("Hard")
    fun testBaldaSearcher() {
        baldaSearcher { inputName, words -> baldaSearcher(inputName, words) }
    }

    @Test
    @Tag("Normal")
    fun testLongest() {
        assertEquals("41SAw343", lesson2.longestCommonSubstring(
                """
                    5441SAw343sssaweq!@#sdxc
                """.trimIndent(),
                """
                    trWERAS12341SAw343
                """.trimIndent()
        ))
    }

    @Test
    @Tag("Hard")
    fun testBalda() {
        assertEquals(setOf("ЛЕД", "ЕДА", "НИНА", "ЗАНОЗА", "ЛИНЗА"), baldaSearcher("input/balda_in2.txt",
                setOf("ЛЕД", "ЕДА", "НИНА", "ЗАНОЗА", "ЛИНЗА", "ЕГА", "СМЫСЛ")))
    }
}