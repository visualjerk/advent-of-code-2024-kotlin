package utils

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

fun readInput(dayName: String): List<String> {
    val filePath = Path("src/${dayName}/input.txt").absolutePathString()
    val input = File(filePath).readLines()
    return input
}
