package com.liyuntao.aoc

import java.io.File

class Day04 {
    fun parseInput(str: String): List<String> {
        return str.split("[ \t]".toRegex())
    }

    /**
     * e.g. abcde -> abcde
     *      bdace -> abcde
     */
    fun toSortedRearrangedWord(input: String): String {
        return String(input.toCharArray().sortedArray())
    }
}

fun main(args: Array<String>) {
    val day04 = Day04()

    // q1
    File("./inputs/day04.txt")
            .readLines()
            .map { day04.parseInput(it) }
            .filter { (it.size - it.toSet().size) == 0 }
            .let { println(it.size) }

    // q2
    File("./inputs/day04.txt")
            .readLines()
            .map { day04.parseInput(it) }
            .map { it.map { day04.toSortedRearrangedWord(it) } }
            .filter { (it.size - it.toSet().size) == 0 }
            .let { println(it.size) }
}