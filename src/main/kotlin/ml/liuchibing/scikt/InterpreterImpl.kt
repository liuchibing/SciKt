package ml.liuchibing.scikt

import ml.liuchibing.scikt.lang.SDouble
import ml.liuchibing.scikt.lang.SNumber
import ml.liuchibing.scikt.lang.SObject
import ml.liuchibing.scikt.lang.SVoid
import java.io.*

internal class InterpreterImpl : Interpreter() {

    private var stdin: BufferedReader? = null
    private var stdout: BufferedWriter? = null
    private var stderr: BufferedWriter? = null

    private fun nextToken(): Token {
        val sb = StringBuilder()
        var matched = false
        var matchedType: TokenType? = null
        var dontStopUntil: Char? = null
        //Read one char from input every loop.
        findMatch@ while (true) {
            val ch: Char = stdin!!.read().let { if (it != -1) it.toChar() else throw EOFException() }
            sb.append(ch)
            if (dontStopUntil != null && ch != dontStopUntil) {
                continue@findMatch
            }
            //Find whether the current string can be a token.
            for (item in TokenType.values()) {
                if (item.mode.matches(sb)) {
                    //then mark the position and continue to ensure this is the end of a token.(To check whether the string is still matched after append another char.)
                    stdin!!.mark(1)
                    matched = true
                    matchedType = item
                    continue@findMatch
                }
            }
            if (matched && matchedType != null) {
                //reset the input and return the token.
                if (ch != ' ' || ch != '\t') {
                    stdin!!.reset()
                } // else just discard it.
                sb.deleteCharAt(sb.length - 1)
                val tokenType = matchedType
                return Token(tokenType, sb.toString())
            }
        }
    }

    private fun eval(tokens: MutableList<Token>, signature: CharSequence): SObject {
        for (expr in exprPatterns) {
            if (expr.matches(signature)) {
                val start = expr.find(signature)!!.range.first
                return SDouble(tokens[start].value) + SDouble(tokens[start + 4].value)
            }
        }
        throw ParseError("Unknown token!")
    }

    private fun next(): SObject {
        val tokens = mutableListOf<Token>()
        val signature = StringBuilder()
        while (true) {
            val next = nextToken()
            tokens[signature.length] = next
            signature.append(next.type)
            if (next.type == TokenType.ENDLINE) break
        }
        return eval(tokens, signature)
    }

    private fun interpret(): SObject {
        var result: SObject = SVoid()
        try {
            while (true) {
                result = next()
            }
        } catch(e: EOFException) {
            return result
        }
    }

    override fun interpret(input: InputStream, output: OutputStream, error: OutputStream): SObject {
        stdin = BufferedReader(InputStreamReader(input))
        stdout = BufferedWriter(OutputStreamWriter(output))
        stderr = BufferedWriter(OutputStreamWriter(error))

        val result = interpret()

        stdin = null
        stdout = null
        stderr = null

        return result
    }

    override fun interpret(input: String, output: OutputStream, error: OutputStream): SObject {
        return interpret(ByteArrayInputStream(input.toByteArray()))
    }

    private enum class TokenType(val symbol: String, val mode: Regex) {
        NUMBER("n", "^[0-9]+$|^0x[0-9a-fA-F]+$".toRegex()),
        BOOL("b", "^true$|^false$".toRegex()),
        OPERATOR_ADD("Oa;", "^\\+$".toRegex()),
        OPERATOR_EQUAL("Oe;", "^==$".toRegex()),

        IDENTIFIER("i", "^[a-zA-Z_][0-9a-zA-Z_]*$".toRegex()),
        ENDLINE("e", "^\\n$|^;$".toRegex());

        override fun toString(): String = symbol
    }

    private data class Token(val type: TokenType, val value: String)

    private val exprPatterns = listOf<Regex>(
            "(n)Oa;(n)".toRegex()
    )

}