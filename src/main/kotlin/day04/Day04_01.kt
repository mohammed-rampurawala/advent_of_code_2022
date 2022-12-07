package advent.day04

import advent.readInput

fun main() {
    val input = readInput("day04")
    var inclusivePairs = 0
    input.forEach { pair ->
        val (firstPair, secondPair) = pair.split(",").map {
            val rangeSplit = it.split("-")
            Pair(rangeSplit[0].toInt(), rangeSplit[1].toInt())
        }
        if (isInclusive(firstPair, secondPair) || isInclusive(secondPair, firstPair)) {
            inclusivePairs++
        }
    }

    println(inclusivePairs)
}

private fun isInclusive(firstPair: Pair<Int, Int>, secondPair: Pair<Int, Int>): Boolean {
    return if (firstPair.first >= secondPair.first && firstPair.second <= secondPair.second) {
        true
    } else secondPair.first >= firstPair.first && secondPair.first <= firstPair.first
}
