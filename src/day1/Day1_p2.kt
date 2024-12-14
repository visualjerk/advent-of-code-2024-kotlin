package day1_p2

import utils.readInput
import kotlin.math.abs

fun main() {
    val input = readInput( "day1")
    val numbers = mutableListOf<Int>()
    val numberAppearances = mutableMapOf<Int, Int>()

    for (line in input) {
        val lineItems = line.split(Regex("\\s+"))
        val firstNumber = lineItems[0].toInt()
        val secondNumber = lineItems[1].toInt()

        numbers.add(firstNumber)

        val appearances = numberAppearances.get(secondNumber) ?: 0
        numberAppearances[secondNumber] = appearances + 1
    }

    var totalSimilarity = 0

    for (number in numbers) {
        val similarity = number * (numberAppearances[number] ?: 0)
        totalSimilarity += similarity
    }

    println(totalSimilarity)
}