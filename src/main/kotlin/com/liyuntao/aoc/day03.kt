package com.liyuntao.aoc

fun main(args: Array<String>) {
    // index    0      1        2         3
    // range    1,1    2,9     10,25      26,49     50,81
    // range-formula (total-ringcount+1),(total n+1th)
    // ringSize 1      8       16         24        32
    // ringSize-formula    1*4 + 4  3*4 + 4   5*4 + 4    7*4 + 4
    // ringSize-formula    (2n-1)*4 + 4
    // total    1      9        25         49        81
    // total-formula       (2n-1)^2

    fun getNthSquareTotal(n: Int): Int {
        return (2 * n - 1) * (2 * n - 1)
    }

    fun getNthRingCount(n: Int): Int {
        return 4 * (2 * n - 1) + 4
    }

    fun getNthRange(n: Int): Pair<Int, Int> {
        return Pair(getNthSquareTotal(n + 1) - getNthRingCount(n) + 1,
                getNthSquareTotal(n + 1))
    }

    fun getSteps(input: Int, nth: Int, range: Pair<Int, Int>): Int {
        val offset = nth - 1
        var input = input - offset
        if (input < range.first) input += getNthRingCount(nth)
        val period = (range.second - range.first + 1) / 4
        val remainder = (input - range.first) % period
        return (if ((remainder % period) <= (period/2)) remainder else period - remainder) + nth
    }

    // q1
    fun q1(input: Int) {
        for (i in (2 until 1000)) {
            val pair = getNthRange(i)
            if (pair.first <= input && pair.second >= input) {
//                println("q1 mid result, n=$i, pair=$pair")
                println("q1 result, ${getSteps(input, i, pair)}")
                break
            }
        }
    }
    q1(325489)


}