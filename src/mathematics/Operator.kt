package mathematics

import kotlin.math.pow

/**
 * Abstract class, that unites all possible operators.
 *
 * Derives [Base] class. Haven't any constructor.
 */
abstract class Operator: Base() {

    abstract val operands: Array<out Base>

    abstract val operandTypes: Set<String>

    abstract val order: List<String>

    /**
     * String representation of the type of object.
     */
    override val type: String
        get() = "Operator"
}

/**
 * Representation of mathematical addition (+) operator.
 *
 * Derives [Operator] class.
 *
 * Order of args doesn't matter. Args can be 0 and more.
 *
 * Can be created by using constructor Add(., ., .), when . - args is [Base].
 */
open class Add constructor(private vararg val args: Base): Operator() {

    init {
        for (t in operandTypes) {
            if (t !in order) throw UnknownClassError("Can't use '+' operator on the unknown operand")
        }
    }

    /**
     * String representation of addition. Example: Add(1, 3, 4/5) -> 1 + 3 + 4/5
     */
    override fun toString(): String {
        return args.joinToString(" + ") { it.toString() }
    }

    override val calculated: Double
        get() = TODO("Not yet implemented")

    /**
     * Returns true if and only if other is [Add] and all other operands is equal to all these operands without considering order.
     */
    override fun equals(other: Any?): Boolean {
        return other is Add && other.operands.toList() tantamount operands.toList()
    }

    /**
     * Returns hash code value of the object.
     */
    override fun hashCode(): Int {
        return ((operands.size.toLong() * operands.fold(0) { prevRes, it -> prevRes + it.hashCode() }) % (2.0.pow(31.0).toLong())).toInt()
    }

    /**
     * Returns array of operands.
     */
    final override val operands: Array<out Base>
        get() = args

    /**
     * Returns [Set] of types of operands.
     */
    final override val operandTypes: Set<String>
        get() = operands.map { it.type }.toSet()

    /**
     * Sets hierarchy of the operands.
     */
    final override val order: List<String>
        get() = listOf("Add", "Multiply", "ArithmRad", "Fraction", "Integer")

    /**
     * String representation of the type of the object.
     */
    override val type: String
        get() = "Add"

    /**
     * Creates [Fraction] from add of one [Fraction] and one [Integer]. The object needs to fit to these conditions.
     */
    private fun buildFraction(): Fraction {
        if (operandTypes != setOf("Integer", "Fraction") || operands.size != 2) {
            throw ToFractionError("Can't build fraction from this operands")
        }
        val opf = operands[0]
        val ops = operands[1]
        return if (opf is Integer) {
            val ip: Integer = opf
            val fp: Fraction = ops as Fraction
            Fraction(ip * fp.denValue + fp.numValue, fp.denValue)
        } else if (ops is Integer) {
            val ip: Integer = ops
            val fp: Fraction = opf as Fraction
            Fraction(ip * fp.denValue + fp.numValue, fp.denValue)
        } else {
            throw CouldntHappenError("One of the elements should be Integer.")
        }
    }

    /**
     * Puts out of brackets elements, that is in all operands. Example: 2x + 36x^2 + 4x^3 -> 2x(1 + 18x + 2x^2), but 1 + 2x + 4x^2 !-> 1 + 2x(1 + 2x). If there aren't any elements, that can be moved out, returns itself.
     *
     * [Add] must be simplified.
     */
    fun operandsOverlap(): Multiply {
        val list = operands.toMutableList()

        if (list.size < 2) {
            return Multiply(this)
        }

        val setOfMultiples: MutableList<List<Base>> = mutableListOf()
        for (operand in list) {
            when (operand) {
                is Add -> throw CouldntHappenError("Input data to 'operandsOverlap' method on 'Add' object must be simplified Add.")
                is ArithmRad -> setOfMultiples.add(operand.factorization())
                is E -> setOfMultiples.add(listOf(E()))
                is Multiply -> {
                    val res: MutableList<Base> = mutableListOf()
                    for (item in operand.operands) {
                        when (item) {
                            is ArithmRad -> res.addAll(item.factorization())
                            is E -> res.add(E())
                            is PI -> res.add(PI())
                            is Rational -> res.addAll(item.factorization())
                        }
                    }
                    setOfMultiples.add(res)
                }
                is PI -> setOfMultiples.add(listOf(PI()))
                is Rational -> setOfMultiples.add(operand.factorization())
            }
        }

        val intersection = overlap(*setOfMultiples.toTypedArray())

        return if (intersection.isEmpty()) {
            Multiply(this)
        } else {
            val rendered = Multiply(*intersection.toTypedArray()).simplify()
            val otherOperands: MutableList<Base> = mutableListOf()

            for (kit in setOfMultiples) {
                val multiples = kit.toMutableList()
                multiples.removeAll(intersection)
                otherOperands.add(Multiply(*multiples.toTypedArray()).simplify())
            }

            Multiply(rendered, Add(*otherOperands.toTypedArray()))
        }
    }

    /**
     * Simplifies given expression. Returns mathematically equal to given expression.
     */
    override fun simplify(): Base {
        val types = operandTypes
        val commonTypes = order
        val list: MutableList<Base> = operands.toMutableList()

        for (t in commonTypes) {
            when (t) {
                !in types -> continue

                "Add" -> {
                    val toDelete: MutableList<Add> = mutableListOf()
                    val toAdd: MutableList<Base> = mutableListOf()
                    list.map {
                        if (it is Add) {
                            toDelete.add(it)

                            val simpleAdd = it.simplify()
                            if (simpleAdd is Add) {
                                toAdd.addAll(simpleAdd.operands)
                            } else {
                                toAdd.add(simpleAdd)
                            }
                        }
                    }
                    list.removeAll(toDelete)
                    list.addAll(toAdd)
                }

                "ArithmRad" -> {
                    val toDelete: MutableList<Base> = mutableListOf()
                    val toAdd: MutableList<Base> = mutableListOf()
                    val multiples: MutableMap<ArithmRad, Rational> = mutableMapOf()
                    list.map {
                        if (it is ArithmRad) {
                            toDelete.add(it)
                            var extracted = it.simplify().extractBase()
                            if (extracted is ArithmRad) {
                                extracted = Multiply(Integer(1), extracted)
                            }

                            if (extracted is Multiply) {
                                val op = extracted.operands
                                val radical: ArithmRad = op[1] as ArithmRad
                                val rendered: Rational = op[0] as Rational
                                val k: Rational = multiples[radical]?:(Integer(0))
                                multiples[radical] = k + rendered
                            } else {
                                toAdd.add(extracted as Rational)
                            }
                        }
                        if (it is Multiply) {
                            val ot = it.operandTypes
                            val op = it.operands
                            if (ot == setOf("Integer", "ArithmRad") || ot == setOf("Fraction", "ArithmRad")) {
                                val radical: ArithmRad
                                val rendered: Rational
                                if (op[0] is ArithmRad) {
                                    radical = op[0] as ArithmRad
                                    rendered = op[1] as Rational
                                } else {
                                    radical = op[1] as ArithmRad
                                    rendered = op[0] as Rational
                                }

                                val e = radical.extractBase()
                                val extracted: Multiply = if (e is Multiply) {
                                    val eop = e.operands
                                    Multiply(eop[0] as Rational * rendered, eop[2])
                                } else {
                                    Multiply(rendered, e as ArithmRad)
                                }

                                val operand = extracted.operands
                                val rad: ArithmRad = operand[1] as ArithmRad
                                val rat: Rational = operand[0] as Rational
                                val k: Rational = multiples[rad]?:(Integer(0))
                                multiples[radical] = k + rat
                            }
                        }
                    }
                    list.removeAll(toDelete)

                    for (pair in multiples) {
                        when {
                            pair.value.isZero() -> continue
                            pair.value.isOne() -> list.add(pair.key)
                            else -> list.add(Multiply(pair.value, pair.key))
                        }
                    }
                }

                "Fraction", "Integer" -> {
                    val toDelete: MutableList<Rational> = mutableListOf()
                    var toAdd: Rational = Fraction(Integer(0), Integer(1))
                    list.map {
                        if (it is Rational) {
                            toDelete.add(it)
                            toAdd += it.simplify()
                        }
                    }
                    list.removeAll(toDelete)

                    if (!toAdd.isZero()) {
                        list.add(toAdd.toInteger())
                    }
                }

                "Multiply" -> {
                    val toDelete: MutableList<Multiply> = mutableListOf()
                    val toAdd: MutableList<Base> = mutableListOf()
                    list.map {
                        if (it is Multiply) {
                            val simpleMul = it.simplify()
                            toDelete.add(it)
                            if (simpleMul is Multiply) {
                                toAdd.add(simpleMul)
                            } else if (simpleMul is Rational) {
                                if (!simpleMul.isZero()) {
                                    toAdd.add(simpleMul)
                                }
                            } else if (simpleMul is Add) { // simplified multiply can only return itself, add or single base
                                toAdd.addAll(simpleMul.operands)
                            }
                        }
                    }
                    list.removeAll(toDelete)
                    list.addAll(toAdd)
                }
            }
        }
        return if (list.size == 1) {
            list[0]
        } else {
            Add(*list.toTypedArray())
        }
    }

    /**
     * Convert given expression to [Fraction] if it is possible.
     */
    fun toFraction(): Fraction {
        val simple = this.simplify()
        if (simple is Add && simple.operands != setOf("Integer", "Fraction")) {
            throw ToFractionError("Can't convert add with this operands to fraction.")
        } else if (simple !is Add && simple !is Rational) {
            throw ToFractionError("Can't convert add to fraction, that simplifying to not rational.")
        }

        return when (simple) {
            is Add -> {
                simple.buildFraction()
            }
            is Integer -> {
                simple.toFraction()
            }
            else -> {
                simple as Fraction
            }
        }
    }
}

/**
 * Representation of the mathematical multiplication (*) operator.
 *
 * Derives [Operator] class.
 *
 * Order of args doesn't matter. Args can be 0 and more.
 *
 * Can be created by using constructor Multiply(., ., .), when . - args is [Base].
 */
open class Multiply constructor(private vararg val args: Base): Operator() {

    init {
        for (t in operandTypes) {
            if (t !in order) throw UnknownClassError("Can't use '*' operator on the unknown operand")
        }
    }

    /**
     * String representation of the expression. Example: Multiply(1, 2, 3/4) -> 1 * 2 * 3/4
     */
    override fun toString(): String {
        return args.joinToString(" * ") { it.toString() }
    }

    override val calculated: Double
        get() = TODO("Not yet implemented")

    /**
     * Returns true if and only if other is [Multiply] and all other operands is equal to all these operands without considering order.
     */
    override fun equals(other: Any?): Boolean {
        return other is Multiply && this.operands.toList() tantamount other.operands.toList()
    }

    /**
     * Returns hash code value of the object.
     */
    override fun hashCode(): Int {
        return ((operands.size.toLong() * operands.fold(0) { prevRes, it -> prevRes + it.hashCode() }) % (2.0.pow(31.0).toLong())).toInt()
    }

    /**
     * Returns array of operands.
     */
    final override val operands: Array<out Base>
        get() = args

    /**
     * Returns [Set] of types of operands.
     */
    final override val operandTypes: Set<String>
        get() = operands.map { it.type }.toSet()

    /**
     * Set hierarchy of operands.
     */
    final override val order: List<String>
        get() = listOf("Mul", "Add", "Fraction", "Integer")

    /**
     * String representation of the type of object.
     */
    override val type: String
        get() = "Multiply"

    /**
     * Simplifies given expression. Returns mathematically equal to given expression.
     */
    override fun simplify(): Base {
        val types = operandTypes
        val commonTypes = order
        val list = operands.toMutableList()
        val addList: MutableList<Base> = mutableListOf()

        val toDeleteOnes: MutableList<Base> = mutableListOf()
        for (operand in list) {
            if (operand is Rational && operand.isZero() || operand is ArithmRad && operand.isZero()) {
                return Integer(0)
            } else if (operand is Rational && operand.isOne() || operand is ArithmRad && operand.isOne())
                toDeleteOnes.add(operand)
        }
        list.removeAll(toDeleteOnes)

        for (t in commonTypes) {
            when (t) {
                !in types -> continue

                "Add" -> {
                    val toDelete: MutableList<Add> = mutableListOf()
                    val toAdd: MutableList<Base> = mutableListOf()
                    val otherAdd: MutableList<Add> = mutableListOf()
                    var marker = false

                    list.map {
                        if (it is Add) {
                            toDelete.add(it)

                            try {
                                toAdd.add(it.toFraction())
                            } catch (e: ToFractionError) {
                                val simpleAdd = it.simplify()

                                if (simpleAdd !is Add) {
                                    toAdd.add(simpleAdd)
                                } else {
                                    marker = true
                                    otherAdd.add(simpleAdd)
                                }
                            }
                        }
                    }
                    list.addAll(toAdd)
                    list.removeAll(toDelete)

                    if (marker) {
                        val simpleMul = Multiply(*list.toTypedArray()).simplify()
                        list.clear()
                        if (simpleMul is Multiply) {
                            list.addAll(simpleMul.operands)
                        } else {
                            list.add(simpleMul)
                        }

                        list.addAll(otherAdd)
                        for (add in otherAdd) {
                            val addOp = add.operands
                            for (op in addOp) {
                                list.remove(add)
                                list.add(op)
                                addList.add(Multiply(*list.toTypedArray()).simplify())
                                list.remove(op)
                                list.add(add)
                            }
                        }

                        return Add(*addList.toTypedArray()).simplify()
                    }
                }

                "ArithmRad" -> {
                    val toDelete: MutableList<ArithmRad> = mutableListOf()
                    var rationalAccumulator: Rational = Integer(1)
                    var radicalAccumulator: ArithmRad = ArithmRad(Integer(1), Integer(1))
                    list.map {
                        if (it is ArithmRad) {
                            toDelete.add(it)

                            val simple = it.simplify()
                            val extracted = simple.extractBase()
                            if (extracted is Rational) {
                                rationalAccumulator *= extracted
                            } else if (extracted is Multiply) {
                                rationalAccumulator *= extracted.operands[0] as Rational
                                val composition = extracted.operands[1] as ArithmRad * radicalAccumulator
                                if (composition is ArithmRad) {
                                    radicalAccumulator = composition
                                } else {
                                    rationalAccumulator *= composition as Rational
                                    radicalAccumulator = ArithmRad(Integer(1), Integer(1))
                                }
                            } else {
                                val composition = extracted as ArithmRad * radicalAccumulator
                                if (composition is ArithmRad) {
                                    radicalAccumulator = composition
                                } else {
                                    rationalAccumulator *= composition as Rational
                                    radicalAccumulator = ArithmRad(Integer(1), Integer(1))
                                }
                            }
                            TODO()
                        }
                    }
                    list.removeAll(toDelete)
                    list.add(radicalAccumulator)
                    list.add(rationalAccumulator)
                }

                "Fraction", "Integer" -> {
                    val toDelete: MutableList<Rational> = mutableListOf()
                    var toAdd: Rational = Fraction(Integer(0), Integer(1))
                    list.map {
                        if (it is Rational) {
                            toAdd *= it.simplify()
                        }
                    }
                    list.removeAll(toDelete)
                    list.add(toAdd)
                }

                "Multiply" -> {
                    val toDelete: MutableList<Multiply> = mutableListOf()
                    val toAdd: MutableList<Base> = mutableListOf()
                    list.map {
                        if (it is Multiply) {
                            toDelete.add(it)

                            when (val simpleMul = it.simplify()) {
                                is Multiply -> { // add can only return composition or sum or Rational
                                    toAdd.addAll(simpleMul.operands)
                                }
                                else -> {
                                    toAdd.add(simpleMul)
                                }
                            }
                        }
                    }
                    list.removeAll(toDelete)
                    list.addAll(toAdd)
                }
            }
        }

        return if (list.size == 1) {
            val res = list[0]
            if (res is Rational) {
                res.toInteger()
            } else {
                res
            }
        } else {
            Multiply(*list.toTypedArray())
        }
    }
}
 fun main(){
    //val a = Multiply(Integer(3), Integer(4), Fraction(Integer(8), (Integer(24)))).simplify()
}

