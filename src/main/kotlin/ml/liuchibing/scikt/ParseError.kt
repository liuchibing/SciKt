package ml.liuchibing.scikt

class ParseError(val msg: String) : Exception() {
    override val message: String?
        get() = "Parse error: $msg"
}