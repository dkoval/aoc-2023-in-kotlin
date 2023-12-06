package day06

import println
import readInput

private const val DAY_ID = "06"

fun main() {
    data class Input(val times: List<Int>, val dists: List<Int>)

    fun parseInput(input: List<String>): Input {
        fun parseLine(line: String, prefix: String): List<Int> {
            return line.removePrefix(prefix).split(" ")
                .filter { it.isNotBlank() }
                .map { it.trim().toInt() }
        }

        val times = parseLine(input[0], "Time:")
        val dists = parseLine(input[1], "Distance:")
        return Input(times, dists)
    }

    fun part1(input: List<String>): Int {
        val (times, dists) = parseInput(input)
        val n = times.size

        return (0 until n).asSequence()
            .map { i ->
                (1..times[i]).asSequence()
                    .map { v ->
                        val t = times[i] - v
                        v * t
                    }
                    .count { d -> d > dists[i] }
            }
            .reduce { x, y -> x * y }
    }

    fun part2(input: List<String>): Int {
        val (times, dists) = parseInput(input)

        fun toNum(xs: List<Int>): Long {
            return xs.joinToString(separator = "").toLong()
        }

        val time = toNum(times)
        val dist = toNum(dists)
        return (1..time).asSequence()
            .map { v ->
                val t = time - v
                v * t
            }
            .count { d -> d > dist }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 4568778
    part2(input).println() // answer = 28973936
}
