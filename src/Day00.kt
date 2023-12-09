private const val DAY_ID = "00"

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$DAY_ID/Day${DAY_ID}_test")
    check(part1(testInput) == 42)
    //check(part2(testInput) == 42)

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println()
    //part2(input).println()
}
