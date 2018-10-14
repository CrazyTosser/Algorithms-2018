@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.io.FileInputStream
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
    val wr =
            HashMap<String, SortedMap<Int, SortedMap<String, TreeSet<String>>>>().toSortedMap()
    val reg = Regex("[А-Яа-я]+ [А-Яа-я]+ - [А-Яа-я]+ \\d+")
    File(inputName).forEachLine {
        if (!it.matches(reg)) throw IllegalArgumentException()
        val tmp = it.split('-').map { item -> item.trim() }
        val name = tmp[1].split(' ')[0]
        val num = tmp[1].split(' ')[1].toInt()
        val fio = tmp[0].split(" ")[0]
        val fn = tmp[0].split(" ")[1]
        wr.getOrPut(name) { hashMapOf(num to hashMapOf(fio to sortedSetOf(fn)).toSortedMap()).toSortedMap() }
                .getOrPut(num) { hashMapOf(fio to sortedSetOf(fn)).toSortedMap() }
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
    fun getNum(inp: FileInputStream): Double {
        val res = StringBuilder()
        var buffer = inp.read().toChar()
        while (buffer == '-' || buffer == '.' || buffer.isDigit()) {
            res.append(buffer)
            buffer = inp.read().toChar()
        }
        if (buffer == '\r') inp.read()
        return if (res.toString().trim() == "") -300.0 else res.toString().trim().toDouble()
    }

    fun endRange(inp: FileInputStream): Boolean {
        inp.read()
        val buf = inp.read().toChar()
        if (buf == '\uFFFF') return true
        if (buf != '\'') inp.skip(-2) else inp.skip(1)
        return buf == '\''
    }

    var i = inputName
    var s1: Int
    var s2: Int
    var a1 = 0.0
    var a2 = 0.0
    var mark: Int
    s1 = 1; s2 = 1
    while (s1 > 0 && s2 > 0) {
        mark = 1
        s1 = 0
        s2 = 0
        val f = File(i).inputStream()
        val f1 = File("tmp1").writer()
        val f2 = File("tmp2").writer()
        if (f.available() > 0) a1 = getNum(f)
        if (f.available() > 0) {
            f1.write("$a1\n")
            a2 = getNum(f)
        }
        while (f.available() > 0) {
            if (a2 < a1) {
                when (mark) {
                    1 -> {
                        f1.write("'\n"); mark = 2; s1++
                    }
                    2 -> {
                        f2.write("'\n"); mark = 1; s2++
                    }
                }
            }
            if (a2 > -300.0)
                if (mark == 1) {
                    f1.write("$a2\n"); s1++
                } else {
                    f2.write("$a2\n"); s2++
                }
            a1 = a2
            a2 = getNum(f)
        }
        if (s2 > 0 && mark == 2) f2.write("$a2'")
        if (s1 > 0 && mark == 1) f1.write("$a2'")
        f2.close()
        f1.close()
        f.close()
        i = outputName
        val r = File(i).writer()
        val r1 = File("tmp1").inputStream()
        val r2 = File("tmp2").inputStream()
        a1 = if (r1.available() > 0) getNum(r1) else -300.0
        a2 = if (r2.available() > 0) getNum(r2) else -300.0
        var file1: Boolean
        var file2: Boolean
        while (r1.available() > 0 && r2.available() > 0) {
            file1 = false; file2 = false
            while (!file1 && !file2) {
                if (a1 <= a2) {
                    r.write("$a1\n")
                    file1 = endRange(r1)
                    a1 = getNum(r1)
                } else {
                    r.write("$a2\n")
                    file2 = endRange(r2)
                    a2 = getNum(r2)
                }
            }
            a1 = getNum(r1)
            while (!file1) {
                r.write("$a1\n")
                file1 = endRange(r1)
                a1 = getNum(r1)
            }
            a2 = getNum(r2)
            while (!file2) {
                r.write("$a2\n")
                file2 = endRange(r2)
                a2 = getNum(r2)
            }
        }
        file1 = false
        file2 = false
        while (!file1 && r1.available() > 0) {
            r.write("$a1\n")
            file1 = endRange(r1)
            a1 = getNum(r1)
        }
        if (a1.compareTo(-300.0) != 0) r.write("$a1\n")
        while (!file2 && r2.available() > 0) {
            r.write("$a2\n")
            file2 = endRange(r2)
            a2 = getNum(r2)
        }
        if (a2.compareTo(-300.0) != 0) r.write("$a2\n")
        r.close()
        r1.close()
        r2.close()
    }
    File("tmp1").delete()
    File("tmp2").delete()
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

