package com.liyuntao.aoc

import com.liyuntao.aoc.Day18.Instruction.*
import java.io.File
import java.util.*

object Day18Q2 {

    class GlobalVar(private val queue0: Queue<Long> = LinkedList(),
                    private val queue1: Queue<Long> = LinkedList()) {
        fun getOtherQueue(id: Int): Queue<Long> {
            return if (id == 0) queue1 else queue0
        }

        fun getSelfQueue(id: Int): Queue<Long> {
            return if (id == 0) queue0 else queue1
        }
    }

    class StackContext(private val repo: Array<Long> = Array(26, { if (it == ('p' - 'a')) id.toLong() else 0L }),
                       private val id: Int,
                       private val globalVar: GlobalVar,
                       private var curPos: Long = 0L,
                       private val instructions: List<Day18.Instruction>,
                       var counter: Int = 0) {

        var isRunning = true

        fun tick() {
            if (curPos >= instructions.size) {
                isRunning = false
                return
            }

            if (!isRunning) {
                if (globalVar.getSelfQueue(id).isNotEmpty()) {
                    isRunning = true
                }
                return
            }

            val curI = instructions[curPos.toInt()]
            when (curI) {
                is SND -> {
                    globalVar.getOtherQueue(id).offer(repo[curI.x - 'a'])
                    counter++
                }
                is SND2 -> {
                    globalVar.getOtherQueue(id).offer(curI.y)
                    counter++
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
                    if (globalVar.getSelfQueue(id).isEmpty()) {
                        isRunning = false
                        curPos--
                    } else {
                        repo[curI.x - 'a'] = globalVar.getSelfQueue(id).poll()
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
            curPos++
        }
    }


    fun q2(instructions: List<Day18.Instruction>): Int {
        val gVar = GlobalVar()
        val sc0 = StackContext(id = 0,
                globalVar = gVar,
                instructions = instructions)
        val sc1 = StackContext(id = 1,
                globalVar = gVar,
                instructions = instructions)
        // mock a eventloop
        while (sc0.isRunning || sc1.isRunning) {
            sc0.tick()
            sc1.tick()
        }
        return sc1.counter
    }
}

fun main(args: Array<String>) {
    val instructions = File("./inputs/day18.txt")
            .readLines()
            .map { Day18.parseSingleInstruction(it) }

    Day18Q2.q2(instructions)
            .let { println("q2 result $it") }
}