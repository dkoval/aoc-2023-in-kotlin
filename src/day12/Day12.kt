package day12

import println
import readInput

private const val DAY_ID = "12"

fun main() {
    fun arrangements(s: String, xs: List<Int>): Long {
        val n = s.length
        val m = xs.size
        val cache = mutableMapOf<Pair<Int, Int>, Long>()

        // indices i and j represent suffixes s[i:], xs[j:]
        fun calculate(i: Int, j: Int): Long {
            // base cases
            if (i >= n) {
                return if (j == m) 1 else 0
            }

            if (j == m) {
                return if ('#' in s.substring(i)) 0 else 1
            }

            // 1 _ 1 _ 3 _ 4
            //     ^
            //     j
            //     <------> length = m - j, gaps = m - j - 1
            // <----------> length = m
            if (n - i < xs.takeLast(m - j).sum() + m - j - 1) {
                return 0
            }

            // already solved?
            val key = i to j
            if (key in cache) {
                return cache[key]!!
            }

            // option #1: if s[i] looks like '.', then skip s[i]
            var count = 0L
            if (s[i] in ".?") {
                count += calculate(i + 1, j)
            }

            // option #2: if s[i] looks like '#', then check whether the next x '#'s can be taken
            // ######.
            // ^     ^
            // #?    .?
            // s[i]  s[i + x]
            // there must be no '.' between s[i] and s[i + x - 1]
            val x = xs[j]
            if (s[i] in "#?" && (i + x == n || s[i + x] in ".?") && '.' !in s.substring(i + 1, i + x)) {
                count += calculate(i + x + 1, j + 1)
            }

            return count.also { cache[key] = it }
        }
        return calculate(0, 0)
    }

    fun solve(lines: List<String>, copies: Int = 1): Long {
        // ???.### 1,1,3
        return lines.asSequence()
            .map { line ->
                var (s, nums) = line.split(" ")
                var xs = nums.split(",").map { it.toInt() }
                if (copies > 1) {
                    s = List(copies) { s }.joinToString(separator = "?")
                    xs = List(copies) { xs }.flatten()
                }
                arrangements(s, xs)
            }
            .sum()
    }

    fun part1(lines: List<String>): Long {
        return solve(lines)
    }

    fun part2(lines: List<String>): Long {
        return solve(lines, 5)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 7361
    part2(input).println() // answer = 83317216247365
}
