package com.expediagroup.startup.testutils

import com.expediagroup.startup.model.StartupNode
import kotlin.math.abs
import kotlin.random.Random

fun randomStartupNode(
    id: Int = Random.nextInt(0, 10000),
    name: String = Random.nextInt(1000).toString(),
    durationInSecs: Float = abs(Random.nextFloat()),
    startTime:String = "0",
    endTime:String = "0"
) = StartupNode(
    id,
    name,
    durationInSecs,
    startTime,
    endTime,
)

