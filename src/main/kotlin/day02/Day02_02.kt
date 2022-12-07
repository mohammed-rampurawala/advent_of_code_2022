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
        score += when ((match[1]).toCharArray()[0]) {
            'X' -> {
                //Lost
                scoreIndex.indexOf(getLostFrom(firstOpponent)) + 1
            }
            'Y' -> {
                //Draw
                scoreIndex.indexOf(firstOpponent) + 1 + 3
            }
            'Z' -> {
                //Win
                scoreIndex.indexOf(getWonFrom(firstOpponent)) + 1 + 6
            }
            else -> 0
        }
//        val secondOpponent = secondOppCode.convertToElfCode()
//        secondOppCode
//        if (firstOpponent == secondOpponent) {
//            score += 3
//        } else if (mapOfCombinations[firstOpponent]!!.contains(secondOpponent)) {
//            score += 6
//        }
//        score += keys.indexOf(firstOpponent) + 1
    }
    println(score)
}

fun getLostFrom(firstOpponent: Char) = when (firstOpponent) {
    'A' -> 'C'
    'B' -> 'A'
    'C' -> 'B'
    else -> ' '
}

private fun getWonFrom(firstOpponent: Char) = when (firstOpponent) {
    'A' -> 'B'
    'B' -> 'C'
    'C' -> 'A'
    else -> ' '
}
