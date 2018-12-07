@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.impl.GraphBuilder
import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    this.vertices.forEach { if (it.v % 2 != 0) return emptyList() }
    val st = Stack<Graph.Vertex>()
    st.push(this.vertices.first())
    val edge = edges.toMutableSet()
    val res = mutableListOf<Graph.Edge>()
    while (!st.empty()) {
        if (st.peek().v == 0) {
            if (st.size == 1)
                if (res[0].begin != st.peek()) return emptyList()
                else break
            res += GraphBuilder.EdgeImpl(1, st.pop(), st.peek())
        } else {
            val e = edge.first { it.begin == st.peek() || it.end == st.peek() }
            st.peek().decrV()
            when (st.peek()) {
                e.begin -> st.push(e.end)
                e.end -> st.push(e.begin)
            }
            st.peek().decrV()
            edge.removeIf { it.begin == e.begin && it.end == e.end }
        }
    }
    return res
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    val res = GraphBuilder()
    //    val queue = PriorityQueue<Graph.Vertex>(compareBy { v ->
//        this.edges.filter { it.begin == v || it.end == v }
//                .filter { e ->
//                    res.build().vertices.any { e.begin == it || e.end == it }
//                }.minBy { it.weight }?.weight ?: -1
//    })
    val max = this.vertices.maxBy { it.v }?.v ?: return res.build()
    val vert = this.vertices.filter { it.v == max }
    val st = Stack<Graph.Vertex>()
    res.addVertex(vert.first().name)
    vert.forEach { v ->
        for ((end, edge) in this.getConnections(v)) {
            if (res.getVertex(end) == null) {
                st.push(end)
                res.addVertex(end.name)
                res.addConnection(res.getVertex(edge.begin)!!, res.getVertex(edge.end)!!)
            }
        }
    }
    while (!st.empty()) {
        for ((end, edge) in this.getConnections(st.pop())) {
            if (res.getVertex(end) == null) {
                st.push(end)
                res.addVertex(end.name)
                res.addConnection(res.getVertex(edge.begin)!!, res.getVertex(edge.end)!!)
            }
        }
    }
    return res.build()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    TODO()
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    TODO()
}