package day11

import println
import readInput

private const val DAY_ID = "11"

fun main() {
    data class Input(val galaxies: List<Pair<Int, Int>>, val emptyRows: List<Int>, val emptyCols: List<Int>)

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

        val emptyRows = grid.withIndex().asSequence()
            .filter { (_, line) -> line.all { c -> c == '.' }  }
            .map { (row, _) -> row }
            .toList()

        val emptyCols = mutableListOf<Int>()
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

    fun dist(input: Input, g1: Int, g2: Int, expandRatio: Int): Long {
        val (galaxies, emptyRows, emptyCols) = input
        val (row1, col1) = galaxies[g1]
        val (row2, col2) = galaxies[g2]

        var dist = 0L
        for (row in minOf(row1, row2) until maxOf(row1, row2)) {
            dist += if (row in emptyRows) expandRatio else 1
        }

        for (col in minOf(col1, col2) until maxOf(col1, col2)) {
            dist += if (col in emptyCols) expandRatio else 1
        }
        return dist
    }

    fun solve(lines: List<String>, expansionRatio: Int = 2): Long {
        val input = parseInput(lines)
        val (galaxies, _, _) = input

        // generate all possible pairs of galaxies
        var sum = 0L
        for (g1 in 0 until  galaxies.lastIndex) {
            for (g2 in g1 + 1 until galaxies.size) {
                sum += dist(input, g1, g2, expansionRatio)
            }
        }
        return sum
    }

    fun part1(lines: List<String>): Long {
        return solve(lines)
    }

    fun part2(lines: List<String>, expansionRatio: Int): Long {
        return solve(lines, expansionRatio)
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
