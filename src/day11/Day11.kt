package day11

import println
import readInput

private const val DAY_ID = "11"

fun main() {
    data class Input(val galaxies: List<Pair<Int, Int>>, val emptyRows: Set<Int>, val emptyCols: Set<Int>)

    fun parseInput(grid: List<String>): Input {
        val m = grid.size
        val n = grid[0].length

        val galaxies = mutableListOf<Pair<Int, Int>>()
        for ((row, line) in grid.withIndex()) {
            for ((col, c) in line.withIndex()) {
                if (c == '#') {
                    galaxies += row to col
                }
            }
        }

        val emptyRows = mutableSetOf<Int>()
        for (row in 0 until m) {
            if (grid[row].all { c -> c == '.' }) {
                emptyRows += row
            }
        }

        val emptyCols = mutableSetOf<Int>()
        for (col in 0 until n) {
            var empty = true
            for (row in 0 until m) {
                if (grid[row][col] != '.') {
                    empty = false
                    break
                }
            }
            if (empty) {
                emptyCols += col
            }
        }
        return Input(galaxies, emptyRows, emptyCols)
    }

    fun dist(
        galaxy1: Pair<Int, Int>,
        galaxy2: Pair<Int, Int>,
        emptyRows: Set<Int>,
        emptyCols: Set<Int>,
        ratio: Int
    ): Long {
        val (row1, col1) = galaxy1
        val (row2, col2) = galaxy2

        fun steps(x: Int, y: Int, empty: Set<Int>): Long {
            var steps = 0L
            for (row in minOf(x, y) until maxOf(x, y)) {
                steps += if (row in empty) ratio else 1
            }
            return steps
        }

        return steps(row1, row2, emptyRows) + steps(col1, col2, emptyCols)
    }

    fun solve(lines: List<String>, ratio: Int = 2): Long {
        val (galaxies, emptyRows, emptyCols) = parseInput(lines)

        // generate all possible pairs of galaxies
        var sum = 0L
        for (i in 0 until galaxies.lastIndex) {
            for (j in i + 1 until galaxies.size) {
                sum += dist(galaxies[i], galaxies[j], emptyRows, emptyCols, ratio)
            }
        }
        return sum
    }

    fun part1(lines: List<String>): Long {
        return solve(lines)
    }

    fun part2(lines: List<String>, ratio: Int): Long {
        return solve(lines, ratio)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, 10) == 1030L)
    check(part2(testInput, 100) == 8410L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println()
    part2(input, 1000000).println()
}
