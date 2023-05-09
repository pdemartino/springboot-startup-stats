package com.pdemartino.springstartup.output

import com.pdemartino.springstartup.model.StartupTree

class StatsPrinter(private val threshold: Double) : StartupTreePrinter {

    override fun print(tree: StartupTree) {
        var stringBuffer = mutableListOf<String>()

        stringBuffer.add("Number of nodes: ${tree.nodes.count()}")
        stringBuffer.add("Number of roots: ${tree.roots.count()}")
        stringBuffer.add("Number of leaves: ${tree.nodes.count { it.isLeaf() }}")
        stringBuffer.add("Global duration: ${tree.roots.sumOf { it.treeDuration.toDouble() }}")

        val offendingNodes = tree.nodes
            .filter { it.stepDuration >= threshold }
            .sortedByDescending { it.stepDuration }
        stringBuffer.add("Offending nodes: ${offendingNodes.count()}")
        offendingNodes.forEach {
            stringBuffer.add(it.toString())
        }
        println(stringBuffer.joinToString("\n"))
    }
}
