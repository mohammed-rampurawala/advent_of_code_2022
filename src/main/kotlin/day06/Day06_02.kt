package advent.day06

import advent.readInput

fun main() {
    val input = readInput("day06")[0]
    println(uniqueCharCheck(14, input))
}

fun uniqueCharCheck(size: Int, input: String): Int {
    var startIdx = 0
    var endIdx = startIdx + size
    while (endIdx < input.length) {
        val substring = input.substring(startIdx, endIdx)
        if (isDistinct(substring).not()) {
            startIdx += 1
            endIdx = startIdx + size
        } else {
            return endIdx
        }
    }
    return -1
}

private fun isDistinct(string: String): Boolean {
    return string.length == string.chars().distinct().count().toInt()
}

