package com.liyuntao.aoc

import java.util.*

object Day17 {

    fun q1(step: Int, after: Int): Int {
        val lst = LinkedList<Int>()
        lst.add(0)
        var curPos = 0
        var counter = after
        // loop: do insert 'after' times
        while (counter > 0) {
            val nextCursor = doSpin(lst.size, curPos, step)
            curPos = nextCursor + 1
            lst.add(curPos, lst.size)
            counter--
        }
        return lst[(curPos + 1) % lst.size]
    }

    // return next cursor
    private fun doSpin(curSize: Int, curPos: Int, times: Int): Int {
        return if (curSize < 2) 0 else (curPos + times) % curSize
    }

    /***************** q2 related below ***********/
    data class Package(val size: Int = 1, val curPos: Int = 0, val result: Int = -1)

    fun q2(step: Int, times: Int): Int {
        return (0 until times).fold(Package(), { t, i ->
            val nextCursor = doSpin(t.size, t.curPos, step)
            Package(t.size + 1, nextCursor + 1, if (nextCursor == 0) i + 1 else t.result)
        }).result
    }

}

fun main(args: Array<String>) {
    Day17.q1(386, 2017)
            .let { println("q1 result $it") }

    Day17.q2(386, 50000000)
            .let { println("q2 result $it") }
}