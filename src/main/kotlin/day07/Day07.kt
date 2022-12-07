package advent.day07

import advent.readInput

private const val executeCommandDelimiter = '$'
private const val changeDirectory = "cd"
private const val rootDirectory = "/"
private const val directory = "dir"
private const val listFiles = "ls"
private const val diskSpaceAvailable = 70000000
private const val atleastSpaceRequired: Long = 30000000L

open class File(open val name: String, open var size: Long = 0L) {
    open fun isDirectory() = false
}

class Dir(override val name: String, override var size: Long = 0L) : File(name, size) {
    override fun isDirectory() = true
}

class Tree<T> {
    var root: Node<T>? = null

    class Node<T>(var data: T?, var parent: Node<T>?) {
        val children = mutableListOf<Node<T>>()
        override fun toString(): String {
            return "Data: $data, Children: $children"
        }
    }

    override fun toString(): String {
        return root.toString()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val tree: Tree<File> = Tree()
        navigate(false, input.iterator(), tree.root, tree)
        calculateDirSize(tree.root)
        val sizesList = mutableListOf<Long>()
        getSizeListOfAllDirectories(tree.root, sizesList)
        return sizesList.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Long {
        val tree: Tree<File> = Tree()
        navigate(false, input.iterator(), tree.root, tree)
        calculateDirSize(tree.root)
        val currentlyFreeSpace = diskSpaceAvailable - (tree.root?.data as Dir).size
        if (currentlyFreeSpace >= atleastSpaceRequired) {
            println("Nothing to do")
            return 0
        }
        val spaceToUpdate = atleastSpaceRequired - currentlyFreeSpace
        println("Space to Update: $spaceToUpdate, Current free space: $currentlyFreeSpace")
        val sizesList = mutableListOf<Long>()
        getSizeListOfAllDirectories(tree.root, sizesList)
        return closest(sizesList, spaceToUpdate)
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

fun getSizeListOfAllDirectories(currNode: Tree.Node<File>?, sizesList: MutableList<Long>) {
    if (currNode == null) {
        return
    } else {
        if (currNode.data?.isDirectory() == true) {
            val dir = currNode.data as Dir
            sizesList.add(dir.size)
            currNode.children.forEach {
                getSizeListOfAllDirectories(it, sizesList)
            }
        } else {
            return
        }
    }
}

fun calculateDirSize(currNode: Tree.Node<File>?): Long {
    var size = 0L
    if (currNode == null) {
        return 0L
    } else {
        if (currNode.data?.isDirectory() == true) {
            currNode.children.forEach {
                size += calculateDirSize(it)
            }
            (currNode.data as Dir).size = size
            if (currNode.parent != null) {
                (currNode.parent?.data as Dir).size += size
            }
        } else {
            return (currNode.data as File).size
        }
    }
    return size
}

fun switchDirectory(dirName: String, currNode: Tree.Node<File>?, tree: Tree<File>) = when (dirName) {
    rootDirectory -> {
        val dir = Dir(dirName)
        val root = tree.root ?: Tree.Node<File>(dir, null)
        tree.root = root
        root
    }

    ".." -> currNode!!.parent
    else -> changeDirectory(currNode!!, dirName)
}

fun navigate(isListFiles: Boolean, cmdIterator: Iterator<String>, currNode: Tree.Node<File>?, tree: Tree<File>) {
    if (cmdIterator.hasNext().not()) {
        return
    }

    val command = cmdIterator.next()
    if (command.isEmpty()) {
        return
    }

    if (isChangeDirectoryCommand(command)) {
        val indexOfLast = command.indexOfLast { it == ' ' }
        val dirName = command.substring(indexOfLast, command.length).trim()
        navigate(isListFiles, cmdIterator, switchDirectory(dirName, currNode, tree), tree)
    } else if (isListFilesCommand(command)) {
        navigate(true, cmdIterator, currNode, tree)
    } else if (isListFiles) {
        val (firstPart, secondPart) = command.split(' ')
        val name = secondPart.trim()
        currNode?.children?.add(if (firstPart == directory) {
            Tree.Node(Dir(name), currNode)
        } else {
            val file = File(name).also {
                it.size = firstPart.toLong()
            }
            Tree.Node(file, currNode)
        })
        navigate(true, cmdIterator, currNode, tree)
    }
}

fun isListFilesCommand(next: String): Boolean {
    return next.startsWith(executeCommandDelimiter) && next.contains(listFiles)
}

fun changeDirectory(currNode: Tree.Node<File>, dirName: String): Tree.Node<File>? {
    currNode.children.forEach {
        if (it.data?.isDirectory() == true && it.data?.name == dirName) {
            return it
        }
    }
    return null
}

fun isChangeDirectoryCommand(command: String): Boolean {
    return command.contains(executeCommandDelimiter) && command.contains(changeDirectory)
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
