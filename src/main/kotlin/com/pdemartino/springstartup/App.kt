package com.pdemartino.springstartup

import com.pdemartino.springstartup.model.StartupNode
import com.pdemartino.springstartup.model.StartupTree
import com.pdemartino.springstartup.output.printTree
import com.pdemartino.springstartup.springboot.SpringBootStartupProvider
import com.pdemartino.springstartup.springboot.toStartupTree
import com.pdemartino.springstartup.Arguments.Companion.MODE
import java.net.http.HttpConnectTimeoutException

import kotlin.system.exitProcess

const val COLOR_RED = "\u001b[31m"
const val COLOR_RESET = "\u001b[0m"
val FILENAME_REGEX = """\.{0,2}/.+\.json""".toRegex()
val HTTP_ENDPOINT_REGEX = """https?://.*""".toRegex()

fun main(args: Array<String>) {
    val arguments = Arguments.getFromCommandLineArguments(args)
    println(arguments)
    try {
        when (arguments.mode) {
            MODE.stats -> statsMode(getTree(arguments.source)!!, arguments.offendingThreshold)
            MODE.tree -> treeMode(getTree(arguments.source)!!, arguments.offendingThreshold, arguments.treeRoot)
        }
    } catch (e: HttpConnectTimeoutException) {
        System.err.println("Timeout while connecting to specified endpoint")
    }

}

fun getTree(source: String) =
    when {
        source.matches(FILENAME_REGEX) -> SpringBootStartupProvider().getFromFile(source)
        source.matches(HTTP_ENDPOINT_REGEX) -> SpringBootStartupProvider().getFromUrl(source)
        else -> {
            System.err.println("source must be a file or a URL")
            exitProcess(1)
        }
    }?.toStartupTree()

fun isOffender(node: StartupNode, threshold: Double) = node.stepDuration >= threshold

fun statsMode(tree: StartupTree, threshold: Double) {
    var stringBuffer = mutableListOf<String>()

    stringBuffer.add("Number of nodes: ${tree.nodes.count()}")
    stringBuffer.add("Number of roots: ${tree.roots.count()}")
    stringBuffer.add("Number of leaves: ${tree.nodes.count { it.isLeaf() }}")
    stringBuffer.add("Global duration: ${tree.roots.sumOf { it.treeDuration.toDouble() }}")

    val offendingNodes = tree.nodes
        .filter { isOffender(it, threshold) }
        .sortedByDescending { it.stepDuration }
    stringBuffer.add("Offending nodes: ${offendingNodes.count()}")
    offendingNodes.forEach {
        stringBuffer.add(it.toString())
    }
    println(stringBuffer.joinToString("\n"))
}

fun treeMode(tree: StartupTree, threshold: Double, root: String?) {
    println()
    val roots = if (root != null) tree.getNodeByName(root.trim()) else tree.roots
    roots.forEach {
        it.printTree { node ->
            if (isOffender(node, threshold))
                "$COLOR_RED$node$COLOR_RESET"
            else
                node.toString()
        }
    }
}

