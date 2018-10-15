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
    var i = inputName
    var s1: Int
    var s2: Int
    var a1: Double
    var a2 = 0.0
    var mark: Int
    s1 = 1; s2 = 1
    while (s1 > 0 && s2 > 0) {
        mark = 1
        s1 = 0
        s2 = 0
        val f = Scanner(File(i))
        //f.useDelimiter("\n|(\r\n)") - ne srabativaet
        val f1 = File("tmp1").bufferedWriter()
        val f2 = File("tmp2").bufferedWriter()
        a1 = f.nextLine().toDouble()
        if (f.hasNextLine()) {
            f1.write("$a1\n")
            a2 = f.nextLine().toDouble()
        }
        while (f.hasNextLine()) {
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
            if (mark == 1) {
                f1.write("$a2\n"); s1++
            } else {
                f2.write("$a2\n"); s2++
            }
            a1 = a2
            a2 = f.nextLine().toDouble()
        }
        if (s2 > 0 && mark == 2) f2.write("$a2\n'\n")
        if (s1 > 0 && mark == 1) f1.write("$a2\n'\n")
        f2.close()
        f1.close()
        f.close()
        i = outputName
        if (s1 == 0 && s2 == 0) break
        val r = File(i).bufferedWriter()
        val r1 = Scanner(File("tmp1"))
        val r2 = Scanner(File("tmp2"))
        r1.useDelimiter("\n"); r2.useDelimiter("\n")
        if (r1.hasNextLine()) a1 = r1.nextLine().toDouble()
        if (r2.hasNextLine()) a2 = r2.nextLine().toDouble()
        var file1: Boolean
        var file2: Boolean
        while (r1.hasNextLine() && r2.hasNextLine()) {
            file1 = false; file2 = false
            while (!file1 && !file2) {
                if (a1 <= a2) {
                    r.write("$a1\n")
                    val tmp = r1.nextLine()
                    if (tmp == "'") {
                        file1 = true
                        if (r1.hasNextLine()) a1 = r1.nextLine().toDouble()
                    } else
                        a1 = tmp.toDouble()
                } else {
                    r.write("$a2\n")
                    val tmp = r2.nextLine()
                    if (tmp == "'") {
                        file2 = true
                        if (r2.hasNextLine()) a2 = r2.nextLine().toDouble()
                    } else
                        a2 = tmp.toDouble()
                }
            }
            while (!file1) {
                r.write("$a1\n")
                val tmp = r1.nextLine()
                if (tmp == "'") {
                    file1 = true
                    if (r1.hasNextLine()) a1 = r1.nextLine().toDouble()
                } else
                    a1 = tmp.toDouble()
            }
            while (!file2) {
                r.write("$a2\n")
                val tmp = r2.nextLine()
                if (tmp == "'") {
                    file2 = true
                    if (r2.hasNextLine()) a2 = r2.nextLine().toDouble()
                } else
                    a2 = tmp.toDouble()
            }
        }
        file1 = false
        file2 = false
        while (!file1 && r1.hasNextLine()) {
            r.write("$a1\n")
            val tmp = r1.nextLine()
            if (tmp == "'") {
                file1 = true
                if (r1.hasNextLine()) a1 = r1.nextLine().toDouble()
            } else
                a1 = tmp.toDouble()
        }
        while (!file2 && r2.hasNextLine()) {
            r.write("$a2\n")
            val tmp = r2.nextLine()
            if (tmp == "'") {
                file2 = true
                if (r2.hasNextLine()) a2 = r2.nextLine().toDouble()
            } else
                a2 = tmp.toDouble()
        }
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

