package lesson5.impl

import lesson5.Graph
import lesson5.Graph.Edge
import lesson5.Graph.Vertex

class GraphBuilder {

    data class VertexImpl(private val nameField: String, private var V: Int) : Vertex {
        override fun incrV() = V++
        override fun decrV() = V--
        override fun getName() = nameField
        override fun getV() = V
        override fun toString() = name

        override fun equals(other: Any?) = (this === other ||
                (other != null && other is VertexImpl && this.nameField == other.nameField))

        override fun hashCode(): Int {
            return nameField.hashCode()
        }
    }

    data class EdgeImpl(private val weightField: Int,
                        private val _begin: Vertex,
                        private val _end: Vertex) : Edge {
        override fun getBegin() = _begin

        override fun getEnd() = _end

        override fun getWeight() = weightField

        override fun equals(other: Any?): Boolean = this === other ||
                (other is EdgeImpl && ((this._begin == other.begin || this._begin == other.end) &&
                        (this._end == other.end || this._end == other.begin) && this.weightField == other.weight))

        override fun hashCode(): Int {
            var result = weightField
            result = 31 * result + _begin.hashCode() xor _end.hashCode()
            return result
        }
    }

    private val vertices = mutableMapOf<String, Vertex>()

    private val connections = mutableMapOf<Vertex, Set<EdgeImpl>>()

    private fun addVertex(v: Vertex) {
        vertices[v.name] = v
    }

    fun getVertex(v: Graph.Vertex) = vertices[v.name]

    fun addVertex(name: String): Vertex {
        return VertexImpl(name, 0).apply {
            addVertex(this)
        }
    }

    fun addConnection(begin: Vertex, end: Vertex, weight: Int = 1) {
        val edge = EdgeImpl(weight, begin, end)
        connections[begin] = connections[vertices[begin.name]]?.let { it + edge } ?: setOf(edge)
        connections[end] = connections[vertices[end.name]]?.let { it + edge } ?: setOf(edge)
        vertices[begin.name]?.incrV()
        vertices[end.name]?.incrV()
    }

    fun build(): Graph = object : Graph {

        override fun get(name: String): Vertex? = this@GraphBuilder.vertices[name]

        override fun getVertices(): Set<Vertex> = this@GraphBuilder.vertices.values.toSet()

        override fun getEdges(): Set<Edge> {
            return connections.values.flatten().toSet()
        }

        override fun getConnections(v: Vertex): Map<Vertex, Edge> {
            val edges = connections[v]?.toSet() ?: emptySet()
            val result = mutableMapOf<Vertex, Edge>()
            for (edge in edges) {
                when (v) {
                    edge.begin -> result[edge.end] = edge
                    edge.end -> result[edge.begin] = edge
                }
            }
            return result
        }
    }
}