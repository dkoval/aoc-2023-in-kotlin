package day08

import println
import readInput

private const val DAY_ID = "08"

fun main() {
    data class Input(val instructions: String, val nodes: Map<String, Pair<String, String>>) {
        fun stepsBetween(start: String, stop: (node: String) -> Boolean): Int {
            val n = instructions.length
            var i = 0
            var curr = start
            var steps = 0
            while (!stop(curr)) {
                steps++
                curr = nodes[curr]!!.let { (left, right) -> if (instructions[i++ % n] == 'L') left else right }
            }
            return steps
        }
    }

    fun parseInput(input: List<String>): Input {
        val instructions = input[0]
        val nodes = input.drop(2).asSequence()
            .map { line ->
                // AAA = (BBB, CCC)
                val (src, pair) = line.split(" = ")
                val (left, right) = pair.substring(1, pair.lastIndex).split(", ")
                src to (left to right)
            }
            .toMap()

        return Input(instructions, nodes)
    }

    fun part1(lines: List<String>): Int {
        return parseInput(lines).stepsBetween("AAA") { node -> node == "ZZZ" }
    }

    fun part2(lines: List<String>): Long {
        val input = parseInput(lines)

        fun cycleLength(start: String): Int {
            // cycle length = the number of steps required to reach the node ending with "Z"
            return input.stepsBetween(start) { node -> node.endsWith("Z") }
        }

        fun gcd(x: Long, y: Long): Long {
            return if (y == 0L) x else gcd(y, x % y)
        }

        fun lcm(x: Long, y: Long): Long {
            return x * y / gcd(x, y)
        }

        return input.nodes.keys.asSequence()
            .filter { start -> start.endsWith("A") }
            .map { start -> cycleLength(start).toLong() }
            .reduce { x, y -> lcm(x, y) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test1")) == 2)
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_part1_test2")) == 6)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_part2_test")) == 6L)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 12599
    part2(input).println() // answer = 8245452805243
}
