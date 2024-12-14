package day1

import utils.readIntInput

fun main() {
    val input = readIntInput( "day1")
    val numbers = mutableListOf<Int>()
    val numberAppearances = mutableMapOf<Int, Int>()

    for (lineItems in input) {
        val firstNumber = lineItems[0]
        val secondNumber = lineItems[1]

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