package com.github.unldenis.dama.solo

class Nodo(val valore: Int, val movimento: String, val children: List<Nodo>) {
    override fun toString(): String {
        val buffer = StringBuilder(50)
        print(buffer, "", "")
        return buffer.toString()
    }

    private fun print(buffer: StringBuilder, prefix: String, childrenPrefix: String) {
        buffer.append(prefix)
        buffer.append('(')
        buffer.append(valore)
        buffer.append(')')
        buffer.append('\t')
        buffer.append(movimento)
        buffer.append('\n')
        val it = children.iterator()
        while (it.hasNext()) {
            val next = it.next()
            if (it.hasNext()) {
                next.print(buffer, "$childrenPrefix├── ", "$childrenPrefix│   ")
            } else {
                next.print(buffer, "$childrenPrefix└── ", "$childrenPrefix    ")
            }
        }
    }
}