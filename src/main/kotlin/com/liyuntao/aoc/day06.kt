package com.liyuntao.aoc

import java.io.File

class Day06 {
    private fun allocate(input: Array<Int>): Array<Int> {
        val len = input.size
        val baseIdx: Int = input.indexOf(input.max())
        val willAlloTotal = input[baseIdx]
        input[baseIdx] = 0
        val eachAlloCount = willAlloTotal / len
        val remainder = willAlloTotal % len
//        println("baseIdx $baseIdx eachAlloCount $eachAlloCount remainder $remainder")
        for (i in 0 until len) {
            input[i] += eachAlloCount
            if ((remainder > 0)
                    && (((i + len - baseIdx) % len) > 0)
                    && (((i + len - baseIdx) % len) <= remainder)) input[i] += 1
        }
        return input
    }

    fun toSnapShot(input: Array<Int>): String {
        return input.foldRight("", { a, b -> "$a,$b" })
    }

    tailrec fun getResult(input: Array<Int>, snapshotRepo: MutableMap<String, Int /*times*/>, relocTimes: Int) {
        val snapshot = toSnapShot(input)
        if (snapshotRepo.contains(snapshot)) {
            println("q1 result: $relocTimes")
            println("q2 result: ${relocTimes - (snapshotRepo.get(snapshot)?:0)}")
            return
        } else {
            snapshotRepo.put(snapshot, relocTimes)
        }
        return getResult(allocate(input), snapshotRepo, relocTimes + 1)
    }
}

fun main(args: Array<String>) {
    val day06 = Day06()

    val inputArray = File("./inputs/day06.txt")
            .readLines()
            .first()
            .split("[ \t]+".toRegex())
            .map { it.toInt() }
            .toTypedArray()

    day06.getResult(inputArray, mutableMapOf(), 0)
}