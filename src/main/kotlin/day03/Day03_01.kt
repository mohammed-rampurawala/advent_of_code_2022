package advent.day03

import advent.readInput

fun main() {
    val input = readInput("day03")
    var sum = 0
    var intersections = mutableListOf<Char>()
    input.forEach {
        val half = if (it.length % 2 == 0) it.length / 2 else it.length / 2 + 1
        val firstHalf = it.substring(0, half).toCharArray()
        val secondHalf = it.substring(half).toCharArray()
        intersections.addAll(firstHalf.intersect(secondHalf.toSet()))
    }

    intersections.forEach { misplacedItem ->
        sum += if (misplacedItem.isLowerCase()) {
            misplacedItem.code.minus(96)
        } else {
            misplacedItem.code.minus(64) + 26
        }
    }
    println(sum)
}
