package advent.day01

import advent.readInput

fun main() {
    val input = readInput("day01")
    var counter = 1
    val mapOfCalories = mutableMapOf<Int, Long>()
    input.forEach {
        if (it.isBlank()) {
            counter++
        } else {
            mapOfCalories[counter] = (mapOfCalories[counter] ?: 0).plus(it.toInt())
        }
    }
    println(mapOfCalories.values.sortedDescending().subList(0, 3).sum())
}
