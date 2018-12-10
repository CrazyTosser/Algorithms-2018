@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File
import java.util.*

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    TODO()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Средняя
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 * Сложность O(N), ресурсоемкость R(N) - n длинна листа
 * Алгоритм самопальный - на случайных примерах с карандашом в руках все работает
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.isEmpty())
        return emptyList()

    val queues = HashSet<Deque<Int>>()

    list.forEach { num ->
        val filtered = queues.filter { it.last < num }.onEach { it.addLast(num) }
        if (filtered.none())
            queues.add(LinkedList(listOf(num)))
    }

    val res: Deque<Int> = queues.maxBy { it.size } ?: LinkedList<Int>()

    if (res.size != list.size && res.size > 1) {
        res.removeFirst()
        val head = ArrayDeque<Int>()
        list.take(list.indexOf(res.first) + 1).forEach {
            if (res.first > it && (head.none() || head.last < it))
                head.add(it)
        }

        res.addAll(head.reversed())
    }

    return res.toList()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Сложная
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 * Сложность - O(NM), Ресурсоемкость - R(NM) где N - высота, M - ширина поля
 */
fun shortestPathOnField(inputName: String): Int {
    val input = File(inputName).readLines().map { s -> s.split(" ").map { it.toInt() } }
    val comput = input.toMutableList().map { it.toMutableList() }
    var x = input.first().size - 2
    var y = input.size - 1
    fun getMove(): List<Pair<Int, Int>> {
        if (y < 0) return emptyList()
        val res = mutableListOf<Pair<Int, Int>>()
        if (x + 1 in 0 until input.first().size && y + 1 in 0 until input.size) res.add(Pair(x + 1, y + 1))
        if (x in 0 until input.first().size && y + 1 in 0 until input.size) res.add(Pair(x, y + 1))
        if (x + 1 in 0 until input.first().size && y in 0 until input.size) res.add(Pair(x + 1, y))
        return res
    }
    while (getMove().isNotEmpty()) {
        val min = getMove().map { comput[it.second][it.first] }.min()!!
        comput[y][x--] += min
        if (x == -1) {
            y--
            x = input.first().size - 1
        }
    }
    return comput[0][0]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5