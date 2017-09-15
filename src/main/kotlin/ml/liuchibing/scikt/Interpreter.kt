package ml.liuchibing.scikt

import ml.liuchibing.scikt.lang.SObject
import java.io.InputStream
import java.io.OutputStream

/**
 * Interpreter of A Scilab-like script language -- SciKt.
 * @author Liu Chibing
 */
abstract class Interpreter {

    protected val variables = mutableMapOf<String, SObject>()

    /**
     * Interpret the input as a InputStream. And write stdout and stderr to the given OutputStream.
     * @return The result of the input. If the input has more than one expression inside, the result of the last expression will be returned.
     */
    abstract fun interpret(input: InputStream, output: OutputStream = System.out, error: OutputStream = System.err): SObject

    /**
     * Interpret the input as a String. And write stdout and stderr to the given OutputStream.
     * @return The result of the input. If the input has more than one expression inside, the result of the last expression will be returned.
     */
    abstract fun interpret(input: String, output: OutputStream = System.out, error: OutputStream = System.err): SObject

    /**
     * Clear all variables declared by script.
     */
    fun clear() {
        variables.clear()
    }

    companion object {
        /**
         * Create an instance of the default implementation of the interpreter.
         */
        @JvmStatic fun new(): Interpreter = InterpreterImpl()
    }
}