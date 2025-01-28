package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.overevaluate.BlackboxIota

object OpBlackboxAdd : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(BlackboxIota.TYPE), IotaPredicate.TRUE)) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as BlackboxIota).contents.toMutableList()
		val add = iterator.next()
		list.removeIf { Iota.tolerates(add, it) }
		list.add(add)
		return listOf(BlackboxIota(list))
	}
}