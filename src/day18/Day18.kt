package day18

import println
import readInput
import kotlin.math.abs

private const val DAY_ID = "18"

private enum class Direction(val dr: Int, val dc: Int) {
    L(0, -1), R(0, 1), U(-1, 0), D(1, 0);

    companion object {
        val indexed = listOf(R, D, L, U)
    }
}

fun main() {
    data class Instruction(val dir: Direction, val length: Int)
    data class Point(val r: Int, val c: Int) {
        fun next(d: Direction, length: Int): Point {
            return Point(r + d.dr * length, c + d.dc * length)
        }
    }

    fun parseInput1(lines: List<String>): List<Instruction> {
        return lines.map { line ->
            val (d, length, _) = line.split(" ")
            Instruction(
                enumValueOf(d),
                length.toInt()
            )
        }
    }

    fun parseInput2(lines: List<String>): List<Instruction> {
        return lines.map { line ->
            val (_, _, hex) = line.split(" ")
            val x = hex.removePrefix("(#").removeSuffix(")")
            Instruction(
                Direction.indexed[x.last().digitToInt()],
                x.dropLast(1).toInt(16)
            )
        }
    }

    fun boundaryPoints(points: List<Point>): Long {
        val n = points.size
        var total = 0L
        for (i in 0 until n) {
            val (r1, c1) = points[i]
            val (r2, c2) = points[(i + 1) % n]
            // can use this formula since diagonal moves aren't allowed
            total += abs(r1 - r2) + abs(c1 - c2).toLong()
        }
        return total
    }

    fun area(points: List<Point>): Long {
        // https://en.wikipedia.org/wiki/Shoelace_formula
        val n = points.size
        var total = 0L
        for (i in 0 until n) {
            val (r1, c1) = points[i]
            val (r2, c2) = points[(i + 1) % n]
            total += (r1 + r2) * (c1 - c2).toLong()
        }
        return total / 2
    }

    fun solve(digPlan: List<Instruction>): Long {
        val points = mutableListOf(Point(0, 0))
        var curr = points[0]
        for ((dir, length) in digPlan) {
            val next = curr.next(dir, length)
            points += next
            curr = next
        }

        // Pick's theorem
        // https://en.wikipedia.org/wiki/Pick%27s_theorem
        // A = i + b / 2 - 1
        // i - number of interior points
        // b - number of boundary points
        // answer = i + b, i.e.
        // i = A - b / 2 + 1
        // i + b = A + b / 2 + 1
        return area(points) + boundaryPoints(points) / 2 + 1
    }

    fun part1(lines: List<String>): Long {
        val digPlan = parseInput1(lines)
        return solve(digPlan)
    }

    fun part2(lines: List<String>): Long {
        val digPlan = parseInput2(lines)
        return solve(digPlan)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 62L)
    check(part2(testInput) == 952408144115)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 52055
    part2(input).println() // answer = 67622758357096
}
