package advent.day12

import advent.readInput
import java.util.LinkedList
import kotlin.math.min

val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, +1))

fun main() {

    fun part1(input: List<CharArray>): Int {
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == 'S') {
                    input[i][j] = 'a'
                    return distance(Pos(i, j), input)
                }
            }
        }
        return -100
    }

    fun part2(input: List<CharArray>): Int {
        var minDistance = Int.MAX_VALUE
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == 'S' || input[i][j] == 'a') {
                    input[i][j] = 'a'
                    val distance = (distance(Pos(i, j), input))
                    if (distance != 0) minDistance = min(minDistance, (distance(Pos(i, j), input)))
                }
            }
        }
        return minDistance
    }

    val input = parse(readInput("day12"))
    println(part1(input))
    println(part2(input))
}

data class Pos(val x: Int, val y: Int, val distance: Int? = null)

fun parse(readInput: List<String>): List<CharArray> {
    val result = mutableListOf<CharArray>()
    readInput.forEach {
        result.add(it.toCharArray())
    }
    return result
}

fun isPathAvailable(
    queue: LinkedList<Pos>, xNew: Int, yNew: Int, d: Int, x: Int, y: Int, input: List<CharArray>, visited: MutableSet<Pos>
) {
    if (xNew in input.indices && yNew in input[0].indices) {
        when {
            input[xNew][yNew] == 'E' && ('z' - input[x][y]) <= 1 -> queue.offer(Pos(xNew, yNew, d + 1))
            input[xNew][yNew] != 'E' && visited.contains(Pos(xNew, yNew)).not() && input[xNew][yNew] - input[x][y] <= 1 -> {
                visited.add(Pos(xNew, yNew))
                queue.offer(Pos(xNew, yNew, d + 1))
            }
        }
    }
}

fun distance(start: Pos, input: List<CharArray>): Int {
    val visited = mutableSetOf<Pos>()
    visited.add(start)
    val queue = LinkedList<Pos>()
    queue.add(Pos(start.x, start.y, 0))
    while (queue.isNotEmpty()) {
        val (x, y, d) = queue.pop()
        if (input[x][y] == 'E') {
            return d!!
        }
        directions.forEach {
            isPathAvailable(queue, x + it.first, y + it.second, d ?: 0, x, y, input, visited)
        }
    }
    return 0
}


