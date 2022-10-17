package minesweeper

import kotlin.system.exitProcess

private val row = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)
private val col = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)
class FloodFill {
    /**
     * checks if indexes of neighbor in 1..9 and if it hasn't already checked
     */
    private fun checkNeighbor(copyMat: MutableList<MutableList<Char>>, x: Int, y: Int): Boolean {
        return x in 1..9 && y in 1..9 && copyMat[x][y] != '/'
    }

    /**
     * Flood Fill algorithm
     */
    fun algo(mat: MutableList<MutableList<Char>>,
        copyMat: MutableList<MutableList<Char>>,
        x: Int, y: Int, replaceColor: Char) {
        when (val curColor = mat[x][y]) {       // set current color from matrix with mines (from "mines")
            '.' -> copyMat[x][y] = replaceColor // all dots replace by slashes
            'X' -> {                            // if user choice the mine - he loses
                println("You stepped on a mine and failed!")
                exitProcess(0)
            }
            else -> {                           // if current cell is number - algo doesn't check its neighbors
                copyMat[x][y] = curColor
                return                          // return to don't check neighbors
            }
        }
        for (k in row.indices) {                // does check for every neighbor
            if (checkNeighbor(copyMat, x + row[k], y + col[k])) algo(mat, copyMat, x + row[k], y + col[k], replaceColor)
        }
    }
}

class Minesweeper {
    private var mines = mutableListOf( // 11x11, when 9x9 required for easy set neighbor's symbols
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')
    )
    private var copyMines = mutableListOf(
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')
    )
    private var backupMines = mutableListOf(
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'),
        mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')
    )
    private var numberOfMines = 0
    private var minesMarked = 0
    private var minesMarkedCorrect = 0
    private var startX: Int = 0
    private var startY: Int = 0
    private val floodFill = FloodFill()
    private var indexes = mutableListOf<Pair<Int, Int>>()

    /**
     * sets the requested number of mines
     */
    private fun setMines() {
        var changes = 0
        for (i in 1..9) {
            for (j in 1..9) {
                indexes.add(Pair(i, j))
            }
        }
        while (changes < numberOfMines) {
            val randomIndexes = indexes.random()
            if (randomIndexes.second != startX || randomIndexes.first != startY) {
                mines[randomIndexes.first][randomIndexes.second] = 'X'
                setToNeighbors(randomIndexes.first, randomIndexes.second, ::setSymbolToOneNeighborOnMines)
                changes++
            }
            indexes.remove(randomIndexes)
        }
    }

    /**
     * requests the number of mines and sets it
     */
    private fun setNumberOfMines() {
        println("How many mines do you want on the field? > ")
        numberOfMines = readln().toInt()
    }

    /**
     * "setNeighbors" func bypassed all mine's neighbors
     */
    private fun setToNeighbors(a: Int, b: Int, c: (a: Int, b: Int) -> Unit) {
        for (k in row.indices) {
            c(a + row[k], b + col[k])
        }
    }

    /**
     * "setOneNeighbor" set one of mine's neighbors required symbol
     */
    private fun setSymbolToOneNeighborOnMines(a: Int, b: Int) {
        if (mines[a][b] == '.') mines[a][b] = '1' else {
            if (mines[a][b] != 'X') mines[a][b] =
                (mines[a][b].toString().toInt() + 1).digitToChar() // if not 'X' set 'number + 1'
        }
    }

    /**
     * checks if all safe cells are open
     */
    private fun isAllSafeCellsOpened(): Boolean {
        var dotsRight = 0
        var dots = 0
        for (i in 1..9) {
            for (j in 1..9) {
                if (copyMines[i][j] == '.') {
                    dots++
                    if (mines[i][j] == 'X') dotsRight++
                }
            }
        }
        return dots == numberOfMines && dotsRight == dots
    }

    /**
     * makes a backup of "copyMines" before every marking potential mine
     */
    private fun backup() {
        for (i in 1..9) {
            for (j in 1..9) {
                if (copyMines[i][j] != '*') backupMines[i][j] = copyMines[i][j]
            }
        }
    }

    /**
     * "outMines" prints our playing field
     */
    private fun outMines(a: MutableList<MutableList<Char>>) {
        println(" |123456789|")
        println("-|---------|")
        for (i in 1..9) {
            print("$i|")
            for (j in 1..9) {
                print(a[i][j])
            }
            print("|\n")
        }
        println("-|---------|")
    }

    /**
     * sets or deletes mine mark
     */
    private fun setDelete() {
        if (isAllSafeCellsOpened() || minesMarkedCorrect == numberOfMines) {
            println("Congratulations! You found all the mines!")
            exitProcess(0)
        } else {
            println("Set/unset mines marks or claim a cell as free: >")
            val coordinatesIn = readln().split(" ")
            val coordinateX = coordinatesIn[0].toInt()
            val coordinateY = coordinatesIn[1].toInt()
            val freeOrMine = coordinatesIn[2]
            if (startX == 0) {
                startX = coordinateX
                startY = coordinateY
                setMines()
            }
            if (freeOrMine == "mine") {
                backup()
                if (copyMines[coordinateY][coordinateX] == '*') {
                    if (mines[coordinateY][coordinateX] == 'X') minesMarkedCorrect--
                    copyMines[coordinateY][coordinateX] = backupMines[coordinateY][coordinateX]
                } else {
                    copyMines[coordinateY][coordinateX] = '*'
                    if (mines[coordinateY][coordinateX] == 'X') minesMarkedCorrect++
                }
            } else {
                floodFill.algo(mines, copyMines, coordinateY, coordinateX, '/')
            }
        }
    }

    /**
     * implements the game
     */
    fun game() {
        setNumberOfMines()
        outMines(copyMines)
        while (minesMarkedCorrect < numberOfMines || minesMarked != minesMarkedCorrect) {
            setDelete()
            outMines(copyMines)
        }
    }
}

fun main() {
    Minesweeper().game() // starts the game
}
