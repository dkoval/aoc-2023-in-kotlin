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

    fun next(xs: List<Int>, last: Boolean): Int {
        val nums = mutableListOf<Int>().apply {
            this += if (last) xs.last() else xs.first()
        }

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
            nums += if (last) q.peekLast() else q.peekFirst()
            if (allZeros) {
                break
            }
        }
        return nums.reduceRight { x, acc -> if (last) x + acc else x - acc }
    }

    fun part1(input: List<String>): Int {
        return solve(input) { xs -> next(xs, last = true) }
    }

    fun part2(input: List<String>): Int {
        return solve(input) { xs -> next(xs, last = false) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 2075724761
    part2(input).println() // answer = 1072
}
