package mathematics

import kotlin.reflect.KType
import kotlin.reflect.full.createType

abstract class MyException(override val message: String?): Exception() {

    abstract val type: KType
}

class ComplexNumberError(override val message: String?): MyException(message) {
    override val type: KType
        get() = ComplexNumberError::class.createType()
}

class CouldntHappenError(override val message: String?): MyException(message) {
    override val type: KType
        get() = CouldntHappenError::class.createType()
}

class NegativeNumberError(override val message: String?): MyException(message) {
    override val type: KType
        get() = NegativeNumberError::class.createType()
}

class NegativeQuantityError(override val message: String?): MyException(message) {
    override val type: KType
        get() = NegativeQuantityError::class.createType()
}

class NonexistentDeletingError(override val message: String?): MyException(message) {
    override val type: KType
        get() = NonexistentDeletingError::class.createType()
}

class ToFractionError(override val message: String?): MyException(message) {
    override val type: KType
        get() = ToFractionError::class.createType()
}

class UnknownClassError(override val message: String?): MyException(message) {
    override val type: KType
        get() = UnknownClassError::class.createType()
}

class ZeroDivisionError(override val message: String?): MyException(message) {
    override val type: KType
        get() = ZeroDivisionError::class.createType()
}

class ZeroToZeroPowError(override val message: String?): MyException(message) {
    override val type: KType
        get() = ZeroToZeroPowError::class.createType()
}

class ZeroRadicalError(override val message: String?): MyException(message) {
    override val type: KType
        get() = ZeroRadicalError::class.createType()
}


fun assertThrows(result: KType, function: () -> Any): Unit? {
    try {
        function()
    } catch (e: MyException) {
        if (e.type != result) {
            throw AssertionError("Incorrect exception was found.")
        } else {
            return null
        }
    }

    throw AssertionError("No exception was found.")
}


fun main() {
    val i0 = Integer(0)
    val i1 = Integer(-1)
    assertThrows(NegativeQuantityError::class.createType()) {Float(i1, 2, i0)}
    val a = kotlin.math.abs(-3)
    val foo = ::approximate
}