package day03

import println
import readInput

private const val DAY_ID = "03"

fun main() {
    fun <T> exploreAdjacent(
        grid: List<String>,
        row: Int,
        col: Int,
        init: T,
        isGood: (c: Char, nextRow: Int, nextCol: Int) -> Boolean,
        operation: (answer: T, c: Char, nextRow: Int, nextCol: Int) -> T,
        earlyTerminate: Boolean = false
    ): T {
        val m = grid.size
        val n = grid[0].length

        var answer = init
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) {
                    continue
                }

                val nextRow = row + dx
                val nextCol = col + dy

                // check boundaries
                if (nextRow < 0 || nextRow == m || nextCol < 0 || nextCol == n) {
                    continue
                }

                // check symbol
                val c = grid[nextRow][nextCol]
                if (isGood(c, nextRow, nextCol)) {
                    answer = operation(answer, c, nextRow, nextCol)
                    if (earlyTerminate) {
                        return answer
                    }
                }
            }
        }
        return answer
    }

    fun isAdjacentToSymbol(grid: List<String>, row: Int, col: Int): Boolean {
        return exploreAdjacent(
            grid, row, col,
            init = false,
            isGood = { c, _, _ -> !c.isDigit() && c != '.' },
            operation = { _, _, _, _ -> true },
            earlyTerminate = true
        )
    }

    fun part1(input: List<String>): Int {
        var sumOfPartNumbers = 0
        for ((row, line) in input.withIndex()) {
            var x = 0
            var include = false
            for ((col, c) in line.withIndex()) {
                if (c.isDigit()) {
                    x *= 10
                    x += c.digitToInt()
                    if (!include && isAdjacentToSymbol(input, row, col)) {
                        include = true
                    }
                } else {
                    if (include) {
                        sumOfPartNumbers += x
                        include = false
                    }
                    x = 0
                }
            }

            // corner case: handle the last number on the line
            if (include) {
                sumOfPartNumbers += x
            }
        }
        return sumOfPartNumbers
    }

    fun part2(input: List<String>): Int {
        // digit's (row, col) -> part number
        val partNumbers = mutableMapOf<Pair<Int, Int>, Int>()
        // gear's (row, col)
        val gears = mutableListOf<Pair<Int, Int>>()
        for ((row, line) in input.withIndex()) {
            var x = 0
            var include = false
            var digits = mutableListOf<Pair<Int, Int>>()
            for ((col, c) in line.withIndex()) {
                if (c.isDigit()) {
                    x *= 10
                    x += c.digitToInt()
                    digits += row to col
                    if (!include && isAdjacentToSymbol(input, row, col)) {
                        include = true
                    }
                } else {
                    if (include) {
                        partNumbers += digits.associateWith { x }
                        include = false
                        digits = mutableListOf()
                    }
                    x = 0
                    if (c == '*') {
                        gears += row to col
                    }
                }
            }

            // corner case: handle the last number on the line
            if (include) {
                partNumbers += digits.associateWith { x }
            }
        }

        return gears.asSequence()
            .map { (row, col) ->
                // get connected part numbers
                exploreAdjacent(
                    input, row, col,
                    init = mutableSetOf<Int>(),
                    isGood = { _, nextRow, nextCol -> nextRow to nextCol in partNumbers },
                    operation = { answer, _, nextRow, nextCol -> answer.also { it += partNumbers[nextRow to nextCol]!! } }
                )
            }
            .filter { xs -> xs.size == 2 }
            .map { xs -> xs.fold(1) { acc, curr -> acc * curr }  }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 525911
    part2(input).println() // answer = 75805607
}
