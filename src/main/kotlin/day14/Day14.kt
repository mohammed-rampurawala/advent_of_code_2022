package advent.day14

import advent.readInput

fun main() {
    fun part1(parse: List<List<Pair<Int, Int>>>): Int {
        val map = mutableSetOf<Pair<Int, Int>>()
        parse.forEach {
            var idx = 0
            while (idx < it.size - 1) {
                val from = it[idx]
                val to = it[idx + 1]
                val direction = if (from.first == to.first) {
                    Pair(0, kotlin.math.sign((to.second.toDouble() - from.second.toDouble())).toInt())
                } else {
                    Pair(kotlin.math.sign((to.first.toDouble() - from.first.toDouble())).toInt(), 0)
                }
                var pair = from.copy()
                while (pair.first != to.first || pair.second != to.second) {
                    map.add(Pair(pair.first, pair.second))
                    pair = Pair(pair.first + direction.first, pair.second + direction.second)
                }
                map.add(pair)
                idx++
            }
        }
        var sand = 0
        val maxY = map.maxOf { it.second }
        while (true) {
            var sandX = 500
            var sandY = 0
            if (map.contains(Pair(sandX, sandY))) return sand

            while (true) {
                if (sandY == maxY) return sand

                if (map.contains(Pair(sandX, sandY + 1)).not()) {
                    sandY++
                    continue
                }

                if (map.contains(Pair(sandX - 1, sandY + 1)).not()) {
                    sandX--
                    sandY++
                    continue
                }

                if (map.contains(Pair(sandX + 1, sandY + 1)).not()) {
                    sandX++
                    sandY++
                    continue
                }
                break
            }
            map.add(Pair(sandX, sandY))
            sand++
        }
    }

    fun part2(parse: List<List<Pair<Int, Int>>>): Int {
        val map = mutableSetOf<Pair<Int, Int>>()
        parse.forEach {
            var idx = 0
            while (idx < it.size - 1) {
                val from = it[idx]
                val to = it[idx + 1]
                val direction = if (from.first == to.first) {
                    Pair(0, kotlin.math.sign((to.second.toDouble() - from.second.toDouble())).toInt())
                } else {
                    Pair(kotlin.math.sign((to.first.toDouble() - from.first.toDouble())).toInt(), 0)
                }
                var pair = from.copy()
                while (pair.first != to.first || pair.second != to.second) {
                    map.add(Pair(pair.first, pair.second))
                    pair = Pair(pair.first + direction.first, pair.second + direction.second)
                }
                map.add(pair)
                idx++
            }
        }
        var sand = 0
        var maxY = map.maxOf { it.second }
        val minX = map.minOf { it.first }
        val maxX = map.maxOf { it.first }
        maxY += 2
        var start = minX - maxY
        val end = maxX + maxY
        while (start < end) {
            map.add(Pair(start, maxY))
            start++
        }
        while (true) {
            var sandX = 500
            var sandY = 0
            if (map.contains(Pair(sandX, sandY))) return sand

            while (true) {
                if (sandY == maxY) return sand

                if (map.contains(Pair(sandX, sandY + 1)).not()) {
                    sandY++
                    continue
                }

                if (map.contains(Pair(sandX - 1, sandY + 1)).not()) {
                    sandX--
                    sandY++
                    continue
                }

                if (map.contains(Pair(sandX + 1, sandY + 1)).not()) {
                    sandX++
                    sandY++
                    continue
                }
                break
            }
            map.add(Pair(sandX, sandY))
            sand++
        }
    }
    println(part1(parse(readInput("day14"))))
    println(part2(parse(readInput("day14"))))
}

private fun parse(input: List<String>): List<List<Pair<Int, Int>>> {
    val chunked = input.filter { it.isNotBlank() }.map {
        it.split("->").joinToString().split(",").chunked(2).map {
            Pair(it.first().trim().toInt(), it.last().trim().toInt())
        }
    }
    return chunked
}
