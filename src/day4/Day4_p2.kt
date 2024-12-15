package day4

import utils.readInput

fun main() {
    val rows = readInput( "day4")

    var count = 0

    rows.forEachIndexed { y, row ->
        row.forEachIndexed(fun (x, char) {
            if (char != 'A') {
                return
            }

            if (x - 1 < 0 || x + 1 > row.lastIndex || y -1 < 0 || y + 1 > rows.lastIndex) {
                return
            }

            val topLeftChar = rows[y - 1][x - 1]
            val bottomRightChar = rows[y + 1][x + 1]
            val bottomLeftChar = rows[y + 1][x - 1]
            val topRightChar = rows[y - 1][x + 1]

            if (
                (topLeftChar == 'M' && bottomRightChar == 'S' || topLeftChar == 'S' && bottomRightChar == 'M') &&
                (bottomLeftChar == 'M' && topRightChar == 'S' || bottomLeftChar == 'S' && topRightChar == 'M')
            ) {
                count++
            }
        })
    }

    println(count)
}
