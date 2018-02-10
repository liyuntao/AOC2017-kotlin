package com.liyuntao.aoc

import java.io.File

data class SillyExpression(val r1: String, val cmd1: String, val v1: Int,
                           val r2: String, val cmd2: String, val v2: Int)

class Day08 {

    val varRepo: MutableMap<String, Int> = mutableMapOf()
    var max: Int = 0

    private fun getByName(varName: String): Int {
        return varRepo.getOrDefault(varName, 0)
    }

    private fun addByName(varName: String, newValue: Int) {
        val originValue = varRepo.getOrDefault(varName, 0)
        varRepo.set(varName, originValue + newValue)
        max = Math.max(max, originValue + newValue)
    }

    fun sillyParse(input: String): SillyExpression {
        val astArr = input.split(" ")
        return SillyExpression(
                r1 = astArr[0], cmd1 = astArr[1], v1 = astArr[2].toInt(),
                r2 = astArr[4], cmd2 = astArr[5], v2 = astArr[6].toInt()
        )
    }

    fun execExpression(exp: SillyExpression) {
        val r2Value = getByName(exp.r2)

        val isGuardValid = when (exp.cmd2) {
            ">" -> r2Value > exp.v2
            "<" -> r2Value < exp.v2
            ">=" -> r2Value >= exp.v2
            "<=" -> r2Value <= exp.v2
            "==" -> r2Value == exp.v2
            else -> r2Value != exp.v2
        }

        if (isGuardValid) {
            when (exp.cmd1) {
                "inc" -> addByName(exp.r1, exp.v1)
                else -> addByName(exp.r1, -1 * exp.v1)
            }
        }
    }

}

fun main(args: Array<String>) {
    val day08 = Day08()

    File("./inputs/day08.txt")
            .readLines()
            .map { day08.sillyParse(it) }
            .forEach { day08.execExpression(it) }

    day08.varRepo.maxBy { it.value }
            .let {
                println("q1 result: $it")
                println("q2 result: ${day08.max}")
            }

}