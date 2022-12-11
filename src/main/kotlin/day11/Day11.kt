package advent.day11

import advent.readInput
import java.util.Collections
import java.util.PriorityQueue
import java.util.regex.Pattern

fun main() {
    fun part1(input: List<Monkey>): Long {
        repeat(20) {
            input.forEach { m ->
                while (m.worryLevels.isNotEmpty()) {
                    val number = m.operation() / 3
                    if (m.test(number)) {
                        input[m.forTrue].worryLevels.add(number)
                    } else {
                        input[m.forFalse].worryLevels.add(number)
                    }
                }
            }
        }
        val inspectionList = PriorityQueue<Long>(input.size, Collections.reverseOrder())
        input.forEach {
            inspectionList.add(it.inspectionCount)
        }
        return inspectionList.poll() * inspectionList.poll()
    }

    fun part2(input: List<Monkey>): Long {
        val lcm = lcm(input.map { it.divisibleBy }.toLongArray())
        repeat(10000) {
            input.forEach { monkey ->
                while (monkey.worryLevels.isNotEmpty()) {
                    val number = monkey.operation() % lcm
                    if (monkey.test(number)) {
                        input[monkey.forTrue].worryLevels.add(number)
                    } else {
                        input[monkey.forFalse].worryLevels.add(number)
                    }
                }
            }
        }
        val inspectionList = PriorityQueue<Long>(input.size, Collections.reverseOrder())
        input.forEach {
            inspectionList.add(it.inspectionCount)
        }
        return inspectionList.poll() * inspectionList.poll()
    }

    println(part1(parse(readInput("day11"))))
    println(part2(parse(readInput("day11"))))
}

private fun parse(input: List<String>): MutableList<Monkey> {
    val monkeysList = mutableListOf<Monkey>()
    var monkey: Monkey? = null
    input.forEach { cmd ->
        if (cmd.isBlank().not()) {
            val trimmedCmd = cmd.trim()
            if (trimmedCmd.startsWith("Monkey")) {
                val split = cmd.split(' ')
                monkey = Monkey(split[split.size - 1].replace(":", "").toInt())
                monkeysList.add(monkey!!)
            } else if (trimmedCmd.startsWith("Starting items:")) {
                val split = cmd.replace("Starting items:", "").split(",").map { it.trim().toLong() }
                monkey?.worryLevels?.addAll(split)
            } else if (trimmedCmd.startsWith("Operation:")) {
                monkey?.operation = trimmedCmd.replace("Operation:", "")
            } else if (trimmedCmd.startsWith("Test:")) {
                monkey?.divisibleBy = trimmedCmd.replace("Test: divisible by ", "").toLong()
            } else if (trimmedCmd.startsWith("If true:")) {
                monkey?.forTrue = trimmedCmd.replace("If true: throw to monkey ", "").toInt()
            } else if (trimmedCmd.startsWith("If false:")) {
                monkey?.forFalse = trimmedCmd.replace("If false: throw to monkey ", "").toInt()
            }
        }
    }
    return monkeysList
}

private data class Monkey(val name: Int) : Cloneable {
    var worryLevels = mutableListOf<Long>() //As a queue
    var operation: String = ""
    var forTrue: Int = 0
    var forFalse: Int = 0
    var divisibleBy: Long = 0L
    var inspectionCount = 0L
    fun operation(): Long {
        inspectionCount++
        var first = 0L
        var second = 0L
        val old = worryLevels.removeAt(0)
        val operation = operation.substring(operation.lastIndexOf('=') + 1)
        if (operation.contains("+")) {
            val split = operation.split("+").map { it.trim() }
            first = if (split[0] == "old") {
                old
            } else {
                split[0].toLong()
            }
            second = if (split[1] == "old") {
                old
            } else {
                split[1].toLong()
            }

            return (first + second)
        }

        if (operation.contains("*")) {
            val split = operation.split("*").map { it.trim() }
            first = if (split[0] == "old") {
                old
            } else {
                split[0].toLong()
            }
            second = if (split[1] == "old") {
                old
            } else {
                split[1].toLong()
            }
            return (first * second)
        }
        return Long.MIN_VALUE
    }

    fun test(number: Long): Boolean = number % divisibleBy == 0L
}

fun lcm(eles: LongArray): Long {
    var initalValue: Long = 1
    var divisor = 2
    while (true) {
        var counter = 0
        var divisible = false
        for (i in eles.indices) {
            if (eles[i] == 0L) {
                return 0
            } else if (eles[i] < 0) {
                eles[i] = eles[i] * -1
            }
            if (eles[i] == 1L) {
                counter++
            }
            if (eles[i] % divisor == 0L) {
                divisible = true
                eles[i] = eles[i] / divisor
            }
        }
        if (divisible) {
            initalValue *= divisor
        } else {
            divisor++
        }

        if (counter == eles.size) {
            return initalValue
        }
    }
}
