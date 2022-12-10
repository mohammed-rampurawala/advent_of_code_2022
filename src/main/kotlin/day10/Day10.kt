package advent.day10

import advent.day10.Instruction.AddX
import advent.day10.Instruction.Noop
import advent.readInput

const val horizontalLength = 40
const val litPixel = '#'
fun main() {
    fun part1(input: List<Command>): Int {
        var x = 1
        var cycle = 0
        var previousMatchedCycle = 0
        var sum = 0
        input.forEach { cmd ->
            repeat(cmd.instruction.cycle) {
                cycle += 1
                if (previousMatchedCycle == 0 && cycle == 20) {
                    previousMatchedCycle = cycle
                    sum += (cycle * x)
                } else if (previousMatchedCycle + horizontalLength == cycle) {
                    previousMatchedCycle = cycle
                    sum += (cycle * x)
                }
            }
            x += cmd.xCounter
        }
        return sum
    }

    fun part2(input: List<Command>): Int {
        val display = List(6) { MutableList(horizontalLength) { '.' } }
        var x = 1
        var cycle = 0
        var spritePosition = resetSpritePosition(x)
        input.forEach { cmd ->
            repeat(cmd.instruction.cycle) {
                val spriteIdx = cycle % horizontalLength
                val heightIdx = cycle / horizontalLength
                if (spritePosition[spriteIdx] == litPixel) {
                    display[heightIdx][spriteIdx] = litPixel
                }
                cycle += 1
            }
            x += cmd.xCounter
            spritePosition = resetSpritePosition(x)
        }

        display.forEach {
            print(it.toString().replace(',', ' ').replace("\\s".toRegex(), ""))
            println()
        }
        return 0
    }

    val input = getCommands()
    println(part1(input))
    println(part2(input))
}

private fun getCommands(): List<Command> {
    return readInput("day10").map {
        it.split(" ")
    }.map {
        if (it.first() == "noop") {
            Command(Noop, 0)
        } else {
            Command(AddX, it.last().toInt())
        }
    }
}

private fun resetSpritePosition(x: Int): MutableList<Char> {
    return MutableList(horizontalLength) { if (it == x - 1 || it == x - 1 + 1 || it == x - 1 + 2) litPixel else '.' }
}

private data class Command(val instruction: Instruction, val xCounter: Int)

private sealed class Instruction(val cycle: Int) {
    object Noop : Instruction(1)
    object AddX : Instruction(2)
}
