package com.pdemartino.springstartup

import com.pdemartino.springstartup.springboot.SpringBootStartupProvider
import com.pdemartino.springstartup.springboot.toStartupTree
import com.pdemartino.springstartup.Arguments.Companion.MODE
import com.pdemartino.springstartup.output.BeansCsvPrinter
import com.pdemartino.springstartup.output.StatsPrinter
import com.pdemartino.springstartup.output.TreePrinter
import java.net.http.HttpConnectTimeoutException

import kotlin.system.exitProcess

const val COLOR_RED = "\u001b[31m"
const val COLOR_RESET = "\u001b[0m"
val FILENAME_REGEX = """\.{0,2}/.+\.json""".toRegex()
val HTTP_ENDPOINT_REGEX = """https?://.*""".toRegex()

fun main(args: Array<String>) {
    val arguments = Arguments.getFromCommandLineArguments(args)

    try {
        arguments.validate()
    } catch (e: IllegalArgumentException) {
        System.err.println(e.localizedMessage)
        exitProcess(1)
    }

    println(arguments)
    val printer = when(arguments.mode) {
        MODE.tree -> TreePrinter(arguments.offendingThreshold, arguments.treeRoot)
        MODE.csv -> BeansCsvPrinter(arguments.filepath!!)
        else -> StatsPrinter(arguments.offendingThreshold)
    }
    try {
        val tree = getTree(arguments.source)
        printer.print(tree!!)
    } catch (e: HttpConnectTimeoutException) {
        System.err.println("Timeout while connecting to specified endpoint")
        exitProcess(1)
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


