package mathematics

import junit.framework.TestCase
import org.junit.jupiter.api.Assertions.assertThrows

class IntegerTest: TestCase() {

    fun testFactorization() {
        val int0 = Integer(0)
        val int1 = Integer(1)
        val intNeg1 = Integer(-1)
        val int2 = Integer(2)
        val int3 = Integer(3)

        assertEquals(listOf(int0), int0.factorization())
        println(1)
        assertEquals(listOf(int1), int1.factorization())
        println(2)
        assertEquals(listOf(intNeg1), intNeg1.factorization())
        println(3)
        assertEquals(listOf(intNeg1, int3), Integer(-3).factorization())
        println(4)
        assertEquals(listOf(int1, int2, int2, int3, int3), Integer(36).factorization())
        println(5)
        assertEquals(listOf(intNeg1, Integer(7), Integer(11), Integer(13)), Integer(-1001).factorization())
        println(6)
        assertEquals(listOf(int1, Integer(317)), Integer(317).factorization())
        println(7)
    }

    fun testIsMutPrime() {
        assertEquals(true, Integer(0).isMutPrime(Integer(18)))
        println(1)
        assertEquals(true, Integer(2).isMutPrime(Integer(-7)))
        println(2)
        assertEquals(false, Integer(-36).isMutPrime(Integer(82)))
        println(3)
        assertEquals(true, Integer(-14).isMutPrime(Integer(-65)))
        println(4)
        assertEquals(false, Integer(1000000).isMutPrime(Integer(5)))
        println(5)
        assertEquals(true, Integer(1).isMutPrime(Integer(-7)))
        println(6)
    }

    fun testIsPrime() {
        assertEquals(false, Integer(0).isPrime())
        println(1)
        assertEquals(false, Integer(1).isPrime())
        println(2)
        assertEquals(false, Integer(-1).isPrime())
        println(3)
        assertEquals(true, Integer(317).isPrime())
        println(4)
        assertEquals(true, Integer(2).isPrime())
        println(5)
        assertEquals(true, Integer(3).isPrime())
        println(6)
        assertEquals(false, Integer(9).isPrime())
        println(7)
        assertEquals(false, Integer(10000).isPrime())
        println(8)
    }

    fun testDiv() {
        assertEquals(Integer(2), Integer(8) / Integer(4))
        println(1)
        assertEquals(Integer(8), Integer(8) / Integer(1))
        println(2)
        assertEquals(Integer(0), Integer(0) / Integer(45297))
        println(3)
        assertEquals(Fraction(Integer(5), Integer(3)), Integer(15) / Integer(9))
        println(4)
        assertEquals(Fraction(Integer(13), Integer(48)), Integer(1001) / Integer(3696))
        println(5)
        assertThrows(ZeroDivisionError::class.java) {Integer(0) / Integer(0)}
        println(6)
        assertThrows(ZeroDivisionError::class.java) {Integer(10) / Integer(0)}
        println(7)
    }

    fun testLength() {
        assertEquals(3, Integer(456).length())
        println(1)
        assertEquals(5, Integer(10000).length())
        println(2)
        assertEquals(1, Integer(0).length())
        println(3)
    }

    fun testPow() {
        val int1 = Integer(1)
        assertEquals(Integer(8), Integer(2).pow(Integer(3)))
        println(1)
        assertEquals(Fraction(int1, Integer(27)), Integer(3).pow(Integer(-3)))
        println(2)
        assertEquals(int1, Integer(1).pow(Integer(1000000)))
        println(3)
        assertEquals(int1, Integer(1000000).pow(Integer(0)))
        println(4)
        assertThrows(ZeroToZeroPowError::class.java) {Integer(0).pow(Integer(1))}
        println(5)
    }
}

class FractionTest: TestCase() {

    fun testInit() {
        assertThrows(ZeroDivisionError::class.java) {Fraction(Integer(1000), Integer(0))}
        println(1)
    }

    fun testSimplify() {
        assertEquals(Fraction(Integer(0), Integer(1)), Fraction(Integer(0), Integer(100000000)).simplify())
        println(1)
        assertEquals(Fraction(Integer(1), Integer(1)), Fraction(Integer(-547839), Integer(-547839)).simplify())
        println(2)
        assertEquals(Fraction(Integer(-229), Integer(8)), Fraction(Integer(458), Integer(-16)).simplify())
        println(3)
        assertEquals(Fraction(Integer(65), Integer(99)), Fraction(Integer(65), Integer(99)).simplify())
        println(4)

    }

    fun testExtractInteger() {
        assertEquals(Integer(-1), Fraction(Integer(-9), Integer(9)).extractInteger())
        println(1)
        assertEquals(Integer(5), Fraction(Integer(35), Integer(7)).extractInteger())
        println(2)
        assertEquals(Add(Integer(1), Fraction(Integer(4), Integer(7))), Fraction(Integer(11), Integer(7)).extractInteger())
        println(3)
        assertEquals(Integer(0), Fraction(Integer(0), Integer(-9999999)).extractInteger())
        println(4)
    }

    fun testPow() {
        TODO()
    }
}

class FloatTest : TestCase() {

    fun testInit() {
        assertThrows(NegativeQuantityError::class.java) { Float(Integer(0), -1, Integer(0)) }
    }

    fun testSimplify() {
        assertEquals(Integer(0), Float(Integer(0), 1000, Integer(0)).simplify())
        println(1)
        assertEquals(Integer(-150), Float(Integer(-150), 100000, Integer(0)).simplify())
        println(2)
        assertEquals(Fraction(Integer(-35982), Integer(1000)), Float(Integer(-35), 0, Integer(982)).simplify())
        println(3)
        assertEquals(Fraction(Integer(156), Integer(10000000)), Float(Integer(0), 4, Integer(156)).simplify())
        println(4)
    }
}

class PeriodicFloatTest: TestCase() {

    fun testInit() {
        //assertThrows(NegativeQuantityError::class) { PeriodicFloat(Integer(0), -1, Integer(0), Integer(0)) }
    }

    fun testSimplify() {
        TODO()
    }
}

class FunctionsTest: TestCase() {

    fun intersectTest() {
        TODO()
    }

    fun StringTest() {
        TODO()
    }

    fun buildIntegerTest() {
        TODO()
    }
}