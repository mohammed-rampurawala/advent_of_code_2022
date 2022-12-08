package advent.day08

import advent.readInput
import java.lang.Integer.max

fun main() {
    fun part1(input: List<String>): Int {
        val visible = mutableListOf<Int>()
        input.forEachIndexed { rowIdx, row ->
            if (rowIdx != 0 && rowIdx != input.size - 1) {
                row.toCharArray().forEachIndexed { colIdx, c ->
                    val treeHeight = c.digitToInt()
                    if (colIdx != 0 && colIdx != row.length - 1) {
                        if (isVisibleFromTop(treeHeight, input, rowIdx - 1, 0, colIdx).first) {
                            visible.add(treeHeight)
                        } else if (isVisibleFromBottom(treeHeight, input, rowIdx + 1, input.size, colIdx).first) {
                            visible.add(treeHeight)
                        } else if (isVisibleFromLeft(treeHeight, input, colIdx - 1, 0, rowIdx).first) {
                            visible.add(treeHeight)
                        } else if (isVisibleFromRight(treeHeight, input, colIdx + 1, row.length, rowIdx).first) {
                            visible.add(treeHeight)
                        }
                    }
                }
            }
        }
        return visible.count() + (input[0].length * 2) + (input.size * 2) - 4
    }

    fun part2(input: List<String>): Int {
        var maxScenicScore = Int.MIN_VALUE
        input.forEachIndexed { rowIndex, row ->
            if (rowIndex != 0 && rowIndex != input.size - 1) {
                row.toCharArray().forEachIndexed { colIdx, c ->
                    val treeHeight = c.digitToInt()
                    if (colIdx != 0 && colIdx != row.length - 1) {
                        val scoreFromTop = isVisibleFromTop(treeHeight, input, rowIndex - 1, 0, colIdx).second
                        val scoreFromBottom =
                            isVisibleFromBottom(treeHeight, input, rowIndex + 1, input.size, colIdx).second
                        val scoreFromLeft = isVisibleFromLeft(treeHeight, input, colIdx - 1, 0, rowIndex).second
                        val scoreFromRight =
                            isVisibleFromRight(treeHeight, input, colIdx + 1, row.length, rowIndex).second

                        maxScenicScore =
                            max(scoreFromTop * scoreFromBottom * scoreFromLeft * scoreFromRight, maxScenicScore)
                    }
                }
            }
        }
        return maxScenicScore
    }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun isVisibleFromTop(
    no: Int, input: List<String>, startIdx: Int, endIndx: Int, col: Int
): Pair<Boolean, Int> {
    var count = 0
    for (i in startIdx downTo endIndx) {
        count++
        if (no <= input[i][col].digitToInt()) {
            return Pair(false, count)
        }
    }
    return Pair(true, count)
}

fun isVisibleFromLeft(
    no: Int, input: List<String>, startIdx: Int, endIndx: Int, row: Int
): Pair<Boolean, Int> {
    var count = 0
    for (i in startIdx downTo endIndx) {
        count++
        if (no <= input[row][i].digitToInt()) {
            return Pair(false, count)
        }
    }
    return Pair(true, count)
}

fun isVisibleFromBottom(
    no: Int, input: List<String>, startIdx: Int, endIndx: Int, col: Int
): Pair<Boolean, Int> {
    var count = 0
    for (i in startIdx until endIndx) {
        count++
        if (no <= input[i][col].digitToInt()) {
            return Pair(false, count)
        }
    }
    return Pair(true, count)
}

fun isVisibleFromRight(
    no: Int, input: List<String>, startIdx: Int, endIndx: Int, row: Int
): Pair<Boolean, Int> {
    var count = 0
    for (i in startIdx until endIndx) {
        count++
        if (no <= input[row][i].digitToInt()) {
            return Pair(false, count)
        }
    }
    return Pair(true, count)
}
