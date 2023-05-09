package com.pdemartino.springstartup.output

import com.pdemartino.springstartup.COLOR_RED
import com.pdemartino.springstartup.COLOR_RESET
import com.pdemartino.springstartup.model.StartupTree

class TreePrinter(
    private val threshold: Double,
    private val root: String?
) : StartupTreePrinter {

    override fun print(tree: StartupTree) {
        println()
        val roots = if (root != null) tree.getNodeByName(root.trim()) else tree.roots
        roots.forEach {
            it.printTree { node ->
                if (it.stepDuration >= threshold)
                    "$COLOR_RED$node$COLOR_RESET"
                else
                    node.toString()
            }
        }
    }
}
