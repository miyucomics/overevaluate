package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.overevaluate.BlackboxIota

class OpBlackboxBinary(private val process: (HashSet<Iota>, HashSet<Iota>) -> HashSet<Iota>) : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(BlackboxIota.TYPE), IotaPredicate.ofType(BlackboxIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val it = iotas.iterator()
		val a = (it.next() as BlackboxIota).set.toHashSet()
		val b = (it.next() as BlackboxIota).set.toHashSet()
		return listOf(BlackboxIota(process(a, b)))
	}
}