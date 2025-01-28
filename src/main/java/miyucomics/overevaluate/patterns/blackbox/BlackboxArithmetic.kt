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
			OR -> OpBlackboxBinary { a, b ->
				val orSet = a.toMutableList()
				orSet.addAll(b)
				removeDuplicates(orSet)
			}
			AND -> OpBlackboxBinary { a, b ->
				val andSet = a.toMutableList()
				andSet.retainAll(b)
				removeDuplicates(andSet)
			}
			XOR -> OpBlackboxBinary { a, b ->
				val xorSet = a.toMutableList()
				xorSet.addAll(b)
				val intersection = a.toMutableList()
				intersection.retainAll(b)
				xorSet.removeAll(intersection)
				removeDuplicates(xorSet)
			}
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