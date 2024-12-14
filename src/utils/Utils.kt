package utils

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

fun readInput(dayName: String): List<String> {
    val filePath = Path("src/${dayName}/input.txt").absolutePathString()
    return File(filePath).readLines()
}

fun readIntInput(dayName: String): List<List<Int>> {
    val input = readInput(dayName)
    return input.map { it.split(Regex("\\s+")).map { it.toInt() } }
}
