package mathematics

/**
 * Represents roots of mathematical rational equations monomials. Includes [Rational] and [ArithmRad] classes.
 */
sealed interface MRoot1 {
    operator fun times(other: ArithmRad): MRoot1

    operator fun times(other: Rational): MRoot1

    operator fun times(other: MRoot1): MRoot1
}

