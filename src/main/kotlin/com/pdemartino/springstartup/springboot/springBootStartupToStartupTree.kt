package com.pdemartino.springstartup.springboot

import com.pdemartino.springstartup.model.StartupNode
import com.pdemartino.springstartup.model.StartupTree

val timeRegex = """PT(?:(?<minutes>\d+)M)*(?<seconds>\d+(?:\.\d+))S""".toRegex()

fun StartupActuatorInformation.toStartupTree(): StartupTree {
    val treeBuilder = StartupTree.Builder()
    timeline.events.sortedBy { it.startupStep.id }
        .forEach() {
            treeBuilder.addNode(
                StartupNode(
                    id = it.startupStep.id,
                    name = it.startupStep.name,
                    treeDuration = springBootTimeToSeconds(it.duration),
                    startTime = it.startTime,
                    endTime = it.endTime,
                    beanName = it.startupStep.tags.firstOrNull{ step -> step.key == "beanName" }?.value
                ), it.startupStep.parentId)
        }

    return treeBuilder.build()
}

fun springBootTimeToSeconds(springBootTime: String): Float {
    var timeInSeconds: Float = 0f

    val match = timeRegex.matchEntire(springBootTime)

    timeInSeconds += match?.groups?.get("seconds")?.value?.toFloat() ?: 0f
    timeInSeconds += 60 * (match?.groups?.get("minutes")?.value?.toFloat() ?: 0f)

    return timeInSeconds
}
