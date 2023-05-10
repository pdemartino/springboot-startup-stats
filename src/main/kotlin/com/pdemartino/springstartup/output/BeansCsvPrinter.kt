package com.pdemartino.springstartup.output

import com.pdemartino.springstartup.model.StartupTree

import java.io.File

class BeansCsvPrinter(
    private val filepath: String
): StartupTreePrinter {

    val headers = listOf(
        "beanName", "isLeaf", "SubTreeTime", "BeanTime"
    )
    val separator = ","

    override fun print(tree: StartupTree) {
        File(filepath).bufferedWriter().use { writer ->
            writer.write(headers.joinToString(separator))
            writer.newLine()
            tree.nodes
                .filter { it.beanName != null }
                .map { listOf(it.beanName, it.isLeaf(), "%.5f".format(it.treeDuration), "%.5f".format(it.stepDuration)) }
                .map { it.joinToString(separator) }
                .forEach{ writer.appendLine(it) }
        }
    }
}
