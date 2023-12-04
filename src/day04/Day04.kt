package day04

import println
import readInput
import java.util.ArrayDeque
import java.util.Queue

private const val DAY_ID = "04"

fun main() {
    fun part1(input: List<String>): Int {
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17 9 48 53
        return input.asSequence()
            .map { line ->
                val (_, s2) = line.split(": ")
                val (nums1, nums2) = s2.split(" | ")

                val xs = nums1.split(" ").asSequence().filter { it.isNotEmpty() }.map { it.trim().toInt() }.toSet()
                val ys = nums2.split(" ").asSequence().filter { it.isNotEmpty() }.map { it.trim().toInt() }.toSet()

                xs to ys
            }
            .map { (xs, ys) ->
                val matches = xs.count { it in ys }
                if (matches > 0) 1 shl (matches - 1) else 0
            }
            .sum()
    }

    data class Card(val id: Int, val xs: Set<Int>, val ys: Set<Int>) {
        val matches: Int by lazy { xs.count { it in ys } }
    }

    fun part2(input: List<String>): Int {
        // Card ID -> Card
        val cards = input.asSequence()
            .map { line ->
                val (s1, s2) = line.split(": ")
                val (nums1, nums2) = s2.split(" | ")

                val id = s1.removePrefix("Card ").trim().toInt()
                val xs = nums1.split(" ").asSequence().filter { it.isNotEmpty() }.map { it.trim().toInt() }.toSet()
                val ys = nums2.split(" ").asSequence().filter { it.isNotEmpty() }.map { it.trim().toInt() }.toSet()

                id to Card(id, xs, ys)
            }
            .toMap()

        // cards to process
        val q: Queue<Int> = ArrayDeque(cards.keys)
        // card ID -> count
        val counts = mutableMapOf<Int, Int>()
        while (!q.isEmpty()) {
            val id = q.poll()
            counts[id] = (counts[id] ?: 0) + 1

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
