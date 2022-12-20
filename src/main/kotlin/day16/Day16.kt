package advent.day16

import advent.day16.Day16.Cache
import advent.readInput
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.Scope
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.max

val cache = mutableMapOf<Cache, Int>()

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
class Day16 {

    @Benchmark
    fun part1(valves: List<Valve>): Int {
        val start = valves.first {
            it.name == "AA"
        }
        return calculatePressure(start, 30, mutableListOf(), valves, 0)
    }

    @Benchmark
    fun part2(valves: List<Valve>): Int {
        val start = valves.first {
            it.name == "AA"
        }
        return calculatePressure(start, 26, mutableListOf(), valves, 1)
    }


    private fun calculatePressure(start: Valve, min: Int, openValves: MutableList<Valve>, valves: List<Valve>, nrOfOtherPlayers: Int): Int {
        //Time out
        if (min == 0) {
            return if (nrOfOtherPlayers <= 0) {
                0
            } else {
                val valve = valves.first {
                    it.name == "AA"
                }
                calculatePressure(valve, 26, openValves, valves, nrOfOtherPlayers - 1)
            }
        }
        val cache = Cache(start, min, openValves, nrOfOtherPlayers)
        if (advent.day16.cache.containsKey(cache)) {
            return advent.day16.cache[cache]!!
        }

        var max = Int.MIN_VALUE
        if (start.flow > 0 && openValves.contains(start).not()) {
            openValves.add(start)
            openValves.sortBy { it.flow }

            val pressure = (min - 1) * cache.valve.flow + calculatePressure(start, min - 1, openValves, valves, nrOfOtherPlayers)
            openValves.remove(start)
            max = pressure
        }
        start.neighbours.forEach { neighbour ->
            val x = valves.first { it.name == neighbour }
            max = max(max, calculatePressure(x, min - 1, openValves, valves, nrOfOtherPlayers))
        }
        advent.day16.cache[cache] = max
        return max
    }

    internal fun parse(input: List<String>): List<Valve> {
        val valves = mutableListOf<Valve>()
        val namePattern = Pattern.compile("([A-Z]{2})")
        val flowPattern = Pattern.compile("\\d+")
        input.forEach {
            val flow: Int = flowPattern.matcher(it).run {
                this.find()
                this.group().toInt()
            }

            val neighbours = mutableListOf<String>()
            val name: String = namePattern.matcher(it).run {
                find()
                val name = group()
                while (find()) {
                    neighbours.add(group())
                }
                name
            }
            valves.add(Valve(name, flow).apply {
                addNeighbours(neighbours)
            })
        }
        return valves
    }

    data class Cache(
        val valve: Valve,
        val min: Int,
        val openValves: List<Valve>,
        val nrOfOtherPlayers: Int,
    )

    data class Valve(val name: String, val flow: Int) {
        private val _neighbours = mutableSetOf<String>()
        val neighbours
            get() = _neighbours

        fun addNeighbour(neighbour: String) {
            _neighbours.add(neighbour)
        }

        fun addNeighbours(n: List<String>) {
            n.forEach {
                addNeighbour(it)
            }
        }
    }
}

fun main() {
    cache.clear()
    val day = Day16()
    println("Part 1: ${day.part1(day.parse(readInput("day16")))}")
    cache.clear()
    println("Part 2: ${day.part2(day.parse(readInput("day16")))}")
}
