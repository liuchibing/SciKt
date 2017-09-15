package ml.liuchibing.scikt

import ml.liuchibing.scikt.lang.SDouble
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainTest {
    lateinit var interpreter: Interpreter
    @Before fun init() {
        interpreter = Interpreter.new()
    }

    @Test fun testCalc() {
        Assert.assertEquals((interpreter.interpret("1 + 1") as SDouble).toDouble(), 2)
        Assert.assertEquals((interpreter.interpret("1+1") as SDouble).toDouble(), 2)
        Assert.assertEquals((interpreter.interpret("1 +1") as SDouble).toDouble(), 2)
        Assert.assertEquals((interpreter.interpret("1+ 1") as SDouble).toDouble(), 2)
    }
}