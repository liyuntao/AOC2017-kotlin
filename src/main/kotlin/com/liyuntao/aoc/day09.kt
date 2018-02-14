package com.liyuntao.aoc

import java.io.File

class Day09 {

    class SillyFSMParser {
        enum class State {
            NORMAL_PARSE,
            GARBAGE_PARSE,
            IGNORE_PARSE
        }

        var curState = State.NORMAL_PARSE
        var stackState = State.NORMAL_PARSE
        var intStack = 0
        var result = 0
        var garbageCharCount = 0

        private val transition: (State, Char) -> State = { oldState, c ->
            val nextState = when (oldState) {
                State.NORMAL_PARSE -> {
                    if ('{' == c) {
                        intStack += 1
                        State.NORMAL_PARSE
                    } else if ('}' == c) {
                        result += intStack
                        intStack -= 1
                        State.NORMAL_PARSE
                    } else if ('<' == c) {
                        stackState = State.NORMAL_PARSE
                        State.GARBAGE_PARSE
                    } else if ('!' == c) {
                        stackState = State.NORMAL_PARSE
                        State.IGNORE_PARSE
                    } else {
                        State.NORMAL_PARSE
                    }
                }
                State.GARBAGE_PARSE -> {
                    when (c) {
                        '>' -> State.NORMAL_PARSE
                        '!' -> {
                            stackState = State.GARBAGE_PARSE
                            State.IGNORE_PARSE
                        }
                        else -> {
                            garbageCharCount += 1
                            oldState
                        }
                    }
                }
                State.IGNORE_PARSE -> stackState
            }
            nextState
        }

        fun parse(input: String) {
            input.forEach {
                this.curState = transition(curState, it)
            }
            println("q1 result $result")
            println("q2 result $garbageCharCount")
        }
    }
}

fun main(args: Array<String>) {
    val input = File("./inputs/day09.txt")
            .readLines().first()

    val fsm = Day09.SillyFSMParser()
    fsm.parse(input)
}