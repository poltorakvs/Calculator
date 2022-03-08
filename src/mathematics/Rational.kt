package mathematics

import kotlin.math.*


/**
 * Abstract class, that unites all possible mathematical rational numbers.
 *
 * Derives [Base] class. Haven't any constructor.
*/
abstract class Rational: Real(), MRoot1 {

    /**
     * Returns string representation of the class type.
     */
    override val type: String
        get() = "Rational"

    abstract fun abs(): Rational

    abstract fun extractInteger(): Base

    abstract override fun factorization(): List<Rational>

    abstract override fun inverse(): Rational

    abstract override fun opposite(): Rational

    abstract fun pow(exp: Integer): Rational

    /**
     * Simplifies given object.
     */
    override fun simplify(): Rational {
        return this
    }

    open fun toInteger(): Rational {
        return this
    }

    abstract operator fun div(other: Fraction): Rational

    abstract operator fun div(other: Integer): Rational

    abstract operator fun div(other: Rational): Rational

    abstract operator fun minus(other: Fraction): Rational

    abstract operator fun minus(other: Integer): Rational

    abstract operator fun minus(other: Rational): Rational

    abstract operator fun plus(other: Fraction): Rational

    abstract operator fun plus(other: Integer): Rational

    abstract operator fun plus(other: Rational): Rational

    abstract operator fun times(other: Fraction): Rational

    abstract operator fun times(other: Integer): Rational

    abstract override operator fun times(other: Rational): Rational

    override operator fun times(other: ArithmRad): MRoot1 {
        return other * this
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
 * Representation of mathematical integer number.
 *
 * Derives [Rational] class.
 *
 * Can be created by using construction 'Integer(i)', where 'i' is Int.
 */
class Integer constructor(private val i: Int): Rational() {

    /**
     * Returns string representation of given number.
     */
    override fun toString(): String {
        return i.toString()
    }

    /**
     * Returns true if and only if other is also Integer and other value is the same as this value.
     */
    override fun equals(other: Any?): Boolean {
        return other is Integer && other.value == value
    }

    /**
     * Returns hash code value of the given object.
     */
    override fun hashCode(): Int {
        return this.toString().hashCode()
    }

    /**
     * Returns [Double] representation of value.
     */
    override val calculated: Double
        get() = i.toDouble()

    /**
     * Returns string representation of the class type.
     */
    override val type: String
        get() = "Integer"

    /**
     * Returns mathematical value of given number.
     *
     * Is equal to constructor parameter.
     */
    val value: Int
        get() = i

    /**
     * Returns the absolute value of the given number.
     */
    override fun abs(): Integer {
        return if (i >= 0) {
            this
        } else {
            Integer(-i)
        }
    }

    /**
     * Extracts integer part from the number.
     */
    override fun extractInteger(): Base {
        return this
    }
    /**
     * Factorize the given number.
     *
     * Returns [List] of all prime dividers.
     *
     * If given number is 0, return (0), otherwise return (1, ...) if given number > than 0, otherwise (-1, ...).
     *
     * The Great Arithmetic Theorem approves that there can be only one equal factorization.
     */
    override fun factorization(): List<Integer> {
        val res: MutableList<Integer> = mutableListOf()
        var n: Int = value

        if (n == 0) return listOf(Integer(0))
        if (n > 0) res.add(Integer(1))
        if (n < 0) res.add(Integer(-1))

        n = abs(n)

        if (Integer(n).isPrime()) {
            res.add(Integer(n))
            return res.toList()
        }

        var p = 2
        while (n > 1) {
            if (n % p != 0) {
                p++
                if (p > 3) {
                    p++
                }
            } else {
                res.add(Integer(p))
                n /= p
            }
        }

        return res.toList()
    }

    /**
     * Return the greatest common divider (GCD) of two given integer numbers.
     *
     * GCD of 0 and any number returns 1.
     *
     * The sign doesn't matter for GCD.
     */
    infix fun gcd(other: Integer): Integer {
        if (value == 0 || other.value == 0) {
            return Integer(1)
        }

        val thisAbs = this.abs()
        val otherAbs = other.abs()

        if (thisAbs.value == 1 || otherAbs.value == 1) {
            return Integer(1)
        } else if (thisAbs == otherAbs) {
            return thisAbs
        } else if (otherAbs.value % thisAbs.value != 0) {
            return thisAbs
        } else if (thisAbs.value % otherAbs.value != 0) {
            return otherAbs
        }

        val bool1 = thisAbs.isPrime()
        val bool2 = otherAbs.isPrime()
        if (bool1 && bool2 || bool1 && otherAbs.value % thisAbs.value != 0 || bool2 && thisAbs.value % otherAbs.value != 0) {
            return Integer(1)
        }

        var res = 1
        var mn = min(thisAbs.value, otherAbs.value)
        var mx = max(thisAbs.value, otherAbs.value)
        var p = 2
        while (mn > 1) {
            if (mn % p != 0 || mx % p != 0) {
                p++
                if (p > 3) {
                    p++
                }
            } else {
                res *= p
                mn /= p
                mx /= p
            }
        }

        return Integer(res)
    }

    /**
     * Returns the inverse of the given number.
     */
    override fun inverse(): Fraction {
        return if (this.isZero()) {
            Fraction(Integer(0), Integer(1))
        } else if (this.isPositive()) {
            Fraction(Integer(1), this)
        } else {
            Fraction(Integer(-1), this.opposite())
        }
    }

    /**
     * Returns true if and only if the number = -1
     */
    override fun isMinusOne(): Boolean {
        return value == -1
    }

    /**
     * Returns true if and only if the number = 1.
     */
    override fun isOne(): Boolean {
        return value == 1
    }

    /**
     * Returns true if and only if GCD of the numbers = 1.
     */
    infix fun isMutPrime(other: Integer): Boolean {
        return this gcd other == Integer(1)
    }

    /**
     * Returns true if and only if given number >= 0.
     */
    override fun isPositive(): Boolean {
        return value >= 0
    }

    /**
     * Returns true if and only if given integer number has only 2 integer dividers - 1 and itself.
     *
     * 0 and 1 isn't prime.
     *
     * The sign doesn't matter for prime.
     */
    fun isPrime(): Boolean {
        val n: Int = this.abs().value

        if (n <= 1) {
            return false
        } else if (n == 2 || n == 3) {
            return true
        } else if (!(n % 6 == 1 || n % 6 == 5)) {
            return false
        }

        for (f in 3..sqrt(n.toDouble()).toInt() step 2) {
            if (n % f == 0 && n != f) {
                return false
            }
        }

        return true
    }

    /**
     * Returns true if given number is equal to 0, otherwise false.
     */
    override fun isZero(): Boolean {
        return value == 0
    }

    /**
     * Returns the quantity of figures in the number.
     */
    fun length(): Int {
        return if (this.value < 0) {
            this.toString().length - 1
        } else {
            this.toString().length
        }
    }

    /**
     * Returns the opposite of the given number.
     */
    override fun opposite(): Integer {
        return Integer(-value)
    }

    /**
     * Raises a given integer number (base) in an integer power (exponent).
     *
     * Returns [Integer] if exponent >= 0, otherwise [Fraction].
     *
     * Exponent and base can't both = 0, if base = 0, exponent can't be < 0.
     */
    override fun pow(exp: Integer): Rational {
        if (value == 0 && exp.value == 0) {
            throw ZeroToZeroPowError("Can't use power operator on operands (0, 0)")
        } else if (value == 0 && !exp.isPositive()) {
            throw ZeroDivisionError("Can't use power operator on operands (0, a), where a < 0.")
        }

        return if (exp.value < 0) {
            Fraction(Integer(1), Integer(calculated.pow(exp.opposite().value).toInt()))
        } else if (exp.value == 0) {
            Integer(1)
        } else {
            Integer(calculated.pow(exp.value).toInt())
        }
    }

    /**
     * Returns the smallest common multiple (SCM) of two given integer number, not equal to 0.
     *
     * The sign doesn't matter for SCM.
     */
    infix fun scm(other: Integer): Integer {
        return this * (other / (this gcd other)) as Integer
    }

    /**
     * Simplifies given object.
     */
    override fun simplify(): Integer {
        return this
    }

    /**
     * Converts given integer number to [Fraction]. Returns n/1
     */
    fun toFraction(): Fraction {
        return Fraction(this, Integer(1))
    }

    /**
     * Overloads '/' operator on [Integer] and [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation division.
     *
     * Second operator can't be = 0.
     */
    override operator fun div(other: Fraction): Rational {
        if (other.isZero()) {
            throw ZeroDivisionError("Can't divide by 0")
        }

        return (this * other.inverse()).simplify()
    }

    /**
     * Overloads '/' operator on 2 [Integer] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation division.
     *
     * Second operator can't be = 0.
     */
    override operator fun div(other: Integer): Rational {
        if (other.isZero()) {
            throw ZeroDivisionError("Can't divide by 0")
        }

        return if (value % other.value == 0) {
            (value / other.value).toInteger()
        } else {
            Fraction(this, other).simplify()
        }
    }

    /**
     * Overloads '/' operator on [Integer] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation division.
     *
     * Second operator can't be = 0.
     */
    override operator fun div(other: Rational): Rational {
        if (other.isZero()) {
            throw ZeroDivisionError("Can't divide by 0")
        }

        return (this * other.inverse()).simplify()
    }

    /**
     * Overloads '-' operator on [Integer] and [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation subtraction.
     */
    override fun minus(other: Fraction): Rational {
        return (this + other.opposite()).simplify()
    }

    /**
     * Overloads '-' operator on 2 [Integer] objects.
     *
     * Representation of mathematical operation subtraction.
     */
    override fun minus(other: Integer): Integer {
        return this + other.opposite()
    }

    /**
     * Overloads '-' operator on [Integer] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation subtraction.
     */
    override operator fun minus(other: Rational): Rational {
        return (this + other.opposite()).simplify()
    }

    /**
     * Overloads '+' operator on [Integer] and [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation addition.
     */
    override fun plus(other: Fraction): Rational {
        return Fraction((value * other.denValue.value + other.numValue.value).toInteger(), other.denValue).simplify()
    }

    /**
     * Overloads '+' operator on 2 [Integer] objects.
     *
     * Representation of mathematical operation addition.
     */
    override fun plus(other: Integer): Integer {
        return (value + other.value).toInteger()
    }

    /**
     * Overloads '+' operator on [Integer] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation addition.
     */
     override operator fun plus(other: Rational): Rational {
        return if (other is Integer) {
            this + other
        } else {
            this + other as Fraction
        }
    }

    /**
     * Overloads '*' operator on [Integer] and [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation multiplication.
     */
    override fun times(other: Fraction): Rational {
        return Fraction(this * other.numValue, other.denValue).simplify()
    }

    /**
     * Overloads '*' operator on [Integer] and [Integer] objects.
     *
     * Representation of mathematical operation multiplication.
     */
    override fun times(other: Integer): Integer {
        return (value * other.value).toInteger()
    }


    /**
     * Overloads '*' operator on [Integer] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation multiplication.
     */
    override operator fun times(other: Rational): Rational {
        return if (other is Integer) {
            this * other
        } else {
            this * (other as Fraction)
        }
    }
}

/**
 * Representation of mathematical fraction number.
 *
 * Derives [Rational] class.
 *
 * Can be created by using construction 'Fraction(n, m)', where n - numerator is Integer, m - denominator is Integer.
 *
 * Denominator can't be 0.
 */
open class Fraction constructor(private val numer: Integer, private val denom: Integer): Rational() {

    init {
        if (denom.value == 0) {
            throw ZeroDivisionError("Fraction can't have zero in the denominator")
        }
    }

    /**
     * String representation of fraction.
     *
     * Example: Fraction(1, 2) -> 1/2
     */
    override fun toString(): String {
        return "$numer/$denom"
    }

    /**
     * Returns true if and only if other's parameters is equal to these parameters.
     */
    override fun equals(other: Any?): Boolean {
        return other is Fraction && other.numValue == numValue && other.denValue == denValue
    }

    /**
     * Returns hash code value of the given object.
     */
    override fun hashCode(): Int {
        return this.toString().hashCode()
    }

    /**
     * Returns [Double] representation of value.
     */
    override val calculated: Double
        get() = numValue.calculated / denValue.calculated

    /**
     * Returns the denominator of the fraction.
     *
     * Is equal to constructor second operand.
     *
     * Example: Fraction(3, 6) -> 6, not 2
     */
    open val denValue: Integer
        get() = denom

    /**
     * Returns the numerator of the fraction.
     *
     * Is equal to constructor first operand.
     *
     * Example: Fraction(3, 6) -> 3, not 1
     */
    open val numValue: Integer
        get() = numer

    /**
     * Returns string representation of the class type.
     */
    override val type: String
        get() = "Fraction"

    /**
     * Returns the absolute value of a given fraction, but doesn't simplify it.
     */
    override fun abs(): Fraction {
        return if (!this.isPositive()) {
            Fraction(numValue.abs(), denValue.abs())
        } else {
            this
        }
    }

    /**
     * Extracts an integer part from the given fraction.
     *
     * Can return [Add] (if fraction is incorrect), [Fraction] (if fraction is correct) or [Integer] (if fraction is equal to integer).
     */
    override fun extractInteger(): Base {
        return if (numer.value % denom.value == 0) {
            Integer(numer.value / denom.value)
        } else  if (numer.value > denom.value) {
            Add(Integer(numer.value / denom.value), Fraction(Integer(numer.value % denom.value), Integer(denom.value)))
        } else {
            this
        }
    }

    /**
     * Simplify and then factorize given fraction. Example: -4/-14 -> listOf(1, 2, 1/7)
     */
    override fun factorization(): List<Rational> {
        val simple = this.simplify()
        val res: MutableList<Rational> = simple.numValue.factorization().toMutableList()
        val denFact = simple.denValue.factorization().toMutableList()

        denFact.remove(Integer(1))
        denFact.remove(Integer((-1)))
        denFact.map {
            res.add(it.inverse())
        }

        return res
    }

    /**
     * Returns the inverse of the number.
     */
    override fun inverse(): Fraction {
        return Fraction(denValue, numValue)
    }

    /**
     * Returns true if and only if the fraction = -1
     */
    override fun isMinusOne(): Boolean {
        return numValue == denValue.opposite()
    }

    /**
     * Returns true if and only if the fraction = 1
     */
    override fun isOne(): Boolean {
        return numValue == denValue
    }

    /**
     * Returns true if and only if given number >= 0.
     */
    override fun isPositive(): Boolean {
        return (numValue * denValue).isPositive()
    }

    /**
     * Returns true if and only if given fraction is equal to 0.
     */
    override fun isZero(): Boolean {
        return numValue.value == 0
    }

    /**
     * Returns the opposite of the given fraction.
     */
    override fun opposite(): Fraction {
        return if (this.isPositive()) {
            Fraction(numValue.abs().opposite(), denValue.abs())
        } else {
            Fraction(numValue.abs(), denValue.abs())
        }
    }

    /**
     * Raises a given integer number in an integer power.
     *
     * Returns [Integer] = 1 if exponent = 0, otherwise [Fraction].
     *
     * Exponent and base can't both = 0, if base = 0, exponent can't be < 0.
     */
    override fun pow(exp: Integer): Rational {
        if (!exp.isPositive() && numValue.value == 0) {
            throw ZeroDivisionError("Can't raise zero in negative power")
        } else if (exp.value == 0 && numValue.value == 0) {
            throw ZeroToZeroPowError("Can't use power operator on operands (0, 0)")
        }

        val thisSimple = this.simplify()
        val extractedThis = thisSimple.extractInteger()
        if (extractedThis is Integer) {
            return extractedThis.pow(exp)
        }

        val absExp = exp.abs()
        val powNum = numValue.pow(absExp) as Integer
        val powDen = denValue.pow(absExp) as Integer
        return if (exp.isZero()) {
            Integer(1)
        } else if (exp.value > 0) {
            Fraction(powNum, powDen)
        } else {
            Fraction(powDen, powNum)
        }
    }

    /**
     * Shorten given fraction.
     *
     * Always returns fraction.
     *
     * If fraction is equal to 0, returns Fraction(0, 1).
     *
     * Denominator always > 0, numerator can be any integer.
     */
    override fun simplify(): Fraction {
        if (numValue.value == 0) {
            return Fraction(Integer(0), Integer(1))
        }

        val greatDivider = numValue gcd denValue
        var ni: Integer = (numValue.abs().value / greatDivider.value).toInteger()
        var di: Integer = (denValue.abs().value / greatDivider.value).toInteger()

        if (!this.isPositive()) {
            ni = ni.abs().opposite()
            di = di.abs()
        }

        return Fraction(ni, di)
    }

    /**
     * Converts the [Fraction] to [Integer], if it is possible.
     */
    override fun toInteger(): Rational {
        return numValue / denValue
    }

    /**
     * Overloads '/' operator on 2 [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation division.
     *
     * Second operator can't be = 0.
     */
    override operator fun div(other: Fraction): Rational {
        return this * other.inverse()
    }

    /**
     * Overloads '/' operator on [Fraction] and [Integer] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation division.
     *
     * Second operator can't be = 0.
     */
    override operator fun div(other: Integer): Rational {
        return this * other.inverse()
    }

    /**
     * Overloads '/' operator on [Fraction] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation division.
     *
     * Second operator can't be = 0.
     */
    override operator fun div(other: Rational): Rational {
        return this * other.inverse()
    }

    /**
     * Overloads '-' operator on 2 [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation subtraction.
     */
    override operator fun minus(other: Fraction): Rational {
        return this + other.opposite()
    }

    /**
     * Overloads '-' operator on [Fraction] and [Integer] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation subtraction.
     */
    override operator fun minus(other: Integer): Rational {
        return this + other.opposite()
    }

    /**
     * Overloads '-' operator on [Fraction] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation subtraction.
     */
    override operator fun minus(other: Rational): Rational {
        return this + other.opposite()
    }

    /**
     * Overloads '+' operator on 2 [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation addition.
     */
    override operator fun plus(other: Fraction): Rational {
        val den = denValue scm other.denValue
        return Fraction(numValue * ((den / denValue) as Integer) + other.numValue * ((den / other.denValue) as Integer), den).simplify().toInteger()
    }

    /**
     * Overloads '+' operator on [Fraction] and [Integer] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation addition.
     */
    override operator fun plus(other: Integer): Rational {
        return (other + this).simplify().toInteger()
    }

    /**
     * Overloads '+' operator on [Fraction] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation addition.
     */
    override operator fun plus(other: Rational): Rational {
        return if (other is Integer) {
            this + other
        } else {
            this + other as Fraction
        }
    }

    /**
     * Overloads '*' operator on 2 [Fraction] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation multiplication.
     */
    override operator fun times(other: Fraction): Rational {
        return Fraction(numValue * other.numValue, denValue * other.denValue).simplify().toInteger()
    }

    /**
     * Overloads '*' operator on [Fraction] and [Integer] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation multiplication.
     */
    override operator fun times(other: Integer): Rational {
        return (other * this).simplify().toInteger()
    }

    /**
     * Overloads '*' operator on [Fraction] and [Rational] objects. If returns [Fraction] simplifies it.
     *
     * Representation of mathematical operation multiplication.
     */
    override operator fun times(other: Rational): Rational {
        return if (other is Integer) {
            this * other
        } else {
            this * (other as Fraction)
        }
    }
}


/**
 * Representation of mathematical non-periodic float numbers.
 *
 * Derives [Rational] class.
 *
 * Can't be used, because after creating is always converting to [Fraction] or [Integer] or calculating to [Double]
 *
 * Can be created by construction 'Float(b, nq, fp)', where b - before dot and fp - after dot is [Integer], nq - nulls after dot is [Int]. Example: Float(1, 3, 1) <=> 1.0001, Float(0, 0, 25) <=> 0.25.
 *
 * Nq and fp are always >= 0.
 */
open class Float constructor(private val base: Integer, private val nullQuantity: Int, private val fractionPart: Integer): Base() {

    init {
        if (nullQuantity < 0) {
            throw NegativeQuantityError("Float number can't have $nullQuantity nulls after dot")
        } else if (!fractionPart.isPositive()) {
            throw NegativeNumberError("Float number can't have negative part after dot")
        }

    }

    /**
     * String representation of the object. Is equal to string representation of calculated [Double].
     */
    override fun toString(): String {
        return calculated.toString()
    }

    /**
     * Returns approximated value for a given float.
     */
    val approximated: Fraction
        get() = approximate(calculated)

    /**
     * Calculate given float. Returns double.
     */
    override val calculated: Double
        get() {
            val nulls = "0" * nullQuantity
            return "$base.$nulls$fractionPart".toDouble()
        }

    /**
     * Convert Float to [Fraction]. If float is equal to integer, returns [Integer].
     */
    fun toFraction(): Rational {
        if (fractionPart.value == 0) {
            return base
        }

        var simplifiedFractionPart = fractionPart
        while (simplifiedFractionPart.value % 10 == 0) {
            simplifiedFractionPart = Integer(simplifiedFractionPart.value / 10)
        }

        val fractionPartLen = simplifiedFractionPart.length() + nullQuantity
        val k = Integer(10).pow(Integer(fractionPartLen)) as Integer

        return Fraction(buildInteger(base.toString(), simplifiedFractionPart.toString()), k).simplify()
    }
}

/**
 * Representation of mathematical periodic float numbers.
 *
 * Derives [Rational] class.
 *
 * Can't be used, because after creating is always converting to [Fraction] or [Integer] or calculating to [Double]
 *
 * Can be created by construction 'PeriodicFloat(b, nq, npp, pp)', where b - before dot, pp - periodic part after dot and npp - non-periodic part after dot is [Integer], nq - nulls after dot is [Int]. Example: PeriodicFloat(1, 3, 1, 3) <=> 1.0001(3), PeriodicFloat(0, 0, 25, 13) <=> 0.25(13).
 *
 * Nq, npp and pp are always >= 0.
 */
open class PeriodicFloat constructor(private val base: Integer, private val nullQuantity: Int, private val npp: Integer, private val pp: Integer): Base() {

    init {
        if (nullQuantity < 0) {
            throw NegativeQuantityError("Periodic float number can't have $nullQuantity nulls after dot")
        } else if (!npp.isPositive()) {
            throw NegativeNumberError("Periodic float number can't have negative non-periodic part after dot")
        } else if (!pp.isPositive()) {
            throw NegativeNumberError("Periodic float number can't have negative periodic part after dot")
        }
    }
    /**
     * String representation of the object. Is equal to string representation of calculated [Double].
     */
    override fun toString(): String {
        return calculated.toString()
    }

    /**
     * Returns approximated value for a given float.
     */
    val approximated: Fraction
        get() = approximate(calculated)

    /**
     * Calculate given float. Returns double.
     */
    override val calculated: Double
        get() {
            val nulls = "0" * nullQuantity
            val period = "$pp" * (20 / pp.length())
            return "$base.$nulls$npp$period".toDouble()
        }

    /**
     * Convert float to [Fraction]. If float is equal to integer, returns [Integer].
     *
     * Periodic floats is converting to fraction using next algorithm. Example: x = 1.12(123) => 100x = 112.(123); 100000x = 112123.(123) => 99900x = 112011 <=> x = 112011/99900 <=> x = 37337/33300.
     *
     *
     */
    fun toFraction(): Rational {
        if (pp.value == 0) {
            return Float(base, nullQuantity, npp).toFraction()
        }

        val len = npp.length() + nullQuantity
        val firstMul = Integer(10).pow(Integer(len)) as Integer
        val secondMul = Integer(10).pow(Integer(len + pp.length())) as Integer
        val nulls = "0" * nullQuantity

        return Fraction(buildLong(base.toString(), nulls, npp.toString(), pp.toString()).minus(buildLong(base.toString(), nulls, npp.toString())).toInteger(), secondMul - firstMul).simplify()
    }
}

fun main() {

    //val set = setOf(Integer(4), Integer(5), Integer(6))
    //val otherSet = setOf(Integer(5), Integer(6), Integer(7))
}