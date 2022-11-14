package com.pdemartino.springstartup.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

import com.pdemartino.springstartup.testutils.randomStartupNode

class StartupTreeTest {

    @Test
    fun testSingleRootNoChildren() {
        // given
        val root = randomStartupNode()
        // when
        val tree = StartupTree.Builder()
            .addNode(root, null)
            .build()

        // then
        assert(tree.roots.size == 1)
        assert(tree.roots.any { it.id == root.id })
        assert(tree.roots.first().isLeaf())
    }

    @Test
    fun testSingleRootWithIdGtZero() {
        // given
        val root = StartupNode(
            id = 1,
            name = "root",
            treeDuration = 0f,
            startTime = "0",
            endTime = "0")
        // when
        val tree = StartupTree.Builder()
            .addNode(root, null)
            .build()

        // then
        assert(tree.roots.size == 1)
        assert(tree.roots.any { it.id == root.id })
        assert(tree.roots.first().isLeaf())
    }

    @Test
    fun testParentSingleChild() {
        // given
        val root = randomStartupNode()
        val child1 = randomStartupNode()
        // when
        val tree = StartupTree.Builder()
            .addNode(root, null)
            .addNode(child1, root.id)
            .build()

        // then
        assert(tree.roots.first().children.any { it.id == child1.id })
    }

    @Test
    fun testParentWithTwoChildren() {
        // given
        val root = randomStartupNode()
        val child1 = randomStartupNode()
        val child2 = randomStartupNode()

        // when
        val tree = StartupTree.Builder()
            .addNode(root, null)
            .addNode(child1, root.id)
            .addNode(child2, root.id)
            .build()

        // then
        assert(tree.roots.first().children.any { it.id == child1.id })
        assert(tree.roots.first().children.any { it.id == child2.id })
    }

    @Test
    fun testMultipleLevels() {
        // given
        val root = randomStartupNode()
        val middle = randomStartupNode()
        val leaf = randomStartupNode()

        // when
        val tree = StartupTree.Builder()
            .addNode(root, null)
            .addNode(middle, root.id)
            .addNode(leaf, middle.id)
            .build()

        // then
        // Test root
        assertEquals(1, tree.roots.size, "Tree should have exactly one root")
        val actualRoot = tree.roots[0]
        assertEquals(root.id, actualRoot.id, "Tree should have a root with id ${root.id}")
        assertFalse(actualRoot.isLeaf(), "Root node should not be a leaf")
        assertEquals(1, actualRoot.children.size, "Root should have exactly 1 child")

        // Test middle node
        var actualMiddle = actualRoot.children[0]
        assertEquals(middle.id, actualMiddle.id,  "Tree root should have a child with id ${middle.id}")
        assertEquals(1, actualMiddle.children.size, "Middle should have exactly 1 child")
        assertFalse(actualMiddle.isLeaf(), "Middle node should not be a leaf")


        var actualLeaf = actualMiddle.children[0]
        assertEquals(leaf.id, actualLeaf.id,  "Leaf node should have a child with id ${leaf.id}")
        assertTrue(actualLeaf.isLeaf(), "Leaf node should not be a leaf")
        assertEquals(0, leaf.children.size, "Leaf should not have children")
    }
}
