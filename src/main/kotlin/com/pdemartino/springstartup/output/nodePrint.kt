package com.pdemartino.springstartup.output

import com.pdemartino.springstartup.model.StartupNode
import kotlin.text.StringBuilder

fun StartupNode.printTree(nodeToString: (node: StartupNode) -> String) {

    fun printTreeInternal(node: StartupNode, buffer: StringBuilder, prefix: String, childPrefix: String) {
        buffer.append(prefix)
        buffer.append(nodeToString(node))
        buffer.append("\n")

        val it = node.children.iterator()
        while(it.hasNext()) {
            val child = it.next()
            val nextPrefix = childPrefix + (if (it.hasNext()) "├──" else "└──")
            val nextChildPrefix = childPrefix + (if (it.hasNext()) "│   " else "    ")
            printTreeInternal(child, buffer, nextPrefix, nextChildPrefix)
        }
    }

    var buffer = StringBuilder()
    printTreeInternal(this, buffer, "", "")
    println(buffer.toString())


}


