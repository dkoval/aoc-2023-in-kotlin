package day02

import println
import readInput

private const val DAY_ID = "02"

fun main() {
    // Format: game ID -> sets of cubes.
    // Each individual set of cubes is a map of (color -> count) entries.
    fun parseInput(input: List<String>): Sequence<Pair<Int, List<Map<String, Int>>>> {
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        return input.asSequence()
            .map { line ->
                val (s1, s2) = line.split(":")
                val (_, id) = s1.split(" ")

                val setsOfCubes = s2.split(";")
                    .map { setOfCubes ->
                        setOfCubes.trim().split(",")
                            .map { cubes ->
                                val (count, color) = cubes.trim().split(" ")
                                count.toInt() to color
                            }
                            .associateBy(
                                keySelector = { (_, color) -> color },
                                valueTransform = { (count, _) -> count }
                            )
                    }

                id.toInt() to setsOfCubes
            }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .filter { (_, setsOfCubes) ->
                setsOfCubes.all { setOfCubes ->
                    setOfCubes.getOrDefault("red", 0) <= 12
                            && setOfCubes.getOrDefault("green", 0) <= 13
                            && setOfCubes.getOrDefault("blue", 0) <= 14
                }
            }
            .sumOf { (id, _) -> id }
    }

    fun part2(input: List<String>): Int {
        val colors = listOf("red", "green", "blue")
        return parseInput(input)
            .map { (_, setsOfCubes) ->
                setsOfCubes.fold(
                    colors.associateWithTo(mutableMapOf()) { 0 }
                )
                { acc, curr ->
                    for (color in colors) {
                        acc[color] = maxOf(
                            acc.getOrDefault(color, 0),
                            curr.getOrDefault(color, 0)
                        )
                    }
                    acc
                }
            }
            .map { cubes ->
                // power of a set of cubes
                cubes.values.fold(1) { acc, curr -> acc * curr }
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 2913
    part2(input).println() // answer = 55593
}
