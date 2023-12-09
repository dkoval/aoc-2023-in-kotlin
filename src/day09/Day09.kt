package day09

import println
import readInput
import java.util.ArrayDeque

private const val DAY_ID = "09"

fun main() {
    fun solve(input: List<String>, next: (xs: List<Int>) -> Int): Int {
        return input.map { line -> line.split(" ").map { it.toInt() } }.asSequence()
            .map { xs -> next(xs) }
            .sum()
    }

    fun part1(input: List<String>): Int {
        fun nextLast(xs: List<Int>): Int {
            val lasts = mutableListOf(xs.last())
            val q = ArrayDeque(xs)
            while (q.size > 1) {
                // generate next sequence of diffs
                var allZeros = true
                repeat(q.size - 1) {
                    val x = q.pollFirst()
                    val y = q.peekFirst()
                    val diff = y - x
                    if (diff != 0) {
                        allZeros = false
                    }
                    q.offerLast(diff)
                }

                q.pollFirst()
                lasts += q.peekLast()
                if (allZeros) {
                    break
                }
            }
            return lasts.reduceRight { x, acc -> x + acc }
        }

        return solve(input) { xs -> nextLast(xs) }
    }

    fun part2(input: List<String>): Int {
        fun nextFirst(xs: List<Int>): Int {
            val firsts = mutableListOf(xs.first())
            val q = ArrayDeque(xs)
            while (q.size > 1) {
                // generate next sequence of diffs
                var allZeros = true
                repeat(q.size - 1) {
                    val x = q.pollFirst()
                    val y = q.peekFirst()
                    val diff = y - x
                    if (diff != 0) {
                        allZeros = false
                    }
                    q.offerLast(diff)
                }

                q.pollFirst()
                firsts += q.peekFirst()
                if (allZeros) {
                    break
                }
            }
            return firsts.reduceRight { x, acc -> x - acc }
        }

        return solve(input) { xs -> nextFirst(xs) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 2075724761
    part2(input).println() // answer = 1072
}
