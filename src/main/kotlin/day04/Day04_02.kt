package advent.day04

import advent.readInput

fun main() {
    val input = readInput("day04")
    var overlappingPairs = 0
    input.forEach { pair ->
        val (firstPair, secondPair) = pair.split(",").map {
            val rangeSplit = it.split("-")
            Pair(rangeSplit[0].toInt(), rangeSplit[1].toInt())
        }
        if (doesOverlap(firstPair, secondPair) || doesOverlap(secondPair, firstPair)) {
            overlappingPairs++
        }
    }
    println(overlappingPairs)
}

private fun doesOverlap(firstPair: Pair<Int, Int>, secondPair: Pair<Int, Int>): Boolean {
    return if (firstPair.first < secondPair.first && firstPair.second < secondPair.first) {
        false
    } else if (firstPair.first > secondPair.second && firstPair.second > secondPair.second) {
        false
    } else {
        true
    }
}
