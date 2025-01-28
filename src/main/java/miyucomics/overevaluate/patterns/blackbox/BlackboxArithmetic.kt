package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator.Companion.downcast
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.all
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import miyucomics.overevaluate.BlackboxIota

object BlackboxArithmetic : Arithmetic {
	override fun arithName() = "blackbox"
	override fun opTypes() = listOf(APPEND, REMOVE, INDEX_OF, ABS, AND, XOR, OR)
	override fun getOperator(pattern: HexPattern): Operator {
		return when (pattern) {
			APPEND -> OpBlackboxAdd
			REMOVE -> OpBlackboxRemove
			INDEX_OF -> OpBlackboxTest
			ABS -> OperatorUnary(all(IotaPredicate.ofType(BlackboxIota.TYPE))) { blackbox: Iota -> DoubleIota(downcast(blackbox, BlackboxIota.TYPE).contents.size.toDouble()) }
			OR -> OpBlackboxBinary { a, b -> a + b.filter { x -> a.none { Iota.tolerates(x, it) } } }
			AND -> OpBlackboxBinary { a, b -> a.filter { x -> b.any { Iota.tolerates(x, it) } } }
			XOR -> OpBlackboxBinary { a, b -> a.filter { x0 -> b.none {Iota.tolerates(x0, it) } } + b.filter { x1 -> a.none { Iota.tolerates(x1, it) } } }
			else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
		}
	}

	private fun removeDuplicates(input: List<Iota>): List<Iota> {
		val out = mutableListOf<Iota>()
		for (subiota in input)
			if (out.none { Iota.tolerates(it, subiota) })
				out.add(subiota)
		return out
	}
}