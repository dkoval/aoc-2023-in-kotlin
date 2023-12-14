package day14

import println
import readInput

private const val DAY_ID = "14"

fun main() {
    class Grid(private val grid: List<MutableList<Char>>) {
        val m = grid.size
        val n = grid[0].size

        fun rollNorth(): Grid {
            for (col in 0 until n) {
                for (row in 1 until m) {
                    var x = row
                    while (x - 1 >= 0 && grid[x][col] == 'O' && grid[x - 1][col] == '.') {
                        grid[x][col] = '.'
                        grid[x - 1][col] = 'O'
                        x--
                    }
                }
            }
            return this
        }

        fun rollSouth(): Grid {
            for (col in 0 until n) {
                for (row in m - 1 downTo 0) {
                    var x = row
                    while (x + 1 < m && grid[x][col] == 'O' && grid[x + 1][col] == '.') {
                        grid[x][col] = '.'
                        grid[x + 1][col] = 'O'
                        x++
                    }
                }
            }
            return this
        }

        fun rollWest(): Grid {
            for (row in 0 until m) {
                for (col in 1 until n) {
                    var y = col
                    while (y - 1 >= 0 && grid[row][y] == 'O' && grid[row][y - 1] == '.') {
                        grid[row][y] = '.'
                        grid[row][y - 1] = 'O'
                        y--
                    }
                }
            }
            return this
        }

        fun rollEast(): Grid {
            for (row in 0 until m) {
                for (col in m - 1 downTo 0) {
                    var y = col
                    while (y + 1 < n && grid[row][y] == 'O' && grid[row][y + 1] == '.') {
                        grid[row][y] = '.'
                        grid[row][y + 1] = 'O'
                        y++
                    }
                }
            }
            return this
        }

        fun state(): List<List<Char>> {
            return grid.map { it.toList() }
        }

        override fun toString(): String {
            return grid.joinToString("\n") { row -> row.joinToString("") }
        }
    }

    fun parseInput(lines: List<String>): Grid {
        return Grid(lines.map { it.toMutableList() })
    }

    fun totalLoad(grid: List<List<Char>>): Int {
        val m = grid.size
        val n = grid[0].size

        var total = 0
        for (row in 0 until m) {
            for (col in 0 until n) {
                if (grid[row][col] == 'O') {
                    total += m - row
                }
            }
        }
        return total
    }

    fun part1(lines: List<String>): Int {
        val grid = parseInput(lines)

        val curr = grid.rollNorth().state()
        return totalLoad(curr)
    }

    fun part2(lines: List<String>): Int {
        val grid = parseInput(lines)
        val cycles = 1000000000

        var i = 0
        var curr = grid.state()
        val states = mutableListOf(curr)
        while (i < cycles) {
            curr = grid.rollNorth().rollWest().rollSouth().rollEast().state()
            i++
            if (curr in states) {
                break
            }
            states += curr
        }

        if (i < cycles) {
            val offset = states.indexOf(curr)
            val cycleLength = i - offset
            curr = states[(cycles - offset) % cycleLength + offset]
        }

        return totalLoad(curr)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 112046
    part2(input).println() // answer = 104619
}
