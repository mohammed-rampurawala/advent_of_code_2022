package advent.day07

import advent.readInput

private const val diskSpaceAvailable = 70000000
private const val atleastSpaceRequired: Long = 30000000L

fun main() {
    val commands = readInput("day07")
    val tree: Tree<File> = Tree()
    navigate(false, commands.iterator(), tree.root, tree)
    calculateDirSize(tree.root)
    val currentlyFreeSpace = diskSpaceAvailable - (tree.root?.data as Dir).size
    if (currentlyFreeSpace >= atleastSpaceRequired) {
        println("Nothing to do")
        return
    }
    val spaceToUpdate = atleastSpaceRequired - currentlyFreeSpace
    println("Space to Update: $spaceToUpdate, Current free space: $currentlyFreeSpace")
    val sizesList = mutableListOf<Long>()
    getSizeListOfAllDirectories(tree.root, sizesList)
    println(closest(sizesList, spaceToUpdate))
}

fun closest(sizesList: MutableList<Long>, of: Long): Long {
    sizesList.sortDescending()
    var mininumDifference = Long.MAX_VALUE
    var closest = of
    for (value in sizesList) {
        val diff = Math.abs(of - value)
        if (diff < mininumDifference) {
            mininumDifference = diff
            closest = value
        }
    }
    return closest
}
