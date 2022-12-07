package advent

fun main(args: Array<String>) {
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
    var maxCalories = Long.MIN_VALUE
    var elfWithMaxCalories = -1
    for (key in mapOfCalories.keys) {
        if ((mapOfCalories[key] ?: 0) > maxCalories) {
            maxCalories = mapOfCalories[key]!!
            elfWithMaxCalories = key
        }
    }
    println("$maxCalories (carried by the $elfWithMaxCalories Elf)")
}
