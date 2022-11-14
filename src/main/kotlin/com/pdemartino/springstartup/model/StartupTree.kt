package com.pdemartino.springstartup.model

class StartupTree(
    val nodesMap: Map<Int, StartupNode>,
    val rootsId: Set<Int>
) {
    val nodes get() = nodesMap.values.toList()
    val roots get() = nodesMap.filter { rootsId.contains(it.key) }.values.toList()

    fun getNodeByName(nodeName: String): List<StartupNode> = nodes.filter { it.fullName == nodeName }

    class Builder {
        var nodes: MutableMap<Int, StartupNode> = mutableMapOf()
        var children: MutableMap<Int, MutableSet<Int>> = mutableMapOf()
        var roots: MutableSet<Int> = mutableSetOf()

        fun addNode(node: StartupNode, parentId: Int?): Builder {
            nodes[node.id] = node
            children[node.id] = children[node.id] ?: mutableSetOf()

            if (parentId == null) {
                roots.add(node.id)
            } else {
                (children[parentId] ?: mutableSetOf()).add(node.id)
            }
            return this
        }

        fun build(): StartupTree {
            nodes.values.forEach{ parent ->
                children[parent.id]
                    ?.map {childId -> nodes[childId] }
                    ?.forEach { if (it != null) parent.addChild(it) }
            }
            return StartupTree(nodes, roots)
        }

    }
}

