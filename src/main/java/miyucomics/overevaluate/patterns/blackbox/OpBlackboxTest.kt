package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.overevaluate.BlackboxIota

object OpBlackboxTest : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(BlackboxIota.TYPE), IotaPredicate.TRUE)) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val it = iotas.iterator()
		return listOf(BooleanIota((it.next() as BlackboxIota).set.contains(it.next())))
	}
}