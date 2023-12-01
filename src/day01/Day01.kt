package day01

import println
import readInput

private const val DAY_ID = "01"

fun main() {
    fun part1(input: List<String>): Int =
        input.asSequence()
            .map { line ->
                val d1 = line.first { it.isDigit() }.digitToInt()
                val d2 = line.last { it.isDigit() }.digitToInt()
                d1 * 10 + d2
            }
            .sum()

    fun part2(input: List<String>): Int {
        val mapping = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )

        val words = mapping.keys
        return input.asSequence()
            .map { line ->
                val indexOfFirstDigit = line.indexOfFirst { c -> c.isDigit() }
                val indexOfLastDigit = line.indexOfLast { c -> c.isDigit() }

                val (indexOfFirstWord, firstWord) = words.asSequence()
                    .map { word ->
                        val index = line.indexOf(word)
                        (if (index != -1) index else line.length) to word
                    }
                    .minBy { (index, _) -> index }

                val (indexOfLastWord, lastWord) = words.asSequence()
                    .map { word ->
                        val index = line.lastIndexOf(word)
                        index to word
                    }
                    .maxBy { (index, _) -> index }

                val d1 =
                    if (indexOfFirstDigit != -1 && indexOfFirstDigit < indexOfFirstWord) line[indexOfFirstDigit].digitToInt()
                    else mapping[firstWord]!!
                val d2 =
                    if (indexOfFirstDigit != -1 && indexOfLastDigit > indexOfLastWord) line[indexOfLastDigit].digitToInt()
                    else mapping[lastWord]!!

                d1 * 10 + d2
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_test1")) == 142)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test2")) == 281)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 54990
    part2(input).println() // answer = 54473
}
