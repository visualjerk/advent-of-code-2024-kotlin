package day4

import utils.readInput

val XMAS_REGEX = Regex("XMAS")
val SAMX_REGEX = Regex("SAMX")

fun main() {
    val input = readInput( "day4")

    val readableLines = getReadableLines(input)

    val count = readableLines.sumOf {
        XMAS_REGEX.findAll(it).count() + SAMX_REGEX.findAll(it).count()
    }

    println(count)
}

fun getReadableLines(rows: List<String>): List<String> {
    val columnCount = rows[0].count()
    val rowCount = rows.count()
    val diagonalsCount = columnCount + rowCount - 1

    val columns = MutableList(columnCount) { "" }
    val ascDiagonals = MutableList(diagonalsCount) { "" }
    val descDiagonals = MutableList(diagonalsCount) { "" }

    rows.forEachIndexed { y, row ->
        row.forEachIndexed { x, char -> run {
            columns[x] = columns[x].plus(char)

            val ascDiagonalIndex = x - y + rowCount - 1
            ascDiagonals[ascDiagonalIndex] = ascDiagonals[ascDiagonalIndex].plus(char)

            val descDiagonalIndex = x + y
            descDiagonals[descDiagonalIndex] = descDiagonals[descDiagonalIndex].plus(char)
        }}
    }

    return rows.plus(columns).plus(ascDiagonals).plus(descDiagonals)
}
