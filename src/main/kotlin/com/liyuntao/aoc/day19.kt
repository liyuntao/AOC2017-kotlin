package com.liyuntao.aoc

import com.liyuntao.aoc.Day19.State.*
import java.io.File

object Day19 {
    enum class State { UP, DOWN, LEFT, RIGHT, END }

    class Matrix(val meta: List<String>) {
        private val width = meta.map { it.length }.max() ?: 0
        private val height = meta.size

        init {
            println("width:$width height: $height")
        }

        fun getChar(pos: Int): Char {
            try {
                val x = pos % width
                val y = pos / width
                return meta[y][x]
            } catch (e: Exception) {
                println("pos $pos ${pos % width}  ${pos / width}")
                throw RuntimeException("DEBUG")
            }
        }

        fun getChar(x: Int, y: Int): Char {
            return meta[y][x]
        }

        private fun afterTurn(curDirection: State, curPos: Int): Pair<State, Int> {
            var x = curPos % width
            var y = curPos / width
            val newDirection: State
            when (curDirection) {
                UP, DOWN -> {
                    if (isPosValid(x - 1, y) && getChar(x - 1, y) != ' ') {
                        newDirection = LEFT
                        x -= 1
                    } else {
                        newDirection = RIGHT
                        x += 1
                    }
                }
                LEFT, RIGHT -> {
                    if (isPosValid(x, y - 1) && getChar(x, y - 1) != ' ') {
                        newDirection = UP
                        y -= 1
                    } else {
                        newDirection = DOWN
                        y += 1
                    }
                }
                else -> throw RuntimeException("not possiable")
            }
            return newDirection to (y * width + x)
        }

        private fun isPosValid(x: Int, y: Int): Boolean {
            return !(x < 0 || y < 0 || y >= height || meta[y].length <= x)
        }

        fun step(curState: State, curPos: Int): Pair<State, Int> {
            val curChar = getChar(curPos)
            if (curChar == '+') {
                return afterTurn(curState, curPos)
            } else if(curChar == ' ') {
                return END to curPos
            } else {
                var x = curPos % width
                var y = curPos / width
                when (curState) {
                    UP -> y--
                    DOWN -> y++
                    LEFT -> x--
                    RIGHT -> x++
                    else -> {
                    }
                }
                return if (isPosValid(x, y)) {
                    curState to y * width + x
                } else {
                    END to curPos
                }
            }
        }
    }

    fun q1() {
        val meta = File("./inputs/day19.txt")
                .readLines()
        val maxLength = meta.map { it.length }.max()?:0
        val newMeta = meta.map { it.padEnd(maxLength, ' ') }
        Day19.tick(DOWN, 1, Matrix(newMeta))
    }

    private fun tick(initState: State, initPos: Int, repo: Matrix) {
        var curState = initState
        var curPos = initPos
        var result = ""
        var stepCount = 0
        while (curState != END) {
            val pair = repo.step(curState, curPos)
            stepCount++
            curState = pair.first
            curPos = pair.second
            if (repo.getChar(curPos) in ('A'..'Z')) {
                result += repo.getChar(curPos)
            }
        }
        println("q1 result $result")
        println("q2 result ${stepCount - 1}") // take the last step->END off
    }
}

fun main(args: Array<String>) {
    Day19.q1()
}