package com.liyuntao.aoc

import java.io.File
import java.util.*

class Day25 {
    enum class State {
        PARSE_INIT_STATE,
        PARSE_STEPS,
        PARSE_STATE_NAME,
        PARSE_IF_COND,
        PARSE_DETAIL_1,
        PARSE_DETAIL_2,
        PARSE_DETAIL_3
    }

    data class Command(var write: Int = 0, var moveTo: String = "", var continueWith: String = "")
    data class BluePrint(var initialState: String = "", var steps: Int = 0,
                         val cond: MutableMap<String/*state*/, Array<Command>> = mutableMapOf())

    class SillyFSMParser {
        private var currentState = State.PARSE_INIT_STATE
        private val result = BluePrint()

        data class FsmData(var currentStage: String = "",
                           var currentCond: Int = -1,
                           var currentWriteValue: Int = -1,
                           var currentDirection: String = "",
                           var currentContinueStage: String = "")

        private val fsmData = FsmData()

        private val transition: (State, String) -> State = { oldState, line ->
            if (line.isBlank()) State.PARSE_STATE_NAME else when (oldState) {
                State.PARSE_INIT_STATE -> {
                    result.initialState = line.split(" ").last().removeSuffix(".")
                    State.PARSE_STEPS
                }
                State.PARSE_STEPS -> {
                    result.steps = line.split(" ").dropLast(1).last().toInt()
                    State.PARSE_STATE_NAME
                }
                State.PARSE_STATE_NAME -> {
                    fsmData.currentStage = line.split(" ").last().removeSuffix(":")
                    result.cond.put(fsmData.currentStage, Array(2, { Command() }))
                    State.PARSE_IF_COND
                }
                State.PARSE_IF_COND -> {
                    fsmData.currentCond = line.split(" ").last().removeSuffix(":").toInt()
                    State.PARSE_DETAIL_1
                }
                State.PARSE_DETAIL_1 -> {
                    fsmData.currentWriteValue = line.split(" ").last().removeSuffix(".").toInt()
                    State.PARSE_DETAIL_2
                }
                State.PARSE_DETAIL_2 -> {
                    fsmData.currentDirection = line.split(" ").last().removeSuffix(".")
                    State.PARSE_DETAIL_3
                }
                State.PARSE_DETAIL_3 -> {
                    fsmData.currentContinueStage = line.split(" ").last().removeSuffix(".")
                    result.cond.get(fsmData.currentStage)!![fsmData.currentCond] = Command(
                            fsmData.currentWriteValue,
                            fsmData.currentDirection,
                            fsmData.currentContinueStage)
                    State.PARSE_IF_COND
                }
            }
        }

        fun parse(input: List<String>): BluePrint {
            input.forEach {
                this.currentState = transition(currentState, it)
            }
            println(result)
            return result
        }
    }

    fun getChecksum(bs: BitSet): Int {
        return bs.cardinality()
    }

    tailrec fun exeCMD(bp: BluePrint, bs: BitSet, currentState: String, cursor: Int, totalTimes: Int) {
        if (totalTimes == 0) {
            println("q1 result ${getChecksum(bs)}")
            return
        }
        val cmdArr = bp.cond[currentState]
        val currentCond = if (bs[cursor]) 1 else 0
        val willExecCMD = cmdArr!![currentCond]

        bs[cursor] = willExecCMD.write == 1
        val nextCursor = cursor + (if (willExecCMD.moveTo == "left") 1 else -1)

        return exeCMD(bp, bs, willExecCMD.continueWith, nextCursor, totalTimes - 1)
    }

    fun q1() {
        val input = File("./inputs/day25.txt")
                .readLines()
        val fsm = Day25.SillyFSMParser()
        val bp = fsm.parse(input)
        exeCMD(bp, BitSet(400000), bp.initialState, 200000, bp.steps)
    }
}

fun main(args: Array<String>) {
    val day25 = Day25()

    // q1
    day25.q1()

    // q2

}