package com.expediagroup.startup.model

import com.expediagroup.startup.testutils.randomStartupNode
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

const val DURATION_DIFF_TOLERANCE = 1e-4

class StartupNodeTest {

    @Test
    fun testTreeDurationEqualToStepDurationOnLeaves() {
        // given
        val leaf = randomStartupNode()
        // when-then
        assertEquals(leaf.treeDuration, leaf.stepDuration, "Tree Duration should be equal to step duration on leaves")
    }

    @Test
    fun testStepDurationIsCorrect2Levels() {
        // given
        val leaves = listOf(randomStartupNode(), randomStartupNode())
        val expectedStepDuration = abs(Random.nextFloat())
        val root = randomStartupNode(durationInSecs = leaves.map { it.treeDuration }.sum() + expectedStepDuration)
        leaves.forEach { root.addChild(it) }


        // when then
        assertTrue (abs(expectedStepDuration - root.stepDuration) < DURATION_DIFF_TOLERANCE, "Step duration is wrong")
    }

}
