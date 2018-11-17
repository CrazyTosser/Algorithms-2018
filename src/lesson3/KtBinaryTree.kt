package lesson3

import java.util.SortedSet
import kotlin.NoSuchElementException


// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>>() : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    private var start: T? = null
    private var end: T? = null

    private constructor(root: Node<T>?, start: T?, end: T?) : this() {
        this.root = root
        this.start = start
        this.end = end
    }

    override var size = 0
        private set
        get() {
            var res = 0
            for (i in this)
                if (isValid(i))
                    res++
            return res
        }


    private class Node<T>(var value: T) {
        var left: Node<T>? = null
        var right: Node<T>? = null
        var uplink: Node<T>? = null
        var parent: Node<T>? = null

        fun isList(): Boolean = left == null && right == null
        override fun toString(): String {
            return value.toString()
        }
    }

    //Сложность O(logN)
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

    private fun isValid(element: T): Boolean {
        if (start != null && element < start!!)
            return false
        if (end != null && element >= end!!)
            return false
        return true
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     * Сложность - O(logN)
     */
    override fun remove(element: T): Boolean {
        var x = root
        var y: Node<T>? = null
        while (x != null) {
            val cmp = element.compareTo(x.value)
            if (cmp == 0)
                break
            else {
                y = x
                x = if (cmp < 0)
                    x.left
                else
                    x.right
            }
        }
        if (x == null)
            return false
        remove(x, y)
        return true
    }

    private fun remove(x: Node<T>, y: Node<T>?) {
        if (x.isList() && y?.right == x)
            y.uplink = x.uplink
        if (x.right == null) {
            if (y == null) {
                root = x.left
                root?.parent = null
                root?.uplink = null
            } else {
                if (x == y.left) {
                    y.left = x.left
                    y.left?.uplink = y
                    y.left?.parent = y
                } else {
                    x.left?.uplink = x.uplink
                    y.right = x.left
                    y.right?.parent = y
                }
            }
            var m = x.left
            val up = x.left?.uplink
            while (m?.right != null) {
                m.uplink = null
                m = m.right
            }
            m?.uplink = up
        } else if (x.left == null) {
            if (y == null) {
                root = x.right
                root?.parent = null
                root?.uplink = null
            } else {
                if (x == y.left) {
                    y.left = x.right
                    y.left?.parent = null
                } else {
                    y.right = x.right
                    y.right?.parent = y
                }
            }
        } else {
//            x.value = x.right!!.value
//            remove(x.right!!, x)
//            size++
            val f = min(x.right!!)
            x.value = f.value
            remove(f, f.parent)
        }
    }

    private fun findMax(node: KtBinaryTree.Node<T>?): Node<T>? {
        if (node == null) return null
        else {
            var res = node
            while (res?.right != null)
                res = res.right!!
            return res
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0 && isValid(closest.value)
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
     * Сложность полного обхода O(n)
     * сложность в худшем случае O(n), в среднем O(1)
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

        override fun hasNext(): Boolean = next != null && (
                (end != null && next!!.value < end!!) || end == null)

        override fun next(): T {
            return if (start != null) {
                var res = last
                while (hasNext() && findNext()?.value!! < start!!)
                    res = last
                last?.value ?: throw NoSuchElementException()
            } else
                (findNext() ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            if (last != null) {
                remove(last!!, last?.parent)
            }
            last = null
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     * Сложность в худшем - O(n), где n глубина левого дерева больше toElement
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        var node = root
        while (node?.left != null && toElement > node.value)
            node = node.left
        return KtBinaryTree(root, fromElement, toElement)
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     * Сложность в худшем - O(n), где n глубина левого дерева больше toElement
     */
    override fun headSet(toElement: T): SortedSet<T> {
        var node = root
        while (toElement > node?.value!! && node.left != null)
            node = node.left
        return KtBinaryTree(root, null, toElement)
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     * O(1)
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        return KtBinaryTree(root, fromElement, null)
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

    override fun toString(): String {
        return "[${this.joinToString()}]"
    }
}
