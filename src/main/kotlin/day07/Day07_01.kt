package advent.day07

import advent.readInput

private const val executeCommandDelimiter = '$'
private const val changeDirectory = "cd"
private const val rootDirectory = "/"
private const val directory = "dir"
private const val listFiles = "ls"

open class File(open val name: String, open var size: Long = 0L) {
    open fun isDirectory(): Boolean {
        return false
    }
}

class Dir(override val name: String, override var size: Long = 0L) : File(name, size) {
    override fun isDirectory(): Boolean {
        return true
    }
}

class Tree<T> {
    var root: Node<T>? = null

    class Node<T>(var data: T?, var parent: Node<T>?) {
        val childrens = mutableListOf<Node<T>>()
        override fun toString(): String {
            return "Data: $data, Childrens: $childrens"
        }
    }

    override fun toString(): String {
        return root.toString()
    }
}

fun main() {
    val commands = readInput("day07")
    val tree: Tree<File> = Tree()
    prepareTree(false, commands.iterator(), tree.root, tree)
    calculateDirSize(tree.root)
    val sizesList = mutableListOf<Long>()
    getSizeListOfAllDirectories(tree.root, sizesList)
    println(sizesList.filter { it <= 100000 }.sum())
}

fun getSizeListOfAllDirectories(currNode: Tree.Node<File>?, sizesList: MutableList<Long>) {
    if (currNode == null) {
        return
    } else {
        if (currNode.data?.isDirectory() == true) {
            val dir = currNode.data as Dir
            sizesList.add(dir.size)
            currNode.childrens.forEach {
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
            currNode.childrens.forEach {
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

fun prepareTree(isListFiles: Boolean, commandIter: Iterator<String>, currNode: Tree.Node<File>?, tree: Tree<File>) {
    if (commandIter.hasNext().not()) {
        return
    }

    val command = commandIter.next()
    if (command.isEmpty()) {
        return
    }

    if (isChangeDirectoryCommand(command)) {
        val indexOfLast = command.indexOfLast { it == ' ' }
        when (val dirName = command.substring(indexOfLast, command.length).trim()) {
            rootDirectory -> {
                val dir = Dir(dirName)
                val root = tree.root ?: Tree.Node<File>(dir, null)
                tree.root = root
                prepareTree(false, commandIter, root, tree)
            }

            ".." -> {
                prepareTree(isListFiles, commandIter, currNode!!.parent, tree)
            }

            else -> {
                val currentDir = changeDirectory(currNode!!, dirName)
                prepareTree(false, commandIter, currentDir!!, tree)
            }
        }
    } else if (isListFilesCommand(command)) {
        prepareTree(true, commandIter, currNode, tree)
    } else if (isListFiles) {
        val indexOfLast = command.indexOfLast { it == ' ' }
        val name = command.substring(indexOfLast, command.length).trim()
        currNode?.childrens?.add(if (command.startsWith(directory)) {
            Tree.Node(Dir(name), currNode)
        } else {
            val file = File(name).also {
                it.size = command.substring(0, indexOfLast).toLong()
            }
            Tree.Node(file, currNode)
        })
        prepareTree(isListFiles, commandIter, currNode, tree)
    }
}

fun isListFilesCommand(next: String): Boolean {
    return next.startsWith(executeCommandDelimiter) && next.contains(listFiles)
}

fun changeDirectory(currNode: Tree.Node<File>, dirName: String): Tree.Node<File>? {
    currNode.childrens.forEach {
        if (it.data?.isDirectory() == true && it.data?.name == dirName) {
            return it
        }
    }
    return null
}

fun isChangeDirectoryCommand(command: String): Boolean {
    return command.contains(executeCommandDelimiter) && command.contains(changeDirectory)
}
