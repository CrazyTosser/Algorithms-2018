@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.util.*

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
 * каждый на отдельной строке. Пример:
 *
 * 13:15:19
 * 07:26:57
 * 10:00:03
 * 19:56:14
 * 13:15:19
 * 00:40:31
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 00:40:31
 * 07:26:57
 * 10:00:03
 * 13:15:19
 * 13:15:19
 * 19:56:14
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    var tm: Any = mutableListOf<Int>()
    val reg = Regex("\\d{2}:\\d{2}:\\d{2}")
    for (str in File(inputName).readLines()) {
        if (!str.matches(reg)) throw IllegalArgumentException()
        val tmp = str.split(":")
        (tm as MutableList<Int>).add(tmp[0].toInt() * 3600 + tmp[1].toInt() * 60 + tmp[2].toInt())
    }
    tm = (tm as MutableList<Int>).toIntArray()
    heapSort(tm)
    File(outputName).printWriter().use { out ->
        tm.forEach {
            out.println("%02d:%02d:%02d".format(it / 3600, it % 3600 / 60, it % 3600 % 60))
        }
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    val wr = sortedMapOf<String, SortedMap<Int, SortedMap<String, TreeSet<String>>>>()
    val reg = Regex("[А-Яа-я]+ [А-Яа-я]+ - [А-Яа-я]+ \\d+")
    File(inputName).forEachLine {
        if (!it.matches(reg)) throw IllegalArgumentException()
        val tmp = it.split('-').map { item -> item.trim() }
        val name = tmp[1].split(' ')[0]
        val num = tmp[1].split(' ')[1].toInt()
        val fio = tmp[0].split(" ")[0]
        val fn = tmp[0].split(" ")[1]
        wr.getOrPut(name) { sortedMapOf(num to sortedMapOf(fio to sortedSetOf(fn))) }
                .getOrPut(num) { sortedMapOf(fio to sortedSetOf(fn)).toSortedMap() }
                .getOrPut(fio) { sortedSetOf(fn) }
                .add(fn)
    }
    File(outputName).printWriter().use { out ->
        for (str in wr) {
            for (h in str.value) {
                val names = StringBuilder()
                for (hum in h.value)
                    for (n in hum.value)
                        names.append(hum.key).append(" ").append(n).append(", ")
                out.println("%s %d - %s".format(str.key, h.key,
                        names.toString().substring(0, names.toString().length - 2)))
            }
        }
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val wr = listOf<Short>()
    val min = -2730
    val max = 5000
    val size = max - min + 1
    val ind = IntArray(size)
    val fin = Scanner(File(inputName))
    while (fin.hasNextLine()) {
        ind[(fin.nextLine().toDouble() * 10).toInt() - min]++
    }
    File(outputName).printWriter().use {
        for (i in 0 until size) {
            var count = ind[i]
            while (count > 0) {
                it.println((i + min).toDouble() / 10)
                count--
            }
        }
    }
    return print(wr)
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    var i = first.size
    var j = 0
    var f = 0
    while (f < first.size && i < second.size) {
        if (first[f] < second[i]!!) {
            second[j] = first[f]
            f++
        } else {
            second[j] = second[i]
            i++
        }
        j++
    }
    if (i == second.size && f < first.size)
        for (c in f until first.size)
            second[j++] = first[c]
}

