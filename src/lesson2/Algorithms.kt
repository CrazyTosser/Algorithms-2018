@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File


/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    TODO()
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
fun longestCommonSubstring(first: String, second: String): String {
    if (first.isEmpty() || second.isEmpty()) return ""
    if (first == second) return first
    val matrix = arrayOfNulls<IntArray>(first.length)
    var maxLength = 0
    var maxI = 0
    for (i in 0 until matrix.size) {
        matrix[i] = IntArray(second.length)
        for (j in 0 until matrix[i]!!.size) {
            if (first[i] == second[j]) {
                if (i != 0 && j != 0) {
                    matrix[i]!![j] = matrix[i - 1]!![j - 1] + 1
                } else {
                    matrix[i]!![j] = 1
                }
                if (matrix[i]!![j] > maxLength) {
                    maxLength = matrix[i]!![j]
                    maxI = i
                }
            }
        }
    }
    return first.substring(maxI - maxLength + 1, maxI + 1)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
fun calcPrimesNumber(limit: Int): Int {
    if (limit < 2) return 0
    if (limit == 2) return 1
    val a = BooleanArray(limit)
    a.fill(true, 2)
    for (i in 2 until limit)
        if (a[i]) {
            var j = 2
            while (j * i in 1 until limit) {
                a[i * j] = false
                j++
            }
        }
    return a.count { it }
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    val deck = mutableListOf<List<Char>>()
    val move = listOf(Pair(1, 0), Pair(0, 1), Pair(0, -1), Pair(-1, 0))

    fun find(pos: Pair<Int, Int>, c: Char, string: String): Boolean {
        for (it in move) {
            val x = it.first + pos.first
            val y = it.second + pos.second
            if ((x >= 0) && (x < deck[pos.second].size) && (y >= 0) && (y < deck.size))
                if (deck[y][x] == c) {
                    if (string.isEmpty()) return true
                    val res = find(Pair(x, y), string[0], string.substring(1))
                    if (res) return res
                }
        }
        return false
    }
    File(inputName).readLines().forEach { line ->
        deck.add(line.split(" ").map { it[0] })
    }
    val tmp = words.toMutableSet()
    val res = mutableSetOf<String>()
    for (y in 0 until deck.size) {
        for (x in 0 until deck[y].size) {
            for (w in tmp) {
                if (deck[y][x] == w[0] && find(Pair(x, y), w[1], w.substring(2)))
                    res.add(w)
            }
            tmp.removeAll(res)
            if (tmp.size == 0) return res
        }
    }
    return res
}