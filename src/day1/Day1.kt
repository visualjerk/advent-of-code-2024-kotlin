package day1

import utils.readInput
import kotlin.math.abs

fun main() {
    val input = readInput( "day1")
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    for (line in input) {
        val lineItems = line.split(Regex("\\s+"))

        list1.add(lineItems[0].toInt())
        list2.add(lineItems[1].toInt())
    }

    list1.sort()
    list2.sort()

    var totalDiff = 0

    for (index in list1.indices) {
        val diff = abs(list1[index] - list2[index])
        totalDiff += diff
    }

    println(totalDiff)
}