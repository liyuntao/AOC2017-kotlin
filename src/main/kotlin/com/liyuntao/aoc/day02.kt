package com.liyuntao.aoc

import java.io.File

class Day02 {
    fun parseInput(str: String): List<Int> {
        return str.split("[ \t]".toRegex()).map { it.toInt() }
    }

    fun divisionOfRow(list: List<Int>): Int {
        for (i in 0 until list.size) {
            for (j in i + 1 until list.size) {
                if (i + 1 == list.size) continue
                val a = list[i]
                val b = list[j]
                if (a / b != 0 && a % b == 0) return a / b
            }
        }
        return 0
    }

}

fun main(args: Array<String>) {
    val day02 = Day02()

    // q1
    File("./inputs/day02.txt")
            .readLines()
            .map { day02.parseInput(it) }
            .map { (it.max()?:0) - (it.min()?:0) } // seems agly
            .sum()
            .let { println(it) }

    // q2
    File("./inputs/day02.txt")
            .readLines()
            .map { day02.parseInput(it).sortedDescending() }
            .map { day02.divisionOfRow(it) }
            .sum()
            .let { println(it) }
}