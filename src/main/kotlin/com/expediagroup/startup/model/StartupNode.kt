package com.expediagroup.startup.model

const val TO_STRING_MAX_LEN=100
class StartupNode (
    val id: Int,
    val name: String,
    val treeDuration: Float,
    val startTime: String,
    val endTime: String,
    val beanName: String? = ""
) {
    val children: MutableList<StartupNode> = mutableListOf()
    val fullName get() = name + if (beanName.isNullOrEmpty()) "" else ":${beanName}"

    val stepDuration get() = treeDuration - children.map { it.treeDuration }.sum()

    fun addChild(node: StartupNode) {
        children.add(node)
    }

    fun isLeaf() = children.isNullOrEmpty()

    fun doRecursive(function: (node: StartupNode, level: Int) -> Unit) = doRecursive(function, 0)

    private fun doRecursive(function: (node: StartupNode, level: Int) -> Unit, level: Int) {
        function(this, level)
        this.children.forEach{it.doRecursive(function, level + 1)}
    }

    override fun toString(): String {
        val stringBuffer = mutableListOf<String>()
        val printableName =
            if (fullName.length > TO_STRING_MAX_LEN)
                "..." + fullName.subSequence(fullName.length - TO_STRING_MAX_LEN, fullName.length)
            else
                fullName

        stringBuffer.add("[${id}]${printableName}")
        stringBuffer.add("t:${stepDuration}")
        if (!isLeaf()) {
            stringBuffer.add("∑t:${treeDuration}s")
        }

        return stringBuffer.joinToString(" - ")
    }
}
