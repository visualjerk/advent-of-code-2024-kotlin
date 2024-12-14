package day3_p2

import utils.readInput

fun main() {
    val input = readInput( "day3")

    val tokens = tokenizeInput(input.joinToString (separator = "\n"))
    val expressions = parseTokens(tokens)

    val program = DoExpression(tokens, expressions)

    println(program.evaluate())
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
    DONT {
        val marker = "don't()"
        override fun getToken(line: String): Token? {
            if (line.startsWith(marker)) {
                return Token(DONT, marker)
            }
            return null
        }
    },
    DO {
        val marker = "do()"
        override fun getToken(line: String): Token? {
            if (line.startsWith(marker)) {
                return Token(DO, marker)
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

fun tokenizeInput(input: String): List<Token> {
    var remainingInput = input
    val tokens = mutableListOf<Token>()

    while (remainingInput.isNotEmpty()) {
        val token = getNextToken(remainingInput)
        tokens.add(token)
        remainingInput = remainingInput.drop(token.value.length)
    }

    return tokens
}

fun getNextToken(part: String): Token {
    for (tokenType in TokenType.entries) {
        val token = tokenType.getToken(part)

        if (token != null) {
            return token
        }
    }
    throw Exception("part could not be tokenized: $part")
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
        var output = 0
        for (expression in expressions) {
            output += expression.evaluate()
        }
        return output
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

            var remainingTokens = tokens.drop(1)
            val innerTokens = mutableListOf<Token>()

            while (
                remainingTokens.isNotEmpty() &&
                remainingTokens[0].type != TokenType.DO &&
                remainingTokens[0].type != TokenType.DONT
            ) {
                innerTokens.add(remainingTokens[0])
                remainingTokens = remainingTokens.drop(1)
            }

            val innerExpressions = parseTokens(innerTokens)

            return DoExpression(innerTokens, innerExpressions)
        }
    },
    DONT {
        override fun getExpression(tokens: List<Token>): Expression? {
            if (tokens[0].type != TokenType.DONT) {
                return null
            }

            var remainingTokens = tokens.drop(1)
            val innerTokens = mutableListOf<Token>()

            while (
                remainingTokens.isNotEmpty() &&
                remainingTokens[0].type != TokenType.DO &&
                remainingTokens[0].type != TokenType.DONT
            ) {
                innerTokens.add(remainingTokens[0])
                remainingTokens = remainingTokens.drop(1)
            }

            val innerExpressions = parseTokens(innerTokens)

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
