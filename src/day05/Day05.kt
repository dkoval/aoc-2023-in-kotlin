package day05

import println
import readInputAsString

private const val DAY_ID = "05"

fun main() {
    data class MappingRange(val dstStart: Long, val srcStart: Long, val length: Long)
    data class Input(val seeds: List<Long>, val mappings: Map<String, List<MappingRange>>)

    fun parseInput(input: String): Input {
        //seeds: 79 14 55 13
        //
        //seed-to-soil map:
        //50 98 2
        //52 50 48
        //
        //soil-to-fertilizer map:
        //0 15 37
        //37 52 2
        //39 0 15
        //...
        val lines = input.split("\n\n")
        val seeds = lines.first().removePrefix("seeds: ").split(" ").map { it.trim().toLong() }

        val mappings = lines.drop(1).asSequence()
            .map { block ->
                //seed-to-soil map:
                //50 98 2
                //52 50 48
                val mapping = block.split("\n")
                val (category, _) = mapping.first().split(" ")

                val ranges = mapping.drop(1)
                    .map { nums ->
                        val (srcRangeStart, dstRangeStart, length) = nums.split(" ").map { it.toLong() }
                        MappingRange(srcRangeStart, dstRangeStart, length)
                    }

                category to ranges
            }
            .toMap()

        return Input(seeds, mappings)
    }

    fun part1(input: String): Long {
        val (seeds, mappings) = parseInput(input)
        return seeds.asSequence()
            .map { seed ->
                var x = seed
                for ((_, ranges) in mappings) {
                    for (range in ranges) {
                        if (x in range.srcStart until range.srcStart + range.length) {
                            val offset = x - range.srcStart
                            x = range.dstStart + offset
                            break
                        }
                    }
                }
                x
            }
            .min()
    }

    fun part2(input: String): Long {
        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 35L)
    //check(part2(testInput).println() == 46L)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 51580674
    //part2(input).println()
}
