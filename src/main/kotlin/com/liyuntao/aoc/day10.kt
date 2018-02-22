package com.liyuntao.aoc

import java.io.File
import java.util.*


object Day10 {

    fun q1(circularArray: Array<Int>, input: List<Int>) {
        input.fold(0 to 0, { state, len -> inner(circularArray, len, state.first, state.second) })
        println("q1 result ${circularArray[0] * circularArray[1]}")
    }

    private fun inner(cArr: Array<Int>, len: Int, cursor: Int, skip: Int): Pair<Int, Int> {
        // 1. swap (cursor, cursor + len)
        sillySwap(cArr, cursor, cursor + len - 1, len)
        // 2. cursor += len + skip
        val newCursor = (cursor + len + skip) % cArr.size
        // 3. skip += 1
        val newSkip = skip + 1
        return newCursor to newSkip
    }

    private fun sillySwap(cArr: Array<Int>, from: Int, to: Int, swapRange: Int) {
        var swapTimes = swapRange / 2
        var f = from
        var t = toNormalCursor(to, cArr.size)
        if (f == t) return
        while (swapTimes > 0) {
            val tmp = cArr[f]
            cArr[f] = cArr[t]
            cArr[t] = tmp
            f = toNormalCursor(f + 1, cArr.size)
            t = toNormalCursor(t - 1, cArr.size)
            swapTimes -= 1
        }
    }

    private fun toNormalCursor(c: Int, arrLen: Int): Int {
        return if (c >= 0) c % arrLen else arrLen + c
    }

    /********** q2 below ********/
    fun q2(input: String): String {
        val list = (0 until 256).toMutableList()
        val lengths = input.map { it.toInt() } + listOf(17, 31, 73, 47, 23)

        var hashState = HashState(0, 0)
        for (a in 0 until 64) {
            hashState = hash(list, lengths, hashState)
        }

        return list.chunked(16)
                .map { it.reduce { acc, i -> acc.xor(i) } }
                .joinToString("") { "%02x".format(it) }
    }

    data class HashState(val position: Int, val skipSize: Int)

    private fun hash(list: MutableList<Int>,
                     lengths: List<Int>,
                     hashState: HashState = HashState(0, 0)): HashState {
        val size = list.size

        var position = hashState.position
        var skipSize = hashState.skipSize

        for (length in lengths) {
            reverse(list, position, position + length)
            position = (position + length + skipSize) % size
            skipSize++
        }

        return HashState(position, skipSize)
    }

    private fun reverse(list: MutableList<Int>, fromInclusive: Int, toExclusive: Int) {
        val mid = (fromInclusive + toExclusive) / 2
        val length = (toExclusive - fromInclusive)
        for (a in (0 until length)) {
            val reversePosition = fromInclusive + a
            if (reversePosition >= mid) {
                return
            }

            val i = reversePosition % list.size
            val j = (toExclusive - a - 1) % list.size
            Collections.swap(list, i, j)
        }
    }

}

fun main(args: Array<String>) {
    val input = File("./inputs/day10.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toInt() }

    // q1
//    day10.q1(arrayOf(0, 1, 2, 3, 4), listOf(3, 4, 1, 5))
    Day10.q1(Array(256, { it }), input)

    // q2
    val inputStr = File("./inputs/day10.txt")
            .readLines()
            .first()
    Day10.q2(inputStr).let { println("q2 result $it") }
}