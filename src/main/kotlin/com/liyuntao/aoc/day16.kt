package com.liyuntao.aoc

import java.io.File

object Day16 {

    sealed class Moves {
        data class Spin(val x: Int) : Moves()
        data class Exchange(val posA: Int, val posB: Int) : Moves()
        data class Partner(val c1: Char, val c2: Char) : Moves()
    }

    fun sillyParse(input: String): List<Moves> {
        return input.split(",").map {
            when {
                it.startsWith('s') -> Moves.Spin(it.substring(1).toInt())
                it.startsWith('x') -> {
                    val t = it.substring(1).split("/")
                    Moves.Exchange(t[0].toInt(), t[1].toInt())
                }
                else -> Moves.Partner(it[1], it[3])
            }
        }
    }

    fun execMoves(input: String, moves: List<Moves>): String {
        return moves.fold(input, { str, move ->
            when (move) {
                is Moves.Spin -> str.substring(str.length - move.x) + str.substring(0, str.length - move.x)
                is Moves.Exchange -> {
                    val char1 = str[move.posA]
                    val char2 = str[move.posB]
                    str.replace(char1, '1')
                            .replace(char2, '2')
                            .replace('2', char1)
                            .replace('1', char2)
                }
                is Moves.Partner -> {
                    val char1 = move.c1
                    val char2 = move.c2
                    str.replace(char1, '1')
                            .replace(char2, '2')
                            .replace('2', char1)
                            .replace('1', char2)
                }
            }
        })
    }

    /****** q2 below *****/
    fun genMapping(newStr: String): Array<Int> {
        return newStr.map { it - 'a' }.toTypedArray()
    }

    fun execMapping(input: CharArray, vector16: Array<Int>): CharArray {
        val result = CharArray(16)
        for ((index, i) in vector16.withIndex()) {
            result[index] = input[i]
        }
        return result
    }
}

fun main(args: Array<String>) {
    // q1
    val input = File("./inputs/day16.txt")
            .readLines()
            .first()

    val moves = Day16.sillyParse(input)

    // test
//    val testQ1Res = Day16.execMoves("abcde", listOf(
//            Day16.Moves.Spin(1),
//            Day16.Moves.Exchange(3, 4),
//            Day16.Moves.Partner('e', 'b')
//    ))
//    val mapping = Day16.genMapping(testQ1Res)
//    println(Day16.execMapping("abcde".toCharArray(), mapping)).toString()

    // q1
    val q1Result = Day16.execMoves("abcdefghijklmnop", moves)
    println("q1 result $q1Result")

    //q2
    // TIPS: Partner动作有副作用，不适用于mapping. 但是它与顺序无关，可以单独看待
    // 另外，相同的Partner动作两两抵消，偶数次(1billion)相当于不执行，故将之filter掉
    val midResult = Day16.execMoves("abcdefghijklmnop", moves.filter { it !is Day16.Moves.Partner })
    val mapping = Day16.genMapping(midResult)

    println(Day16.execMapping("abcdefghijklmnop".toCharArray(), mapping)).toString()

    val q2CharArr = (0 until 1000000000).fold("abcdefghijklmnop".toCharArray(), { charArr, _ ->
        Day16.execMapping(charArr, mapping)
    })
    println("q2 result ${String(q2CharArr)}")
}