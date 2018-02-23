package com.liyuntao.aoc

import java.lang.Integer.parseInt
import java.lang.Integer.toBinaryString
import java.util.*

object Day14 {

    fun q1(input: String): Int {
        val knotHashStr = (0..127).fold("", { r, i -> r + Day10.q2("$input-$i") })
        return knotHashStr.map { toBinaryString(parseInt(it.toString(), 16)) }
                .fold(0, { r, str -> str.count { it == '1' } + r })
    }

    /***** q2 related below *********/

    class Matrix128(val data: BooleanArray) {

        enum class Direction { UP, DOWN, LEFT, RIGHT }

        private fun isPosValid(p: Int): Boolean {
            return p in 0 until 128 * 128
        }

        private fun isPosValid(x: Int, y: Int): Boolean {
            return x in 0..127 && y in 0..127
        }

        fun countRegions(): Int {
            var count = 0
            var curPos = findNext(0)
            while (-1 != curPos) {
                markOne(curPos, Stack())
                curPos = findNext(curPos)
                count++
            }
            return count
        }

        fun posExists(curPos: Int, action: Direction): Boolean {
            var x = curPos % 128
            var y = curPos / 128
            when (action) {
                Direction.UP -> y++
                Direction.DOWN -> y--
                Direction.LEFT -> x--
                Direction.RIGHT -> x++
            }
            return if (isPosValid(x, y)) data[y * 128 + x] else false
        }

        fun getPos(curPos: Int, action: Direction): Int {
            var x = curPos % 128
            var y = curPos / 128
            when (action) {
                Direction.UP -> y++
                Direction.DOWN -> y--
                Direction.LEFT -> x--
                Direction.RIGHT -> x++
            }
            return if (isPosValid(x, y)) y * 128 + x else -1
        }

        private fun markOne(curPos: Int, stack: Stack<Int>) {
            if (!data[curPos]) return

            stack.push(curPos)
            data[curPos] = false
            while (stack.isNotEmpty()) {
                val pos: Int = stack.pop()!!

                if (posExists(pos, Direction.UP)) {
                    val surroundPos = getPos(pos, Direction.UP)
                    data[surroundPos] = false
                    stack.push(surroundPos)
                }

                if (posExists(pos, Direction.DOWN)) {
                    val surroundPos = getPos(pos, Direction.DOWN)
                    data[surroundPos] = false
                    stack.push(surroundPos)
                }

                if (posExists(pos, Direction.LEFT)) {
                    val surroundPos = getPos(pos, Direction.LEFT)
                    data[surroundPos] = false
                    stack.push(surroundPos)
                }

                if (posExists(pos, Direction.RIGHT)) {
                    val surroundPos = getPos(pos, Direction.RIGHT)
                    data[surroundPos] = false
                    stack.push(surroundPos)
                }
            }
        }

        private fun findNext(curPos: Int): Int {
            var pos = curPos
            while (isPosValid(pos)) {
                if (data[pos]) return pos
                pos++
            }
            return -1
        }

    }

    private fun generateMatrix(input: String): Matrix128 {
        val knotHashStr = (0..127).fold("", { r, i -> r + Day10.q2("$input-$i") })
        val data = knotHashStr.map { toBinaryString(parseInt(it.toString(), 16)) }
                .map { it.padStart(4, '0') }
                .reduce { a, b -> a + b }
                .map { it == '1' }
                .toBooleanArray()

        return Matrix128(data)
    }

    fun q2(input: String): Int {
        return generateMatrix(input).countRegions()
    }

}

fun main(args: Array<String>) {
    Day14.q1("ugkiagan").let { println("q1 result $it") }

    Day14.q2("ugkiagan").let { println("q2 result $it") }
}