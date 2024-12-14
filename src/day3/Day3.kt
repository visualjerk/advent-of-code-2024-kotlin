package day3

import utils.readInput

fun main() {
    val input = readInput( "day3")

    val expressions = mutableListOf<Expression>()
    for (codeLine in input) {
        val tokens = tokenizeLine(codeLine)
        expressions.addAll(parseLine(tokens))
    }

    var output = 0
    for (expression in expressions) {
        output += expression.evaluate()
    }

    println(output)
}

enum class TokenType {
    MUL {
        val marker = "mul"
        override fun getToken(line: String): Token? {
            if (line.startsWith(marker)) {
                return Token(MUL, marker)
            }
            return null
        }
    },
    OPEN_BRACKET {
        val marker = "("
        override fun getToken(line: String): Token? {
            if (line.startsWith(marker)) {
                return Token(OPEN_BRACKET, marker)
            }
            return null
        }
    },
    CLOSE_BRACKET {
        val marker = ")"
        override fun getToken(line: String): Token? {
            if (line.startsWith(marker)) {
                return Token(CLOSE_BRACKET, marker)
            }
            return null
        }
    },
    COMMA {
        val marker = ","
        override fun getToken(line: String): Token? {
            if (line.startsWith(marker)) {
                return Token(COMMA, marker)
            }
            return null
        }
    },
    INTEGER {
        override fun getToken(line: String): Token? {
            var value = ""
            var index = 0

            while (line[index].isDigit()) {
                value += line[index]
                index++
            }

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

    abstract fun getToken(line: String): Token?
}

data class Token (val type: TokenType, val value: String)

fun tokenizeLine(line: String): List<Token> {
    var remainingLine = line
    val tokens = mutableListOf<Token>()

    while (remainingLine.isNotEmpty()) {
        val token = getNextToken(remainingLine)
        tokens.add(token)
        remainingLine = remainingLine.drop(token.value.length)
    }

    return tokens
}

fun getNextToken(linePart: String): Token {
    for (tokenType in TokenType.entries) {
        val token = tokenType.getToken(linePart)

        if (token != null) {
            return token
        }
    }
    throw Exception("Line could not be parsed: $linePart")
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

enum class ExpressionType {
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
                    tokens.subList(0, 5),
                    tokens[2].value.toInt(),
                    tokens[4].value.toInt()
                )
            }

            return null
        }
    };

    abstract fun getExpression(tokens: List<Token>): Expression?
}

fun parseLine(tokens: List<Token>): List<Expression> {
    var remainingTokens = tokens
    val expressions = mutableListOf<Expression>()

    while (remainingTokens.isNotEmpty()) {
        val expression = getNextExpression(remainingTokens)

        if (expression != null) {
            expressions.add(expression)
            remainingTokens = remainingTokens.drop(expression.tokens.count())
        } else {
            remainingTokens = remainingTokens.drop(1)
        }
    }

    return expressions
}

fun getNextExpression(tokens: List<Token>): Expression? {
    for (expressionType in ExpressionType.entries) {
        val expression = expressionType.getExpression(tokens)

        if (expression != null) {
            return expression
        }
    }

    return null
}
