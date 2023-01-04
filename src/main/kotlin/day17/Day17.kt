package advent.day17

import advent.readInput

private const val isDebug = true
fun main() {
    fun part1() {
        val input = readInput("day17").first().toCharArray()
        println(solve(input = input))
    }

    fun part2() {
        val input = readInput("day17").first().toCharArray()
        //1 trillion
        println(solve(input = input, 1_000_000_000_000L, true))
    }
    part1()
    val startTime = System.currentTimeMillis()
    part2()
    println("End Time: ${(System.currentTimeMillis() - startTime) / 1000}")
}

fun debugPrint(log: Any) {
    if (isDebug) {
        println(log.toString())
    }
}

private fun Char.toDirection(): Direction? {
    return when (this) {
        '>' -> Direction.RIGHT
        '<' -> Direction.LEFT
        'v' -> Direction.DOWN
        else -> null
    }
}

fun solve(input: CharArray, noOfRocksToAdd: Long = 2022, detectCycles: Boolean = false): Long {
    var movIndx = 0L
    val chamber = Chamber()

    var foundCycle = detectCycles.not()
    var cycleHeight = 0L
    val gamesSeen = mutableMapOf<Long, GameResult>()

    while (chamber.noOfRocks < noOfRocksToAdd) {
        val movIndx1 = movIndx++
        val dr = input[movIndx1.toInt()]
        movIndx %= input.size

        val direction = dr.toDirection()
        if (chamber.canFit(direction)) {
            chamber.rock.move(direction)
//            debugPrint("Moved $direction")
        }

        if (chamber.canFit(Direction.DOWN)) {
            chamber.rock.move(Direction.DOWN)
//            debugPrint("Move ${Direction.DOWN}")
        } else {
            chamber.addRock()
//            debugPrint("Added rock ${chamber.noOfRocks}, New height is ${chamber.height()}")
        }

        if (foundCycle.not()) {
            val hash = GameState(movIndx, chamber.rock.shape, chamber.rColHeight()).hashCode().toLong()
            val cache: GameResult? = gamesSeen[hash]
            if (cache != null) {
                foundCycle = true
                val rocksLeft: Long = noOfRocksToAdd - chamber.noOfRocks
                val rocksPerCycle: Long = chamber.noOfRocks - cache.noOfRocks
                val heightPerCycle: Long = (chamber.height() - cache.height)
                val noOfCycles: Long = rocksLeft.div(rocksPerCycle)
                debugPrint("Skipping $noOfCycles cycles with $rocksPerCycle rocks and $heightPerCycle height.")

                chamber.noOfRocks += noOfCycles * rocksPerCycle
                cycleHeight = noOfCycles * heightPerCycle
            } else {
                gamesSeen[hash] = GameResult(chamber.noOfRocks, chamber.height())
            }
        }
    }
    return chamber.height() + cycleHeight
}

private data class GameResult(
    val noOfRocks: Long,
    val height: Long,
)

private data class GameState(
    val indx: Long, val rockShape: Long, val columnHeight: List<Long>
)

private class Chamber {
    private val blocks = mutableSetOf<Point>()

    var rock = Rock(0, Point(2, 3))

    var noOfRocks = 0L
    fun height(): Long {
        return if (blocks.isNotEmpty()) {
            blocks.maxOf { it.y }
        } else {
            0
        } + 1
    }

    fun addRock() {
        blocks.addAll(rock.blocks())
        noOfRocks++
        rock = Rock((noOfRocks % 5).toLong(), Point(2, height() + 3))
    }

    fun canFit(dr: Direction?): Boolean {
        val newPos = rock.pos.move(dr)
        rock.blocks(newPos).forEach {
            if (blocks.contains(it)) return false
            if (it.y < 0 || it.x < 0 || it.x > 6) return false
        }
        return true
    }

    fun rColHeight(): MutableList<Long> {
        val height = height()
        val result = mutableListOf<Long>()
        (0..6).map {

        }
        for (i in 0..6) {
            val maxY = if (blocks.isNotEmpty()) {
                val filter = blocks.filter { it.x == i.toLong() }
                if (filter.isNotEmpty()) {
                    filter.maxBy { it.y }.y
                } else {
                    0
                }

            } else {
                0
            }
            result.add(height - maxY)
        }
        return result
    }
}

private data class Rock(val shape: Long, var pos: Point) {
    fun blocks(pos: Point? = null): List<Point> {
        val tempPos = pos ?: this.pos
        return when (shape) {
            0L -> listOf(
                Point(tempPos.x, tempPos.y),
                Point(tempPos.x + 1, tempPos.y),
                Point(tempPos.x + 2, tempPos.y),
                Point(tempPos.x + 3, tempPos.y),
            )

            1L -> listOf(
                Point(tempPos.x + 1, tempPos.y),
                Point(tempPos.x + 1, tempPos.y + 2),
                Point(tempPos.x, tempPos.y + 1),
                Point(tempPos.x + 1, tempPos.y + 1),
                Point(tempPos.x + 2, tempPos.y + 1),
            )

            2L -> listOf(
                Point(tempPos.x, tempPos.y),
                Point(tempPos.x + 1, tempPos.y),
                Point(tempPos.x + 2, tempPos.y),
                Point(tempPos.x + 2, tempPos.y + 1),
                Point(tempPos.x + 2, tempPos.y + 2),
            )

            3L -> listOf(
                Point(tempPos.x, tempPos.y),
                Point(tempPos.x, tempPos.y + 1),
                Point(tempPos.x, tempPos.y + 2),
                Point(tempPos.x, tempPos.y + 3),
            )

            4L -> listOf(
                Point(tempPos.x, tempPos.y),
                Point(tempPos.x + 1, tempPos.y),
                Point(tempPos.x, tempPos.y + 1),
                Point(tempPos.x + 1, tempPos.y + 1),
            )

            else -> emptyList()
        }
    }

    fun move(dr: Direction?) {
        pos = pos.move(dr)!!
    }
}

private data class Point(val x: Long, val y: Long) {
    fun move(dr: Direction?): Point? {
        return when (dr) {
            Direction.LEFT -> Point(x - 1, y)
            Direction.RIGHT -> Point(x + 1, y)
            Direction.DOWN -> Point(x, y - 1)
            else -> null
        }
    }
}

sealed class Direction(val symbol: String) {
    object LEFT : Direction("<") {
        override fun toString(): String {
            return "Left"
        }
    }

    object RIGHT : Direction(">") {
        override fun toString(): String {
            return "Right"
        }
    }

    object DOWN : Direction("v") {
        override fun toString(): String {
            return "Down"
        }
    }
}
