package advent.day03

import advent.readInput

fun main() {
    val input = readInput("day03")
    var sum = 0
    val intersections = mutableListOf<Char>()
    var idx = 0
    while (idx <= input.size - 1) {
        val firstGroup = input[idx].toCharArray()
        val secondGroup = input[idx + 1].toCharArray()
        val thirdGroup = input[idx + 2].toCharArray()
        intersections.addAll(firstGroup.intersect(secondGroup.toSet()).intersect(thirdGroup.toSet()))
        idx += 3
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
