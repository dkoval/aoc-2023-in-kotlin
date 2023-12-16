package day16

import println
import readInput
import java.util.*

private const val DAY_ID = "16"

private enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1)
}

private data class Tile(val row: Int, val col: Int) {
    fun inbounds(numRows: Int, numCols: Int): Boolean {
        return (row in 0 until numRows) && (col in 0 until numCols)
    }

    fun next(dir: Direction): Tile {
        return Tile(row + dir.dx, col + dir.dy)
    }
}

private data class BeamState(val tile: Tile, val dir: Direction) {
    fun next(c: Char): Collection<BeamState> {
        return when (c) {
            '.' -> {
                listOf(BeamState(tile.next(dir), dir))
            }

            '/' -> {
                val nextDir = when (dir) {
                    Direction.RIGHT -> Direction.UP
                    Direction.LEFT -> Direction.DOWN
                    Direction.UP -> Direction.RIGHT
                    Direction.DOWN -> Direction.LEFT
                }
                listOf(BeamState(tile.next(nextDir), nextDir))
            }

            '\\' -> {
                val nextDir = when (dir) {
                    Direction.RIGHT -> Direction.DOWN
                    Direction.LEFT -> Direction.UP
                    Direction.UP -> Direction.LEFT
                    Direction.DOWN -> Direction.RIGHT
                }
                listOf(BeamState(tile.next(nextDir), nextDir))
            }

            '|' -> {
                when (dir) {
                    Direction.UP, Direction.DOWN -> listOf(BeamState(tile.next(dir), dir))
                    else -> listOf(
                        BeamState(tile.next(Direction.UP), Direction.UP),
                        BeamState(tile.next(Direction.DOWN), Direction.DOWN)
                    )
                }
            }

            '-' -> {
                when (dir) {
                    Direction.RIGHT, Direction.LEFT -> listOf(BeamState(tile.next(dir), dir))
                    else -> listOf(
                        BeamState(tile.next(Direction.LEFT), Direction.LEFT),
                        BeamState(tile.next(Direction.RIGHT), Direction.RIGHT)
                    )
                }
            }

            else -> error("Unrecognized marker $c")
        }
    }
}

fun main() {
    fun countEnergizedTiles(grid: List<String>, start: Tile, dir: Direction): Int {
        val m = grid.size
        val n = grid[0].length

        val q = ArrayDeque<BeamState>().apply { offer(BeamState(start, dir)) }
        val visited = mutableSetOf<BeamState>()
        val energized = mutableSetOf<Tile>()
        while (!q.isEmpty()) {
            val curr = q.poll()
            if (!curr.tile.inbounds(m, n) || curr in visited) {
                continue
            }

            visited += curr
            energized += curr.tile

            val (row, col) = curr.tile
            val c = grid[row][col]
            q += curr.next(c)
        }
        return energized.size
    }

    fun part1(grid: List<String>): Int {
        return countEnergizedTiles(grid, Tile(0, 0), Direction.RIGHT)
    }

    fun part2(grid: List<String>): Int {
        val m = grid.size
        val n = grid[0].length

        var best = 0
        for (col in 0 until n) {
            // process top and bottom rows
            if (grid[0][col] == '.') {
                best = maxOf(best, countEnergizedTiles(grid, Tile(0, col), Direction.DOWN))
            }
            if (grid[m - 1][col] == '.') {
                best = maxOf(best, countEnergizedTiles(grid, Tile(m - 1, col), Direction.UP))
            }
        }

        for (row in 0 until m) {
            // process left and right columns
            if (grid[row][0] == '.') {
                best = maxOf(best, countEnergizedTiles(grid, Tile(row, 0), Direction.RIGHT))
            }
            if (grid[row][n - 1] == '.') {
                best = maxOf(best, countEnergizedTiles(grid, Tile(row, n - 1), Direction.LEFT))
            }
        }
        return best
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 7623
    part2(input).println() // answer = 8244
}
