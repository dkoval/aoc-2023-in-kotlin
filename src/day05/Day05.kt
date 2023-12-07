package day05

import println
import readInputAsString

private const val DAY_ID = "05"

fun main() {
    data class Segment(val src: Long, val dst: Long, val length: Long)
    data class Mapping(val id: String, val segments: List<Segment>)
    data class Input(val seeds: List<Long>, val mappings: List<Mapping>)

    fun parseInput(input: String): Input {
        // seeds: 79 14 55 13
        //
        // seed-to-soil map:
        // 50 98 2
        // 52 50 48
        //
        // soil-to-fertilizer map:
        // 0 15 37
        // 37 52 2
        // 39 0 15
        // ...
        val lines = input.split("\n\n")
        val seeds = lines.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

        val mappings = lines.drop(1)
            .map { block ->
                // seed-to-soil map:
                // 50 98 2
                // 52 50 48
                val mapping = block.split("\n")
                val (id, _) = mapping.first().split(" ")

                val segments = mapping.drop(1)
                    .map { nums ->
                        val (dst, src, length) = nums.split(" ").map { it.toLong() }
                        Segment(src, dst, length)
                    }

                Mapping(id, segments)
            }

        return Input(seeds, mappings)
    }

    fun part1(input: String): Long {
        val (seeds, mappings) = parseInput(input)
        return seeds.asSequence()
            .map { seed ->
                var x = seed
                for (mapping in mappings) {
                    for (segment in mapping.segments) {
                        if (x in segment.src until segment.src + segment.length) {
                            val offset = x - segment.src
                            x = segment.dst + offset
                            break
                        }
                    }
                }
                x
            }
            .min()
    }

    fun part2(input: String): Long {
        val (seeds, mappings) = parseInput(input)
        val rangeOfSeeds = seeds.chunked(2).map { (start, length) -> start until start + length }

        fun isGood(x: Long, mappingIndex: Int): Boolean {
            if (mappingIndex < 0) {
                // check if x is in range of seeds
                return rangeOfSeeds.any { x in it }
            }

            // x is the destination in the current mapping
            var next = x
            for (segment in mappings[mappingIndex].segments) {
                if (x in segment.dst until segment.dst + segment.length) {
                    val offset = x - segment.dst
                    next = segment.src + offset
                    break
                }
            }
            return isGood(next, mappingIndex - 1)
        }

        val maxLocation = mappings.last().segments.asSequence()
            .map { segment -> segment.dst + segment.length - 1 }
            .max()

        // TODO: brute force, optimize
        for (location in 0..maxLocation) {
            if (isGood(location, mappings.lastIndex)) {
                return location
            }
        }
        return maxLocation
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsString("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInputAsString("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 51580674
    part2(input).println() // answer = 99751240
}
