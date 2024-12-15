package day5

import utils.readInput

fun main() {
    val input = readInput( "day5")

    val rules = input.takeWhile { it.isNotEmpty() }.map {
        createPageOrderingRuleFromString(it)
    }

    val updates = input.subList(rules.size + 1, input.size).map {
        createPageUpdateFromString(it)
    }

    val output = updates.filter { it.appliesRules(rules) }.sumOf { it.toValue() }

    println(output)
}

data class PageOrderingRule(val firstPage: Int, val secondPage: Int) {
    fun isApplied(pages: List<Int>): Boolean {
        val firstIndex = pages.indexOf(firstPage)
        val secondIndex = pages.indexOf(secondPage)

        return firstIndex == -1 || secondIndex == -1 || firstIndex < secondIndex
    }
}

fun createPageOrderingRuleFromString(input: String): PageOrderingRule {
    val pages = input.split("|").map { it.toInt() }
    return PageOrderingRule(pages[0], pages[1])
}

class PagesUpdate(val pages: List<Int>) {
    fun appliesRules(rules: List<PageOrderingRule>): Boolean {
        return rules.all { it.isApplied(pages) }
    }

    fun toValue(): Int {
        return pages[pages.size / 2]
    }
}

fun createPageUpdateFromString(input: String): PagesUpdate {
    val pages = input.split(",").map { it.toInt() }
    return PagesUpdate(pages)
}
