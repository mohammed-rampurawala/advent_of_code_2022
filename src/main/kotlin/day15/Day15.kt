package advent.day15

import advent.readInput
import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun part1(parse: List<List<Any>>): Int {
        val max = 2000000
        val intervals = mutableListOf<Pair<Int, Int>>()
        parse.forEach {
            val sensor = it[0] as Pair<Int, Int>
            val dx = it[2] as Int - abs(sensor.second - max)
            if (dx > 0) intervals.add(Pair(sensor.first - dx, sensor.first + dx))
        }

        val allowedX = mutableListOf<Int>()
        parse.forEach {
            val beacon = (it[1] as Pair<Int, Int>)
            if (beacon.second == max) {
                allowedX.add(beacon.first)
            }
        }
        val minX = intervals.minOf { it.first }
        val maxX = intervals.maxOf { it.second }

        var ans = 0
        for (x in minX until maxX + 1) {
            if (x in allowedX) {
                continue
            }
            for (dr in intervals) {
                if (x >= dr.first && x <= dr.second) {
                    ans += 1
                    break
                }
            }
        }
        return ans
    }

    fun part2(parse: List<List<Any>>): Long {
        val posLines = mutableListOf<Int>()
        val negLines = mutableListOf<Int>()
        parse.forEach {
            val beacon = (it[1] as Pair<Int, Int>)
            val sensor = it[0] as Pair<Int, Int>
            val dist = it[2] as Int

            negLines.addAll(listOf(sensor.first + sensor.second - dist, sensor.first + sensor.second + dist))
            posLines.addAll(listOf(sensor.first - sensor.second - dist, sensor.first - sensor.second + dist))
        }

        var pos = 0
        var neg = 0
        for (i in 0 until (2 * parse.size)) {
            for (j in i + 1 until (2 * parse.size)) {
                var a = posLines[i]
                var b = posLines[j]
                if (abs(a - b) == 2) {
                    pos = min(a, b) + 1
                }

                a = negLines[i]
                b = negLines[j]
                if (abs(a - b) == 2) {
                    neg = min(a, b) + 1
                }
            }
        }
//        println(pos + neg)
//        println(neg - pos)
        val x = (pos + neg) / 2L
        val y = (neg - pos) / 2L
        return x * 4000000 + y
    }
    println("Part 1: ${part1(parse(readInput("day15")))}")
    println("Part 2: ${part2(parse(readInput("day15")))}")
}

fun parse(readInput: List<String>): List<List<Any>> {
    return readInput.map {
        val split = it.split(":")
        val sensorString = split[0]
        val replace = sensorString.replace("Sensor at ", "").split(",")
        val sensorXValue = replace[0].split("=")[1].toInt()
        val sensorYValue = replace[1].split("=")[1].toInt()

        val beaconString = split[1]
        val replaceBeacon = beaconString.replace(" closest beacon is at ", "").split(",")
        val beaconXValue = replaceBeacon[0].split("=")[1].toInt()
        val beaconYValue = replaceBeacon[1].split("=")[1].toInt()
        val sensorCoord = Pair(sensorXValue, sensorYValue)
        val beaconCoord = Pair(beaconXValue, beaconYValue)
        val manhattanDistance = manhattanDistance(sensorCoord, beaconCoord)
        listOf(sensorCoord, beaconCoord, manhattanDistance)
    }
}

private fun manhattanDistance(sensorCoord: Pair<Int, Int>, beaconCoord: Pair<Int, Int>): Int {
    return abs(sensorCoord.first - beaconCoord.first) + abs(sensorCoord.second - beaconCoord.second)
}
