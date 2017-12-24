package com.liyuntao.aoc

import java.io.File

class Day05 {
    tailrec fun q1(input: MutableList<Int>, startIndex: Int, currentSteps: Int): Int {
        val nextIndex = startIndex + input[startIndex]
        input[startIndex] += 1
        return if (nextIndex >= 0 && nextIndex < input.size) q1(input, nextIndex, currentSteps + 1) else currentSteps + 1
    }

    tailrec fun q2(input: MutableList<Int>, startIndex: Int, currentSteps: Int): Int {
        val nextIndex = startIndex + input[startIndex]
        input[startIndex] += if (input[startIndex] >= 3) -1 else 1
        return if (nextIndex >= 0 && nextIndex < input.size) q2(input, nextIndex, currentSteps + 1) else currentSteps + 1
    }
}

fun main(args: Array<String>) {
    val day05 = Day05()


    val inputs = File("./inputs/day05.txt")
            .readLines()
            .map { it.toInt() }
    // q1
    day05.q1(inputs.toMutableList(), 0, 0).let { println(it) }

    // q2
    day05.q2(inputs.toMutableList(), 0, 0).let { println(it) }
}