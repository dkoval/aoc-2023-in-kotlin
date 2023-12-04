package day04

import println
import readInput
import java.util.ArrayDeque
import java.util.Queue

private const val DAY_ID = "04"

private data class Card(val id: Int, val xs: Set<Int>, val ys: Set<Int>) {
    val matches: Int = xs.count { it in ys }

    companion object {
        // Format:
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17 9 48 53
        fun parseInput(input: List<String>): Map<Int, Card> {
            fun parseNumbers(nums: String): Set<Int> = nums.split(" ").asSequence()
                .filter { it.isNotEmpty() }
                .map { it.trim().toInt() }
                .toSet()

            // card ID -> Card
            return input.asSequence()
                .map { line ->
                    val (s1, s2) = line.split(": ")
                    val (nums1, nums2) = s2.split(" | ")

                    val id = s1.removePrefix("Card ").trim().toInt()
                    val xs = parseNumbers(nums1)
                    val ys = parseNumbers(nums2)

                    id to Card(id, xs, ys)
                }
                .toMap()
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Card.parseInput(input).asSequence()
            .map { (_, card) ->
                val matches = card.matches
                if (matches > 0) 1 shl (matches - 1) else 0
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cards = Card.parseInput(input)
        // cards to process
        val q: Queue<Int> = ArrayDeque(cards.keys)
        // card ID -> count
        val counts = mutableMapOf<Int, Int>()
        while (!q.isEmpty()) {
            val id = q.poll()
            counts[id] = (counts[id] ?: 0) + 1
            // generate IDs of next cards to process
            val matches = cards[id]!!.matches
            for (nextId in (id + 1)..(id + matches).coerceAtMost(cards.size)) {
                q.offer(nextId)
            }
        }
        return counts.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 23750
    part2(input).println() // answer = 13261850
}
