package com.liyuntao.aoc

import java.io.File

object Day01 {
    fun q1(input: String) {
        (input + input[0]).asSequence()  // "1122" -> "11221" -> ['1', '1', '2', '2', '1']
                .windowed(2) // [ ['1', '1'] ['1', '2'] ['2', '2']  ['2', '1'] ]
                .filter { it[0] == it[1] } // [ ['1', '1'], ['2', '2'] ]
                .map { Character.getNumericValue(it[0]) } // [1,2]
                .sum() // 3
                .let { println("q1 result $it") }
    }

    fun q2(input: String) {
        (0 until input.length).map { if (input[it] == input[(it + input.length / 2) % (input.length)]) Character.getNumericValue(input[it]) else 0 }
                .sum()
                .let { println("q2 result $it") }
    }
}

fun main(args: Array<String>) {
    val input = File("./inputs/day01.txt").readText()
    Day01.q1(input)
    Day01.q2(input)
}