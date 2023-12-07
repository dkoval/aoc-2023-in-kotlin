package day07

import println
import readInput

private const val DAY_ID = "07"

private data class HandInfo(
    val countsWithoutJokers: Map<Char, Int>,
    val jokers: Int,
    val mostFrequentCard: Char
) {
    enum class Type(val weight: Int) {
        FiveOfKind(7),
        FourOfKind(6),
        FullHouse(5),
        ThreeOfKind(4),
        TwoPair(3),
        OnePair(2),
        HighCard(1)
    }

    companion object {
        fun of(hand: String): HandInfo {
            val countsWithoutJoker = mutableMapOf<Char, Int>()
            var jokers = 0
            var mostFrequentCard = '?'
            var maxCount = 0
            for (card in hand) {
                if (card == 'J') {
                    jokers++
                    continue
                }

                val newCount = (countsWithoutJoker[card] ?: 0) + 1
                countsWithoutJoker[card] = newCount
                if (mostFrequentCard == '?') {
                    mostFrequentCard = card
                    maxCount = 1
                } else if (newCount > maxCount) {
                    mostFrequentCard = card
                    maxCount = newCount
                }
            }
            return HandInfo(countsWithoutJoker, jokers, mostFrequentCard)
        }
    }

    fun getTypePart1(): Type {
        val xs = countsWithoutJokers.values.toMutableList()
        if (jokers > 0) {
            xs += jokers
        }
        return getType(xs)
    }

    fun getTypePart2(): Type {
        if (jokers == 5) {
            return Type.FiveOfKind
        }

        val xs = if (jokers > 0) {
            countsWithoutJokers.toMutableMap()
                .apply { this[mostFrequentCard] = this[mostFrequentCard]!! + jokers }
                .values.toList()
        } else {
            countsWithoutJokers.values.toList()
        }
        return getType(xs)
    }

    private fun getType(xs: List<Int>): Type = when {
        // 5 of a kind
        xs.size == 1 && xs[0] == 5 -> Type.FiveOfKind
        xs.size == 2 && xs.max() == 4 -> Type.FourOfKind
        xs.size == 2 && xs.max() == 3 -> Type.FullHouse
        xs.size == 3 && xs.max() == 3 -> Type.ThreeOfKind
        xs.size == 3 && xs.max() == 2 -> Type.TwoPair
        xs.size == 4 -> Type.OnePair
        else -> Type.HighCard
    }
}

fun main() {
    // list of (hand, bid) pairs
    fun parseInput(input: List<String>): List<Pair<String, Int>> {
        return input.asSequence()
            .map { line ->
                val (hand, bid) = line.split(" ")
                hand to bid.toInt()
            }
            .toList()
    }

    fun solve(input: List<String>, weights: Map<Char, Int>, type: (hand: String) -> HandInfo.Type): Int {
        return parseInput(input)
            .map { (hand, bid) -> (hand to bid) to type(hand) }
            .sortedWith(Comparator { x, y ->
                val (handToBid1, type1) = x
                val (handToBid2, type2) = y

                if (type1 != type2) {
                    return@Comparator compareValues<Int>(type1.weight, type2.weight)
                }

                val (hand1, _) = handToBid1
                val (hand2, _) = handToBid2
                for ((c1, c2) in hand1.zip(hand2)) {
                    if (c1 != c2) {
                        return@Comparator compareValues<Int>(weights[c1]!!, weights[c2]!!)
                    }
                }
                return@Comparator 0
            })
            .foldIndexed(0) { index, acc, (handToBid, _) ->
                val (_, bid) = handToBid
                acc + (index + 1) * bid
            }
    }

    fun part1(input: List<String>): Int {
        return solve(
            input,
            mapOf(
                '2' to 1,
                '3' to 2,
                '4' to 3,
                '5' to 4,
                '6' to 5,
                '7' to 6,
                '8' to 7,
                '9' to 8,
                'T' to 9,
                'J' to 10,
                'Q' to 11,
                'K' to 12,
                'A' to 13
            )
        ) { hand -> HandInfo.of(hand).getTypePart1() }
    }

    fun part2(input: List<String>): Int {
        return solve(
            input,
            mapOf(
                'J' to 1,
                '2' to 2,
                '3' to 3,
                '4' to 4,
                '5' to 5,
                '6' to 6,
                '7' to 7,
                '8' to 8,
                '9' to 9,
                'T' to 10,
                'Q' to 11,
                'K' to 12,
                'A' to 13
            )
        ) { hand -> HandInfo.of(hand).getTypePart2() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer = 246424613
    part2(input).println() // answer = 248256639
}
