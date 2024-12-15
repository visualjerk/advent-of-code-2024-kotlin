package day5_p2

import utils.readInput

fun main() {
    val input = readInput( "day5")

    val rules = input.takeWhile { it.isNotEmpty() }.map {
        createRuleFromString(it)
    }
    val ruleSet = RuleSet(rules)

    val updates = input.subList(rules.size + 1, input.size).map {
        createPageUpdateFromString(it)
    }

    val output = updates
        .filter {
            !it.appliesRules(ruleSet)
        }.map {
            it.applyRules(ruleSet)
        }.sumOf {
            it.toValue()
        }

    println(output)
}

data class Rule(val firstPage: Int, val secondPage: Int): Comparator<Int> {
    fun isApplied(pages: List<Int>): Boolean {
        val firstIndex = pages.indexOf(firstPage)
        val secondIndex = pages.indexOf(secondPage)

        return firstIndex == -1 || secondIndex == -1 || firstIndex < secondIndex
    }

    override fun compare(p1: Int, p2: Int): Int {
        if (firstPage != p1 && firstPage != p2) {
            return 0
        }

        if (secondPage != p1 && secondPage != p2) {
            return 0
        }

        if (p1 == firstPage) {
            return -1
        }

        return 1
    }
}

data class RuleSet(val rules: List<Rule>): Comparator<Int> {
    fun isApplied(pages: List<Int>): Boolean {
        return rules.all { it.isApplied(pages) }
    }

    override fun compare(p1: Int, p2: Int): Int {
        for (rule in rules) {
            val result = rule.compare(p1, p2)
            if (result != 0) {
                return result
            }
        }
        return 0
    }
}

fun createRuleFromString(input: String): Rule {
    val pages = input.split("|").map { it.toInt() }
    return Rule(pages[0], pages[1])
}

class PagesUpdate(val pages: List<Int>) {
    fun appliesRules(ruleSet: RuleSet): Boolean {
        return ruleSet.isApplied(pages)
    }

    fun applyRules(ruleSet: RuleSet): PagesUpdate {
        return PagesUpdate(pages.sortedWith(ruleSet))
    }

    fun toValue(): Int {
        return pages[pages.size / 2]
    }
}

fun createPageUpdateFromString(input: String): PagesUpdate {
    val pages = input.split(",").map { it.toInt() }
    return PagesUpdate(pages)
}
