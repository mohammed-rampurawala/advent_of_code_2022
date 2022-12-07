package advent.day05

import advent.readInput
import java.util.*
import kotlin.math.max

fun main() {
    val input = readInput("day05")
    val (maxNoOfStacks, stackList) = maxNoOfStacks(input)
    val movementList = cleanInput(maxNoOfStacks, input)
    movementList.forEach { moves ->
        val orderMaintainedLinkedList = mutableListOf<String>()
        for (i in 0 until moves[0]) {
            if (stackList[moves[1] - 1].isNotEmpty()) {
                orderMaintainedLinkedList.add(0, stackList[moves[1] - 1].pop())
            }

        }
        stackList[moves[2] - 1].addAll(orderMaintainedLinkedList)
    }
    val builder = StringBuilder()
    stackList.forEach {
        builder.append(it.pop())
    }
    println(builder.toString().replace('[', ' ').replace(']', ' ').filter { !it.isWhitespace() })
}

private fun cleanInput(height: Int, input: List<String>): MutableList<List<Int>> {
    var idx = height + 1
    val result = mutableListOf<List<Int>>()
    while (idx < input.size) {
        val list = mutableListOf<Int>()
        var innerIdx = 0
        val charArray = input[idx].toCharArray()
        while (innerIdx < charArray.size) {
            var string = ""
            while (innerIdx < charArray.size && charArray[innerIdx].isDigit()) {
                string += charArray[innerIdx]
                innerIdx++
            }
            if (string.isNotEmpty()) {
                list.add(string.toInt())
            }
            innerIdx++
        }
        if (list.isNotEmpty()) result.add(list)
        idx++
    }
    return result
}

private fun maxNoOfStacks(input: List<String>): Pair<Int, List<Stack<String>>> {
    var maxNoOfStacks = Int.MIN_VALUE
    var height = 0
    while (input[height].trimStart()[0].isDigit().not()) {
        val noOfStacks = (input[height].length + 1) / 4
        maxNoOfStacks = max(noOfStacks, maxNoOfStacks)
        height++
    }
    height--
    val stackList = buildList<Stack<String>> { for (i in 0 until maxNoOfStacks) add(Stack()) }
    while (height >= 0) {
        val length = input[height].length
        for (i in 0 until maxNoOfStacks) {
            if (i * 4 + 3 <= length) {
                if (input[height].substring(i * 4, i * 4 + 3)
                        .isNotBlank()
                ) stackList[i].add(input[height].substring(i * 4, i * 4 + 3))
            }
        }
        height--
    }
    return Pair(maxNoOfStacks, stackList)
}
