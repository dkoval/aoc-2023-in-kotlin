package day17

import println
import readInput
import java.util.*

private const val DAY_ID = "17"

private enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0) {
        override fun opposite(): Direction = DOWN
    },
    DOWN(1, 0) {
        override fun opposite(): Direction = UP
    },
    LEFT(0, -1) {
        override fun opposite(): Direction = RIGHT
    },
    RIGHT(0, 1) {
        override fun opposite(): Direction = LEFT
    },
    NONE(0, 0) {
        override fun opposite(): Direction = NONE
    };

    abstract fun opposite(): Direction
}

fun main() {
    data class Cell(val row: Int, val col: Int) {
        fun inbounds(numRows: Int, numCols: Int): Boolean {
            return (row in 0 until numRows) && (col in 0 until numCols)
        }

        fun next(dir: Direction): Cell {
            return Cell(row + dir.dx, col + dir.dy)
        }
    }

    data class State(val pos: Cell, val dir: Direction, val streak: Int)

    fun parseInput(lines: List<String>): List<List<Int>> {
        return lines.map { line -> line.map { c -> c.digitToInt() } }
    }

    fun solve(
        lines: List<String>,
        canMoveSameDirection: (streak: Int) -> Boolean,
        canChangeDirection: (streak: Int) -> Boolean = { true },
        canStop: (streak: Int) -> Boolean = { true }
    ): Int {
        val grid = parseInput(lines)
        val m = grid.size
        val n = grid[0].size

        val source = Cell(0, 0)
        val target = Cell(m - 1, n - 1)

        // Dijkstra
        val q = PriorityQueue<Pair<State, Int>>(compareBy { (_, heatLoss) -> heatLoss })
        val visited = mutableSetOf<State>()
        q.offer(State(source, Direction.NONE, 0) to 0)
        while (!q.isEmpty()) {
            val (curr, heatLoss) = q.poll()
            if (curr in visited) {
                continue
            }

            visited += curr
            if (curr.pos == target && canStop(curr.streak)) {
                return heatLoss
            }

            for (nextDir in EnumSet.of(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN)) {
                // can't go backwards
                if (nextDir == curr.dir.opposite()) {
                    continue
                }

                val nextPos = curr.pos.next(nextDir)
                if (!nextPos.inbounds(m, n)) {
                    continue
                }

                if (nextDir == curr.dir) {
                    // keep on moving in the same direction
                    if (canMoveSameDirection(curr.streak)) {
                        q.offer(State(nextPos, nextDir, curr.streak + 1) to heatLoss + grid[nextPos.row][nextPos.col])
                    }
                } else {
                    // change direction, reset streak
                    if (canChangeDirection(curr.streak)) {
                        q.offer(State(nextPos, nextDir, 1) to heatLoss + grid[nextPos.row][nextPos.col])
                    }
                }
            }
        }
        return -1
    }

    fun part1(lines: List<String>): Int {
        return solve(
            lines,
            canMoveSameDirection = { streak -> streak < 3 }
        )
    }

    fun part2(lines: List<String>): Int {
        return solve(
            lines,
            canMoveSameDirection = { streak -> streak < 10 },
            canChangeDirection = { streak -> streak == 0 || streak >= 4 },
            canStop = { streak -> streak >= 4 }
        )
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_test1")) == 102)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test1")) == 94)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test2")) == 71)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 859
    part2(input).println() // answer = 1027
}
