package lesson1

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Tag
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.fail


class TaskTestsKotlin : AbstractTaskTests() {

    @Test
    @Tag("Easy")
    fun testSortTimes() {
        sortTimes { inputName, outputName -> sortTimes(inputName, outputName) }
    }

    @Test
    @Tag("Easy")
    fun testSortTimes2() {
        try {
            sortTimes("input/time_in4.txt", "temp.txt")
            assertFileContent("temp.txt",
                    """
                     00:00:00
                     00:00:01
                     00:00:02
                     00:00:03
                     00:10:00
                     00:15:00
                """.trimIndent()
            )
        } finally {
            File("temp.txt").delete()
        }
    }

    @Test
    @Tag("Normal")
    fun testSortAddresses() {
        sortAddresses { inputName, outputName -> sortAddresses(inputName, outputName) }
    }

    @Test
    @Tag("Normal")
    fun testSortAddresses2() {
        try {
            sortAddresses("input/addr_in2.txt", "temp.txt")
            fail("Excepted IllegalArgumentException")
        } catch (ex: IllegalArgumentException) {

        }
    }

    @Test
    @Tag("Normal")
    fun testSortTemperatures() {
        sortTemperatures { inputName, outputName -> sortTemperatures(inputName, outputName) }
    }

    @Test
    @Tag("Normal")
    fun testSortTemperatures2() {
        val random = Random()
        File("temp_unsorted.txt").bufferedWriter().use { out ->
            for (i in 1..100000) {
                out.write("${random.nextInt(7730).toDouble() / 10 - 273.0}\n")
            }
        }
        sortTemperatures("temp_unsorted.txt", "tmp")
        Scanner(File("tmp")).use {
            var s1 = it.nextLine().toDouble()
            var s2 = it.nextLine().toDouble()
            while (it.hasNextLine()) {
                if (s1 > s2) fail("$s1 ? $s2")
                s1 = s2
                s2 = it.nextLine().toDouble()
            }
        }
    }

    @Test
    @Tag("Normal")
    fun testSortSequence() {
        sortSequence { inputName, outputName -> sortSequence(inputName, outputName) }
    }

    @Test
    @Tag("Easy")
    fun testMergeArrays() {
        val result = arrayOf(null, null, null, null, null, 1, 3, 9, 13, 18, 23)
        mergeArrays(arrayOf(4, 9, 15, 20, 23), result)
        assertArrayEquals(arrayOf(1, 3, 4, 9, 9, 13, 15, 18, 20, 23, 23), result)

        run {
            val (first, second, expectedResult) = generateArrays(20000, 20000)
            mergeArrays(first, second)
            assertArrayEquals(expectedResult, second)
        }

        run {
            val (first, second, expectedResult) = generateArrays(500000, 500000)
            mergeArrays(first, second)
            assertArrayEquals(expectedResult, second)
        }
    }

    @Test
    @Tag("Easy")
    fun testMergeArrays2() {
        var result = arrayOf(null, null, 1, 2, 3)
        mergeArrays(arrayOf(4, 5), result)
        assertArrayEquals(arrayOf(1, 2, 3, 4, 5), result)

        result = arrayOf(null, null, 2, 3, 4)
        mergeArrays(arrayOf(1, 5), result)
        assertArrayEquals(arrayOf(1, 2, 3, 4, 5), result)
    }
}