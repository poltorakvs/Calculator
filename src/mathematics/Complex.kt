package mathematics

abstract class Numeric(): Base() {

    /**
     * String representation of the type of the object.
     */
    override val type: String
        get() = "Numeric"

    /**
     * Simplifies the object.
     */
    override fun simplify(): Numeric {
        return this
    }
}

class Complex(val real: Real, val imaginary: Real): Numeric() {

    override fun toString(): String {
        TODO("Not yet implemented")
    }

    override val calculated: Double
        get() = TODO("Not yet implemented")

    /**
     * String representation of the type of the object.
     */
    override val type: String
        get() = "Complex"

    /**
     * Simplifies the object.
     */
    override fun simplify(): Complex {
        TODO()
    }
}