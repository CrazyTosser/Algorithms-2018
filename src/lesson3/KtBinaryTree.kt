package lesson3

import java.util.SortedSet
import kotlin.NoSuchElementException


// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(var value: T) {
        var left: Node<T>? = null
        var right: Node<T>? = null
        var uplink: Node<T>? = null
        var parent: Node<T>? = null

        fun full(): Boolean = left != null && right != null
        override fun toString(): String {
            return value.toString()
        }
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                newNode.uplink = closest
                newNode.parent = closest
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                newNode.uplink = closest.uplink
                newNode.parent = closest
                closest.right = newNode
                closest.uplink = null
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        if (!contains(element) || root == null) return false
        val del = find(element)
        remove(del)
        return if (!contains(element)) {
            size--
            true
        } else
            false
    }

    private fun remove(del: Node<T>?) {
        if (del!!.full()) {
            val n = min(del.right!!)
            del.value = n.value
            remove(n)
        } else if (del.left != null) {
            if (del.left!!.right == null)
                del.left!!.uplink = del.parent
            else
                del.left!!.uplink = null
            if (del === del.parent?.left)
                del.parent!!.left = del.left
            else
                del.parent!!.right = del.left
        } else if (del.right != null) {
            if (del === del.parent?.left) {
                del.parent!!.left = del.right
            } else {
                del.parent!!.right = del.right
            }
        } else {
            if (del === del.parent?.left) {
                del.parent!!.left = null
            } else {
                del.parent!!.right = null
                del.parent!!.uplink = del.uplink
            }
        }
    }

    private fun findMax(node: KtBinaryTree.Node<T>?): Node<T> {
        var res = node ?: return node!!
        while (res.right != null)
            res = res.right!!
        return res
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator
    /**
     * Поиск следующего элемента
     * Средняя
     */
        : MutableIterator<T> {

        private var next: Node<T>? = null
        private var last: Node<T>? = null

        init {
            this.next = root?.let { min(it) } ?: throw NoSuchElementException()
        }

        private fun findNext(): Node<T>? {
            last = next
            next = if (next?.right != null) {
                min(next?.right!!)
            } else {
                if (next?.uplink != null) {
                    next?.uplink
                } else
                    null
            }
            return last
        }

        override fun hasNext(): Boolean = next != null

        override fun next(): T {
            return (findNext() ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            if (last != null) {
                remove(last)
                size--
            }
            last = null
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    private fun min(element: Node<T>): Node<T> {
        var current: Node<T> = element
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }

//    override fun toString(): String {
//        val res = StringBuilder().append("[")
//        for (i in this)
//            res.append(i).append(", ")
//        return res.append("]").toString()
//    }
}
