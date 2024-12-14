package day2

import utils.readInput

fun main() {
    val input = readInput( "day2")
    var safeReportCount = 0

    for (line in input) {
        val report = line.split(Regex("\\s+")).map { it.toInt() }

        if (isReportSafeWithProblemDampener(report)) {
            safeReportCount += 1
        }
    }

    println(safeReportCount)
}

fun isReportSafeWithProblemDampener(report: List<Int>): Boolean {
   if (isReportSafe(report)) {
       return true
   }

    for (dampedLevelIndex in report.indices) {
        if (isReportSafe((report.filterIndexed { index, _ -> index != dampedLevelIndex }))) {
            return true
        }
    }

    return false
}