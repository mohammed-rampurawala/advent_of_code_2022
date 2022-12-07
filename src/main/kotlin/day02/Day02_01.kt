package advent.day02

import advent.readInput

fun main() {
    val readInput = readInput("day02")
    //A-> Rock, B-> Paper, C->Scissors
    //To get the score
    val scoreIndex = listOf('A', 'B', 'C')
    var score = 0
    readInput.forEach {
        val match = it.split(" ")
        val firstOpponent = match[0].toCharArray()[0]
        val secondOpponent = (match[1]).toCharArray()[0].convertToElfCode()
        if (firstOpponent == secondOpponent) {
            score += 3
        } else if (getWonFrom(firstOpponent) == secondOpponent) {
            score += 6
        }
        score += scoreIndex.indexOf(secondOpponent) + 1
    }
    println(score)
}

private fun getWonFrom(firstOpponent: Char) = when (firstOpponent) {
    'A' -> 'B'
    'B' -> 'C'
    'C' -> 'A'
    else -> ' '
}

private fun Char.convertToElfCode(): Char {
    return when (this) {
        'X' -> 'A'
        'Y' -> 'B'
        'Z' -> 'C'
        else -> ' '
    }
}
