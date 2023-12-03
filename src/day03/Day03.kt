package day03

import println
import readInput

private const val DAY_ID = "03"

fun main() {

    fun adjacentToSymbol(grid: List<String>, row: Int, col: Int): Boolean {
        val m = grid.size
        val n = grid[0].length

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
                if (!c.isDigit() && c != '.') {
                    return true
                }
            }
        }
        return false
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
                    if (!include && adjacentToSymbol(input, row, col)) {
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
        val m = input.size
        val n = input[0].length

        // digit's (row, col) -> part number
        val partNumbers = mutableMapOf<Pair<Int, Int>, Int>()
        for ((row, line) in input.withIndex()) {
            var x = 0
            var include = false
            var digits = mutableListOf<Pair<Int, Int>>()
            for ((col, c) in line.withIndex()) {
                if (c.isDigit()) {
                    x *= 10
                    x += c.digitToInt()
                    digits += row to col
                    if (!include && adjacentToSymbol(input, row, col)) {
                        include = true
                    }
                } else {
                    if (include) {
                        partNumbers += digits.associateWith { x }
                        include = false
                        digits = mutableListOf()
                    }
                    x = 0
                }
            }

            // corner case: handle the last number on the line
            if (include) {
                partNumbers += digits.associateWith { x }
            }
        }

        fun connectedPartNumbers(row: Int, col: Int): Set<Int> {
            val xs = mutableSetOf<Int>()
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

                    val cell = nextRow to nextCol
                    if (cell in partNumbers) {
                        xs += partNumbers[cell]!!
                    }
                }
            }
            return xs
        }

        var sumOfGearRatios = 0
        for ((row, line) in input.withIndex()) {
            for ((col, c) in line.withIndex()) {
                if (c == '*') {
                    val xs = connectedPartNumbers(row, col)
                    if (xs.size == 2) {
                        sumOfGearRatios += xs.fold(1) { acc, curr -> acc * curr }
                    }
                }
            }
        }
        return sumOfGearRatios
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 525911
    part2(input).println() // answer = 75805607
}
