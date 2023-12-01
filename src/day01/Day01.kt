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

        fun indexOfDigit(line: String, last: Boolean): Int {
            val predicate = Char::isDigit
            return if (last) line.indexOfLast(predicate) else line.indexOfFirst(predicate)
        }

        fun indexOfWord(line: String, last: Boolean): IndexedValue<String> {
            // word -> occurs at index
            val occurrences = mapping.keys.asSequence()
                .map { word ->
                    var index = if (last) line.lastIndexOf(word) else line.indexOf(word)
                    if (index == -1 && !last) {
                        index = line.length
                    }
                    IndexedValue(index, word)
                }

            val selector = { element: IndexedValue<String> -> element.index }
            return if (last) occurrences.maxBy(selector) else occurrences.minBy(selector)
        }

        fun findDigit(line: String, last: Boolean = false): Int {
            val indexOfDigit = indexOfDigit(line, last)
            val (indexOfWord, word) = indexOfWord(line, last)
            val canTakeIndexOfDigit = if (last) indexOfDigit > indexOfWord else indexOfDigit < indexOfWord
            return if (indexOfDigit != -1 && canTakeIndexOfDigit) line[indexOfDigit].digitToInt() else mapping[word]!!
        }

        return input.asSequence()
            .map { line ->
                val d1 = findDigit(line)
                val d2 = findDigit(line, last = true)
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
