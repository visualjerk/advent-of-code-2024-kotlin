package day1

import utils.readIntInput
import kotlin.math.abs

fun main() {
    val input = readIntInput("day1")
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    for (lineItems in input) {
        list1.add(lineItems[0])
        list2.add(lineItems[1])
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