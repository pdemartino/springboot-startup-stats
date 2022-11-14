package com.pdemartino.springstartup.springboot

import kotlinx.serialization.Serializable

@Serializable
data class StartupStepTag(val key: String, val value: String)

@Serializable
data class StartupStep(
    val id: Int,
    val name: String,
    val parentId: Int?,
    val tags: List<StartupStepTag>
)

@Serializable
data class StartupEvent(
    val startTime: String,
    val endTime: String,
    val duration: String,
    val startupStep: StartupStep
)

@Serializable
data class StartupTimeline(
    val startTime: String,
    val events: List<StartupEvent>
)

@Serializable
data class StartupActuatorInformation(
    val timeline: StartupTimeline,
    val springBootVersion: String
)
