package day09

import advent.readInput
import kotlin.math.abs

fun main() {
    //L, U, R, D (Row, Column)
    val directionSteps = listOf(Pair(0, -1), Pair(-1, 0), Pair(0, 1), Pair(1, 0))

    fun getDirectionPos(direction: String): Int {
        return when (direction) {
            "L" -> 0
            "U" -> 1
            "R" -> 2
            "D" -> 3
            else -> -1
        }
    }

    fun findTailPosition(hPos: Pair<Int, Int>, tPos: Pair<Int, Int>): Pair<Int, Int> {
        return when {
            abs(hPos.first - tPos.first) <= 1 && abs(hPos.second - tPos.second) <= 1 -> tPos
            abs(hPos.first - tPos.first) >= 2 && abs(hPos.second - tPos.second) >= 2 -> {
                val tAdjustedRowPos = if (tPos.first < hPos.first) {
                    hPos.first - 1
                } else {
                    hPos.first + 1
                }

                val tAdjustedColPos = if (tPos.second < hPos.second) {
                    hPos.second - 1
                } else {
                    hPos.second + 1
                }
                return Pair(tAdjustedRowPos, tAdjustedColPos)
            }

            abs(hPos.first - tPos.first) >= 2 -> {
                val tAdjustedRowPos = if (tPos.first < hPos.first) {
                    hPos.first - 1
                } else {
                    hPos.first + 1
                }
                Pair(tAdjustedRowPos, hPos.second)
            }

            abs(hPos.second - tPos.second) >= 2 -> {
                val tAdjustedColPos = if (tPos.second < hPos.second) {
                    hPos.second - 1
                } else {
                    hPos.second + 1
                }
                Pair(hPos.first, tAdjustedColPos)
            }

            else -> {
                Pair(Int.MIN_VALUE, Int.MIN_VALUE)
            }
        }
    }

    fun part1(input: List<String>): Int {
        var hPos = Pair(0, 0)
        var tPos = Pair(0, 0)
        val tailTrack = mutableSetOf<Pair<Int, Int>>()
        tailTrack.addAll(listOf(tPos))
        input.forEachIndexed { _, d ->
            val (direction, stepSize) = d.split(" ")
            for (step in 0 until stepSize.toInt()) {
                val dPos = getDirectionPos(direction)
                hPos = Pair(hPos.first + directionSteps[dPos].first, hPos.second + directionSteps[dPos].second)
                tPos = findTailPosition(hPos, tPos)
                if (tPos.first != Int.MIN_VALUE) tailTrack.addAll(listOf(tPos))
            }
        }
        return tailTrack.count()
    }

    fun part2(input: List<String>): Int {
        var hPos = Pair(0, 0)
        //Build tPos for tails
        val tPos: MutableList<Pair<Int, Int>> = buildList { for (i in 0 until 9) add(Pair(0, 0)) }.toMutableList()
        val endPositionTrack = mutableSetOf<Pair<Int, Int>>()
        endPositionTrack.add(tPos[0])
        input.forEachIndexed { _, d ->
            val (direction, stepSize) = d.split(" ")
            for (step in 0 until stepSize.toInt()) {
                val dPos = getDirectionPos(direction)
                hPos = Pair(hPos.first + directionSteps[dPos].first, hPos.second + directionSteps[dPos].second)
                tPos[0] = findTailPosition(hPos, tPos[0])
                for (i in 1 until 9) {
                    tPos[i] = findTailPosition(tPos[i - 1], tPos[i])
                }
                if (tPos[8].first != Int.MIN_VALUE) endPositionTrack.add(tPos[8])
            }

        }
        return endPositionTrack.count()
    }

    val input = readInput("day09")
    println(part1(input))
    println(part2(input))
}
