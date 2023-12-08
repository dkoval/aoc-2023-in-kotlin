package day08

import println
import readInput

private const val DAY_ID = "08"

fun main() {
    data class Input(val instructions: String, val nodes: Map<String, Pair<String, String>>)

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

    fun part1(input: List<String>): Int {
        val (instructions, nodes) = parseInput(input)
        val n = instructions.length

        var i = 0
        var curr = "AAA"
        var steps = 0
        while (curr != "ZZZ") {
            steps++
            curr = nodes[curr]!!.let { (left, right) -> if (instructions[i++ % n] == 'L') left else right }
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val (instructions, nodes) = parseInput(input)
        val n = instructions.length

        fun getCycleLength(src: String): Int {
            var i = 0
            var curr = src
            fun next(): String {
                return nodes[curr]!!.let { (left, right) -> if (instructions[i++ % n] == 'L') left else right }
            }

            // find the 1st node ending with "Z"
            while (!curr.endsWith("Z")) {
                curr = next()
            }

            // compute the length of a cycle
            val start = curr
            var length = 0
            do {
                length++
                curr = next()
            } while (curr != start)
            return length
        }

        fun gcd(x: Long, y: Long): Long {
            return if (y == 0L) x else gcd(y, x % y)
        }

        fun lcm(x: Long, y: Long): Long {
            return x * y / gcd(x, y)
        }

        return nodes.keys.asSequence()
            .filter { src -> src.endsWith("A") }
            .map { src -> getCycleLength(src).toLong() }
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
