package com.liyuntao.aoc

import com.liyuntao.aoc.Day18.Instruction.*
import java.io.File
import java.lang.Long.parseLong

object Day18 {

    sealed class Instruction {
        data class SND(val x: Char) : Instruction()
        data class SND2(val y: Long) : Instruction()
        data class SET(val x: Char, val y: Long) : Instruction()
        data class SET2(val x: Char, val x2: Char) : Instruction()
        data class ADD(val x: Char, val y: Long) : Instruction()
        data class ADD2(val x: Char, val x2: Char) : Instruction()
        data class MUL(val x: Char, val y: Long) : Instruction()
        data class MUL2(val x: Char, val x2: Char) : Instruction()
        data class MOD(val x: Char, val y: Long) : Instruction()
        data class MOD2(val x: Char, val x2: Char) : Instruction()
        data class RCV(val x: Char) : Instruction()
        data class JGZ(val x: Char, val y: Long) : Instruction()
        data class JGZ2(val x: Char, val x2: Char) : Instruction()
        data class JGZ3(val y: Long, val y2: Long) : Instruction()
    }

    fun parseSingleInstruction(str: String): Instruction {
        val arr = str.split(" ")
        return when (arr[0]) {
            "snd" -> if (isRegisterChar(arr[1])) SND(arr[1][0]) else SND2(parseLong(arr[1]))
            "set" -> if (isRegisterChar(arr[2])) SET2(arr[1][0], arr[2][0]) else SET(arr[1][0], parseLong(arr[2]))
            "add" -> if (isRegisterChar(arr[2])) ADD2(arr[1][0], arr[2][0]) else ADD(arr[1][0], parseLong(arr[2]))
            "mul" -> if (isRegisterChar(arr[2])) MUL2(arr[1][0], arr[2][0]) else MUL(arr[1][0], parseLong(arr[2]))
            "mod" -> if (isRegisterChar(arr[2])) MOD2(arr[1][0], arr[2][0]) else MOD(arr[1][0], parseLong(arr[2]))
            "jgz" -> if (isRegisterChar(arr[2])) JGZ2(arr[1][0], arr[2][0]) else {
                if (isRegisterChar(arr[1]))
                    JGZ(arr[1][0], parseLong(arr[2]))
                else
                    JGZ3(parseLong(arr[1]), parseLong(arr[2]))
            }
            else -> RCV(arr[1][0])
        }
    }

    // specially for such case
    private fun isRegisterChar(str: String): Boolean {
        return str[0] in 'a'..'z'
    }

    fun q1(instructions: List<Instruction>) {
        val repo = Array(26, { 0L })
        var curPos = 0L
        var stack = -1L
        loop@ while (curPos < instructions.size) {
            val curI = instructions[curPos.toInt()]
            when (curI) {
                is SND -> {
                    stack = repo[curI.x - 'a']
                }
                is SND2 -> {
                    stack = curI.y
                }
                is SET -> {
                    repo[curI.x - 'a'] = curI.y
                }
                is SET2 -> {
                    repo[curI.x - 'a'] = repo[curI.x2 - 'a']
                }
                is ADD -> {
                    repo[curI.x - 'a'] += curI.y
                }
                is ADD2 -> {
                    repo[curI.x - 'a'] += repo[curI.x2 - 'a']
                }
                is MUL -> {
                    repo[curI.x - 'a'] *= curI.y
                }
                is MUL2 -> {
                    repo[curI.x - 'a'] *= repo[curI.x2 - 'a']
                }
                is MOD -> {
                    repo[curI.x - 'a'] %= curI.y
                }
                is MOD2 -> {
                    repo[curI.x - 'a'] %= repo[curI.x2 - 'a']
                }
                is RCV -> {
                    if (repo[curI.x - 'a'] != 0L) {
                        repo[curI.x - 'a'] = stack
                        println("q1 result $stack")
                        break@loop
                    }
                }
                is JGZ -> {
                    if (repo[curI.x - 'a'] > 0) {
                        curPos += curI.y - 1
                    }
                }
                is JGZ2 -> {
                    if (repo[curI.x - 'a'] > 0) {
                        curPos += repo[curI.x2 - 'a'] - 1
                    }
                }
                is JGZ3 -> {
                    if (curI.y > 0) {
                        curPos += curI.y2 - 1
                    }
                }
            }
            curPos += 1
        }
    }
}

fun main(args: Array<String>) {
    val instructions = File("./inputs/day18.txt")
            .readLines()
            .map { Day18.parseSingleInstruction(it) }

    Day18.q1(instructions)
}