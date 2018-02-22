package com.liyuntao.aoc

object Day03Q2 {
    class Grid(private val len: Int) {
        enum class Direction { UP, DOWN, LEFT, RIGHT }
        enum class Action { STEP_AHEAD, TURN_LEFT }

        private val n = (len / 2)
        private val data = Array(len, { Array(len, { 0 }) })
        private var curDirection = Direction.DOWN // trick
        private var curX = n
        private var curY = n


        fun setValue(v: Int) {
            data[curY][curX] = v
        }

        fun getValue(): Int {
            return data[curY][curX]
        }

        fun setValue(x: Int, y: Int, value: Int) {
            if (!isPosValid(x) || !isPosValid(y))
                throw RuntimeException("invalid index $x $y")
            data[y][x] = value
        }

        fun getValue(x: Int, y: Int): Int {
            if (!isPosValid(x) || !isPosValid(y))
                throw RuntimeException("invalid index $x $y")
            return data[y][x]
        }

        fun doStep() {
            if (isLeftHasValue()) {
                executeAction(Action.STEP_AHEAD)
            } else {
                executeAction(Action.TURN_LEFT)
                executeAction(Action.STEP_AHEAD)
            }
        }

        fun sumOfAround(): Int {
            return data[curY - 1][curX + 1] +
                    data[curY - 1][curX - 1] +
                    data[curY + 1][curX + 1] +
                    data[curY + 1][curX - 1] +
                    data[curY + 1][curX] +
                    data[curY - 1][curX] +
                    data[curY][curX + 1] +
                    data[curY][curX - 1]
        }

        private fun isPosValid(idx: Int): Boolean {
            return idx in 0 until len
        }

        private fun executeAction(action: Action) {
            when (action) {
                Action.STEP_AHEAD -> {
                    val newPos = when (curDirection) {
                        Direction.UP -> curX to curY + 1
                        Direction.DOWN -> curX to curY - 1
                        Direction.LEFT -> curX - 1 to curY
                        Direction.RIGHT -> curX + 1 to curY
                    }
                    curX = newPos.first
                    curY = newPos.second
                }
                Action.TURN_LEFT -> {
                    curDirection = when (curDirection) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.DOWN
                        Direction.RIGHT -> Direction.UP
                    }
                }
            }
        }

        private fun isLeftHasValue(): Boolean {
            val leftPos = when (curDirection) {
                Direction.UP -> curX - 1 to curY
                Direction.DOWN -> curX + 1 to curY
                Direction.LEFT -> curX to curY - 1
                Direction.RIGHT -> curX to curY + 1
            }
            return getValue(leftPos.first, leftPos.second) != 0
        }
    }

    fun q2(input: Int): Int {
        val grid = Grid(5000)
        grid.setValue(1)
        while (true) {
            grid.doStep()
            val nextValue = grid.sumOfAround()
            if (nextValue > input) {
                return nextValue
            }
            grid.setValue(nextValue)
        }
    }
}

fun main(args: Array<String>) {
    Day03Q2.q2(325489)
            .let { println("q2 result $it") }
}