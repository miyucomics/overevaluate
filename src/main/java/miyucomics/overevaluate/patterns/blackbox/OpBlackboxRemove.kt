package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.overevaluate.BlackboxIota

object OpBlackboxRemove : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(BlackboxIota.TYPE), IotaPredicate.TRUE)) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val it = iotas.iterator()
		val set = (it.next() as BlackboxIota).set.toHashSet()
		set.remove(it.next())
		return listOf(BlackboxIota(set))
	}
}