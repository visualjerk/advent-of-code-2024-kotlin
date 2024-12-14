package day2

import utils.readIntInput

fun main() {
    val input = readIntInput( "day2")
    var safeReportCount = 0

    for (report in input) {
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