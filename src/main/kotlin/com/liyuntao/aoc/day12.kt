package com.liyuntao.aoc

import java.io.File
import java.util.*

class Day12 {

    fun sillyParse(input: List<String>): MutableMap<Int, List<Int>> {
        val result = mutableMapOf<Int, List<Int>>()
        input.forEach {
            val arr01 = it.split("<->")
            val lst = arr01[1].split(",").map { it.trim().toInt() }
            result.put(arr01[0].trim().toInt(), lst)
        }
        return result
    }

    fun findCountContailsZero(repo: Map<Int, List<Int>>, start: Int, hasAccessedMarker: BitSet): Int {
        return if (hasAccessedMarker[start]) {
            0
        } else {
            hasAccessedMarker[start] = true
            1 + repo[start]!!.map { findCountContailsZero(repo, it, hasAccessedMarker) }.sum()
        }
    }

    fun removeAllRelated(repo: MutableMap<Int, List<Int>>, start: Int, hasAccessedMarker: BitSet) {
        if (hasAccessedMarker[start]) {
            return
        } else {
            hasAccessedMarker[start] = true
            repo[start]!!.map { removeAllRelated(repo, it, hasAccessedMarker) }
            repo.remove(start)
        }
    }

    private fun clearOneGroup(repo: MutableMap<Int, List<Int>>): Boolean {
        if (repo.isEmpty()) return false
        val nextKey = repo.iterator().next().key
        removeAllRelated(repo, nextKey, BitSet(5000))
        return true
    }

    fun findTotalGroups(repo: MutableMap<Int, List<Int>>): Int {
        var result = 0
        while (clearOneGroup(repo)) {
            result += 1
        }
        return result
    }
}

fun main(args: Array<String>) {
    val day12 = Day12()

    val input = File("./inputs/day12.txt")
            .readLines()
    // q1
    day12.findCountContailsZero(day12.sillyParse(input), 0, BitSet(5000))
            .let { println("q1 result $it") }
    // q2
    day12.findTotalGroups(day12.sillyParse(input))
            .let { println("q2 result $it") }
}