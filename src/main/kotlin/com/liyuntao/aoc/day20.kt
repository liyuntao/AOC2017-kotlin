package com.liyuntao.aoc

import java.io.File
import java.util.regex.Pattern

object Day20 {

    private val PARTICLE_PATTERN = Pattern.compile("p=<(-?\\d+),(-?\\d+),(-?\\d+)>, v=<(-?\\d+),(-?\\d+),(-?\\d+)>, a=<(-?\\d+),(-?\\d+),(-?\\d+)>")

    data class XYZ(val x: Int, val y: Int, val z: Int) {
        val distenseSquare = x * x + y * y + z * z
        fun add(other: XYZ) = XYZ(x + other.x, y + other.y, z + other.z)
    }

    data class Particle(val p: XYZ, val v: XYZ, val a: XYZ) {
        fun distenceBetween(other: Particle): Long {
            return (p.x - other.p.x).toLong() * (p.x - other.p.x) +
                    (p.y - other.p.y).toLong() * (p.y - other.p.y) +
                    (p.z - other.p.z).toLong() * (p.z - other.p.z)
        }
    }

    private fun parseParticle(line: String): Particle {
        val matcher = PARTICLE_PATTERN.matcher(line)
        matcher.matches()
        return Particle(
                p = XYZ(matcher.group(1).toInt(), matcher.group(2).toInt(), matcher.group(3).toInt()),
                v = XYZ(matcher.group(4).toInt(), matcher.group(5).toInt(), matcher.group(6).toInt()),
                a = XYZ(matcher.group(7).toInt(), matcher.group(8).toInt(), matcher.group(9).toInt())
        )
    }

    fun q1() {
        val inputs = File("./inputs/day20.txt")
                .readLines()
        val particleList = inputs.map { Day20.parseParticle(it) }
        particleList.indexOf(particleList.minBy { it.a.distenseSquare })
                .let { println("q1 result $it") }
    }

    /************* q2 related below **************/

    private fun tick(list: List<Particle>): List<Particle> {
        return list.map { it.copy(v = it.v.add(it.a)) }
                .map { it.copy(p = it.p.add(it.v)) }
    }

    private fun removeDuplicate(list: List<Particle>): List<Particle> {
        return list.groupBy { it.p }
                .filterValues { it.size == 1 }
                .values
                .toList().flatten()
    }

    private fun countDistance(list: List<Particle>): List<Long> {
        val result = mutableListOf<Long>()
        val len = list.size
        for (i in 0 until len - 1) {
            for (j in i + 1 until len) {
                result.add(list[i].distenceBetween(list[j]))
            }
        }
        return result
    }

    private fun isAnyTwoNearer(beforeTick: List<Long>, afterTick: List<Long>): Boolean {
        for ((i, beforeD) in beforeTick.withIndex()) {
            if (beforeD >= afterTick[i])
                return true
        }
        return false
    }

    fun q2() {
        val inputs = File("./inputs/day20.txt")
                .readLines()
        var particleList = inputs.map { Day20.parseParticle(it) }
        var isAnyTwoNearer = true

        while (particleList.isNotEmpty() && isAnyTwoNearer) {
            particleList = removeDuplicate(particleList)

            val beforeTick = countDistance(particleList)
            particleList = tick(particleList)
            val afterTick = countDistance(particleList)
            isAnyTwoNearer = isAnyTwoNearer(beforeTick, afterTick)
        }

        println("q2 result ${particleList.size}")
    }

}

fun main(args: Array<String>) {
    Day20.q1()
    Day20.q2()
}
