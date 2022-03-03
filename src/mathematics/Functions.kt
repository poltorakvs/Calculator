package mathematics

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round


/**
 * In math, approximated is fraction (or integer), that is as close as possible to a given number if denominator of this fraction isn't > fixed limit.
 *
 * In our case, limit is 2^23 - 1. It used not only to denominator - if numerator is more than 2^23 - 1, this fraction is also not approximated.
 *
 * If value > 2^23 - 1, returns value % 1.
 */
fun approximate(float: Double): Fraction {
    //var x = abs(float)
    //x += x
    //TODO()
    /*val x = abs(float)
    val a = mutableListOf(x.toInt())
    val xes = mutableListOf(x - a[0])

    var i = 1
    var den = Integer(1) + x.toInt().toInteger()
    while (den.value < 2.0.pow(15).toInt() - 1) {
        val ai = 1 / xes[i - 1]
        a.add(ai.toInt())
        xes.add(ai - a[i])
        i++
        den += a.fold(Integer(1)) { prevRes, it -> prevRes * it.toInteger() }
    }



    var resNum = Integer(0) //Integer(a[0])
    var resDen = Integer(0)
    for (k in 0..(a.size + 1)) {
        var nowNum = Integer(1)
        var nowDen = Integer(a[k])

        var counter = k
        while (counter > 0) {
            val inter = nowDen
            nowDen = Integer(nowNum.value + a[counter - 1] * inter.value)
            nowNum = Integer(inter.value)
            counter--
        }

        if (nowDen.value >= 2.0.pow(15).toInt() - 1) {
            break
        }

        if (nowNum.value >= 2.0.pow(23).toInt() - 1 && k > 1) {
            break
        }

        if (k % 2 == 1) {
            resNum = nowNum
            resDen = nowDen
        } else {
            resNum = nowDen
            resDen = nowNum
        }
    }

    if (float < 0) {
        resNum = resNum.opposite()
    }

    return Fraction(resNum, resDen)
     */
    val x = abs(float)
    val a = mutableListOf(x.toInt())
    val xes = mutableListOf(x - a[0])

    var i = 1
    var denominator = 1 + a[0]
    while (denominator < 2.0.pow(15).toInt() && i < 25) {
        val ai = 1 / xes[i - 1]
        a.add(ai.toInt())
        xes.add(ai - a[i])
        i++
        denominator += a.fold(1) { prevRes, it -> prevRes * it }
    }

    val numerator: Int = round(x * denominator.toDouble()).toInt()
    return Fraction(numerator.toInteger(), denominator.toInteger()).simplify()

    /*
    var resNum = 1
    var resDen = 1
    for (k in 1..(a.size + 1)) {
        var nowNum = a[k]
        var nowDen = 1

        for (counter in (k-1) downTo 0) {
            val inter = nowNum
            nowNum = nowDen + a[counter] * nowNum
            nowDen = inter
        }

        if (nowDen >= 2.0.pow(15).toInt() - 1) {
            break
        }

        if (nowNum >= 2.0.pow(23).toInt() - 1 && k > 1) {
            break
        }

        resNum = nowNum
        resDen = nowDen
    }

    return Fraction(resNum.toInteger(), resDen.toInteger())

     */
}

/**
 * Creates [Integer] by strings. All string needed to contain only figures except first, that can also contains '-' sign.
 *
 * Example: buildInteger("-12345", "000", "67890") -> -1234500067890
 */
fun buildInteger(vararg numbers: String): Integer {
    var f = 0
    for (int in numbers) {
        if (f == 0) {
            f = 1
            continue
        }

        if (int.toInt() < 0) {
            throw NegativeNumberError("Not first argument of 'buildInteger' function can't be negative")
        }
    }

    val arrayOfNumbers: List<String> = numbers.toList()
    var str = ""
    arrayOfNumbers.map { str += it }
    return Integer(str.toInt())
}

/**
 * Creates [Long] by strings. All string needed to contain only figures except first, that can also contains '-' sign.
 *
 * Example: buildLong("-12345", "000", "67890") -> -1234500067890
 */
fun buildLong(vararg numbers: String): Long {
    var f = 0L
    for (int in numbers) {
        if (f == 0L) {
            f = 1L
            continue
        }

        if (int.toLong() < 0L) {
            throw NegativeNumberError("Not first argument of 'buildInteger' function can't be negative")
        }
    }

    val arrayOfNumbers: List<String> = numbers.toList()
    var str = ""
    arrayOfNumbers.map { str += it }
    return str.toLong()
}

/**
 * Deletes element from [MutableList], if there are any elements equal to given element. Previous condition is necessary. This function returns nothing, it just changes given object.
 */
fun <T> MutableList<T>.delete(element: T) {
    var removed = false
    for (item in this) {
        if (item == element) {
            removed = true
            break
        }
    }

    if (removed) {
        this.remove(element)
    } else {
        throw NonexistentDeletingError("Can't delete element from collection, that doesn't contains this element.")
    }
}

/**
 * Compares two [Collection] without considering order, returns true if and only if collections have same kit of elements, taking into consideration quantity of these elements.
 *
 * Example: (1, 2, 3, 2) <=> (2, 1, 3, 2), but not <=> (1, 2, 3, 3) or (1, 2, 3).
 */
infix fun <T> Collection<T>.tantamount(other: Collection<T>): Boolean {
    val first = this.toMutableList()
    val second = other.toMutableList()

    if (first.size != second.size) {
        return false
    }

    for (item in first) {
        try {
            second.delete(item)
        } catch (e: NonexistentDeletingError) {
            return false
        }
    }

    return true
}

/**
 * Counts how many elements in given [Iterable] are equal to given element. Returns [Int].
 */
fun <T> Collection<T>.count(element: T): Int {
    var res = 0
    for (e in this) {
        if (e == element) {
            res++
        }
    }
    return res
}

/**
 * Return the greatest common divider (GCD) of consequence.
 */
fun gcd(vararg others: Integer): Integer {
    var g = others[0]
    for (i in others.toList()) {
        if (i.isOne() || i.isZero()) {
            return Integer(1)
        }

        g = g gcd i
    }
    return g
}

/**
 * Expanding [Collection] class method, that returns list of elements, that contains in all collections, taking into consideration quantity of these elements.
 */
fun <T> overlap(vararg others: Collection<T>): List<T> {
    var thisCollect = others[0].toList()

    for (set in others) {
        if (set.isEmpty()) {
            return emptyList()
        }

        thisCollect = thisCollect overlap set
    }

    return thisCollect
}

/**
 * Expanding [Collection] class method, that returns list of elements, that contains in both collections, taking into consideration quantity of these elements.
 */
infix fun <T> Collection<T>.overlap(other: Collection<T>): List<T> {
    val set = this.toMutableList()
    set.retainAll(other)
    return set.toList()
}

/**
 * Returns the smallest common multiple (SCM) of consequence.
 */
fun scm(vararg others: Integer): Integer {
    var c = others[0]
    for (i in others.toList()) {
        c = c scm i
    }
    return c
}

/**
 * Expanding [String] class method, that returns strings, made by repeating given string. Overloads operator '*'.
 *
 * If repeats quantity = 0, returns empty string, if < 0, throws exception, because string can't have negative size.
 *
 * Example: "abc" * 3 -> "abcabcabc"; "0.".times(5) -> "0.0.0.0.0."; "abcdef" * 0 -> ""
 */
operator fun String.times(repeats: Int): String {
    if (repeats < 0) {
        throw NegativeArraySizeException("String can have only positive or zero length")
    }

    var res = ""
    for (i in 1..repeats) {
        res += this
    }
    return res
}

/**
 * Converts [Int] to [Integer].
 */
fun Int.toInteger(): Integer {
    return Integer(this)
}

/**
 * Converts [Long] to [Integer].
 */
fun Long.toInteger(): Integer {
    return this.toInt().toInteger()
}

/**
 * Expanding [Set] class method, that returns all subsets to a given set, including this set and empty set.
 */
fun <T> Set<T>.subsets(): Set<Set<T>> {
    val subset: MutableSet<Set<T>> = mutableSetOf(setOf())
    val addAfter: MutableList<Set<T>> = mutableListOf()
    for (item in this) {
        for (obj in subset) {
            addAfter.add(obj + item)
        }
        subset.addAll(addAfter)
        addAfter.clear()
    }
    return subset.toSet()
}

fun main() {
    println(listOf(1, 2, 2, 2, 3, 4, 3, 5).containsAll(listOf(1, 2, 2, 3, 4, 5)))
}