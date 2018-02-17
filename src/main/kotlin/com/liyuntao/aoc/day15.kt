package com.liyuntao.aoc

object Day15 {

    private const val LOW16_MASK = 0b00000000_00000000_11111111_11111111

    fun q1(a: Int, b: Int, times: Int): Int {
        var i1 = a
        var i2 = b
        var count = 0
        for (i in (0 until times)) {
            val tri = genAndCompare(i1, i2)
            i1 = tri.first
            i2 = tri.second
            if (tri.third) count++
        }
        return count
    }

    private fun genAndCompare(a: Int, b: Int): Triple<Int, Int, Boolean> {
        val nextA = genNext(a, 16807)
        val nextB = genNext(b, 48271)
        return Triple(nextA, nextB, isLowest16Match(nextA, nextB))
    }

    private fun genNext(i: Int, factor: Int): Int {
        return ((i.toLong() * factor) % Integer.MAX_VALUE).toInt()
    }

    private fun isLowest16Match(i1: Int, i2: Int): Boolean {
        return (i1 xor i2) and LOW16_MASK == 0
    }

    /***** q2 related below *********/
    fun q2(a: Int, b: Int, times: Int): Int {
        var i1 = a
        var i2 = b
        var count = 0
        for (i in (0 until times)) {
            val tri = q2GenAndCompare(i1, i2)
            i1 = tri.first
            i2 = tri.second
            if (tri.third) count++
        }
        return count
    }

    private fun q2GenAndCompare(a: Int, b: Int): Triple<Int, Int, Boolean> {
        val nextA = q2GenNext(a, 16807, 4)
        val nextB = q2GenNext(b, 48271, 8)
        return Triple(nextA, nextB, isLowest16Match(nextA, nextB))
    }

    private fun q2GenNext(i: Int, factor: Int, criteria: Int): Int {
        val res = ((i.toLong() * factor) % Integer.MAX_VALUE).toInt()
        return if (res % criteria == 0) res else q2GenNext(res, factor, criteria)
    }

}

fun main(args: Array<String>) {
    Day15.q1(679, 771, 40000000).let {
        println("q1 result $it")
    }

    Day15.q2(679, 771, 5000000).let {
        println("q2 result $it")
    }
}