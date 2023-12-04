package day04

import println
import readInput

private const val DAY_ID = "04"

fun main() {
    data class Card(val id: Int, val xs: Set<Int>, val ys: Set<Int>) {
        val matches: Int = xs.count { it in ys }
    }

    // Format:
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17 9 48 53
    fun parseInput(input: List<String>): List<Card> {
        fun parseNumbers(nums: String): Set<Int> = nums.split(" ").asSequence()
            .filter { it.isNotEmpty() }
            .map { it.trim().toInt() }
            .toSet()

        // card ID -> Card
        return input.map { line ->
            val (s1, s2) = line.split(": ")
            val (nums1, nums2) = s2.split(" | ")

            val id = s1.removePrefix("Card ").trim().toInt()
            val xs = parseNumbers(nums1)
            val ys = parseNumbers(nums2)

            Card(id, xs, ys)
        }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).sumOf { card ->
            val matches = card.matches
            if (matches > 0) 1 shl (matches - 1) else 0
        }
    }

    fun part2(input: List<String>): Int {
        val cards = parseInput(input)
        val n = cards.size

        val counts = IntArray(n) { 1 }
        for (i in 0 until  n - 1) {
            for (j in (i + 1)..minOf(i + cards[i].matches, n - 1)) {
                counts[j] += counts[i]
            }
        }
        return counts.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 23750
    part2(input).println() // answer = 13261850
}
