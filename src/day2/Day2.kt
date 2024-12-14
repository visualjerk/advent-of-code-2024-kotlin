package day2

import utils.readInput
import kotlin.math.abs

fun main() {
    val input = readInput( "day2")
    var safeReportCount = 0

    for (line in input) {
        val report = line.split(Regex("\\s+")).map { it.toInt() }

        if (isReportSafe(report)) {
            safeReportCount += 1
        }
    }

    println(safeReportCount)
}

fun isReportSafe(report: List<Int>): Boolean {
    var lastDiff = 0
    var lastLevel = report[0]

    for (level in report.drop(1)) {
        val diff = level - lastLevel

        if (diff < 0 && lastDiff > 0 || lastDiff < 0 && diff > 0) {
            return false
        }

        if (abs(diff) < 1 || abs(diff) > 3) {
            return false
        }

        lastDiff = diff
        lastLevel = level
    }

    return true
}