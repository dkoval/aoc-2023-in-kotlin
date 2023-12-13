package day13

import println
import readInputAsString

private const val DAY_ID = "13"

fun main() {
    data class Block(val rows: List<List<Char>>, val cols: List<List<Char>>)

    fun parseInput(input: String): Sequence<Block> {
        return input.split("\n\n").asSequence()
            .map { block ->
                val lines = block.split("\n")
                val rows = lines.map { it.toList() }
                val cols = rows[0].indices.fold(mutableListOf<List<Char>>()) { ans, j ->
                    val col = rows.indices.fold(mutableListOf<Char>()) { acc, i -> acc.also { it += rows[i][j] } }
                    ans.also { it += col }
                }
                Block(rows, cols)
            }
    }

    fun findMirrorPoint(grid: List<List<Char>>, diffs: Int): Int {
        val n = grid.size
        for (center in 1 until n) {
            var left = center - 1
            var right = center
            var count = 0
            while (left >= 0 && right < n && count <= diffs) {
                for ((x, y) in grid[left].zip(grid[right])) {
                    if (x != y) {
                        count++
                    }
                    if (count > diffs) {
                        break
                    }
                }
                left--
                right++
            }
            if (count == diffs) {
                return center
            }
        }
        return 0
    }

    fun solve(input: String, diffs: Int = 0): Int {
        return parseInput(input)
            .map { (rows, cols) -> findMirrorPoint(rows, diffs) * 100 + findMirrorPoint(cols, diffs) }
            .sum()
    }

    fun part1(input: String): Int {
        return solve(input)
    }

    fun part2(input: String): Int {
        return solve(input, diffs = 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 36448
    part2(input).println() // answer = 35799
}
