package day3_p2

import utils.readInput

fun main() {
    val input = readInput( "day3")

    val tokens = tokenizeInput(input.joinToString (separator = "\n"))
    val expressions = parseTokens(tokens)

    val program = DoExpression(tokens, expressions)

    println(program.evaluate())
}

enum class TokenType(private val marker: String? = null) {
    MUL("mul"),
    DONT("don't()"),
    DO("do()"),
    OPEN_BRACKET("("),
    CLOSE_BRACKET( ")"),
    COMMA(","),
    INTEGER {
        override fun getToken(line: String): Token? {
            val value = line.takeWhile { it.isDigit() }
            if (value.isNotEmpty()) {
                return Token(INTEGER, value)
            }
            return null
        }
    },
    UNKNOWN {
        override fun getToken(line: String): Token {
            return Token(UNKNOWN, line[0].toString())
        }
    };

    open fun getToken(line: String): Token? {
        marker?.let {
            if (line.startsWith(it)) {
                return Token(this, it)
            }
        }
        return null
    }
}

data class Token (val type: TokenType, val value: String)

fun tokenizeInput(input: String): List<Token> {
    val tokens = mutableListOf<Token>()
    var index = 0

    while (index < input.length) {
        val token = getNextToken(input.substring(index))
        tokens.add(token)
        index += token.value.length
    }

    return tokens
}

class TokenizationException(part: String) : Exception("Unable to tokenize: $part")

fun getNextToken(part: String): Token {
    return TokenType.entries.firstNotNullOfOrNull { it.getToken(part) }
        ?: throw TokenizationException(part)
}

interface Expression {
    val tokens: List<Token>
    fun evaluate(): Int
}

data class MulExpression (
    override val tokens: List<Token>,
    val firstOperand: Int,
    val secondOperand: Int
): Expression {
    override fun evaluate(): Int {
        return firstOperand * secondOperand
    }
}

data class DoExpression (
    override val tokens: List<Token>,
    val expressions: List<Expression>
): Expression {
    override fun evaluate(): Int {
        return expressions.sumOf { it.evaluate() }
    }
}

data class DontExpression (
    override val tokens: List<Token>,
    val expressions: List<Expression>
): Expression {
    override fun evaluate(): Int {
        return 0
    }
}

enum class ExpressionType {
    DO {
        override fun getExpression(tokens: List<Token>): Expression? {
            if (tokens[0].type != TokenType.DO) {
                return null
            }

            val (innerTokens, innerExpressions) = parseInnerExpressions(tokens.subList(1, tokens.size))
            return DoExpression(innerTokens, innerExpressions)
        }
    },
    DONT {
        override fun getExpression(tokens: List<Token>): Expression? {
            if (tokens[0].type != TokenType.DONT) {
                return null
            }

            val (innerTokens, innerExpressions) = parseInnerExpressions(tokens.subList(1, tokens.size))
            return DontExpression(innerTokens, innerExpressions)
        }
    },
    MUL {
        override fun getExpression(tokens: List<Token>): Expression? {
            if (
                tokens.lastIndex >= 5 &&
                tokens[0].type == TokenType.MUL &&
                tokens[1].type == TokenType.OPEN_BRACKET &&
                tokens[2].type == TokenType.INTEGER &&
                tokens[3].type == TokenType.COMMA &&
                tokens[4].type == TokenType.INTEGER &&
                tokens[5].type == TokenType.CLOSE_BRACKET
            ) {
                return MulExpression(
                    tokens.subList(0, 6),
                    tokens[2].value.toInt(),
                    tokens[4].value.toInt()
                )
            }

            return null
        }
    };

    abstract fun getExpression(tokens: List<Token>): Expression?
}

fun parseTokens(tokens: List<Token>): List<Expression> {
    val expressions = mutableListOf<Expression>()
    var index = 0

    while (index < tokens.size) {
        val expression = getNextExpression(tokens.subList(index, tokens.size))

        if (expression != null) {
            expressions.add(expression)
            index += expression.tokens.size
        } else {
            index++
        }
    }

    return expressions
}

fun getNextExpression(tokens: List<Token>): Expression? {
    return ExpressionType.entries.firstNotNullOfOrNull { it.getExpression(tokens) }
}

fun parseInnerExpressions(tokens: List<Token>): Pair<List<Token>, List<Expression>> {
    var endIndex = 0

    while (
        endIndex < tokens.size &&
        tokens[endIndex].type != TokenType.DO &&
        tokens[endIndex].type != TokenType.DONT
    ) {
        endIndex++
    }

    val innerTokens = tokens.subList(0, endIndex)
    val innerExpressions = parseTokens(innerTokens)
    return Pair(innerTokens, innerExpressions)
}