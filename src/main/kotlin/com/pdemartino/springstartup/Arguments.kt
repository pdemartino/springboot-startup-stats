package com.pdemartino.springstartup

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

class Arguments(
    val source: String,
    val mode: MODE,
    val offendingThreshold: Double,
    val treeRoot: String?,
    val filepath: String?
) {

    companion object {
        const val DEFAULT_OFFENDING_THRESHOLD = Double.MAX_VALUE

        enum class MODE(val description: String) {
            stats("Print statistics about the whole start-up tree"),
            tree("Print start-up tree with start-up info"),
            csv("Print all start-up information into a CSV file")
        }

        fun getFromCommandLineArguments(args: Array<String>): Arguments {
            val parser = ArgParser("springboot-startup-stats")

            val source by parser.argument(
                ArgType.String,
                description = "Startup Actuator source, it can be a JSON file or an URL"
            )

            val mode by parser.option(
                ArgType.Choice<MODE>(),
                description = "Type of analysis you want to get as output",
            ).default(MODE.stats)

            val filepath by parser.option(
                ArgType.String,
                description = "Path of the output file"
            )

            val offendingThreshold by parser.option(
                ArgType.Double,
                description = "Threshold in seconds after which a startup step is considered too slow",
            )

            val treeRoot by parser.option(
                ArgType.String,
                description = "Consider subtree identified by this root (working only in '${MODE.tree}' mode)"
            )

            parser.parse(args)

            return Arguments(
                source,
                mode,
                offendingThreshold ?: DEFAULT_OFFENDING_THRESHOLD,
                treeRoot,
                filepath
            )
        }

    }

    override fun toString(): String = listOf<String>(
        "Source: $source",
        "Mode: $mode",
        "Offending Threshold: $offendingThreshold",
        "Tree Root: ${treeRoot.orEmpty()}",
        "Filepath: $filepath"
    ).joinToString("\n")

    fun validate(): Boolean {
        if (mode.equals(MODE.csv) && filepath == null) {
            throw IllegalArgumentException("Filename is mandatory if output mode is CSV")
        }

        return true
    }
}
