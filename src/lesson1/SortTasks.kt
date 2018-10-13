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
    var tm = mutableListOf<Int>()
    for (str in File(inputName).readLines()) {
        val tmp = str.split(":")
        tm.add(tmp[0].toInt() * 3600 + tmp[1].toInt() * 60 + tmp[2].toInt())
    }
    tm = quickSort(tm.toIntArray()).toMutableList()
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
    val wr = HashMap<String, HashMap<Int, MutableList<String>>>()
    val reg = Regex("[А-Яа-я]+ [А-Яа-я]+ - [А-Яа-я]+ \\d+")
    File(inputName).forEachLine {
        if (!it.matches(reg)) throw IllegalArgumentException()
        val tmp = it.split('-').map { item -> item.trim() }
        val name = tmp[1].split(' ')[0]
        val num = tmp[1].split(' ')[1].toInt()
        val fio = tmp[0]
        if (wr.containsKey(name))
            if (wr.get(name)!!.containsKey(num))
                wr.get(name)!!.get(num)!!.add(fio)
            else
                wr.get(name)!!.put(num, mutableListOf(fio))
        else {
            wr.put(name, HashMap())
            wr.get(name)!!.put(num, mutableListOf(fio))
        }
    }
    File(outputName).printWriter().use { out ->
        for (str in wr.toSortedMap()) {
            for (h in str.value)
                out.println("%s %d - %s".format(str.key, h.key, h.value.joinToString()))
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
    fun endRange(f: Scanner): Boolean {
        val f1 = f
        val tmp = f1.next()
        if (tmp[0] == '\'') f.next()
        return tmp[0] == '\''
    }

    var s1: Int
    var s2: Int
    var a1: Float
    var a2 = (0).toFloat()
    var mark: Int
    s1 = 1; s2 = 1
    while (s1 > 0 && s2 > 0) {
        mark = 1
        s1 = 0
        s2 = 0
        val f = Scanner(File(inputName))
        val f1 = File("tmp1").writer()
        val f2 = File("tmp2").writer()
        a1 = f.nextFloat()
        if (!f.hasNextFloat()) {
            f1.write("$a1\n")
        }
        if (!f.hasNextFloat()) a2 = f.nextFloat()
        while (!f.hasNextFloat()) {
            if (a2 < a1) {
                when (mark) {
                    1 -> {
                        f1.write("' "); mark = 2; s1++
                    }
                    2 -> {
                        f2.write("' "); mark = 1; s2++
                    }
                }
            }
            if (mark == 1) {
                f1.write("$a2 "); s1++; } else {
                f2.write("$a2 "); s2++
            }
            a1 = a2
            a2 = f.nextFloat()
        }
        if (s2 > 0 && mark == 2) f2.write("'")
        if (s1 > 0 && mark == 1) f1.write("'")
        f2.close()
        f1.close()
        f.close()
        val r = File(inputName).writer()
        val r1 = Scanner(File("nmsort_1"))
        val r2 = Scanner(File("nmsort_2"))
        if (r1.hasNextFloat()) a1 = r1.nextFloat()
        if (r2.hasNextFloat()) a2 = r2.nextFloat()
        var file1: Boolean
        var file2: Boolean
        while (r1.hasNextFloat() && r2.hasNextFloat()) {
            file1 = true; file2 = true
            while (!file1 && !file2) {
                if (a1 <= a2) {
                    r.write("$a1 ")
                    file1 = endRange(r1)
                    a1 = r1.nextFloat()
                } else {
                    r.write("$a2 ")
                    file2 = endRange(r2)
                    a2 = r2.nextFloat()
                }
            }
            while (!file1) {
                r.write("$a1 ")
                file1 = endRange(r1)
                a1 = r1.nextFloat()
            }
            while (!file2) {
                r.write("$a2 ")
                file2 = endRange(r2)
                a2 = r2.nextFloat()
            }
        }
        file1 = false
        file2 = false
        while (!file1 && !r1.hasNextFloat()) {
            r.write("$a1 ")
            file1 = endRange(r1)
            a1 = r1.nextFloat()
        }
        while (!file2 && !r2.hasNextFloat()) {
            r.write("$a2 ")
            file2 = endRange(r2)
            a2 = r2.nextFloat()
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

