package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.overevaluate.BlackboxIota

object OpBlackboxTest : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(BlackboxIota.TYPE), IotaPredicate.TRUE)) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as BlackboxIota).contents
		val test = iterator.next()
		list.forEach {
			if (Iota.tolerates(it, test))
				return true.asActionResult
		}
		return false.asActionResult
	}
}