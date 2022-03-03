package mathematics

/**
 * Abstract class, that unites all possible types.
 *
 * Haven't any constructor.
 */
abstract class Base {

    abstract override fun toString(): String

    abstract val calculated: Double

    open val type: String
        get() = "Base"

    open fun simplify(): Base {
        return this
    }
}

fun main() {
    println((10).toString(2))
}