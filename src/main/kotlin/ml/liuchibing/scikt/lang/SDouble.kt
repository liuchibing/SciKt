package ml.liuchibing.scikt.lang

class SDouble : SNumber {
    var value: Double = 0.0

    constructor(v: String) {
        value = v.toDouble()
    }

    constructor(v: Double) {
        value = v
    }

    operator fun plus(b: SDouble): SDouble {
        return SDouble(value + b.value)
    }

    override fun toDouble(): Double = value
}