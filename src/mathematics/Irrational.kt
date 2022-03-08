package mathematics

import kotlin.math.pow


abstract class Real: Numeric() {

    /**
     * String representation of the type of the object.
     */
    override val type: String
        get() = "Real"

    abstract fun factorization(): List<Numeric>

    abstract fun inverse(): Real

    abstract fun isMinusOne(): Boolean

    abstract fun isOne(): Boolean

    /**
     * !!!If the object is equal to 0, also returns true (so maybe it would be better, if function name was 'isNotNegative' or 'isPositiveOrZero').
     */
    abstract fun isPositive(): Boolean

    abstract fun isZero(): Boolean

    abstract fun opposite(): Real

    /**
     * Simplifies the object.
     */
    override fun simplify(): Numeric {
        return this
    }

    /**
     * Converts the object to the [Rational] object, if it is possible.
     */
    open fun toRational(): Real {
        return this
    }
}


/**
 * Abstract class, that unites all needed mathematical irrational numbers.
 *
 * Derives [Base] class. Haven't any constructor.
 */
abstract class Irrational: Real() {

    abstract val approximated: Fraction

    /**
     * String representation of an object type.
     */
    override val type: String
        get() = "Irrational"

    override fun simplify(): Irrational {
        return this
    }
}

/**
 * Representation of mathematical arithmetical radicals.
 *
 * Derives [Irrational] class.
 *
 * Can be created by using constructor 'ArithmRad(b, e)', where b - base and e - exponent are Rational.
 *
 * Exponent can't be 0, exponent can't be negative if base is 0 and base can't be negative if exponent is even.
 */
class ArithmRad(private val base: Rational, private val exp: Rational, private val character: Boolean = true): Irrational(), MRoot1 {

    init {
        if (exp.isZero()) {
            throw ZeroRadicalError("Arithmetical radical can't have zero as exponent")
        } else if (!exp.isPositive() && base.isZero()) {
            throw ZeroDivisionError("Zero can't be raised in negative power")
        } else if (!base.isPositive() && (exp is Fraction && exp.numValue.value % 2 == 0 || exp is Integer && exp.value % 2 == 0)) {
            throw ComplexNumberError("Arithmetical radical with even exponent can only have positive or zero base")
        }
    }

    /**
     * String representation of the radical. Example: ArithmRad(2, 3) -> (3)⎷2, but ArithmRad(2, 2) -> ⎷2.
     */
    override fun toString(): String {
        return if (exp == Integer(2)) {
            if (sign) {
                "⎷$base"
            } else {
                "-⎷$base"
            }
        } else {
            if (sign) {
                "($exp)⎷$base"
            } else {
                "-($exp)⎷$base"
            }
        }
    }

    /**
     * Returns true if and only if all other's parameters is equal to these parameters.
     */
    override fun equals(other: Any?): Boolean {
        return other is ArithmRad && other.baseValue == baseValue && other.expValue == expValue
    }

    /**
     * Returns hash code value to a given object.
     */
    override fun hashCode(): Int {
        return this.toString().hashCode()
    }

    /**
     * Returns approximated value to given irrational value.
     */
    override val approximated: Fraction
        get() = approximate(calculated)

    /**
     * Returns base of the radical.
     *
     * Is equal to constructor's base.
     */
    val baseValue: Rational
        get() = base.simplify()

    /**
     * Calculate [Double] value of the radical.
     */
    override val calculated: Double
        get() {
            val simple = this.simplify()
            val baseSimple = simple.baseValue
            val expSimple = simple.expValue as Integer
            val powExp = Fraction(Integer(1), expSimple).calculated
            return baseSimple.calculated.pow(powExp)
        }

    val sign: Boolean
        get() = character

    /**
     * Returns exponent of the radical.
     *
     * Is equal to constructor's exponent.
     */
    val expValue: Rational
        get() = exp.simplify()

    /**
     * String representation of the type of object.
     */
    override val type: String
        get() = "ArithmRad"

    /**
     * Extracts all rational number from the base. Given radical must be simplified.
     *
     * Can return [Multiply] (if radical base [factorization] has more than 1, but less than all prime divider in power > exponent), [ArithmRad] (if there aren't any prime dividers fits previous condition) and [Rational] (if all prime dividers are in power that divisible to exponent).
     *
     * If returns multiply, first operand is always extracted and second is always radical.
     */
    fun extractBase(): Base {
        val simpleExp = expValue as Integer

        when {
            simpleExp.value == 1 -> return baseValue
            baseValue.isZero() -> return Integer(0)
            baseValue.isOne() -> return Integer(1)
        }

        val factorList = baseValue.factorization()
        if (factorList == factorList.toSet().toList()) {
            return this
        }

        val toDelete: MutableList<Rational> = mutableListOf()
        var result: Rational = Fraction(Integer(1), Integer(1))

        for (factor in factorList.toSet()) {
            if (!factor.abs().isOne()) {
                val q = factorList.count(factor)
                if (q >= simpleExp.value) {
                    val multiplier = q % simpleExp.value
                    toDelete.addAll(List(multiplier) {factor} )
                    result *= factor.pow(Integer(multiplier))
                }
            }
        }
        val newBase: Rational = baseValue / result.pow(simpleExp)

        if (newBase.isOne() && sign) {
            return result
        } else if (newBase.isOne() && !sign) {
            return result.opposite()
        } else if (result.isOne()) {
            return this
        }

        if (!sign) {
            result = result.opposite()
        }

        return Multiply(result, ArithmRad(newBase, simpleExp))
    }

    /**
     * Factorize the radical. It should be simplified. If the given radical has sign == false, sign of first radical will be false. Example: ⎷12 -> (⎷2, ⎷2, ⎷3)
     */
    override fun factorization(): List<ArithmRad> {
        val res: MutableList<ArithmRad> = mutableListOf()
        for (factor in baseValue.factorization()) {
            res.add(ArithmRad(factor, expValue))
        }
        if (!sign) {
            res[0] = res[0].opposite()
        }

        return res
    }

    override fun inverse(): ArithmRad {
        return ArithmRad(base.inverse(), exp, sign)
    }

    override fun isMinusOne(): Boolean {
        return base.isMinusOne() || base.isOne() && !sign
    }

    /**
     * Returns true if and only if radical is equal to 1.
     */
    override fun isOne(): Boolean {
        return base.isOne() && sign || base.isMinusOne() && !sign
    }

    override fun isPositive(): Boolean {
        return sign == base.isPositive()
    }

    /**
     * Returns true if and only if radical is equal to 0.
     */
    override fun isZero(): Boolean {
        return base.isZero()
    }

    override fun opposite(): ArithmRad {
        return ArithmRad(base, exp, !sign)
    }

    /**
     * Simplifies given radical. Simplified radical exponent is always [Integer] and never < 0, also simplifies base (if it is possible). Also base is always positive.
     */
    override fun simplify(): ArithmRad {
        var texp = expValue.simplify()
        var tbase = baseValue.simplify()

        when {
            baseValue.isOne() -> {
                return ArithmRad(Integer(1), Integer(1))
            }
            baseValue.isZero() -> {
                return ArithmRad(Integer(0), Integer(1))
            }
            baseValue.isMinusOne() -> {
                return ArithmRad(Integer(1), Integer(1), false)
            }
            !texp.isPositive() -> {
                texp = texp.opposite()
                tbase = tbase.inverse()
            }

        }

        /*return if (texp is Fraction) {
            if (texp.denValue.isOne()) {
                ArithmRad(tbase, texp.extractInteger() as Integer)
            } else {
                ArithmRad(tbase.pow(texp.denValue), texp.numValue)
            }
        } else {
            ArithmRad(tbase, texp as Integer)
        }

         */
        val s = tbase.isPositive()
        tbase = tbase.abs()
        return if (texp is Fraction) {
            tbase = tbase.pow(texp.denValue)
            texp = texp.numValue
            if (!texp.isPositive()) {
                tbase = tbase.inverse()
                texp = texp.opposite()
            }
            ArithmRad(tbase, texp, s)
        } else {
            if (!texp.isPositive()) {
                tbase = tbase.inverse()
                texp = texp.opposite()
            }
            ArithmRad(tbase, texp, s)
        }
    }

    /**
     * Converts the radical to rational number, if it is possible.
     */
    override fun toRational(): Real {
        val extracted = this.extractBase()
        return if (extracted is Rational) {
            extracted
        } else {
            this
        }
    }

    /**
     * Multiplies given radicals.
     */
    override operator fun times(other: ArithmRad): MRoot1 {
        if (other.isZero()) {
            return Integer(0)
        } else if (other.isOne()) {
            return this
        } else if (other.isMinusOne()) {
            return this.opposite()
        }

        val thisRad = this.simplify()
        val otherRad = other.simplify()
        val newExp = thisRad.exp as Integer scm otherRad.exp as Integer
        val newBase = thisRad.base.pow((newExp / thisRad.exp) as Integer) * otherRad.base.pow((newExp / otherRad.exp) as Integer)
        val newSign = thisRad.sign == otherRad.sign
        val res = ArithmRad(newBase, newExp, newSign).simplify()
        val extracted = res.extractBase()
        return if (extracted is Rational) {
            extracted
        } else {
            ArithmRad(newBase, newExp, newSign).simplify()
        }
    }

    override operator fun times(other: Rational): MRoot1 {
        if (other.isZero()) {
            return Integer(0)
        } else if (other.isOne()) {
            return this
        } else if (other.isMinusOne()) {
            return this.opposite()
        }

        val thisRad = this.simplify()
        val otherSign = other.isPositive()
        val multiplier = other.simplify().abs()
        val newRad = ArithmRad(thisRad.base * multiplier.pow(thisRad.exp as Integer), thisRad.exp, otherSign == thisRad.sign).simplify()
        val extracted = newRad.extractBase()
        return if (extracted is Rational) {
            extracted
        } else {
            newRad
        }
    }

    override operator fun times(other: MRoot1): MRoot1 {
        return if (other is Rational) {
            this * other
        } else {
            this * other as ArithmRad
        }
    }
}

/**
 * Representation of mathematical constant, like π or e.
 */
abstract class Constant: Irrational() {

    override val type: String
        get() = "Constant"


}


/**
 * Fundamental mathematical constant equals to the ratio of the circumference of every circle to its radius. 1 radian (in trigonometry) = 180 / PI degrees.
 */
class PI: Constant() {

    /**
     * Returns approximated value to PI.
     */
    override val approximated: Fraction
        get() = approximate(calculated)

    /**
     * String representation of the type of the object.
     */
    override val type: String
        get() = "PI"

    /**
     * String representation of PI - "π".
     */
    override fun toString(): String {
        return "π"
    }

    /**
     * Returns calculated value to PI - 3.141592653589793.
     */
    override val calculated: Double
        get() = kotlin.math.PI

    /**
     * Returns (PI). Needs to factorize add.
     */
    override fun factorization(): List<PI> {
        return listOf(PI())
    }

    override fun inverse(): Real {
        TODO("Not yet implemented")
    }

    override fun isMinusOne(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isOne(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPositive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isZero(): Boolean {
        TODO("Not yet implemented")
    }

    override fun opposite(): Real {
        TODO("Not yet implemented")
    }
}

/**
 * Fundamental mathematical constant - Euler's number, equal to the limit of (1 + 1/n)^n with n -> infinity. Derivative by x of f(x) = E^x is E^x. E is the base of the natural logarithm.
 */
class E: Constant() {

    /**
     * Returns approximated value to E.
     */
    override val approximated: Fraction
        get() = approximate(calculated)

    /**
     * String representation of the type of the object.
     */
    override val type: String
        get() = "E"

    /**
     * String representation of euler's number - "e".
     */
    override fun toString(): String {
        return "e"
    }

    /**
     * Returns calculated value to E - 2.718281828459045.
     */
    override val calculated: Double
        get() = kotlin.math.E

    /**
     * Returns (E). Needs to factorize add.
     */
    override fun factorization(): List<E> {
        return listOf(E())
    }

    override fun inverse(): Real {
        TODO("Not yet implemented")
    }

    override fun isMinusOne(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isOne(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPositive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isZero(): Boolean {
        TODO("Not yet implemented")
    }

    override fun opposite(): Real {
        TODO("Not yet implemented")
    }
}




fun main() {
    val a = mutableListOf(1, 2, 3, 4)
    a.remove(5)
    println(a)
}