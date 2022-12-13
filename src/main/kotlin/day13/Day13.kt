package advent.day13

import advent.readInput
import org.json.JSONArray

fun main() {
    fun compare(a: JSONArray, b: JSONArray): Int {
        if (a.length() == 1 && a[0] is Int && b.length() == 1 && b[0] is Int) {
            return if (a.getInt(0) < b.getInt(0)) {
                1
            } else if (a.getInt(0) == b.getInt(0)) {
                0
            } else {
                -1
            }
        }

        var idx = 0
        while (idx < a.length() && idx < b.length()) {
            val first: JSONArray = if (a[idx] is Int) {
                JSONArray("[${a[idx]}]")
            } else {
                a[idx] as JSONArray
            }
            val second: JSONArray = if (b[idx] is Int) {
                JSONArray("[${b[idx]}]")
            } else {
                b[idx] as JSONArray
            }
            val x = compare(first, second)
            if (x == 1) {
                return 1
            }
            if (x == -1) {
                return -1
            }
            idx++
        }
        if (idx == a.length()) {
            if (a.length() == b.length()) {
                return 0
            }
            return 1
        }
        return -1
    }


    fun part1(parse: List<JSONArray>): Int {
        val input = parse.chunked(2)
        var result = 0
        input.forEachIndexed { i, jsonArrays ->
            val compare = compare(jsonArrays.first(), jsonArrays.last())
            if (compare == 1) {
                result += i + 1
            }
        }
        return result
    }

    fun part2(parse: List<JSONArray>): Int {
        val divider1 = JSONArray("[[2]]")
        val divider2 = JSONArray("[[6]]")
        val dividerPackets = listOf(divider1, divider2)
        val newSortedPackets = buildList {
            addAll(dividerPackets)
            addAll(parse)
        }.sortedWith { o1, o2 -> compare(o1, o2) }.reversed()
        return (newSortedPackets.indexOf(divider1) + 1) * (newSortedPackets.indexOf(divider2) + 1)

    }
    println(part1(parse(readInput("day13"))))
    println(part2(parse(readInput("day13"))))
}

private fun parse(readInput: List<String>) = readInput.filter { it.isNotBlank() }.map { JSONArray(it) }
