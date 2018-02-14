package com.liyuntao.aoc

import java.io.File
import java.util.regex.Pattern


class Day07 {

    data class TreeNode(val id: String, val value: Int,
//                        var total: Int/*include node and all subs*/,
                        val subIds: List<String>?)

    private val treeRegex: Pattern = Pattern.compile("([a-z]+) \\((\\d+)\\) -> (.+)")

    fun parseInput(str: String): TreeNode {
        val m = treeRegex.matcher(str)
        return if (m.find()) {
            val sub = m.group(3)
                    .split(",")
                    .map { it.trim() }
            TreeNode(m.group(1), m.group(2).toInt(), sub)
        } else {
            val arr = str.split(" ")
            val id = arr[0]
            val value = arr[1].removeSurrounding("(", ")").toInt()
            TreeNode(id, value, null)
        }
    }

    fun getRootNode(nodes: List<TreeNode>): TreeNode? {
        for (n in nodes) {
            if (n.subIds == null) continue
            if (!containsId(n.id, nodes)) {
                return n
            }
        }
        return null
    }

    private fun containsId(id: String, nodes: List<TreeNode>): Boolean {
        for (n in nodes) {
            if (n.subIds == null) continue
            if (n.subIds.contains(id)) return true
        }
        return false
    }

    fun buildTree(nodes: List<TreeNode>): Map<String, TreeNode> {
        return nodes.foldRight(mutableMapOf(), { node, map -> map[node.id] = node;map })
    }

    /**
     * count all subNodes
     *
     * if all same/no sub then currentNode is wrong
     * if not all same then the unique sub-node is wrong
     *  - (two sub different nodes case is tricky)
     */
    private fun getProblemNodeId(rootId: String, repo: Map<String, TreeNode>, context: Int = -1): Pair<String, Int>? {
        val subIds = repo[rootId]!!.subIds
        if (subIds == null) return rootId to context
        val shit = subIds.map { countNodeWeight(it, repo) }
        if (isAllSubSameWeight(shit)) return rootId to context

        if (shit.size == 2) {
//            val opt1 = getProblemNodeId(shit[0].first, repo, shit[1].second)
//            val opt2 = getProblemNodeId(shit[1].first, repo, shit[0].second)
//            return opt1 ?: opt2
            throw RuntimeException("shit! cannot be solved easily")
        } else {
            val uniquePair = findUnique(shit)!!
            val uniqueId = uniquePair.first
            var context = shit[0].second
            if (context == uniquePair.second) context = shit[1].second
            return getProblemNodeId(uniqueId, repo, context)
        }
    }

    private fun isAllSubSameWeight(lst: List<Pair<String, Int>>): Boolean {
        val map = lst.groupingBy { it.second }.eachCount()
        return map.size == 1
    }

    private fun findUnique(lst: List<Pair<String, Int>>): Pair<String, Int>? {
        val map = lst.groupingBy { it.second }.eachCount()
        val uniqueWeight: Int = map.filter { it.value == 1 }.entries.first().key
        val uniquePair = lst.filter { it.second == uniqueWeight }.first()
        return uniquePair
    }

    fun countNodeWeight(id: String, repo: Map<String, TreeNode>): Pair<String, Int> {
        val nodeObj = repo[id]!!
        var result = nodeObj.value
        if (nodeObj.subIds != null) {
            nodeObj.subIds.forEach {
                val subNode = repo[it]!!
                result += countNodeWeight(subNode.id, repo).second
            }
        }
        return id to result
    }

    fun q2(rootId: String, nodes: List<TreeNode>) {
        val map = buildTree(nodes)
        val problemNodeWithCorrectContextWeight = getProblemNodeId(rootId, map)!!
        println(problemNodeWithCorrectContextWeight)
        val problemWeight = countNodeWeight(problemNodeWithCorrectContextWeight.first, map)
        println(problemWeight)
        val correctOffset = problemNodeWithCorrectContextWeight.second - problemWeight.second
        println("q2 result ${map[problemNodeWithCorrectContextWeight.first]!!.value + correctOffset}")
    }
}

fun main(args: Array<String>) {
    val day07 = Day07()

    val nodes = File("./inputs/day07.txt")
            .readLines()
            .map { day07.parseInput(it) }

    // q1
    val rootId = day07.getRootNode(nodes)!!.id
    println("q1 result $rootId")

    // q2
    day07.q2(rootId, nodes)

}