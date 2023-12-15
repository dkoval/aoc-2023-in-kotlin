package day15

import println
import readInputAsString

private const val DAY_ID = "15"

fun main() {
    fun hash(s: String) = s.fold(0) { acc, c ->
        (acc + c.code) * 17 % 256
    }

    fun part1(input: String): Int {
        return input.split(",")
            .fold(0) { acc, s -> acc + hash(s) }
    }

    fun part2(input: String): Int {
        val boxes = input.split(",")
            .fold(List(256) { mutableMapOf<String, Int>() }) { acc, s ->
                if (s.last().isDigit()) {
                    // rn=1
                    val label = s.dropLast(2)
                    val x = s.last().digitToInt()
                    acc[hash(label)][label] = x
                } else {
                    // cm-
                    val label = s.dropLast(1)
                    acc[hash(label)] -= label
                }
                acc
            }

        return boxes.foldIndexed(0) { boxIndex, acc1, lenses ->
            acc1 + lenses.values.foldIndexed(0) { lensIndex, acc2, x ->
                acc2 + (boxIndex + 1) * (lensIndex + 1) * x
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 521341
    part2(input).println() // answer = 252782
}
