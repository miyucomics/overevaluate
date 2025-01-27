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
	override fun arithName() = "blackbox_ops"
	override fun opTypes() = listOf(APPEND, SUB, INDEX_OF, ABS, ADD, AND, XOR, OR)
	override fun getOperator(pattern: HexPattern): Operator {
		return when (pattern) {
			APPEND -> OpBlackboxAdd
			REMOVE -> OpBlackboxRemove
			INDEX_OF -> OpBlackboxTest
			ABS -> OperatorUnary(all(IotaPredicate.ofType(BlackboxIota.TYPE))) { blackbox: Iota -> DoubleIota(downcast(blackbox, BlackboxIota.TYPE).set.size.toDouble()) }
			OR -> OpBlackboxBinary { a, b ->
				val orSet = a.toHashSet()
				orSet.addAll(b)
				orSet
			}
			AND -> OpBlackboxBinary { a, b ->
				val andSet = a.toHashSet()
				andSet.retainAll(b)
				andSet
			}
			XOR -> OpBlackboxBinary { a, b ->
				val xorSet = a.toHashSet()
				xorSet.addAll(b)
				val intersection = a.toHashSet()
				intersection.retainAll(b)
				xorSet.removeAll(intersection)
				xorSet
			}
			else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
		}
	}
}