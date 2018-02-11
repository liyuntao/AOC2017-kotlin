package com.liyuntao.aoc

import java.io.File

data class SixDimensionalCoordinates(var nw: Int, var n: Int, var ne: Int,
                                     var sw: Int, var s: Int, var se: Int) {
    fun doStep(step: String): SixDimensionalCoordinates {
        return when (step) {
            "nw" -> this.copy(nw = nw + 1)
            "n" -> this.copy(n = n + 1)
            "ne" -> this.copy(ne = ne + 1)
            "sw" -> this.copy(sw = sw + 1)
            "s" -> this.copy(s = s + 1)
            else -> this.copy(se = se + 1)
        }
    }

    fun doSimple(): SixDimensionalCoordinates {
        return SixDimensionalCoordinates(
                (nw - se) - (ne - sw), (n - s) + (ne - sw), 0,
                0, 0, 0)
    }

    fun getSteps(): Int {
        return if (nw * n >= 0) {
            Math.abs(nw + n)
        } else {
            Math.max(Math.abs(nw), Math.abs(n))
        }
    }
}

class Day11 {
    fun sillyParseQ1(input: String): SixDimensionalCoordinates {
        val numOccurencesMap = input.split(",")
                .groupingBy { it }
                .eachCount()

        return SixDimensionalCoordinates(
                nw = numOccurencesMap.getOrDefault("nw", 0),
                n = numOccurencesMap.getOrDefault("n", 0),
                ne = numOccurencesMap.getOrDefault("ne", 0),
                sw = numOccurencesMap.getOrDefault("sw", 0),
                s = numOccurencesMap.getOrDefault("s", 0),
                se = numOccurencesMap.getOrDefault("se", 0)
        )
    }

    fun sillyParseQ2(input: String) {
        var max = 0
        var coorObj = SixDimensionalCoordinates(0, 0, 0, 0, 0, 0)
        input.split(",")
                .forEach {
                    coorObj = coorObj.doStep(it)
                    val curStep = coorObj.doSimple().getSteps()
                    max = Math.max(max, curStep)
                }
        println("q2 result $max")

    }
}

fun main(args: Array<String>) {
    val day11 = Day11()

    // q1
    val input = File("./inputs/day11.txt")
            .readLines()
            .first()

    day11.sillyParseQ1(input)
            .doSimple()
            .getSteps()
            .let { println("q1 result $it") }
    // q2
    day11.sillyParseQ2(input)
}