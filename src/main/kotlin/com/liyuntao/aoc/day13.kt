package com.liyuntao.aoc

import java.io.File

class Day13 {

    fun sillyParse(input: List<String>): Array<Int> {
        val result = Array(100, { 0 })
        input.forEach {
            val parsedArr = it.split(":").map { it.trim().toInt() }
            result[parsedArr[0]] = parsedArr[1]
        }
        return result
    }

}

fun main(args: Array<String>) {
    val day13 = Day13()

    val input = File("./inputs/day13.txt")
            .readLines()
    // q1
    day13.sillyParse(input).foldIndexed(0, { index, acc, i ->
        if (i != 0 && (index % ((i - 1) * 2)) == 0) acc + index * i
        else acc
    }).let { println("q1 result $it") }

    // q2
    val inputs = day13.sillyParse(input)
    val total = inputs.size

    var result = 0
    for (delay in (0 until 5000000)) {
        var isOK = true
        for (i in (0 until total)) {
            val value = inputs[i]
            if (value == 0) continue // 跳过无效的列

            // 求第 i 列 在 (delay + i)秒的时刻，是否被捕获
            val j = delay + i
            if (j % ((value - 1) * 2) == 0) {
                isOK = false
                break
            }
        }
        if (isOK) {
            println("enter OK!")
            result = delay
            break
        }
    }
    println("q2 result $result")
}